/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.notelysia.restservices.controller.newsapp;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;
import com.notelysia.restservices.config.DecodeString;
import com.notelysia.restservices.exception.ResourceNotFound;
import com.notelysia.restservices.model.dto.newsapp.RssDto;
import com.notelysia.restservices.model.entity.newsapp.NewsSource;
import com.notelysia.restservices.service.newsapp.NewsSourceServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;


@RestController
@RequestMapping("/news-app")
@Tag(name = "News Source", description = "API for News Source")
public class NewsSourceController {
    private static final Logger logger = LogManager.getLogger(NewsSourceController.class);
    private final DecodeString decodeString = new DecodeString();
    @Autowired
    private NewsSourceServices newsSourceServices;

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    @GetMapping(value = "/guest/news-source")
    public ResponseEntity<Map<String, List<NewsSource>>> allNewsSource() {
        Map<String, List<NewsSource>> listRespond = new HashMap<>();
        listRespond.put("newsSource", this.newsSourceServices.findAllNewsSource());
        return new ResponseEntity<>(listRespond, HttpStatus.OK);
    }

    //For user login
    @GetMapping(value = "/account/news-source", params = {"userid"})
    public ResponseEntity<Map<String, NewsSource>> userNewsSource(
            @RequestParam(value = "userid") int userid
    ) throws ResourceNotFound {
        Map<String, NewsSource> respond = new HashMap<>();
        NewsSource newsSources = this.newsSourceServices.findByUserId(userid)
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        respond.put("newsSource", newsSources);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    //Convert RSS to JSON
    @GetMapping("/rss-to-json")
    public ResponseEntity<Map<String, List<RssDto>>> convertRSS2JSON(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "type") String type
    ) {
        List<String> rssUrls;
        if (userId.equals("guest")) {
            //Guest only use one news source
            rssUrls = this.newsSourceServices.guestRssUrlByType(this.getDecode(type.getBytes()));
        } else {
            rssUrls = this.newsSourceServices.findAllRssUrlByType(this.getDecode(type.getBytes()));
        }
        RssReader rssReader = new RssReader();
        Map<String, List<RssDto>> respond = new ConcurrentHashMap<>();
        List<RssDto> rssDtos = new ArrayList<>();
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Stream<Item> rssFeed = rssReader.read(rssUrls);
            List<Item> items = rssFeed.toList();
            for (Item item : items) {
                String title = item.getTitle().get();
                String description = item.getDescription().get();
                String urlLink = item.getLink().get();
                String pubDate = item.getPubDate().get();
                RssDto rssDto = new RssDto(title, description, urlLink, pubDate);
                rssDtos.add(rssDto);
            }
            latch.countDown();
            respond.put("result", rssDtos);
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Error: " + e, e);
        }
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    //Search from all rss url type 'breaking news'
    @GetMapping("/search-news")
    public ResponseEntity<Map<String, List<RssDto>>> searchAllNews(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "keyWord") String keyWord) {
        List<String> urls;
        if (userId.equals("guest")) {
            //Guest only use one news source
            urls = this.newsSourceServices.guestAllRssUrl();
        } else {
            urls = this.newsSourceServices.findAllRssUrl();
        }
        RssReader rssReader = new RssReader();
        Map<String, List<RssDto>> respond = new ConcurrentHashMap<>();
        List<RssDto> rssDtos = new ArrayList<>();
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Stream<Item> rssFeed = rssReader.read(urls).sorted()
                    .filter(i -> i.getTitle().get().contains(this.getDecode(keyWord.getBytes())));
            List<Item> items = rssFeed.toList();
            for (Item item : items) {
                String title = item.getTitle().get();
                String description = item.getDescription().get();
                String urlLink = item.getLink().get();
                String pubDate = item.getPubDate().get();
                RssDto rssDto = new RssDto(title, description, urlLink, pubDate);
                rssDtos.add(rssDto);
            }
            latch.countDown();
            respond.put("result", rssDtos);
            latch.await();
        } catch (InterruptedException e) {
            logger.error("Error: " + e, e);
        }
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
}

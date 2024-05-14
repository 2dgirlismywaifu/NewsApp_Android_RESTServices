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
import com.notelysia.config.DecodeString;
import com.notelysia.exception.ResourceNotFound;
import com.notelysia.restservices.model.dto.newsapp.NewsSourceDto;
import com.notelysia.restservices.model.dto.newsapp.RssNews;
import com.notelysia.restservices.model.dto.newsapp.RssNewsDto;
import com.notelysia.restservices.model.entity.newsapp.NewsSource;
import com.notelysia.restservices.service.newsapp.NewsSourceServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news-app")
@Tag(name = "News Source", description = "API for News Source")
public class NewsSourceController {
  private static final Logger logger = LogManager.getLogger(NewsSourceController.class);
  private final DecodeString decodeString = new DecodeString();
  @Autowired private NewsSourceServices newsSourceServices;

  private String getDecode(byte[] data) {
    return this.decodeString.decodeString(data);
  }

  @GetMapping(value = "/guest/news-source")
  public ResponseEntity<NewsSourceDto> allNewsSource() {
    List<NewsSource> newsSource = this.newsSourceServices.findAllNewsSource();
    if (newsSource.isEmpty()) {
      return new ResponseEntity<>(
          new NewsSourceDto(HttpStatus.NOT_FOUND.toString(), "0", "0", null), HttpStatus.NOT_FOUND);
    } else {
      NewsSourceDto newsSourceDto =
          new NewsSourceDto(
              HttpStatus.OK.toString(),
              String.valueOf(System.currentTimeMillis()),
              String.valueOf(newsSource.size()),
              newsSource);
      return new ResponseEntity<>(newsSourceDto, HttpStatus.OK);
    }
  }

  // For user login
  @GetMapping(
      value = "/account/news-source",
      params = {"userid"})
  public ResponseEntity<NewsSourceDto> userNewsSource(@RequestParam(value = "userid") int userid)
      throws ResourceNotFound {
    List<NewsSource> newsSource = this.newsSourceServices.findByUserId(userid);
    if (newsSource.isEmpty()) {
      throw new ResourceNotFound("News Source not found for user id: " + userid);
    } else {
      NewsSourceDto newsSourceDto =
          new NewsSourceDto(
              HttpStatus.OK.toString(),
              String.valueOf(System.currentTimeMillis()),
              String.valueOf(newsSource.size()),
              newsSource);
      return new ResponseEntity<>(newsSourceDto, HttpStatus.OK);
    }
  }

  // Convert RSS to JSON
  @GetMapping("/rss-to-json")
  public ResponseEntity<RssNewsDto> convertRSS2JSON(
      @RequestParam(value = "userId", required = false) String userId,
      @RequestParam(value = "type") String type,
      @RequestParam(value = "size") String size) {
    Stream<Item> rssFeed;
    List<Item> items = new ArrayList<>();
    List<String> rssUrls;
    List<String> rssSynSubscribe = null;
    if (userId != null && !userId.isEmpty()) {
      rssUrls = this.newsSourceServices.findAllRssUrlByType(this.getDecode(type.getBytes()));
      rssSynSubscribe =
          this.newsSourceServices.findAllRssUrlByTypeWithSynSubscribe(
              Integer.valueOf(userId), type);
    } else {
      // Guest only use one news source
      rssUrls = this.newsSourceServices.guestRssUrlByType(this.getDecode(type.getBytes()));
    }
    RssReader rssReader = new RssReader();
    List<RssNews> rssNewsList = new ArrayList<>();
    RssNewsDto rssNewsDto = new RssNewsDto();
    try {
      CountDownLatch latch = new CountDownLatch(1);
      if (rssSynSubscribe != null) {
        if (rssSynSubscribe.isEmpty()) {
          for (String rssUrl : rssUrls) {
            rssFeed = rssReader.read(rssUrl);
            items.addAll(rssFeed.limit(Long.parseLong(size)).toList());
          }
        } else {
          for (String rssUrl : rssSynSubscribe) {
            rssFeed = rssReader.read(rssUrl);
            items.addAll(rssFeed.limit(Long.parseLong(size)).toList());
          }
        }
      } else {
        rssFeed = rssReader.read(rssUrls);
        items.addAll(rssFeed.limit(Long.parseLong(size)).toList());
      }
      for (Item item : items) {
        if (item.getTitle().isPresent()
            && item.getDescription().isPresent()
            && item.getLink().isPresent()
            && item.getPubDate().isPresent()) {
          String title = item.getTitle().get();
          String description = item.getDescription().get();
          String urlLink = item.getLink().get();
          String pubDate = item.getPubDate().get();
          RssNews rssNews = new RssNews(title, description, urlLink, pubDate);
          rssNewsList.add(rssNews);
        }
      }
      latch.countDown();
      latch.await();
    } catch (InterruptedException e) {
      logger.error("Error: " + e, e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    if (rssNewsList.isEmpty()) {
      rssNewsDto.setStatus(HttpStatus.NOT_FOUND.toString());
      rssNewsDto.setTime(String.valueOf(System.currentTimeMillis()));
      rssNewsDto.setTotalResults("0");
      rssNewsDto.setRssNewsList(null);
      return new ResponseEntity<>(rssNewsDto, HttpStatus.NOT_FOUND);
    } else {
      rssNewsDto.setStatus(HttpStatus.OK.toString());
      rssNewsDto.setTime(String.valueOf(System.currentTimeMillis()));
      rssNewsDto.setTotalResults(String.valueOf(rssNewsList.size()));
      rssNewsDto.setRssNewsList(rssNewsList);
      return new ResponseEntity<>(rssNewsDto, HttpStatus.OK);
    }
  }

  // Search from all rss url type 'breaking news'
  @GetMapping("/search-news")
  public ResponseEntity<RssNewsDto> searchAllNews(
      @RequestParam(value = "userId", required = false) String userId,
      @RequestParam(value = "type") String type,
      @RequestParam(value = "keyWord") String keyWord,
      @RequestParam(value = "size") String size) {
    Stream<Item> rssFeed;
    List<Item> items = new ArrayList<>();
    List<String> urls;
    List<String> rssSynSubscribe = null;
    RssReader rssReader = new RssReader();
    List<RssNews> rssNewsList = new ArrayList<>();
    RssNewsDto rssNewsDto = new RssNewsDto();
    if (userId != null && !userId.isEmpty()) {
      urls = this.newsSourceServices.findAllRssUrlByType(this.getDecode(type.getBytes()));
      rssSynSubscribe =
          this.newsSourceServices.findAllRssUrlWithSyncSubscribe(
              Integer.valueOf(userId), this.getDecode(type.getBytes()));
    } else {
      // Guest only use one news source
      urls = this.newsSourceServices.guestRssUrlByType(this.getDecode(type.getBytes()));
    }
    try {
      CountDownLatch latch = new CountDownLatch(1);
      if (rssSynSubscribe != null) {
        if (rssSynSubscribe.isEmpty()) {
          for (String rssUrl : urls) {
            rssFeed =
                rssReader
                    .read(rssUrl)
                    .sorted()
                    .filter(i -> i.getTitle().toString().contains(keyWord));
            items.addAll(rssFeed.limit(Long.parseLong(size)).toList());
          }
        } else {
          for (String rssUrl : rssSynSubscribe) {
            rssFeed =
                rssReader
                    .read(rssUrl)
                    .sorted()
                    .filter(i -> i.getTitle().toString().contains(keyWord));
            items.addAll(rssFeed.limit(Long.parseLong(size)).toList());
          }
        }
      } else {
        rssFeed =
            rssReader.read(urls).sorted().filter(i -> i.getTitle().toString().contains(keyWord));
        items.addAll(rssFeed.limit(Long.parseLong(size)).toList());
      }
      for (Item item : items) {
        String title = item.getTitle().get();
        String description = item.getDescription().get();
        String urlLink = item.getLink().get();
        String pubDate = item.getPubDate().get();
        RssNews rssNews = new RssNews(title, description, urlLink, pubDate);
        rssNewsList.add(rssNews);
      }
      latch.countDown();
      latch.await();
    } catch (InterruptedException e) {
      logger.error("Error: " + e, e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    if (rssNewsList.isEmpty()) {
      rssNewsDto.setStatus(HttpStatus.NOT_FOUND.toString());
      rssNewsDto.setTime(String.valueOf(System.currentTimeMillis()));
      rssNewsDto.setTotalResults("0");
      rssNewsDto.setRssNewsList(null);
      return new ResponseEntity<>(rssNewsDto, HttpStatus.NOT_FOUND);
    } else {
      rssNewsDto.setStatus(HttpStatus.OK.toString());
      rssNewsDto.setTime(String.valueOf(System.currentTimeMillis()));
      rssNewsDto.setTotalResults(String.valueOf(rssNewsList.size()));
      rssNewsDto.setRssNewsList(rssNewsList);
      return new ResponseEntity<>(rssNewsDto, HttpStatus.OK);
    }
  }
}

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

import com.notelysia.config.DecodeString;
import com.notelysia.restservices.model.dto.newsapp.NewsDetailDto;
import com.notelysia.restservices.model.dto.newsapp.RssList;
import com.notelysia.restservices.model.dto.newsapp.RssListDto;
import com.notelysia.restservices.model.entity.newsapp.NewsDetail;
import com.notelysia.restservices.service.newsapp.NewsSourceServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/news-app")
@Tag(name = "News Details List Country", description = "API for News Details List Country")
public class NewsDetailController {
    private final DecodeString decodeString = new DecodeString();
    @Autowired
    private NewsSourceServices newsSourceServices;

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    //This is only for guest user, follow subscribe only for login user
    @GetMapping("/guest/news-details")
    public ResponseEntity<NewsDetailDto> allNewsSource(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "name") String name) {
        List<NewsDetail> newsDetailList = this.newsSourceServices.findByUrlTypeAndSourceName(this.getDecode(type.getBytes()), this.getDecode(name.getBytes()));
        if (newsDetailList.isEmpty()) {
            return new ResponseEntity<>(new NewsDetailDto(HttpStatus.NOT_FOUND.toString(),
                    "0", "0", null), HttpStatus.NOT_FOUND);
        } else {
            NewsDetailDto newsDetailDto = new NewsDetailDto(HttpStatus.OK.toString(), String.valueOf(System.currentTimeMillis()),
                    String.valueOf(newsDetailList.size()), newsDetailList);
            return new ResponseEntity<>(newsDetailDto, HttpStatus.OK);
        }
    }

    //This is list url for each source
    @GetMapping("/account/news-details/list-url")
    public ResponseEntity<NewsDetailDto> getTypeOfEachUrlNewsSource(
            @RequestParam(value = "name") String name) {
        List<NewsDetail> newsDetailList = this.newsSourceServices.findBySourceName(this.getDecode(name.getBytes()));
        if (newsDetailList.isEmpty()) {
            return new ResponseEntity<>(new NewsDetailDto(HttpStatus.NOT_FOUND.toString(),
                    "0", "0", null), HttpStatus.NOT_FOUND);
        } else {
            NewsDetailDto newsDetailDto = new NewsDetailDto(HttpStatus.OK.toString(), String.valueOf(System.currentTimeMillis()),
                    String.valueOf(newsDetailList.size()), newsDetailList);
            return new ResponseEntity<>(newsDetailDto, HttpStatus.OK);
        }
    }

    //GET URL RSS LIST FOLLOW SOURCE_NAME
    @GetMapping(value = "/account/news-details/list-rss")
    public ResponseEntity<RssListDto> getRssListEachSourceName(
            @RequestParam(value = "name") String name) {
        List<RssList> rssList = this.newsSourceServices.findUrlBySourceName(this.getDecode(name.getBytes()));
        if (rssList.isEmpty()) {
            return new ResponseEntity<>(new RssListDto(HttpStatus.NOT_FOUND.toString(),
                    "0", "0", null), HttpStatus.NOT_FOUND);
        } else {
            RssListDto rssListDto = new RssListDto(HttpStatus.OK.toString(), String.valueOf(System.currentTimeMillis()),
                    String.valueOf(rssList.size()), rssList);
            return new ResponseEntity<>(rssListDto, HttpStatus.OK);
        }
    }

}

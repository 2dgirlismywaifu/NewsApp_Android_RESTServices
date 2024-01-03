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

import com.notelysia.restservices.config.DecodeString;
import com.notelysia.restservices.model.dto.newsapp.RSSList;
import com.notelysia.restservices.model.entity.newsapp.NewsDetail;
import com.notelysia.restservices.service.newsapp.NewsSourceServices;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/api/v2")
@Tag(name = "News Details List Country", description = "API for News Details List Country")
public class NewsDetailController {
    private final DecodeString decodeString = new DecodeString();
    @Autowired
    private NewsSourceServices newsSourceServices;

    private String getDecode(byte[] data) {
        return decodeString.decodeString(data);
    }

    //This is only for guest user, follow subscribe only for logined user
    @GetMapping("/guest/news-details")
    public ResponseEntity<HashMap<String, List<NewsDetail>>> allNewsSource(
            @Parameter(name = "type", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "type") String type,
            @Parameter(name = "name", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "name") String name) {
        List<NewsDetail> newsDetailList = newsSourceServices.findByUrlTypeAndSourceName(getDecode(type.getBytes()), getDecode(name.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("newsDetails", newsDetailList);
            }
        }, HttpStatus.OK);
    }

    //This is list url for each source
    @GetMapping("/user/news-details/list")
    public ResponseEntity<HashMap<String, List<NewsDetail>>> allNewsSource(
            @Parameter(name = "name", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "name") String name) {
        List<NewsDetail> newsDetailList = newsSourceServices.findBySourceName(getDecode(name.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("List" + getDecode(name.getBytes()), newsDetailList);
            }
        }, HttpStatus.OK);
    }

    //GET URL RSS LIST FOLLOW SOURCE_NAME
    @GetMapping(value = "/user/news-details/rss/list")
    public ResponseEntity<HashMap<String, List<RSSList>>> allRSSList(
            @Parameter(name = "name", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "name") String name) {
        List<RSSList> rssList = newsSourceServices.findUrlBySourceName(getDecode(name.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("RSSList", rssList);
            }
        }, HttpStatus.OK);
    }

}

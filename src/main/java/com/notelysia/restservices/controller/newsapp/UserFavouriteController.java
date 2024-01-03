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
import com.notelysia.restservices.config.RandomNumber;
import com.notelysia.restservices.model.entity.newsapp.SyncNewsFavourite;
import com.notelysia.restservices.model.entity.newsapp.SyncSubscribe;
import com.notelysia.restservices.service.newsapp.SyncServices;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "Standard Account Favourite Controller", description = "API for Standard Account Favourite Controller")
public class UserFavouriteController {
    private final DecodeString decodeString = new DecodeString();
    private SyncSubscribe syncSubscribe;
    private SyncNewsFavourite syncNewsFavourite;
    @Autowired
    private SyncServices syncServices;

    private String getDecode(byte[] data) {
        return decodeString.decodeString(data);
    }

    //Insert source subscribe from user
    @PostMapping(value = "/account/favourite/add")
    public ResponseEntity<HashMap<String, String>> userSourceSubscribe(
            @Parameter(name = "userId", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userId") String userId,
            @Parameter(name = "sourceId", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourceId") String source_id) {
        String sync_id_random = new RandomNumber().generateSSONumber();
        syncSubscribe = new SyncSubscribe(Integer.parseInt(getDecode(sync_id_random.getBytes())),
                Integer.parseInt(getDecode(userId.getBytes())), Integer.parseInt(getDecode(source_id.getBytes())));
        syncServices.saveSubscribe(syncSubscribe);
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("sync_id", String.valueOf(syncSubscribe.getSyncId()));
                put("userId", String.valueOf(syncSubscribe.getUserId()));
                put("source_id", String.valueOf(syncSubscribe.getSourceId()));
                put("status", "success");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //insert news favourite
    //services will use params: userId, url, title, imageUrl, sourceName
    @PostMapping(value = "/account/favourite/news/add")
    public ResponseEntity<HashMap<String, String>> userNewsFavourite(
            @Parameter(name = "userId", description = "Encode it to BASE64 before input", required = true)
            String userId,
            @Parameter(name = "url", description = "Encode it to BASE64 before input", required = true)
            String url,
            @Parameter(name = "title", description = "Encode it to BASE64 before input", required = true)
            String title,
            @Parameter(name = "imageUrl", description = "Encode it to BASE64 before input", required = true)
            String imageUrl,
            @Parameter(name = "pubDate", description = "Encode it to BASE64 before input", required = true)
            String pubDate,
            @Parameter(name = "sourceName", description = "Encode it to BASE64 before input", required = true)
            String sourceName) {

        String favourite_id_random = new RandomNumber().generateSSONumber();
        syncNewsFavourite = new SyncNewsFavourite(Integer.parseInt(getDecode(favourite_id_random.getBytes())),
                Integer.parseInt(getDecode(userId.getBytes())), getDecode(url.getBytes()), getDecode(title.getBytes()),
                getDecode(imageUrl.getBytes()), getDecode(pubDate.getBytes()), getDecode(sourceName.getBytes()));
        syncServices.saveNewsFavourite(syncNewsFavourite);
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("favourite_id", String.valueOf(syncNewsFavourite.getFavouriteId()));
                put("userId", String.valueOf(syncNewsFavourite.getUserId()));
                put("url", syncNewsFavourite.getUrl());
                put("title", syncNewsFavourite.getTitle());
                put("imageUrl", syncNewsFavourite.getImageUrl());
                put("pubdate", syncNewsFavourite.getPubDate());
                put("sourceName", syncNewsFavourite.getSourceName());
                put("status", "success");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //Unsubscribe source from user (delete from tabale)
    //services will use params: userId, source_id
    @DeleteMapping(value = "/account/favourite/delete", params = {"userId", "sourceid"})
    public ResponseEntity<HashMap<String, String>> userSourceUnsubscribe(
            @Parameter(name = "userId", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userId") String userId,
            @Parameter(name = "sourceid", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourceid") String sourceId) {
        syncServices.deleteByUserIdAndSourceId(Integer.parseInt(getDecode(userId.getBytes())), getDecode(sourceId.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("userId", getDecode(userId.getBytes()));
                put("source_id", getDecode(sourceId.getBytes()));
                put("status", "deleted");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //Delete news favourite from user (use params: userId, url, title, imageUrl, sourceName)
    @RequestMapping(value = "/account/favourite/news/delete", params = {"userId", "url", "title", "imageurl", "sourcename"}, method = RequestMethod.DELETE)
    public ResponseEntity<HashMap<String, String>> userNewsFavouriteDelete(
            @Parameter(name = "userId", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userId") String userId,
            @Parameter(name = "url", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "url") String url,
            @Parameter(name = "title", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "title") String title,
            @Parameter(name = "imageUrl", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "imageUrl") String imageUrl,
            @Parameter(name = "sourceName", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourceName") String sourceName) {
        syncServices.deleteNewsFavourite(getDecode(userId.getBytes()), getDecode(url.getBytes()),
                getDecode(title.getBytes()), getDecode(imageUrl.getBytes()), getDecode(sourceName.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("userId", getDecode(userId.getBytes()));
                put("url", getDecode(url.getBytes()));
                put("title", getDecode(title.getBytes()));
                put("imageUrl", getDecode(imageUrl.getBytes()));
                put("sourceName", getDecode(sourceName.getBytes()));
                put("status", "deleted");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //show favourite news with params: userId in sync_news_favourite
    @GetMapping(value = "/account/favourite/news/show", params = {"userId"})
    public ResponseEntity<HashMap<String, List<SyncNewsFavourite>>> userNewsFavouriteShow(
            @RequestParam(value = "userId") String userId) {
        syncServices.findByUserId(Integer.parseInt(getDecode(userId.getBytes())));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("news_favourite", syncServices.findByUserId(Integer.parseInt(getDecode(userId.getBytes()))));
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //check news is favourite or not (use params: userId, url, title, imageUrl, sourceName)
    @GetMapping("/sso/favourite/news/check")
    public ResponseEntity<HashMap<String, String>> userSourceFavouriteCheck(
            @Parameter(name = "userId", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "userId") String userId,
            @Parameter(name = "url", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "url") String url,
            @Parameter(name = "title", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "title") String title,
            @Parameter(name = "imageUrl", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "imageUrl") String imageUrl,
            @Parameter(name = "sourceName", description = "Encode it to BASE64 before input", required = true)
            @RequestParam(value = "sourceName") String sourceName) {
        syncNewsFavourite = syncServices.findSyncNewsFavouriteBy(Integer.parseInt(getDecode(userId.getBytes())), getDecode(url.getBytes()),
                getDecode(title.getBytes()), getDecode(imageUrl.getBytes()), getDecode(sourceName.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("userId", String.valueOf(syncNewsFavourite.getUserId()));
                put("url", syncNewsFavourite.getUrl());
                put("title", syncNewsFavourite.getTitle());
                put("imageUrl", syncNewsFavourite.getImageUrl());
                put("sourceName", syncNewsFavourite.getSourceName());
                put("status", "found");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //Check source news in SYNC_SUBSCRIBE table. use params: userId, source_id. DO NOT USE SYNC_NEWS_FAVOURITE TABLE
    @GetMapping("/account/subscribe/check")
    public ResponseEntity<HashMap<String, String>> userSourceSubscribeCheck(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "sourceid") String source_id) {
        syncSubscribe = syncServices.findByUserIdAndSourceId(Integer.parseInt(getDecode(userId.getBytes())), getDecode(source_id.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                put("userId", String.valueOf(syncSubscribe.getUserId()));
                put("source_id", String.valueOf(syncSubscribe.getSourceId()));
                put("status", "found");
            }
        }, org.springframework.http.HttpStatus.OK);
    }
}

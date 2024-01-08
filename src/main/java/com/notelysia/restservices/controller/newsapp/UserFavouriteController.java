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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/news-app")
@Tag(name = "Standard Account Favourite Controller", description = "API for Standard Account Favourite Controller")
public class UserFavouriteController {
    private final DecodeString decodeString = new DecodeString();
    private SyncSubscribe syncSubscribe;
    private SyncNewsFavourite syncNewsFavourite;
    @Autowired
    private SyncServices syncServices;

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    //Insert source subscribe from user
    @PostMapping(value = "/account/subscribe-source")
    public ResponseEntity<HashMap<String, String>> userSourceSubscribe(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "sourceId") String source_id) {
        String syncIdRandom = new RandomNumber().generateSSONumber();
        this.syncSubscribe = new SyncSubscribe(Integer.parseInt(this.getDecode(syncIdRandom.getBytes())),
                Integer.parseInt(this.getDecode(userId.getBytes())), Integer.parseInt(this.getDecode(source_id.getBytes())), 0, 1);
        this.syncServices.saveSubscribe(this.syncSubscribe);
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("syncId", String.valueOf(UserFavouriteController.this.syncSubscribe.getSyncId()));
                this.put("userId", String.valueOf(UserFavouriteController.this.syncSubscribe.getUserId()));
                this.put("isChecked", "1");
                this.put("sourceId", String.valueOf(UserFavouriteController.this.syncSubscribe.getSourceId()));
                this.put("status", "success");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //Unsubscribe source from user
    //services will use params: userId, source_id
    @DeleteMapping(value = "/account/unsubscribe-news", params = {"userId", "sourceid"})
    public ResponseEntity<HashMap<String, String>> userSourceUnsubscribe(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "sourceId") String sourceId) {
        this.syncServices.deleteByUserIdAndSourceId(Integer.parseInt(this.getDecode(userId.getBytes())), this.getDecode(sourceId.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("userId", UserFavouriteController.this.getDecode(userId.getBytes()));
                this.put("sourceId", UserFavouriteController.this.getDecode(sourceId.getBytes()));
                this.put("status", "deleted");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //insert news favourite
    //services will use params: userId, url, title, imageUrl, sourceName
    @PostMapping(value = "/account/save-news-favourite")
    public ResponseEntity<HashMap<String, String>> userNewsFavourite(
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "url") String url,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "imageUrl") String imageUrl,
            @RequestParam(name = "pubDate") String pubDate,
            @RequestParam(name = "sourceName") String sourceName) {

        String favouriteIdRandom = new RandomNumber().generateSSONumber();
        this.syncNewsFavourite = new SyncNewsFavourite(Integer.parseInt(this.getDecode(favouriteIdRandom.getBytes())),
                Integer.parseInt(this.getDecode(userId.getBytes())), this.getDecode(url.getBytes()), this.getDecode(title.getBytes()),
                this.getDecode(imageUrl.getBytes()), this.getDecode(pubDate.getBytes()), this.getDecode(sourceName.getBytes()), 0);
        this.syncServices.saveNewsFavourite(this.syncNewsFavourite);
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("favourite_id", String.valueOf(UserFavouriteController.this.syncNewsFavourite.getFavouriteId()));
                this.put("userId", String.valueOf(UserFavouriteController.this.syncNewsFavourite.getUserId()));
                this.put("url", UserFavouriteController.this.syncNewsFavourite.getUrl());
                this.put("title", UserFavouriteController.this.syncNewsFavourite.getTitle());
                this.put("imageUrl", UserFavouriteController.this.syncNewsFavourite.getImageUrl());
                this.put("pubDate", UserFavouriteController.this.syncNewsFavourite.getPubDate());
                this.put("sourceName", UserFavouriteController.this.syncNewsFavourite.getSourceName());
                this.put("status", "success");
            }
        }, org.springframework.http.HttpStatus.OK);
    }


    //Delete news favourite from user (use params: userId, url, title, imageUrl, sourceName)
    @RequestMapping(value = "/account/delete-news-favourite", params = {"userId", "url", "title", "imageurl", "sourcename"}, method = RequestMethod.DELETE)
    public ResponseEntity<HashMap<String, String>> userNewsFavouriteDelete(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "url") String url,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "imageUrl") String imageUrl,
            @RequestParam(value = "sourceName") String sourceName) {
        this.syncServices.deleteNewsFavourite(this.getDecode(userId.getBytes()), this.getDecode(url.getBytes()),
                this.getDecode(title.getBytes()), this.getDecode(imageUrl.getBytes()), this.getDecode(sourceName.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("userId", UserFavouriteController.this.getDecode(userId.getBytes()));
                this.put("url", UserFavouriteController.this.getDecode(url.getBytes()));
                this.put("title", UserFavouriteController.this.getDecode(title.getBytes()));
                this.put("imageUrl", UserFavouriteController.this.getDecode(imageUrl.getBytes()));
                this.put("sourceName", UserFavouriteController.this.getDecode(sourceName.getBytes()));
                this.put("status", "deleted");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //show favourite news with params: userId in sync_news_favourite
    @GetMapping(value = "/account/show-news-favourite", params = {"userId"})
    public ResponseEntity<HashMap<String, List<SyncNewsFavourite>>> userNewsFavouriteShow(
            @RequestParam(value = "userId") String userId) {
        this.syncServices.findByUserId(Integer.parseInt(this.getDecode(userId.getBytes())));
        List<SyncNewsFavourite> syncNewsFavourites = this.syncServices.findByUserId(Integer.parseInt(UserFavouriteController.this.getDecode(userId.getBytes())));
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("newsFavourite", syncNewsFavourites);
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //Check source news in SYNC_SUBSCRIBE table. use params: userId, source_id. DO NOT USE SYNC_NEWS_FAVOURITE TABLE
    @GetMapping("/account/check-subscribe")
    public ResponseEntity<HashMap<String, String>> userSourceSubscribeCheck(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "sourceId") String source_id) {
        this.syncSubscribe = this.syncServices.findByUserIdAndSourceId(Integer.parseInt(this.getDecode(userId.getBytes())), this.getDecode(source_id.getBytes()));
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("userId", String.valueOf(UserFavouriteController.this.syncSubscribe.getUserId()));
                this.put("sourceId", String.valueOf(UserFavouriteController.this.syncSubscribe.getSourceId()));
                this.put("isChecked",String.valueOf(UserFavouriteController.this.syncSubscribe.getIsChecked()));
                this.put("isDeleted",String.valueOf(UserFavouriteController.this.syncSubscribe.getIsDeleted()));
                this.put("status", "found");
            }
        }, org.springframework.http.HttpStatus.OK);
    }
}

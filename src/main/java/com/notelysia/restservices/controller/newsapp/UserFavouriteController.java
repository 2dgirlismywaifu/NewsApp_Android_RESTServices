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
import com.notelysia.restservices.model.dto.newsapp.NewsFavouriteDto;
import com.notelysia.restservices.model.entity.newsapp.SyncNewsFavourite;
import com.notelysia.restservices.model.entity.newsapp.SyncSubscribe;
import com.notelysia.restservices.service.newsapp.SyncServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        String syncIdRandom = new RandomNumber().generateRandomNumber();
        this.syncSubscribe = new SyncSubscribe(Integer.parseInt(syncIdRandom),
                Integer.parseInt(userId), Integer.parseInt(source_id), 0, 1);
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
    @DeleteMapping(value = "/account/unsubscribe-source")
    public ResponseEntity<HashMap<String, String>> userSourceUnsubscribe(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "sourceId") String sourceId) {
        this.syncServices.deleteByUserIdAndSourceId(Integer.parseInt(userId), sourceId);
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("userId", userId);
                this.put("isChecked", "0");
                this.put("sourceId", sourceId);
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
            @RequestParam(name = "pubDate") String pubDate) {
        HashMap<String, String> respond = new HashMap<>();
        String favouriteIdRandom = new RandomNumber().generateRandomNumber();
        long total = this.syncServices.findByNewsFavourite(this.getDecode(userId.getBytes()),
                this.getDecode(url.getBytes()));
        if (total > 0) {
            respond.put("status", "found");
            return new ResponseEntity<>(respond, HttpStatus.BAD_REQUEST);

        } else {
            this.syncNewsFavourite = new SyncNewsFavourite(Integer.parseInt(favouriteIdRandom),
                    Integer.parseInt(this.getDecode(userId.getBytes())),
                    this.getDecode(url.getBytes()), this.getDecode(title.getBytes()),
                    this.getDecode(imageUrl.getBytes()), this.getDecode(pubDate.getBytes()), 0);
            this.syncServices.saveNewsFavourite(this.syncNewsFavourite);
            respond.put("favouriteId", String.valueOf(UserFavouriteController.this.syncNewsFavourite.getFavouriteId()));
            respond.put("userId", String.valueOf(UserFavouriteController.this.syncNewsFavourite.getUserId()));
            respond.put("url", UserFavouriteController.this.syncNewsFavourite.getUrl());
            respond.put("title", UserFavouriteController.this.syncNewsFavourite.getTitle());
            respond.put("imageUrl", UserFavouriteController.this.syncNewsFavourite.getImageUrl());
            respond.put("pubDate", UserFavouriteController.this.syncNewsFavourite.getPubDate());
            respond.put("status", "success");
            return new ResponseEntity<>(respond, HttpStatus.OK);
        }
    }

    //Delete news favourite from user
    @DeleteMapping(value = "/account/delete-news-favourite")
    public ResponseEntity<HashMap<String, String>> userNewsFavouriteDelete(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "favouriteId") String favouriteId,
            @RequestParam(value = "title", required = false) String title) {
        if (favouriteId == null || favouriteId.isEmpty()) {
            favouriteId = this.syncServices.findFavoriteId(this.getDecode(userId.getBytes()), this.getDecode(title.getBytes()));
        }
        this.syncServices.deleteNewsFavourite(this.getDecode(userId.getBytes()), this.getDecode(favouriteId.getBytes()));
        String finalFavouriteId = favouriteId;
        return new ResponseEntity<>(new HashMap<>() {
            {
                this.put("userId", UserFavouriteController.this.getDecode(userId.getBytes()));
                this.put("favouriteId", UserFavouriteController.this.getDecode(finalFavouriteId.getBytes()));
                this.put("status", "deleted");
            }
        }, org.springframework.http.HttpStatus.OK);
    }

    //show favourite news with params: userId in sync_news_favourite
    @GetMapping(value = "/account/show-news-favourite")
    public ResponseEntity<NewsFavouriteDto> userNewsFavouriteShow(
            @RequestParam(value = "userId") String userId) {
        this.syncServices.findByUserId(Integer.parseInt(this.getDecode(userId.getBytes())));
        List<SyncNewsFavourite> syncNewsFavourites = this.syncServices.findByUserId(Integer.parseInt(UserFavouriteController.this.getDecode(userId.getBytes())));
        String totalResults = String.valueOf(syncNewsFavourites.size());
        if (syncNewsFavourites.isEmpty()) {
            return new ResponseEntity<>(new NewsFavouriteDto("not-found", String.valueOf(System.currentTimeMillis()), "No favourite news found", totalResults, null), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new NewsFavouriteDto("found", String.valueOf(System.currentTimeMillis()),
                    "Favourite news found", totalResults, syncNewsFavourites), HttpStatus.OK);
        }
    }

    //Check source news in SYNC_SUBSCRIBE table. use params: userId, source_id. DO NOT USE SYNC_NEWS_FAVOURITE TABLE
    @GetMapping("/account/check-subscribe")
    public ResponseEntity<HashMap<String, String>> userSourceSubscribeCheck(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "sourceId") String source_id) {
        HashMap<String, String> respond = new HashMap<>();
        this.syncSubscribe = this.syncServices.findByUserIdAndSourceId(Integer.parseInt(this.getDecode(userId.getBytes())), this.getDecode(source_id.getBytes()));
        if (this.syncSubscribe == null) {
            respond.put("status", "not-found");
            respond.put("time", String.valueOf(System.currentTimeMillis()));
            respond.put("message", "Source NOT found in subscribe list");
            return new ResponseEntity<>(respond, HttpStatus.BAD_REQUEST);
        } else {
            respond.put("status", "found");
            respond.put("time", String.valueOf(System.currentTimeMillis()));
            respond.put("message", "Source found in subscribe list");
            respond.put("syncId", String.valueOf(UserFavouriteController.this.syncSubscribe.getSyncId()));
            respond.put("userId", String.valueOf(UserFavouriteController.this.syncSubscribe.getUserId()));
            respond.put("sourceId", String.valueOf(UserFavouriteController.this.syncSubscribe.getSourceId()));
            respond.put("isChecked", String.valueOf(UserFavouriteController.this.syncSubscribe.getIsChecked()));
            respond.put("isDeleted", String.valueOf(UserFavouriteController.this.syncSubscribe.getIsDeleted()));
            return new ResponseEntity<>(respond, HttpStatus.OK);
        }
    }

    @GetMapping("/account/check-news-favourite")
    public ResponseEntity<HashMap<String, String>> userNewsFavouriteCheck(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "title") String title) {
        HashMap<String, String> respond = new HashMap<>();
        this.syncNewsFavourite = this.syncServices.checkNewsFavouriteOrNot(this.getDecode(userId.getBytes()), this.getDecode(title.getBytes()));
        if (this.syncNewsFavourite == null) {
            respond.put("status", "not-found");
            respond.put("time", String.valueOf(System.currentTimeMillis()));
            respond.put("message", "Source NOT found in favourite list");
            return new ResponseEntity<>(respond, HttpStatus.BAD_REQUEST);
        } else {
            respond.put("status", "found");
            respond.put("time", String.valueOf(System.currentTimeMillis()));
            respond.put("message", "Source found in favourite list");
            respond.put("favouriteId", String.valueOf(UserFavouriteController.this.syncNewsFavourite.getFavouriteId()));
            respond.put("userId", String.valueOf(UserFavouriteController.this.syncNewsFavourite.getUserId()));
            respond.put("url", UserFavouriteController.this.syncNewsFavourite.getUrl());
            respond.put("title", UserFavouriteController.this.syncNewsFavourite.getTitle());
            respond.put("imageUrl", UserFavouriteController.this.syncNewsFavourite.getImageUrl());
            respond.put("pubDate", UserFavouriteController.this.syncNewsFavourite.getPubDate());
            return new ResponseEntity<>(respond, HttpStatus.OK);
        }
    }
}

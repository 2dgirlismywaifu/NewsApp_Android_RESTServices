/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.RandomNumber;
import com.notelysia.newsandroidservices.azure.AzureSQLConnection;
import com.notelysia.newsandroidservices.models.SourceFavourite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class EmailFavouriteController {
    Connection con = null;
    PreparedStatement ps;
    ResultSet rs;
    //Insert source subscribe from user
    @RequestMapping(value = "/account/favourite/add", params = {"userid", "sourceid"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> userSourceSubscribe (
            @RequestParam(value = "userid") String user_id, @RequestParam(value = "sourceid") String source_id) {
        con = new AzureSQLConnection().getConnection();
        String sync_id_random = new RandomNumber().generateSSONumber();
        HashMap<String, String> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("INSERT INTO SYNC_SUBSCRIBE (sync_id, user_id, source_id) VALUES (?, ?, ?)");
            ps.setString(1, sync_id_random);
            ps.setString(2, user_id);
            ps.setString(3, source_id);
            int result = ps.executeUpdate();
            if (result > 0) {
                respond.put("sync_id", sync_id_random);
                respond.put("user_id", user_id);
                respond.put("source_id", source_id);
                respond.put("status", "success");
            } else {
                respond.put("status", "fail");
            }

            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(respond, org.springframework.http.HttpStatus.OK);
    }

    //insert news favourite
    //services will use params: user_id, url, title, image_url, source_name
    @RequestMapping(value = "/account/favourite/news/add", params = {"userid", "url", "title", "imageurl", "pubdate", "sourcename"}, method = RequestMethod.POST)
    public ResponseEntity<HashMap<String, String>> userNewsFavourite (
            @RequestParam(value = "userid") String user_id, @RequestParam(value = "url") String url,
            @RequestParam(value = "title") String title, @RequestParam(value = "imageurl") String image_url,
            @RequestParam(value = "pubdate") String pub_date, @RequestParam(value = "sourcename") String source_name) {
        con = new AzureSQLConnection().getConnection();
        String favourite_id_random = new RandomNumber().generateSSONumber();
        HashMap<String, String> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("INSERT INTO SYNC_NEWS_FAVOURITE (favourite_id, user_id, url, title, image_url, pubdate, source_name) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, favourite_id_random);
            ps.setString(2, user_id);
            ps.setString(3, url);
            ps.setString(4, title);
            ps.setString(5, image_url);
            ps.setString(6, pub_date);
            ps.setString(7, source_name);
            int result = ps.executeUpdate();
            if (result > 0) {
                respond.put("sync_id", favourite_id_random);
                respond.put("user_id", user_id);
                respond.put("url", url);
                respond.put("title", title);
                respond.put("image_url", image_url);
                respond.put("source_name", source_name);
                respond.put("pubdate", pub_date);
                respond.put("status", "success");
            } else {
                respond.put("status", "fail");
            }

            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(respond, org.springframework.http.HttpStatus.OK);
    }
    //Unsubscribe source from user (delete from tabale)
    //services will use params: user_id, source_id
    @RequestMapping(value = "/account/favourite/delete", params = {"userid", "sourceid"}, method = RequestMethod.DELETE)
    public ResponseEntity<HashMap<String, String>> userSourceUnsubscribe (
            @RequestParam(value = "userid") String user_id, @RequestParam(value = "sourceid") String source_id) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("DELETE FROM SYNC_SUBSCRIBE WHERE user_id = ? AND source_id = ?");
            ps.setString(1, user_id);
            ps.setString(2, source_id);
            int result = ps.executeUpdate();
            if (result > 0) {
                respond.put("user_id", user_id);
                respond.put("source_id", source_id);
                respond.put("status", "success");
            } else {
                respond.put("status", "fail");
            }

            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(respond, org.springframework.http.HttpStatus.OK);
    }
    //Delete news favourite from user (use params: user_id, url, title, image_url, source_name)
    @RequestMapping(value = "/account/favourite/news/delete", params = {"userid", "url", "title", "imageurl", "sourcename"}, method = RequestMethod.DELETE)
    public ResponseEntity<HashMap<String, String>> userNewsFavouriteDelete (
            @RequestParam(value = "userid") String user_id, @RequestParam(value = "url") String url,
            @RequestParam(value = "title") String title, @RequestParam(value = "imageurl") String image_url,
            @RequestParam(value = "sourcename") String source_name) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("DELETE FROM SYNC_NEWS_FAVOURITE WHERE user_id = ? AND url = ? AND title = ? AND image_url = ? AND source_name = ?");
            ps.setString(1, user_id);
            ps.setString(2, url);
            ps.setString(3, title);
            ps.setString(4, image_url);
            ps.setString(5, source_name);
            int result = ps.executeUpdate();
            if (result > 0) {
                respond.put("user_id", user_id);
                respond.put("url", url);
                respond.put("title", title);
                respond.put("image_url", image_url);
                respond.put("source_name", source_name);
                respond.put("status", "success");
            } else {
                respond.put("status", "fail");
            }

            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(respond, org.springframework.http.HttpStatus.OK);
    }
    //show favourite news with params: user_id in sync_news_favourite
    @RequestMapping(value = "/account/favourite/news/show", params = {"userid"}, method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, List<SourceFavourite>>> userNewsFavouriteShow (
            @RequestParam(value = "userid") String user_id) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, List<SourceFavourite>> respond = new HashMap<>();
        List<SourceFavourite> sourceFavourites = new ArrayList<>();
        try {
            ps = con.prepareStatement("SELECT * FROM SYNC_NEWS_FAVOURITE WHERE user_id = ?");
            ps.setString(1, user_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                SourceFavourite sourceFavourite = new SourceFavourite();
                sourceFavourite.setFavorite_id(rs.getString("favourite_id"));
                sourceFavourite.setUser_id(rs.getString("user_id"));
                sourceFavourite.setUrl(rs.getString("url"));
                sourceFavourite.setTitle(rs.getString("title"));
                sourceFavourite.setImage_url(rs.getString("image_url"));
                sourceFavourite.setPub_date(rs.getString("pubdate"));
                sourceFavourite.setSource_name(rs.getString("source_name"));
                sourceFavourites.add(sourceFavourite);
            }
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        respond.put("NewsFavourite",sourceFavourites);
        return new ResponseEntity<>(respond, org.springframework.http.HttpStatus.OK);
    }
    //check source is favourite or not (use params: user_id, source_id)
@RequestMapping(value = "/account/favourite/news/check", params = {"userid", "url", "title", "imageurl", "sourcename"}, method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, String>> userSourceFavouriteCheck (
            @RequestParam(value = "userid") String user_id, @RequestParam(value = "url") String url,
            @RequestParam(value = "title") String title, @RequestParam(value = "imageurl") String image_url,
            @RequestParam(value = "sourcename") String source_name) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("SELECT * FROM SYNC_NEWS_FAVOURITE WHERE user_id = ? AND url = ? AND title = ? AND image_url = ? AND source_name = ? ");
            ps.setString(1, user_id);
            ps.setString(2, url);
            ps.setString(3, title);
            ps.setString(4, image_url);
            ps.setString(5, source_name);
            rs = ps.executeQuery();
            if (rs.next()) {
                respond.put("user_id", user_id);
                respond.put("url", url);
                respond.put("title", title);
                respond.put("image_url", image_url);
                respond.put("source_name", source_name);
                respond.put("status", "success");
            } else {
                respond.put("status", "fail");
            }

            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(respond, org.springframework.http.HttpStatus.OK);
    }
    //Check source news in SYNC_SUBSCRIBE table. use params: user_id, source_id. DO NOT USE SYNC_NEWS_FAVOURITE TABLE
    @RequestMapping(value = "/account/subscribe/check", params = {"userid","sourceid"}, method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, String>> userSourceSubscribeCheck (
            @RequestParam(value = "userid") String user_id, @RequestParam(value = "sourceid") String source_id) {
        con = new AzureSQLConnection().getConnection();
        HashMap<String, String> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("SELECT * FROM SYNC_SUBSCRIBE WHERE user_id = ? AND source_id = ?");
            ps.setString(1, user_id);
            ps.setString(2, source_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                respond.put("user_id", user_id);
                respond.put("source_id", source_id);
                respond.put("status", "success");
            } else {
                respond.put("status", "fail");
            }

            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(respond, org.springframework.http.HttpStatus.OK);
    }
}
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

package com.notelysia.newsandroidservices.deprecated;

import com.notelysia.newsandroidservices.util.AzureSQLConnection;
import com.notelysia.newsandroidservices.model.NewsDetail;
import com.notelysia.newsandroidservices.deprecated.RSSList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Deprecated
@RestController
public class NewsDetailsController {
//    Connection con = null;
//    PreparedStatement ps;
//    ResultSet rs;
//    //This is only for guest user, follow subscribe only for logined user
//    @RequestMapping(value = "/guest/newsdetails", params = {"type", "name"}, method = RequestMethod.GET)
//    public ResponseEntity<Map<String, List<NewsDetail>>> allNewsSource(
//            @RequestParam(value = "type") String type
//            , @RequestParam(value = "name") String name) {
//        con = new AzureSQLConnection().getConnection();
//        List<NewsDetail> newsDetailList = new ArrayList<>();
//        Map<String, List<NewsDetail>> respond = new HashMap<>();
//        try {
//            ps = con.prepareStatement("SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND url_type = ? AND NEWS_SOURCE.source_name=?");
//            ps.setString(1, type);
//            ps.setString(2, name);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                NewsDetail newsDetail = new NewsDetail();
//                newsDetail.setSource_id(rs.getString("source_id"));
//                newsDetail.setSource_name(rs.getString("source_name"));
//                newsDetail.setUrl_type(rs.getString("url_type"));
//                newsDetail.setUrl(rs.getString("url"));
//                newsDetailList.add(newsDetail);
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        respond.put("newsDetails", newsDetailList);
//        return new ResponseEntity<>(respond, HttpStatus.OK);
//    }
//
//    //This is list url for each source
//    @RequestMapping(value = "/newsdetails/list", params = {"name"}, method = RequestMethod.GET)
//    public ResponseEntity<Map<String, List<NewsDetail>>> allNewsSource(
//            @RequestParam(value = "name") String name) {
//        con = new AzureSQLConnection().getConnection();
//        List<NewsDetail> sourceDetailsList = new ArrayList<>();
//        Map<String, List<NewsDetail>> respond = new HashMap<>();
//        try {
//            ps = con.prepareStatement("SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND NEWS_SOURCE.source_name=?");
//            ps.setString(1, name);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                NewsDetail newsDetail = new NewsDetail();
//                newsDetail.setSource_id(rs.getString("source_id"));
//                newsDetail.setSource_name(rs.getString("source_name"));
//                newsDetail.setUrl_type(rs.getString("url_type"));
//                newsDetail.setUrl(rs.getString("url"));
//                sourceDetailsList.add(newsDetail);
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        respond.put("List" + name, sourceDetailsList);
//        return new ResponseEntity<>(respond, HttpStatus.OK);
//    }
//    //GET URL RSS LIST FOLLOW SOURCE_NAME
//    @RequestMapping(value = "/newsdetails/rss/list", params = {"name"}, method = RequestMethod.GET)
//    public ResponseEntity<Map<String, List<RSSList>>> allRSSList(
//            @RequestParam(value = "name") String name) {
//        con = new AzureSQLConnection().getConnection();
//        List<RSSList> rssList = new ArrayList<>();
//        Map<String, List<RSSList>> respond = new HashMap<>();
//        try {
//            ps = con.prepareStatement("SELECT NEWS_DETAIL.url_type, NEWS_DETAIL.url, NEWSTYPE_IMAGE.url_image FROM NEWS_DETAIL, NEWS_SOURCE, NEWSTYPE_IMAGE " +
//                    "WHERE NEWS_DETAIL.url_type = NEWSTYPE_IMAGE.url_type and NEWS_DETAIL.source_id = NEWS_SOURCE.source_id and NEWS_SOURCE.source_name = ?");
//            ps.setString(1, name);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                RSSList rss = new RSSList();
//                rss.setUrl_type(rs.getString("url_type"));
//                rss.setUrl(rs.getString("url"));
//                rss.setUrl_image(rs.getString("url_image"));
//                rssList.add(rss);
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        respond.put("RSSList", rssList);
//        return new ResponseEntity<>(respond, HttpStatus.OK);
//    }

}

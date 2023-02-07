package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.azure.AzureSQLConnection;
import com.notelysia.newsandroidservices.models.NewsDetails;
import com.notelysia.newsandroidservices.models.RSSList;
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

@RestController
public class NewsDetailsController {
    Connection con = null;
    PreparedStatement ps;
    ResultSet rs;
    //This is only for guest user, follow subscribe only for logined user
    @RequestMapping(value = "/guest/newsdetails", params = {"type", "name"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsDetails>>> allNewsSource(
            @RequestParam(value = "type") String type
            , @RequestParam(value = "name") String name) {
        con = new AzureSQLConnection().getConnection();
        List<NewsDetails> newsDetailsList = new ArrayList<>();
        Map<String, List<NewsDetails>> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND url_type = ? AND NEWS_SOURCE.source_name=?");
            ps.setString(1, type);
            ps.setString(2, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                NewsDetails newsDetails = new NewsDetails();
                newsDetails.setSource_id(rs.getString("source_id"));
                newsDetails.setSource_name(rs.getString("source_name"));
                newsDetails.setUrl_type(rs.getString("url_type"));
                newsDetails.setUrl(rs.getString("url"));
                newsDetailsList.add(newsDetails);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("newsDetails", newsDetailsList);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    //This is list url for each source
    @RequestMapping(value = "/newsdetails/list", params = {"name"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsDetails>>> allNewsSource(
            @RequestParam(value = "name") String name) {
        con = new AzureSQLConnection().getConnection();
        List<NewsDetails> sourceDetailsList = new ArrayList<>();
        Map<String, List<NewsDetails>> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND NEWS_SOURCE.source_name=?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                NewsDetails newsDetails = new NewsDetails();
                newsDetails.setSource_id(rs.getString("source_id"));
                newsDetails.setSource_name(rs.getString("source_name"));
                newsDetails.setUrl_type(rs.getString("url_type"));
                newsDetails.setUrl(rs.getString("url"));
                sourceDetailsList.add(newsDetails);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("List" + name, sourceDetailsList);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
    //GET URL RSS LIST FOLLOW SOURCE_NAME
    @RequestMapping(value = "/newsdetails/rss/list", params = {"name"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<RSSList>>> allRSSList(
            @RequestParam(value = "name") String name) {
        con = new AzureSQLConnection().getConnection();
        List<RSSList> rssList = new ArrayList<>();
        Map<String, List<RSSList>> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("SELECT NEWS_DETAIL.url_type, NEWS_DETAIL.url, NEWSTYPE_IMAGE.url_image FROM NEWS_DETAIL, NEWS_SOURCE, NEWSTYPE_IMAGE " +
                    "WHERE NEWS_DETAIL.url_type = NEWSTYPE_IMAGE.url_type and NEWS_DETAIL.source_id = NEWS_SOURCE.source_id and NEWS_SOURCE.source_name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                RSSList rss = new RSSList();
                rss.setUrl_type(rs.getString("url_type"));
                rss.setUrl(rs.getString("url"));
                rss.setUrl_image(rs.getString("url_image"));
                rssList.add(rss);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("RSSList", rssList);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

}

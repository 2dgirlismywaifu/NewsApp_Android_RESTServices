package com.notelysia.newsandroidservices.controller;


import com.notelysia.newsandroidservices.azure.AzureSQLConnection;
import com.notelysia.newsandroidservices.models.NewsSource;
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
public class NewsSourceController {

    Connection con = null;
    private final String GET_ALL_NEWS_SOURCE = "SELECT NEWS_SOURCE.source_id, source_name, urlmain ,information, IMAGE_INFORMATION.[image]  FROM NEWS_SOURCE, IMAGE_INFORMATION WHERE NEWS_SOURCE.source_id = IMAGE_INFORMATION.source_id";
    public String getGET_ALL_NEWS_SOURCE() {
        return GET_ALL_NEWS_SOURCE;
    }
    @RequestMapping(value = "/newssource", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsSource>>> allNewsSource() {
        con = new AzureSQLConnection().getConnection();
        Map<String, List<NewsSource>> respond = new HashMap<>();
        List<NewsSource> sourceList = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(getGET_ALL_NEWS_SOURCE());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NewsSource newsSource = new NewsSource();
                newsSource.setSource_id(rs.getString("source_id"));
                newsSource.setSource_name(rs.getString("source_name"));
                newsSource.setSource_url(rs.getString("urlmain"));
                newsSource.setInformation(rs.getString("information"));
                newsSource.setImgae(rs.getString("image"));
                sourceList.add(newsSource);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("newsSource", sourceList);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    @RequestMapping(value = "/account/newssource", params = {"userid"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsSource>>> userNewsSource(
            @RequestParam(value = "userid") String userid
    ) {
        con = new AzureSQLConnection().getConnection();
        Map<String, List<NewsSource>> respond = new HashMap<>();
        List<NewsSource> sourceList = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT NEWS_SOURCE.source_id, source_name, urlmain ,information, IMAGE_INFORMATION.[image]  " +
                    "FROM NEWS_SOURCE, IMAGE_INFORMATION, SYNC_SUBSCRIBE " +
                    "WHERE NEWS_SOURCE.source_id = IMAGE_INFORMATION.source_id AND NEWS_SOURCE.source_id = SYNC_SUBSCRIBE.source_id " +
                    "AND SYNC_SUBSCRIBE.user_id = ?");
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NewsSource newsSource = new NewsSource();
                newsSource.setSource_id(rs.getString("source_id"));
                newsSource.setSource_name(rs.getString("source_name"));
                newsSource.setSource_url(rs.getString("urlmain"));
                newsSource.setInformation(rs.getString("information"));
                newsSource.setImgae(rs.getString("image"));
                sourceList.add(newsSource);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("newsSource", sourceList);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    @RequestMapping(value = "/sso/newssource", params = {"userid"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsSource>>> ssoNewsSource(
            @RequestParam(value = "userid") String userid
    ) {
        con = new AzureSQLConnection().getConnection();
        Map<String, List<NewsSource>> respond = new HashMap<>();
        List<NewsSource> sourceList = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT NEWS_SOURCE.source_id, source_name, urlmain ,information, IMAGE_INFORMATION.[image]  " +
                    "FROM NEWS_SOURCE, IMAGE_INFORMATION, SYNC_SUBSCRIBE_SSO " +
                    "WHERE NEWS_SOURCE.source_id = IMAGE_INFORMATION.source_id AND NEWS_SOURCE.source_id = SYNC_SUBSCRIBE_SSO.source_id " +
                    "AND SYNC_SUBSCRIBE_SSO.user_id = ?");
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NewsSource newsSource = new NewsSource();
                newsSource.setSource_id(rs.getString("source_id"));
                newsSource.setSource_name(rs.getString("source_name"));
                newsSource.setSource_url(rs.getString("urlmain"));
                newsSource.setInformation(rs.getString("information"));
                newsSource.setImgae(rs.getString("image"));
                sourceList.add(newsSource);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("newsSource", sourceList);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
}

package com.notelysia.newsandroidservices.controller;

import com.notelysia.newsandroidservices.AzureSQLConnection;
import com.notelysia.newsandroidservices.models.NewsDetails;
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

    //This is only for guest user, follow subscribe only for logined user
    @RequestMapping(value = "/guest/newsdetails", params = {"type", "name"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsDetails>>> allNewsSource(
            @RequestParam(value = "type") String type
            , @RequestParam(value = "name") String name) {
        con = new AzureSQLConnection().getConnection();
        List<NewsDetails> newsDetailsList = new ArrayList<>();
        Map<String, List<NewsDetails>> respond = new HashMap<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND url_type = ? AND NEWS_SOURCE.source_name=?");
            ps.setString(1, type);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
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
}

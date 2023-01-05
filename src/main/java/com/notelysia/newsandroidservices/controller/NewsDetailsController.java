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
import java.util.List;

@RestController
public class NewsDetailsController {
    Connection con = null;

    //This is only for guest user, follow subscribe only for logined user
    @RequestMapping(value = "/guest/newsdetails", params = "type", method = RequestMethod.GET)
    public ResponseEntity<List<NewsDetails>> allNewsSource(
            @RequestParam(value = "type") String type) {
        con = new AzureSQLConnection().getConnection();
        List<NewsDetails> respond = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM NEWS_DETAIL WHERE url_type = ?");
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NewsDetails newsDetails = new NewsDetails();
                newsDetails.setSource_id(rs.getString("source_id"));
                newsDetails.setUrl_type(rs.getString("url_type"));
                newsDetails.setUrl(rs.getString("url"));
                respond.add(newsDetails);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
}

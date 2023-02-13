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

import com.notelysia.newsandroidservices.azure.AzureSQLConnection;
import com.notelysia.newsandroidservices.models.NewsAPICountry;
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
public class NewsAPICountryController {
    Connection con = null;
    PreparedStatement ps;
    ResultSet rs;
    //get list country
    @RequestMapping(value = "/newsapi/country/list", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsAPICountry>>> allCountryList() {
        con = new AzureSQLConnection().getConnection();
        List<NewsAPICountry> countryList = new ArrayList<>();
        Map<String, List<NewsAPICountry>> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("SELECT * FROM NEWSAPI_COUNTRY");
            rs = ps.executeQuery();
            while (rs.next()) {
                NewsAPICountry newsAPICountry = new NewsAPICountry();
                newsAPICountry.setCountry_id(rs.getString("country_id"));
                newsAPICountry.setCountry_code(rs.getString("country_code"));
                newsAPICountry.setCountry_name(rs.getString("country_name"));
                countryList.add(newsAPICountry);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("countrylist", countryList);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    //get code from country
    @RequestMapping(value = "/newsapi/country/list", params = {"name"}, method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<NewsAPICountry>>> getCountryCode(
            @RequestParam(value = "name") String name) {
        con = new AzureSQLConnection().getConnection();
        List<NewsAPICountry> countryCode = new ArrayList<>();
        Map<String, List<NewsAPICountry>> respond = new HashMap<>();
        try {
            ps = con.prepareStatement("SELECT * FROM NEWSAPI_COUNTRY WHERE country_name = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                NewsAPICountry newsAPICountry = new NewsAPICountry();
                newsAPICountry.setCountry_id(rs.getString("country_id"));
                newsAPICountry.setCountry_code(rs.getString("country_code"));
                newsAPICountry.setCountry_name(rs.getString("country_name"));
                countryCode.add(newsAPICountry);
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        respond.put("countrycode", countryCode);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
}

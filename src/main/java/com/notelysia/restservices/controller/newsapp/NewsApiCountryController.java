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
import com.notelysia.restservices.model.entity.newsapp.NewsAPICountry;
import com.notelysia.restservices.service.newsapp.NewsApiServices;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/news-api")
@Tag(name = "NewsAPI Country", description = "API for NewsAPI Country List")
public class NewsApiCountryController {
    DecodeString decodeString = new DecodeString();
    @Autowired
    private NewsApiServices newsApiServices;

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    @Tag(name = "NewsAPI Country", description = "Get list of all country code")
    @GetMapping(value = "/country/list")
    public ResponseEntity<Map<String, List<NewsAPICountry>>> allCountryList() {
        Map<String, List<NewsAPICountry>> respond = new HashMap<>();
        respond.put("countrylist", this.newsApiServices.findAll());
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    @Tag(name = "NewsAPI Country", description = "Get country code by country name")
    @GetMapping(value = "/country/code")
    public ResponseEntity<Map<String, String>> getCountryCode(
            @Parameter(name = "name", description = "Country Name only required if get country code")
            @RequestParam(value = "name") String name) {
        Map<String, String> respond = new HashMap<>();
        respond.put("countryCode", this.newsApiServices.findByCountryName(this.getDecode(name.getBytes())));
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
}

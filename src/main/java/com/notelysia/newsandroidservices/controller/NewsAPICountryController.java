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

import com.notelysia.newsandroidservices.config.DecodeString;
import com.notelysia.newsandroidservices.jparepo.NewsAPICountryRepo;
import com.notelysia.newsandroidservices.model.NewsAPICountry;
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
@RequestMapping("/api/v2")
@Tag(name = "NewsAPI Country", description = "API for NewsAPI Country List")
public class NewsAPICountryController {
    DecodeString decodeString = new DecodeString();
    private String getDecode (byte[] data) {
        return decodeString.decodeString(data);
    }
    @Autowired
    NewsAPICountryRepo newsAPICountryRepo;

    @GetMapping(value = "/newsapi/country/list")
     public ResponseEntity<Map<String, List<NewsAPICountry>>> allCountryList() {
        Map<String, List<NewsAPICountry>> respond = new HashMap<>();
        respond.put("countrylist", newsAPICountryRepo.findAll());
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
    @GetMapping(value = "/newsapi/country/list", params = {"name"})
    public ResponseEntity<Map<String, List<NewsAPICountry>>> getCountryCode(
            @Parameter(name = "name", description = "Country Name", required = true)
            @RequestParam(value = "name") String name) {
        Map<String, List<NewsAPICountry>> respond = new HashMap<>();
        respond.put("countrycode", newsAPICountryRepo.findByCountry(getDecode(name.getBytes())));
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
}

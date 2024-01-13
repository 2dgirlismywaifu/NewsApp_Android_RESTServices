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

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.notelysia.restservices.config.DecodeString;
import com.notelysia.restservices.model.entity.newsapp.NewsAPICountry;
import com.notelysia.restservices.service.authkey.AuthApiKeyServices;
import com.notelysia.restservices.service.newsapp.NewsApiServices;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/news-api")
@Tag(name = "NewsAPI Country", description = "API for NewsAPI Country List")
public class NewsApiCountryController {
    private static final Logger logger = LogManager.getLogger(NewsApiCountryController.class);
    DecodeString decodeString = new DecodeString();
    @Autowired
    private NewsApiServices newsApiServices;
    @Autowired
    private AuthApiKeyServices authApiKeyServices;
    Properties props = new Properties();
    FileInputStream in;

    private NewsApiClient newsApiClient() throws IOException {
        this.in = new FileInputStream("spring_conf/authkey.properties");
        this.props.load(this.in);
        this.in.close();
        String newApiHeader = new String(Base64.getDecoder().decode(this.props.getProperty("news-api-header-name")));
        String newsApiKey = this.authApiKeyServices.findByNewsApiKey(newApiHeader);
        return new NewsApiClient(newsApiKey);
    }

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    @Tag(name = "NewsAPI Country", description = "Get list of all country code")
    @GetMapping(value = "/country/list")
    public ResponseEntity<Map<String, List<NewsAPICountry>>> allCountryList() {
        Map<String, List<NewsAPICountry>> respond = new HashMap<>();
        respond.put("countryList", this.newsApiServices.findAll());
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

    @GetMapping("/top-headlines")
    public ResponseEntity<Map<String, List<Article>>> getTopHeadlinesNews(
            @RequestParam(value = "keyWord") String keyWord,
            @RequestParam(value = "country") String country,
            @RequestParam(value = "category", required = false) String category
    ) throws IOException, InterruptedException {
        Map<String, List<Article>> respond = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(1);
        this.newsApiClient().getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q(keyWord)
                        .category(category)
                        .language("en")
                        .country(country)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        respond.put("articles", articles);
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        logger.error("NewsAPI Error: " + throwable.getMessage(), throwable.getCause());
                        latch.countDown();
                    }
                }
        );
        latch.await();
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

    @GetMapping("/everything")
    public ResponseEntity<Map<String, List<Article>>> getEverythingNews(
            @RequestParam(value = "keyWord") String keyWord,
            @RequestParam(value = "sortBy") String sortBy
    ) throws IOException, InterruptedException {
        Map<String, List<Article>> respond = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(1);
        this.newsApiClient().getEverything(
                new EverythingRequest.Builder()
                        .q(keyWord)
                        .sortBy(sortBy)
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        respond.put("articles", articles);
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        logger.error("NewsAPI Error: " + throwable.getMessage(), throwable.getCause());
                        latch.countDown();
                    }
                }
        );
        latch.await();
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }
}

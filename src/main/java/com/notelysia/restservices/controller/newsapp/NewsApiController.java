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
import com.notelysia.config.DecodeString;
import com.notelysia.restservices.model.dto.newsapp.NewsApiArticlesDto;
import com.notelysia.restservices.model.dto.newsapp.NewsApiCountryDto;
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
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/news-api")
@Tag(name = "NewsAPI Country", description = "API for NewsAPI Country List")
public class NewsApiController {
    private static final Logger logger = LogManager.getLogger(NewsApiController.class);
    private final Properties props = new Properties();
    DecodeString decodeString = new DecodeString();
    @Autowired
    private NewsApiServices newsApiServices;
    @Autowired
    private AuthApiKeyServices authApiKeyServices;

    private NewsApiClient newsApiClient() throws IOException {
        FileInputStream in = new FileInputStream("spring_conf/authkey.properties");
        this.props.load(in);
        in.close();
        String newApiHeader = new String(Base64.getDecoder().decode(this.props.getProperty("news-api-header-name")));
        String newsApiKey = this.authApiKeyServices.findByNewsApiKey(newApiHeader);
        return new NewsApiClient(newsApiKey);
    }

    private String getDecode(byte[] data) {
        return this.decodeString.decodeString(data);
    }

    @Tag(name = "NewsAPI Country", description = "Get list of all country code")
    @GetMapping(value = "/country/list")
    public ResponseEntity<NewsApiCountryDto> allCountryList() {
        List<NewsAPICountry> countryList = this.newsApiServices.findAll();
        NewsApiCountryDto newsApiCountryDto = new NewsApiCountryDto();
        if (countryList.isEmpty()) {
            newsApiCountryDto.setStatus(HttpStatus.NOT_FOUND.toString());
            newsApiCountryDto.setTime(String.valueOf(System.currentTimeMillis()));
            newsApiCountryDto.setTotalResults("0");
            newsApiCountryDto.setNewsAPICountryList(Collections.emptyList());
            return new ResponseEntity<>(newsApiCountryDto, HttpStatus.NOT_FOUND);
        } else {
            newsApiCountryDto.setStatus(HttpStatus.OK.toString());
            newsApiCountryDto.setTime(String.valueOf(System.currentTimeMillis()));
            newsApiCountryDto.setTotalResults(String.valueOf(countryList.size()));
            newsApiCountryDto.setNewsAPICountryList(countryList);
            return new ResponseEntity<>(newsApiCountryDto, HttpStatus.OK);
        }
    }

    @Tag(name = "NewsAPI Country", description = "Get country code by country name")
    @GetMapping(value = "/country/code")
    public ResponseEntity<Map<String, String>> getCountryCode(
            @Parameter(name = "name", description = "Country Name only required if get country code")
            @RequestParam(value = "name") String name) {
        Map<String, String> respond = new HashMap<>();
        String countryCode = this.newsApiServices.findByCountryName(this.getDecode(name.getBytes()));
        if (countryCode == null) {
            respond.put("status", HttpStatus.NOT_FOUND.toString());
            respond.put("time", String.valueOf(System.currentTimeMillis()));
            respond.put("countryCode", "Country not found");
            return new ResponseEntity<>(respond, HttpStatus.NOT_FOUND);
        } else {
            respond.put("status", HttpStatus.OK.toString());
            respond.put("time", String.valueOf(System.currentTimeMillis()));
            respond.put("countryCode", countryCode);
            return new ResponseEntity<>(respond, HttpStatus.OK);
        }
    }

    @GetMapping("/top-headlines")
    public ResponseEntity<NewsApiArticlesDto> getTopHeadlinesNews(
            @RequestParam(value = "keyWord", required = false) String keyWord,
            @RequestParam(value = "country") String country,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "size", required = false) String size
    ) throws IOException, InterruptedException {
        NewsApiArticlesDto newsApiArticles = new NewsApiArticlesDto();
        CountDownLatch latch = new CountDownLatch(1);
        this.newsApiClient().getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q(keyWord)
                        .category(category)
                        .country(country)
                        .pageSize(20)
                        .page(1)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        newsApiArticles.setStatus(response.getStatus());
                        //get time millis to check how much time it takes to get the response
                        newsApiArticles.setTime(String.valueOf(System.currentTimeMillis()));
                        newsApiArticles.setTotalResults(String.valueOf(response.getTotalResults()));
                        newsApiArticles.setArticles(articles);
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        newsApiArticles.setStatus(HttpStatus.BAD_REQUEST.toString());
                        newsApiArticles.setTime(String.valueOf(System.currentTimeMillis()));
                        newsApiArticles.setTotalResults("0");
                        newsApiArticles.setArticles(Collections.emptyList());
                        logger.error("NewsAPI Error: " + throwable.getMessage(), throwable.getCause());
                        latch.countDown();
                    }
                }
        );
        latch.await();
        return new ResponseEntity<>(newsApiArticles, HttpStatus.OK);
    }

    @GetMapping("/everything")
    public ResponseEntity<NewsApiArticlesDto> getEverythingNews(
            @RequestParam(value = "keyWord", required = false) String keyWord,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "size", required = false) String size
    ) throws IOException, InterruptedException {
        NewsApiArticlesDto newsApiArticles = new NewsApiArticlesDto();
        CountDownLatch latch = new CountDownLatch(1);
        this.newsApiClient().getEverything(
                new EverythingRequest.Builder()
                        .q(keyWord)
                        .sortBy(sortBy)
                        .pageSize(20)
                        .page(1)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<Article> articles = response.getArticles();
                        newsApiArticles.setStatus(response.getStatus());
                        //get time millis to check how much time it takes to get the response
                        newsApiArticles.setTime(String.valueOf(System.currentTimeMillis()));
                        newsApiArticles.setTotalResults(String.valueOf(response.getTotalResults()));
                        newsApiArticles.setArticles(articles);
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        newsApiArticles.setStatus(HttpStatus.BAD_REQUEST.toString());
                        newsApiArticles.setTime(String.valueOf(System.currentTimeMillis()));
                        newsApiArticles.setTotalResults("0");
                        newsApiArticles.setArticles(Collections.emptyList());
                        latch.countDown();
                    }
                }
        );
        latch.await();
        return new ResponseEntity<>(newsApiArticles, HttpStatus.OK);
    }
}

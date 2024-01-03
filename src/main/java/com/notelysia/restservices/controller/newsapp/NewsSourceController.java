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

import com.notelysia.restservices.exception.ResourceNotFound;
import com.notelysia.restservices.model.entity.newsapp.NewsSource;
import com.notelysia.restservices.service.newsapp.NewsSourceServices;
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
@Tag(name = "News Source", description = "API for News Source")
public class NewsSourceController {
    private Map<String, NewsSource> respond;
    @Autowired
    private NewsSourceServices newsSourceServices;

    @GetMapping(value = "/guest/news-source")
    public ResponseEntity<Map<String, List<NewsSource>>> allNewsSource() {
        Map<String, List<NewsSource>> listrespond = new HashMap<>();
        listrespond.put("newsSource", newsSourceServices.findAllNewsSource());
        return new ResponseEntity<>(listrespond, HttpStatus.OK);
    }

    //For user login
    @GetMapping(value = "/account/news-source", params = {"userid"})
    public ResponseEntity<Map<String, NewsSource>> userNewsSource(
            @RequestParam(value = "userid") int userid
    ) throws ResourceNotFound {
        respond = new HashMap<>();
        NewsSource newsSources = newsSourceServices.findByUserId(userid)
                .orElseThrow(() -> new ResourceNotFound("Failed"));
        respond.put("newsSource", newsSources);
        return new ResponseEntity<>(respond, HttpStatus.OK);
    }

}

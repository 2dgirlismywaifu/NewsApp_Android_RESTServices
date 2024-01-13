package com.notelysia.restservices.service.newsapp;

import com.notelysia.restservices.model.dto.newsapp.RssList;
import com.notelysia.restservices.model.entity.newsapp.NewsDetail;
import com.notelysia.restservices.model.entity.newsapp.NewsSource;

import java.util.List;
import java.util.Optional;

public interface NewsSourceServices {
    List<NewsSource> findAllNewsSource();

    Optional<NewsSource> findByUserId(int useId);

    List<NewsDetail> findByUrlTypeAndSourceName(String urlType, String sourceName);

    List<NewsDetail> findBySourceName(String sourceName);

    List<RssList> findUrlBySourceName(String sourceName);
    List<String> guestRssUrlByType(String type);
    List<String> findAllRssUrlByType(String type);
    List<String> findAllRssUrlByTypeWithSynSubscribe(Integer userId, String type);
    List<String> guestAllRssUrl();
    List<String> findAllRssUrl();
    List<String> findAllRssUrlWithSyncSubscribe(Integer userId);
}

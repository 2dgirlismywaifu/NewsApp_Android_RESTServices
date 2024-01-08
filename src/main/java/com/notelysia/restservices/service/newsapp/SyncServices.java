package com.notelysia.restservices.service.newsapp;

import com.notelysia.restservices.model.entity.newsapp.SyncNewsFavourite;
import com.notelysia.restservices.model.entity.newsapp.SyncSubscribe;

import java.util.List;

public interface SyncServices {
    void saveNewsFavourite(SyncNewsFavourite syncNewsFavourite);

    void deleteNewsFavourite(String userId, String url, String title, String imageUrl, String sourceName);

    List<SyncNewsFavourite> findByUserId(int userId);

    void saveSubscribe(SyncSubscribe syncSubscribe);

    void deleteByUserIdAndSourceId(int userId, String sourceId);

    SyncSubscribe findByUserIdAndSourceId(int userId, String sourceId);

}

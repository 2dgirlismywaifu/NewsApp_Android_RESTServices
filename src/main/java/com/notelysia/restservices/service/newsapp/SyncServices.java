package com.notelysia.restservices.service.newsapp;

import com.notelysia.restservices.model.entity.newsapp.SyncNewsFavourite;
import com.notelysia.restservices.model.entity.newsapp.SyncSubscribe;
import java.util.List;

public interface SyncServices {
  long findByNewsFavourite(String userId, String url);

  String findFavoriteId(String userId, String title);

  void saveNewsFavourite(SyncNewsFavourite syncNewsFavourite);

  void deleteNewsFavourite(String userId, String favouriteId);

  List<SyncNewsFavourite> findByUserId(int userId);

  void saveSubscribe(SyncSubscribe syncSubscribe);

  void deleteByUserIdAndSourceId(int userId, String sourceId);

  SyncSubscribe findByUserIdAndSourceId(int userId, String sourceId);

  SyncNewsFavourite checkNewsFavouriteOrNot(String userId, String title);
}

package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.dto.newsapp.RssList;
import com.notelysia.restservices.model.entity.newsapp.NewsDetail;
import com.notelysia.restservices.model.entity.newsapp.NewsSource;
import com.notelysia.restservices.repository.newsapp.NewsDetailRepo;
import com.notelysia.restservices.repository.newsapp.NewsSourceRepo;
import com.notelysia.restservices.service.newsapp.NewsSourceServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

@Service
public class NewsSourceServicesImpl implements NewsSourceServices {
  @Autowired private NewsSourceRepo newsSourceRepo;
  @Autowired private NewsDetailRepo newsDetailRepo;

  @Override
  public List<NewsSource> findAllNewsSource() {
    return this.newsSourceRepo.findAllNewsSource(Limit.of(3));
  }

  @Override
  public List<NewsSource> findByUserId(int useId) {
    return this.newsSourceRepo.findByUserId(useId);
  }

  @Override
  public List<NewsDetail> findByUrlTypeAndSourceName(String urlType, String sourceName) {
    return this.newsDetailRepo.findByUrlTypeAndSourceName(urlType, sourceName);
  }

  @Override
  public List<NewsDetail> findBySourceName(String sourceName) {
    return this.newsDetailRepo.findBySourceName(sourceName);
  }

  @Override
  public List<RssList> findUrlBySourceName(String sourceName) {
    return this.newsDetailRepo.findUrlBySourceName(sourceName);
  }

  @Override
  public List<String> guestRssUrlByType(String type) {
    return this.newsDetailRepo.guestRssUrlByType(type);
  }

  @Override
  public List<String> findAllRssUrlByType(String type) {
    return this.newsDetailRepo.findAllRssUrlByType(type);
  }

  @Override
  public List<String> findAllRssUrlByTypeWithSynSubscribe(Integer userId, String type) {
    return this.newsDetailRepo.findAllRssUrlByTypeWithSynSubscribe(userId, type);
  }

  @Override
  public List<String> findAllRssUrlWithSyncSubscribe(Integer userId, String type) {
    return this.newsDetailRepo.findAllRssUrlWithSyncSubscribe(userId, type);
  }
}

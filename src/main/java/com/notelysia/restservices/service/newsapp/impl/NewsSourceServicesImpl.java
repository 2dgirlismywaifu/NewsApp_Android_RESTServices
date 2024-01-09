package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.dto.newsapp.RSSList;
import com.notelysia.restservices.model.entity.newsapp.NewsDetail;
import com.notelysia.restservices.model.entity.newsapp.NewsSource;
import com.notelysia.restservices.repository.newsapp.NewsDetailRepo;
import com.notelysia.restservices.repository.newsapp.NewsSourceRepo;
import com.notelysia.restservices.service.newsapp.NewsSourceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsSourceServicesImpl implements NewsSourceServices {
    @Autowired
    private NewsSourceRepo newsSourceRepo;
    @Autowired
    private NewsDetailRepo newsDetailRepo;

    @Override
    public List<NewsSource> findAllNewsSource() {
        return this.newsSourceRepo.findAllNewsSource(Limit.of(3));
    }

    @Override
    public Optional<NewsSource> findByUserId(int useId) {
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
    public List<RSSList> findUrlBySourceName(String sourceName) {
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
    public List<String> guestAllRssUrl() {
        return this.newsDetailRepo.guestAllRssUrl();
    }

    @Override
    public List<String> findAllRssUrl() {
        return this.newsDetailRepo.findAllRssUrl();
    }

    @Override
    public List<String> findAllRssUrlWithSyncSubscribe(Integer userId) {
        return this.newsDetailRepo.findAllRssUrlWithSyncSubscribe(userId);
    }
}

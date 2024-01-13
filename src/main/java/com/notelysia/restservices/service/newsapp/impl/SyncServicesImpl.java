package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.entity.newsapp.SyncNewsFavourite;
import com.notelysia.restservices.model.entity.newsapp.SyncSubscribe;
import com.notelysia.restservices.repository.newsapp.SyncNewsFavRepo;
import com.notelysia.restservices.repository.newsapp.SyncSubscribeRepo;
import com.notelysia.restservices.service.newsapp.SyncServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncServicesImpl implements SyncServices {
    @Autowired
    private SyncNewsFavRepo syncNewsFavRepo;
    @Autowired
    private SyncSubscribeRepo syncSubscribeRepo;

    @Override
    public long findByNewsFavourite(String userId, String url) {
        return this.syncNewsFavRepo.findByNewsFavourite(userId, url);
    }

    @Override
    public void saveNewsFavourite(SyncNewsFavourite syncNewsFavourite) {
        this.syncNewsFavRepo.save(syncNewsFavourite);
    }

    @Override
    public void deleteNewsFavourite(String userId, String favouriteId) {
        this.syncNewsFavRepo.deleteNewsFavourite(userId, favouriteId);
    }

    @Override
    public List<SyncNewsFavourite> findByUserId(int userId) {
        return this.syncNewsFavRepo.findByUserId(userId);
    }

    @Override
    public void saveSubscribe(SyncSubscribe syncSubscribe) {
        this.syncSubscribeRepo.save(syncSubscribe);
    }

    @Override
    public void deleteByUserIdAndSourceId(int userId, String sourceId) {
        this.syncSubscribeRepo.deleteByUserIdAndSourceId(userId, sourceId);
    }

    @Override
    public SyncSubscribe findByUserIdAndSourceId(int userId, String sourceId) {
        return this.syncSubscribeRepo.findByUserIdAndSourceId(userId, sourceId);
    }
}

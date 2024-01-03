package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.entity.newsapp.SyncNewsFavourite;
import com.notelysia.restservices.model.entity.newsapp.SyncSubscribe;
import com.notelysia.restservices.repository.SyncNewsFavRepo;
import com.notelysia.restservices.repository.SyncSubscribeRepo;
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
    public void saveNewsFavourite(SyncNewsFavourite syncNewsFavourite) {
        syncNewsFavRepo.save(syncNewsFavourite);
    }

    @Override
    public void deleteNewsFavourite(String userId, String url, String title, String imageUrl, String sourceName) {
        syncNewsFavRepo.deleteNewsFavourite(userId, url, title, imageUrl, sourceName);
    }

    @Override
    public List<SyncNewsFavourite> findByUserId(int userId) {
        return syncNewsFavRepo.findByUserId(userId);
    }

    @Override
    public SyncNewsFavourite findSyncNewsFavouriteBy(int userId, String sourceId, String title, String imageUrl, String sourceName) {
        return syncNewsFavRepo.findSyncNewsFavouriteBy(userId, sourceId, title, imageUrl, sourceName);
    }

    @Override
    public void saveSubscribe(SyncSubscribe syncSubscribe) {
        syncSubscribeRepo.save(syncSubscribe);
    }

    @Override
    public void deleteByUserIdAndSourceId(int userId, String sourceId) {
        syncSubscribeRepo.deleteByUserIdAndSourceId(userId, sourceId);
    }

    @Override
    public SyncSubscribe findByUserIdAndSourceId(int userId, String sourceId) {
        return syncSubscribeRepo.findByUserIdAndSourceId(userId, sourceId);
    }
}

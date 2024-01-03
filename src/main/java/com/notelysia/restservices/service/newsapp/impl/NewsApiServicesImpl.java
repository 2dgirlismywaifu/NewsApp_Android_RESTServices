package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.entity.newsapp.NewsAPICountry;
import com.notelysia.restservices.repository.NewsApiRepo;
import com.notelysia.restservices.service.newsapp.NewsApiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsApiServicesImpl implements NewsApiServices {
    @Autowired
    private NewsApiRepo newsApiRepo;

    @Override
    public List<NewsAPICountry> findAll() {
        return newsApiRepo.findAll();
    }

    @Override
    public String findByCountryName(String country) {
        return newsApiRepo.findByCountry(country);
    }
}

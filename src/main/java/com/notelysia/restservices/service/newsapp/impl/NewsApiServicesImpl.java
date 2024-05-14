package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.entity.newsapp.NewsAPICountry;
import com.notelysia.restservices.repository.newsapp.NewsApiRepo;
import com.notelysia.restservices.service.newsapp.NewsApiServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsApiServicesImpl implements NewsApiServices {
  @Autowired private NewsApiRepo newsApiRepo;

  @Override
  public List<NewsAPICountry> findAll() {
    return this.newsApiRepo.findAll();
  }

  @Override
  public String findByCountryName(String country) {
    return this.newsApiRepo.findByCountry(country);
  }
}

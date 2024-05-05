package com.notelysia.restservices.service.newsapp;

import com.notelysia.restservices.model.entity.newsapp.NewsAPICountry;
import java.util.List;

public interface NewsApiServices {
  List<NewsAPICountry> findAll();

  String findByCountryName(String country);
}

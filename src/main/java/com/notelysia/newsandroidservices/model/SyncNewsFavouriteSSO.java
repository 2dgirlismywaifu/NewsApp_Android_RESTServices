/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notelysia.newsandroidservices.model;

import com.notelysia.newsandroidservices.model.CompositeKey.SyncNewsFavouritePK;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(SyncNewsFavouritePK.class)
@Table(name = "SYNC_NEWS_FAVOURITE_SSO")
public class SyncNewsFavouriteSSO {
    //Instance variables
    @Id
    @Column(name = "favourite_id")
    private int favourite_id;
    @Id
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "url")
    private String url;
    @Column(name = "title")
    private String title;
    @Column(name = "image_url")
    private String image_url;
    @Column(name = "pubdate")
    private String pubdate;
    @Column(name = "source_name")
    private String source_name;

    public SyncNewsFavouriteSSO(int favourite_id, int user_id, String url, String title, String image_url, String pubdate, String source_name) {
        this.favourite_id = favourite_id;
        this.user_id = user_id;
        this.url = url;
        this.title = title;
        this.image_url = image_url;
        this.pubdate = pubdate;
        this.source_name = source_name;
    }

    public SyncNewsFavouriteSSO() {

    }

    public void setFavourite_id(int favourite_id) {
        this.favourite_id = favourite_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }
}

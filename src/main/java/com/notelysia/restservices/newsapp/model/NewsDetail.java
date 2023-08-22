/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.notelysia.restservices.newsapp.model;

import com.notelysia.restservices.newsapp.model.CompositeKey.NewsDetailPK;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(NewsDetailPK.class)
@Table(name = "NEWS_DETAIL")
public class NewsDetail {
    //Instance variables
    @Id
    @Column(name = "source_id")
    private int source_id;
    //private String source_name;
    @Id
    @Column(name = "url_type")
    private String url_type;
    @Column(name = "url", nullable = false)
    private String url;

    public NewsDetail(int source_id, String url_type, String url) {
        this.source_id = source_id;
        //source_name = sourceName;
        this.url_type = url_type;
        this.url = url;
    }
    public NewsDetail() {
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSource_id() {
        return source_id;
    }

    public String getUrl_type() {
        return url_type;
    }

    public String getUrl() {
        return url;
    }
}

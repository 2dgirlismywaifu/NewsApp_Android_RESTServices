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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "NEWSTYPE_IMAGE")
public class NewsTypeImage {
    //Instance variables
    @Id
    @Column(name = "url_type")
    private String url_type;
    @Column(name = "name_type")
    private String name_type;
    @Column(name = "url_image")
    private String url_image;

    public NewsTypeImage(String url_type, String name_type, String url_image) {
        this.url_type = url_type;
        this.name_type = name_type;
        this.url_image = url_image;
    }

    public NewsTypeImage() {

    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    public void setName_type(String name_type) {
        this.name_type = name_type;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}

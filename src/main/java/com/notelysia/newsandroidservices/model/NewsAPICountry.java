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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "NEWSAPI_COUNTRY")
public class NewsAPICountry {
    //53 Countries only used, no China
    @Id
    @Column(name = "country_id")
    private int country_id;
    @Column(name = "country_code", nullable = false)
    private String country_code;
    @Column(name = "country_name", nullable = false)
    private String country_name;

    public NewsAPICountry(int country_id, String country_code, String country_name) {
        this.country_id = country_id;
        this.country_code = country_code;
        this.country_name = country_name;
    }

    public NewsAPICountry() {
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}

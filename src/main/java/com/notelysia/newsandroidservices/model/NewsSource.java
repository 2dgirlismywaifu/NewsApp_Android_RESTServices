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

import jakarta.persistence.*;
import lombok.Getter;
@Getter
@Entity
@Table(name = "NEWS_SOURCE")
@SecondaryTables({
        @SecondaryTable(name = "IMAGE_INFORMATION", pkJoinColumns = @PrimaryKeyJoinColumn(name = "source_id")),
        @SecondaryTable(name = "SYNC_SUBSCRIBE", pkJoinColumns = @PrimaryKeyJoinColumn(name = "source_id")),
        @SecondaryTable(name = "SYNC_SUBSCRIBE_SSO", pkJoinColumns = @PrimaryKeyJoinColumn(name = "source_id"))
})
public class NewsSource {
    //Instance variables
    @Id
    @Column(name = "source_id")
    private int source_id;
    @Column(name = "source_name")
    private String source_name;
    @Column(name = "urlmain")
    private String urlmain;
    @Column(name = "information")
    private String information;
    @Column(name = "image", table = "IMAGE_INFORMATION")
    private String image;

    public NewsSource(int source_id, String source_name, String urlmain, String information, String image) {
        this.source_id = source_id;
        this.source_name = source_name;
        this.urlmain = urlmain;
        this.information = information;
        this.image = image;
    }

    public NewsSource() {
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public void setUrlmain(String urlmain) {
        this.urlmain = urlmain;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

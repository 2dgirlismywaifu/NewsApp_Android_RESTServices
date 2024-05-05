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

package com.notelysia.restservices.model.entity.newsapp;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "news_source")
@SecondaryTables({
  @SecondaryTable(
      name = "image_information",
      pkJoinColumns = @PrimaryKeyJoinColumn(name = "source_id")),
  @SecondaryTable(
      name = "sync_subscribe",
      pkJoinColumns = @PrimaryKeyJoinColumn(name = "source_id")),
  @SecondaryTable(name = "news_detail", pkJoinColumns = @PrimaryKeyJoinColumn(name = "source_id")),
})
public class NewsSource implements Serializable {
  // Instance variables
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "source_id")
  private int sourceId;

  @Column(name = "source_name")
  private String source_name;

  @Column(name = "urlmain")
  private String urlMain;

  @Column(name = "information", length = 4000)
  private String information;

  @Column(name = "image", table = "IMAGE_INFORMATION")
  private String image;
}

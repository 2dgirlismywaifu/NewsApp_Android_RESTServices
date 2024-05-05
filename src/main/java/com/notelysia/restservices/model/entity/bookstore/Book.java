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

package com.notelysia.restservices.model.entity.bookstore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
  @Id
  @Column(name = "book_id")
  private String bookId;

  @Column(name = "book_name")
  private String name;

  @Column(name = "type")
  private String type;

  @Column(name = "author")
  private String author;

  @Column(name = "date_publish")
  private String datepub;

  @Column(name = "publisher")
  private String publisher;

  @Column(name = "date_import")
  private String date;

  @Column(name = "price")
  private String price;

  @Column(name = "amount")
  private String amount;
}

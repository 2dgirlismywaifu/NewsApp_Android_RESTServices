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

import com.notelysia.restservices.model.entity.bookstore.CompositeKey.ProviderPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "Provider")
@IdClass(ProviderPK.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Provider implements Serializable {
    @Id
    @Column(name = "publisher_id")
    private String id;
    @Column(name = "publisher_name")
    private String name;
    @Id
    @Column(name = "book_id")
    private String bookId;
    @Column(name = "phone")
    private String phone;
    @Column(name = "amount")
    private String amount;
    @Column(name = "price")
    private String price;
    @Column(name = "date_import")
    private String date;
    @Column(name = "total")
    private String total;
}

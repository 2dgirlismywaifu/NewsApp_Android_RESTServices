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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Book")
@Getter
@Setter
@AllArgsConstructor
public class Book {
    @Id
    @Column(name = "MaSach")
    private String bookId;
    @Column(name = "TenSach")
    private String name;
    @Column(name = "TheLoai")
    private String type;
    @Column(name = "TacGia")
    private String author;
    @Column(name = "NamXuatBan")
    private String datepub;
    @Column(name = "NhaXuatBan")
    private String publisher;
    @Column(name = "NgayNhap")
    private String date;
    @Column(name = "GiaTri")
    private String price;
    @Column(name = "SoLuong")
    private String amount;
}

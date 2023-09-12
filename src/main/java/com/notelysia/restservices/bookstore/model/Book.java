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

package com.notelysia.restservices.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "Book")
@Getter
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

    public Book() {
    }

    public Book(String bookId, String name, String type, String author, String datepub, String publisher, String date, String price, String amount) {
        this.bookId = bookId;
        this.name = name;
        this.type = type;
        this.author = author;
        this.datepub = datepub;
        this.publisher = publisher;
        this.date = date;
        this.price = price;
        this.amount = amount;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDatepub(String datepub) {
        this.datepub = datepub;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

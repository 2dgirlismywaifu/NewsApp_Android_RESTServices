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

import com.notelysia.restservices.bookstore.model.CompositeKey.ProviderPK;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Provider")
@IdClass(ProviderPK.class)
@Getter
public class Provider {
    @Id
    @Column(name = "MANCC")
    private String id;
    @Column(name = "TeNNCC")
    private String name;
    @Id
    @Column(name = "MaSach")
    private String bookId;
    @Column(name = "SDT")
    private String phone;
    @Column(name = "SoLuong")
    private String amount;
    @Column(name = "GiaTri")
    private String price;
    @Column(name = "NgayNhap")
    private String date;
    @Column(name = "TongGiaTri")
    private String total;

    public Provider() {
    }

    public Provider(String id, String name, String bookId, String phone, String amount, String price, String date, String total) {
        this.id = id;
        this.name = name;
        this.bookId = bookId;
        this.phone = phone;
        this.amount = amount;
        this.price = price;
        this.date = date;
        this.total = total;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

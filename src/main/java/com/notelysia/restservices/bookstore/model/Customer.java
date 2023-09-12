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
@Table(name = "Customer")
@Getter
public class Customer {
    @Id
    @Column(name = "MaKH")
    private String id;
    @Column(name = "HoTenKH")
    private String name;
    @Column(name = "SDT")
    private String phone;
    @Column(name = "ThuHang")
    private String level;
    @Column(name = "NgayLapThe")
    private String date;
    @Column(name = "TongGiaTri")
    private String total;

    public Customer() {
    }

    public Customer(String id, String name, String phone, String level, String date, String total) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.level = level;
        this.date = date;
        this.total = total;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

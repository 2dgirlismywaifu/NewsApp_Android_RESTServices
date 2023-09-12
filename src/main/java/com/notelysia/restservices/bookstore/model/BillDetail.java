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

import com.notelysia.restservices.bookstore.model.CompositeKey.BillDetailPK;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "BillDetail")
@IdClass(BillDetailPK.class)
@Getter
public class BillDetail {
    @Id
    @Column(name = "MaHD")
    private String billId;
    @Id
    @Column(name = "MaSach")
    private String bookId;
    @Column(name = "GiaTri")
    private String price;
    @Column(name = "SoLuong")
    private String amount;
    @Column(name = "ThanhTien")
    private String total;

    public BillDetail() {
    }

    public BillDetail(String billId, String bookId, String price, String amount, String total) {
        this.billId = billId;
        this.bookId = bookId;
        this.price = price;
        this.amount = amount;
        this.total = total;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

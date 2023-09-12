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

import com.notelysia.restservices.bookstore.model.CompositeKey.BillPK;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Bill")
@IdClass(BillPK.class)
@Getter
public class Bill {
    @Id
    @Column(name = "MaHD")
    private String billId;
    @Column(name = "MaKH")
    private String customerId;
    @Id
    @Column(name = "MaNV")
    private String employeeId;
    @Column(name = "NgayMua")
    private String date;
    @Column(name = "ThanhTien")
    private String total;

    public Bill() {
    }

    public Bill(String billId, String customerId, String employeeId, String date, String total) {
        this.billId = billId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.date = date;
        this.total = total;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

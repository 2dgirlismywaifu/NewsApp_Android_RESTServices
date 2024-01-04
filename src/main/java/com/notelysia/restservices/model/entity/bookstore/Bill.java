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

import com.notelysia.restservices.model.entity.bookstore.CompositeKey.BillPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "Bill")
@IdClass(BillPK.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bill implements Serializable {
    @Id
    @Column(name = "bill_id")
    private String billId;
    @Column(name = "customer_id")
    private String customerId;
    @Id
    @Column(name = "employee_id")
    private String employeeId;
    @Column(name = "date_bought")
    private String date;
    @Column(name = "total_money")
    private String total;
}

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

package com.notelysia.restservices.bookstore.model.CompositeKey;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class BillPK implements Serializable {
    private String billId;
    private String employeeId;
    public BillPK(String billId, String employeeId) {
        this.billId = billId;
        this.employeeId = employeeId;
    }
    public void setBillId(String billId) {
        this.billId = billId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillPK that)) return false;
        return getBillId().equals(that.getBillId()) && getEmployeeId().equals(that.getEmployeeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBillId(), getEmployeeId());
    }
}

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

package com.notelysia.restservices.agencystore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "Agency")
@Getter
public class Agency {
    @Id
    @Column(name = "MaDaiLy")
    private String AgentID;
    @Column(name = "TenDaiLy")
    private String name;
    @Column(name = "Loai")
    private String type;
    @Column(name = "DiaChi")
    private String address;
    @Column(name = "Quan")
    private String district;
    @Column(name = "DienThoai")
    private String phone;
    @Column(name = "Email")
    private String email;
    @Column(name = "NgayTiepNhan")
    private String dateApproved;
    @Column(name = "TienNo")
    private String debit;

    public Agency() {
    }

    public Agency(String AgentID, String name, String type, String address, String district, String phone, String email, String dateApproved, String debit) {
        this.AgentID = AgentID;
        this.name = name;
        this.type = type;
        this.address = address;
        this.district = district;
        this.phone = phone;
        this.email = email;
        this.dateApproved = dateApproved;
        this.debit = debit;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateApproved(String dateApproved) {
        this.dateApproved = dateApproved;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }
}

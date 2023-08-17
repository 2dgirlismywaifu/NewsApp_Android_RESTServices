/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.notelysia.newsandroidservices.model;

import com.notelysia.newsandroidservices.model.CompositeKey.SyncSubscribePK;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(SyncSubscribePK.class)
@Table(name = "SYNC_SUBSCRIBE")
public class SyncSubscribe {
    //Instance variables
    @Id
    @Column(name = "sync_id")
    private int sync_id;
    @Id
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "source_id")
    private int source_id;

    public SyncSubscribe(int sync_id, int user_id, int source_id) {
        this.sync_id = sync_id;
        this.user_id = user_id;
        this.source_id = source_id;
    }

    public SyncSubscribe() {

    }

    public void setSync_id(int sync_id) {
        this.sync_id = sync_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public int getSync_id() {
        return sync_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getSource_id() {
        return source_id;
    }
}

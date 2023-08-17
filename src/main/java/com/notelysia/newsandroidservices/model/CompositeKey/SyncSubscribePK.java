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

package com.notelysia.newsandroidservices.model.CompositeKey;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class SyncSubscribePK implements Serializable {
    private int sync_id;
    private int user_id;

    public void setSync_id(int sync_id) {
        this.sync_id = sync_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncSubscribePK that)) return false;
        return getSync_id() == that.getSync_id() && getUser_id() == that.getUser_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSync_id(), getUser_id());
    }
}
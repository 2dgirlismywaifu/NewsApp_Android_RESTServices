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

package com.notelysia.restservices.model.entity.newsapp.CompositeKey;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class SyncNewsFavouritePK implements Serializable {
    private int favouriteId;
    private int userId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SyncNewsFavouritePK that)) return false;
        return this.getFavouriteId() == that.getFavouriteId() && this.getUserId() == that.getUserId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getFavouriteId(), this.getUserId());
    }
}

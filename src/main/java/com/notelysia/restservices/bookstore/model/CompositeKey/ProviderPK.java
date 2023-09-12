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

@Getter
public class ProviderPK implements Serializable {
private String id;
    private String bookId;
    public ProviderPK(String id, String bookId) {
        this.id = id;
        this.bookId = bookId;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProviderPK that)) return false;
        return getId().equals(that.getId()) && getBookId().equals(that.getBookId());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getId(), getBookId());
    }
}

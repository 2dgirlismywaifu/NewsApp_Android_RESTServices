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
public class NewsDetailPK implements Serializable {
    private int source_id;
    private String url_type;

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public void setUrl_type(String url_type) {
        this.url_type = url_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsDetailPK that)) return false;
        return getSource_id() == that.getSource_id() && Objects.equals(getUrl_type(), that.getUrl_type());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource_id(), getUrl_type());
    }
}

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

package com.notelysia.restservices.newsapp.model.CompositeKey;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class ImageInfoPK implements Serializable {
    private int image_id;
    private int source_id;

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageInfoPK that)) return false;
        return getImage_id() == that.getImage_id() && getSource_id() == that.getSource_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImage_id(), getSource_id());
    }
}

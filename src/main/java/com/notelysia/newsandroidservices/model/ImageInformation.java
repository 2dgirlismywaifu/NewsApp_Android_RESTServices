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

import com.notelysia.newsandroidservices.model.CompositeKey.ImageInfoPK;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "IMAGE_INFORMATION")
@IdClass(ImageInfoPK.class)
public class ImageInformation {
    //Instance variables
    @Id
    @Column(name = "image_id")
    private int image_id;
    @Id
    @Column(name = "source_id")
    private int source_id;
    @Column(name = "image", nullable = false)
    private String image;

    public ImageInformation(int image_id, int source_id, String image) {
        this.image_id = image_id;
        this.source_id = source_id;
        this.image = image;
    }

    public ImageInformation() {
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

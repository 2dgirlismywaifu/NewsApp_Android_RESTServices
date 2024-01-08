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

package com.notelysia.restservices.repository.newsapp;

import com.notelysia.restservices.model.entity.newsapp.SyncNewsFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SyncNewsFavRepo extends JpaRepository<SyncNewsFavourite, Integer> {
    //Delete news favourite by user_id, url, title, image_url, source_name
    @Transactional
    @Modifying
    @Query("update SyncNewsFavourite s set s.isDeleted = 1 WHERE s.userId = ?1 AND " +
            "s.favouriteId = ?2")
    void deleteNewsFavourite(String userId, String favouriteId);

    //Get news favourite by user_id
    @Query("SELECT s FROM SyncNewsFavourite s WHERE s.userId = ?1 and s.isDeleted = 0")
    List<SyncNewsFavourite> findByUserId(int userId);

}

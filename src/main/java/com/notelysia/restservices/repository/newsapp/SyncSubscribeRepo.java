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

import com.notelysia.restservices.model.entity.newsapp.SyncSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SyncSubscribeRepo extends JpaRepository<SyncSubscribe, Long> {
    //Delete subscribe by user_id and source_id
    @Transactional
    @Modifying
    @Query("DELETE FROM SyncSubscribe s WHERE s.userId = ?1 AND s.sourceId = ?2")
    void deleteByUserIdAndSourceId(int userId, String sourceId);

    //Get subscribe by user_id and source_id
    @Query("SELECT s FROM SyncSubscribe s WHERE s.userId = ?1 AND s.sourceId = ?2")
    SyncSubscribe findByUserIdAndSourceId(int userId, String sourceId);
}

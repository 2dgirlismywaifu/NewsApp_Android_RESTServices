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

package com.notelysia.restservices.newsapp.jparepo;

import com.notelysia.restservices.newsapp.model.SyncSubscribeSSO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SyncSubscribeSSORepo extends JpaRepository<SyncSubscribeSSO, Long> {
    //Delete subscribe by user_id and source_id
    @Transactional
    @Modifying
    @Query("DELETE FROM SyncSubscribeSSO s WHERE s.user_id = ?1 AND s.source_id = ?2")
    void deleteByUserIdAndSourceId(int user_id, String source_id);

    //Get subscribe by user_id and source_id
    @Query("SELECT s FROM SyncSubscribeSSO s WHERE s.user_id = ?1 AND s.source_id = ?2")
    SyncSubscribeSSO findByUserIdAndSourceId(int user_id, String source_id);
}

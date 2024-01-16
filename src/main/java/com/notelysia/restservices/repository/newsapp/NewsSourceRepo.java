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

import com.notelysia.restservices.model.entity.newsapp.NewsSource;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsSourceRepo extends JpaRepository<NewsSource, Long> {
    //Query to get all news source
    @Query("select newsSource, imageInfo.image FROM NewsSource newsSource, ImageInformation imageInfo " +
            "WHERE newsSource.sourceId = imageInfo.sourceId")
    List<NewsSource> findAllNewsSource(Limit limit);

    //Query to get all news source for the user login with email and password
    @Query("SELECT ns " +
            "FROM NewsSource ns" +
            "         inner join ImageInformation nsi on ns.sourceId = nsi.sourceId" +
            "         LEFT JOIN SyncSubscribe sns" +
            "                   ON ns.sourceId = sns.sourceId" +
            "                       AND sns.userId = ?1")
    List<NewsSource> findByUserId(int useId);
}

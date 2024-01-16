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

import com.notelysia.restservices.model.dto.newsapp.RssList;
import com.notelysia.restservices.model.entity.newsapp.NewsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsDetailRepo extends JpaRepository<NewsDetail, Long> {
    //Get source name and add it to list
    //Query example: SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND url_type = ? AND NEWS_SOURCE.source_name=?
    @Query("SELECT n FROM NewsDetail n, NewsSource ns WHERE n.sourceId = ns.sourceId AND n.urlType = ?1 AND ns.source_name = ?2")
    List<NewsDetail> findByUrlTypeAndSourceName(String urlType, String sourceName);

    //This is list url for each source
    //Query example: "SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND NEWS_SOURCE.source_name=?"
    @Query("SELECT n FROM NewsDetail n, NewsSource ns WHERE n.sourceId = ns.sourceId AND ns.source_name = ?1")
    List<NewsDetail> findBySourceName(String sourceName);

    //GET URL RSS LIST FOLLOW SOURCE_NAME
    @Query("SELECT  new com.notelysia.restservices.model.dto.newsapp.RssList(n.urlType, n.url, ni.urlImage) " +
            "FROM NewsDetail n, NewsSource ns, NewsTypeImage ni " +
            "WHERE n.urlType = ni.urlType AND n.sourceId = ns.sourceId AND ns.source_name = ?1")
    List<RssList> findUrlBySourceName(String sourceName);

    @Query("select nd.url from NewsDetail nd " +
            "inner join NewsSource ns on nd.sourceId = ns.sourceId " +
            "where nd.urlType = ?1 and ns.source_name = 'VNExpress'" +
            "and NOT nd.url = 'not_available'")
    List<String> guestRssUrlByType(String type);
    @Query("select nd.url from NewsDetail nd " +
            "inner join NewsSource ns on nd.sourceId = ns.sourceId " +
            "where nd.urlType = ?1 and NOT nd.url = 'not_available'")
    List<String> findAllRssUrlByType(String type);
    @Query("select nd.url from NewsDetail nd " +
            "inner join NewsSource ns on nd.sourceId = ns.sourceId " +
            "inner join SyncSubscribe s on ns.sourceId = s.sourceId " +
            "where s.userId = ?1 and nd.urlType = ?2 and NOT nd.url = 'not_available'")
    List<String> findAllRssUrlByTypeWithSynSubscribe(Integer userId, String type);

    @Query("select nd.url from NewsDetail nd " +
            "inner join NewsSource ns on nd.sourceId = ns.sourceId " +
            "inner join SyncSubscribe s on ns.sourceId = s.sourceId " +
            "where s.userId = ?1 and nd.urlType = ?2 and nd.url = 'not_available'")
    List<String> findAllRssUrlWithSyncSubscribe(Integer userId, String type);
}

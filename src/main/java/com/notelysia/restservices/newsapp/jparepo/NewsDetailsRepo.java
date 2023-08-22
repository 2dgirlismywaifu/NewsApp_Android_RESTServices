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

import com.notelysia.restservices.newsapp.model.NewsDetail;
import com.notelysia.restservices.newsapp.model.RSSList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsDetailsRepo extends JpaRepository<NewsDetail, Long> {
    //Get source name and add it to list
    //Query example: SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND url_type = ? AND NEWS_SOURCE.source_name=?
    @Query("SELECT n FROM NewsDetail n, NewsSource ns WHERE n.source_id = ns.source_id AND n.url_type = ?1 AND ns.source_name = ?2")
    List<NewsDetail> findByUrlTypeAndSourceName(String urlType, String sourceName);

    //This is list url for each source
    //Query example: "SELECT * FROM NEWS_DETAIL, NEWS_SOURCE WHERE NEWS_DETAIL.source_id = NEWS_SOURCE.source_id AND NEWS_SOURCE.source_name=?"
    @Query("SELECT n FROM NewsDetail n, NewsSource ns WHERE n.source_id = ns.source_id AND ns.source_name = ?1")
    List<NewsDetail> findBySourceName(String sourceName);

    //GET URL RSS LIST FOLLOW SOURCE_NAME
    //Query example: SELECT NEWS_DETAIL.url_type, NEWS_DETAIL.url, NEWSTYPE_IMAGE.url_image FROM NEWS_DETAIL, NEWS_SOURCE, NEWSTYPE_IMAGE " +
    ////                    "WHERE NEWS_DETAIL.url_type = NEWSTYPE_IMAGE.url_type and NEWS_DETAIL.source_id = NEWS_SOURCE.source_id and NEWS_SOURCE.source_name = ?
    @Query("SELECT new com.notelysia.restservices.newsapp.model.RSSList(n.url_type, n.url, ni.url_image) " +
            "FROM NewsDetail n, NewsSource ns, NewsTypeImage ni " +
            "WHERE n.url_type = ni.url_type AND n.source_id = ns.source_id AND ns.source_name = ?1")
    List<RSSList> findUrlBySourceName(String sourceName);

}

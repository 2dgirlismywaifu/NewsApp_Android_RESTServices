/*
 * Copyright By @2dgirlismywaifu (2024) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.notelysia.restservices.repository.newsapp;

import com.notelysia.restservices.model.entity.newsapp.AuthApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthApiKeyRepo extends JpaRepository<AuthApiKey, Long> {
    @Query("select authKey.headerName, authKey.token from AuthApiKey authKey " +
            "where authKey.headerName = ?1 and authKey.token = ?2 and authKey.isEnable = 1")
    Optional<AuthApiKeyRepo> findByHeader(String headerName, String token);

    @Query("update AuthApiKey authKey set authKey.isEnable = 0 where authKey.headerName = ?1 and authKey.token = ?2")
    @Modifying
    @Transactional
    void disableKey(String headerName, String token);
}

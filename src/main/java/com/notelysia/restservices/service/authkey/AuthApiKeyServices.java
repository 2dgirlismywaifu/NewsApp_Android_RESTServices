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

package com.notelysia.restservices.service.authkey;

import com.notelysia.restservices.model.dto.AuthApiKeyDto;
import com.notelysia.restservices.model.entity.authkey.AuthApiKey;

import java.util.List;

public interface AuthApiKeyServices {
    List<AuthApiKey> findByHeader(String headerName);
    String findByNewsApiKey(String headerName);
    void saveAuthApiKey(AuthApiKeyDto authApiKeyDto);
    void disableKey(String headerName, String token);
}

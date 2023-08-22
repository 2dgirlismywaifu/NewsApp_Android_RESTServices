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

import com.notelysia.restservices.newsapp.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserInformationRepo extends JpaRepository<UserInformation, Long> {
    //Update fullname user
    @Transactional
    @Modifying
    @Query("UPDATE UserInformation userInformation " +
            "SET userInformation.name = ?1 WHERE userInformation.user_id = ?2")
    void updateFullname(String fullname, String user_id);
    //Update birthday user
    @Transactional
    @Modifying
    @Query("UPDATE UserInformation userInformation " +
            "SET userInformation.birthday = ?1 WHERE userInformation.user_id = ?2")
    void updateBirthday(String birthday, String user_id);
    //Update gender user
    @Transactional
    @Modifying
    @Query("UPDATE UserInformation userInformation " +
            "SET userInformation.gender = ?1 WHERE userInformation.user_id = ?2")
    void updateGender(String birthday, String user_id);
    //Update avatar user
    @Transactional
    @Modifying
    @Query("UPDATE UserInformation userInformation " +
            "SET userInformation.avatar = ?1 WHERE userInformation.user_id = ?2")
    void updateAvatar(String avatar, String user_id);
}

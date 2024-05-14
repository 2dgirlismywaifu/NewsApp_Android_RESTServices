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

import com.notelysia.restservices.model.entity.newsapp.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserInfoRepo extends JpaRepository<UserInformation, Long> {
  // Update fullname user
  @Transactional
  @Modifying
  @Query(
      "UPDATE UserInformation userInformation SET userInformation.name = ?1 WHERE"
          + " userInformation.userId = ?2 and userInformation.isDeleted = 0")
  void updateFullName(String fullName, String user_id);

  // Update birthday user
  @Transactional
  @Modifying
  @Query(
      "UPDATE UserInformation userInformation SET userInformation.birthday = ?1 WHERE"
          + " userInformation.userId = ?2 and userInformation.isDeleted = 0")
  void updateBirthday(String birthday, String userId);

  // Update gender user
  @Transactional
  @Modifying
  @Query(
      "UPDATE UserInformation userInformation SET userInformation.gender = ?1 WHERE"
          + " userInformation.userId = ?2 and userInformation.isDeleted = 0")
  void updateGender(String gender, String userId);

  // Update avatar user
  @Transactional
  @Modifying
  @Query(
      "UPDATE UserInformation userInformation SET userInformation.avatar = ?1 WHERE"
          + " userInformation.userId = ?2 and userInformation.isDeleted = 0")
  void updateAvatar(String avatar, String userId);
}

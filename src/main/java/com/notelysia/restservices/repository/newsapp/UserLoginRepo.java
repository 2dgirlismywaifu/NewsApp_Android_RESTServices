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

import com.notelysia.restservices.model.entity.newsapp.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserLoginRepo extends JpaRepository<UserLogin, Long> {
    //Get password_hash from email and user_id
    @Query("FROM UserLogin userLogin WHERE userLogin.email = ?1 OR userLogin.userId = ?2")
    Optional<UserLogin> findByEmailOrUserid(String email, String userId);

    //Verify account
    @Transactional
    @Modifying
    @Query("UPDATE UserLogin userLogin " +
            "SET userLogin.verify = ?1 WHERE userLogin.email = ?2")
    void updateVerify(String verify, String email);

    //Sigin in account
    @Query("FROM UserLogin  userLogin, UserInformation userInformation " +
            "WHERE userLogin.userId = userInformation.userId AND userLogin.email = ?1 and userLogin.userToken = ?2")
    Optional<UserLogin> findByEmailAndToken(String email, String userToken);

    //Update password from reset password
    @Transactional
    @Modifying
    @Query("UPDATE UserLogin userLogin " +
            "SET userLogin.userToken = ?1, userLogin.salt = ?2, userLogin.recovery = ?3 " +
            "WHERE userLogin.email = ?4")
    void updatePassword(String password, String salt, String recovery, String email);

    //Check nickname
    @Query("select count(userLogin) FROM UserLogin userLogin WHERE userLogin.nickname = ?1 and userLogin.email = ?2")
    long countNickName(String nickname, String email);

    //Get Recovery Code from email
    @Query("FROM UserLogin userLogin WHERE userLogin.email = ?1")
    Optional<UserLogin> findByRecovery(String recovery);

    //Update recovery code with user_id
    @Transactional
    @Modifying
    @Query("UPDATE UserLogin userLogin " +
            "SET userLogin.recovery = ?1 WHERE userLogin.userId = ?2")
    void updateRecovery(String recovery, String userId);

    //Update nickname user
    @Transactional
    @Modifying
    @Query("UPDATE UserLogin userLogin " +
            "SET userLogin.nickname = ?1 WHERE userLogin.userId = ?2")
    void updateNickname(String nickname, String userId);
}

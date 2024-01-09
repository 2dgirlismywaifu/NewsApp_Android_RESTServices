package com.notelysia.restservices.service.newsapp;

import com.notelysia.restservices.model.entity.newsapp.UserInformation;
import com.notelysia.restservices.model.entity.newsapp.UserPassLogin;
import com.notelysia.restservices.model.entity.newsapp.UserSSO;

import java.util.Optional;

public interface UserServices {
    void saveUser(UserPassLogin userPassLogin);

    void saveSSO(UserSSO userSSO);

    Optional<UserPassLogin> findByEmailOrUserid(String email, String userId);

    void updateVerify(String verify, String email);

    Optional<UserPassLogin> findByEmailOrNickname(String email, String nickname);

    void updatePassword(String password, String salt, String recovery, String email);

    Optional<UserPassLogin> findByNickname(String nickname, String email);

    Optional<UserPassLogin> findByRecovery(String recovery);

    void updateRecovery(String recovery, String userId);

    void updateNickname(String nickname, String userId);

    void saveInformation(UserInformation userInformation);

    void updateFullName(String fullName, String userId);

    void updateBirthday(String birthday, String userId);

    void updateGender(String gender, String userId);

    void updateAvatar(String avatar, String userId);
}

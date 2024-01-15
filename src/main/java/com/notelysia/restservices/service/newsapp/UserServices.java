package com.notelysia.restservices.service.newsapp;

import com.notelysia.restservices.model.entity.newsapp.UserInformation;
import com.notelysia.restservices.model.entity.newsapp.UserLogin;

import java.util.Optional;

public interface UserServices {
    void saveUser(UserLogin userLogin);

    Optional<UserLogin> findByEmailOrUserid(String email, String userId);

    void updateVerify(String verify, String email);

    Optional<UserLogin> findByEmailAndToken(String email, String nickname);
    Optional<UserInformation> findInformationByUserId(String userId);

    void updateUserToken(String userToken, String salt, String recovery, String email);

    long countNickName(String nickname, String email);

    Optional<UserLogin> findByRecovery(String recovery);

    void updateRecovery(String recovery, String userId);

    void updateNickname(String nickname, String userId);

    void saveInformation(UserInformation userInformation);

    void updateFullName(String fullName, String userId);

    void updateBirthday(String birthday, String userId);

    void updateGender(String gender, String userId);

    void updateAvatar(String avatar, String userId);

}

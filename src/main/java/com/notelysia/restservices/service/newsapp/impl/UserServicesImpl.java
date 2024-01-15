package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.entity.newsapp.UserInformation;
import com.notelysia.restservices.model.entity.newsapp.UserLogin;
import com.notelysia.restservices.repository.newsapp.UserInfoRepo;
import com.notelysia.restservices.repository.newsapp.UserLoginRepo;
import com.notelysia.restservices.service.newsapp.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    private UserLoginRepo userLoginRepo;
    @Autowired
    private UserInfoRepo userInfoRepo;

    @Override
    public void saveUser(UserLogin userLogin) {
        this.userLoginRepo.save(userLogin);
    }

    @Override
    public Optional<UserLogin> findByEmailOrUserid(String email, String userId) {
        return this.userLoginRepo.findByEmailOrUserid(email, userId);
    }

    @Override
    public void updateVerify(String verify, String email) {
        this.userLoginRepo.updateVerify(verify, email);
    }

    @Override
    public Optional<UserLogin> findByEmail(String email) {
        return this.userLoginRepo.findByEmail(email);
    }

    @Override
    public Optional<UserInformation> findInformationByUserId(String userId) {
        return this.userInfoRepo.findById(Long.valueOf(userId));
    }

    @Override
    public void updateUserToken(String userToken, String salt, String recovery, String email) {
        this.userLoginRepo.updatePassword(userToken, salt, recovery, email);
    }

    @Override
    public long countNickName(String nickname, String email) {
        return this.userLoginRepo.countNickName(nickname, email);
    }

    @Override
    public Optional<UserLogin> findByRecovery(String recovery) {
        return this.userLoginRepo.findByRecovery(recovery);
    }

    @Override
    public void updateRecovery(String recovery, String userId) {
        this.userLoginRepo.updateRecovery(recovery, userId);
    }

    @Override
    public void updateNickname(String nickname, String userId) {
        this.userLoginRepo.updateNickname(nickname, userId);
    }

    @Override
    public void saveInformation(UserInformation userInformation) {
        this.userInfoRepo.save(userInformation);
    }

    @Override
    public void updateFullName(String fullName, String userId) {
        this.userInfoRepo.updateFullName(fullName, userId);
    }

    @Override
    public void updateBirthday(String birthday, String userId) {
        this.userInfoRepo.updateBirthday(birthday, userId);
    }

    @Override
    public void updateGender(String gender, String userId) {
        this.userInfoRepo.updateGender(gender, userId);
    }

    @Override
    public void updateAvatar(String avatar, String userId) {
        this.userInfoRepo.updateAvatar(avatar, userId);
    }
}

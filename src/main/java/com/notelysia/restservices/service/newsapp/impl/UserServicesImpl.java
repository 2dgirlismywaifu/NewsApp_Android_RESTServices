package com.notelysia.restservices.service.newsapp.impl;

import com.notelysia.restservices.model.entity.newsapp.UserInformation;
import com.notelysia.restservices.model.entity.newsapp.UserPassLogin;
import com.notelysia.restservices.model.entity.newsapp.UserSSO;
import com.notelysia.restservices.repository.SsoLoginRepo;
import com.notelysia.restservices.repository.UserInfoRepo;
import com.notelysia.restservices.repository.UserLoginRepo;
import com.notelysia.restservices.service.newsapp.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServicesImpl implements UserServices {
    @Autowired
    private UserLoginRepo userLoginRepo;
    @Autowired
    private SsoLoginRepo ssoLoginRepo;
    @Autowired
    private UserInfoRepo userInfoRepo;

    @Override
    public void saveUser(UserPassLogin userPassLogin) {
        userLoginRepo.save(userPassLogin);
    }

    @Override
    public void saveSSO(UserSSO userSSO) {
        ssoLoginRepo.save(userSSO);
    }

    @Override
    public Optional<UserPassLogin> findByEmailOrUserid(String email, String userId) {
        return userLoginRepo.findByEmailOrUserid(email, userId);
    }

    @Override
    public void updateVerify(String verify, String email) {
        userLoginRepo.updateVerify(verify, email);
    }

    @Override
    public Optional<UserPassLogin> findByEmailOrNickname(String email, String nickname) {
        return userLoginRepo.findByEmailOrNickname(email, nickname);
    }

    @Override
    public void updatePassword(String password, String salt, String recovery, String email) {
        userLoginRepo.updatePassword(password, salt, recovery, email);
    }

    @Override
    public Optional<UserPassLogin> findByNickname(String nickname, String email) {
        return userLoginRepo.findByNickname(nickname, email);
    }

    @Override
    public Optional<UserPassLogin> findByRecovery(String recovery) {
        return userLoginRepo.findByRecovery(recovery);
    }

    @Override
    public void updateRecovery(String recovery, String userId) {
        userLoginRepo.updateRecovery(recovery, userId);
    }

    @Override
    public void updateNickname(String nickname, String userId) {
        userLoginRepo.updateNickname(nickname, userId);
    }

    @Override
    public void saveInformation(UserInformation userInformation) {
        userInfoRepo.save(userInformation);
    }

    @Override
    public void updateFullName(String fullName, String userId) {
        userInfoRepo.updateFullName(fullName, userId);
    }

    @Override
    public void updateBirthday(String birthday, String userId) {
        userInfoRepo.updateBirthday(birthday, userId);
    }

    @Override
    public void updateGender(String gender, String userId) {
        userInfoRepo.updateGender(gender, userId);
    }

    @Override
    public void updateAvatar(String avatar, String userId) {
        userInfoRepo.updateAvatar(avatar, userId);
    }
}

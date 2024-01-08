package com.notelysia.restservices.service.authkey;

import com.notelysia.restservices.model.dto.AuthApiKeyDto;
import com.notelysia.restservices.model.entity.authkey.AuthApiKey;
import com.notelysia.restservices.repository.authkey.AuthApiKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthApiKeyServicesImpl implements AuthApiKeyServices{
    @Autowired
    private AuthApiKeyRepo authApiKeyRepo;
    @Override
    public List<AuthApiKey> findByHeader(String headerName) {
        return this.authApiKeyRepo.findByHeader(headerName);
    }

    @Override
    public String findByNewsApiKey(String headerName) {
        return this.authApiKeyRepo.findByNewsApiKey(headerName);
    }

    @Override
    public void saveAuthApiKey(AuthApiKeyDto authApiKeyDto) {
        AuthApiKey authApiKey = new AuthApiKey();
        authApiKey.setId(authApiKeyDto.getId());
        authApiKey.setHeaderName(authApiKeyDto.getHeaderName());
        authApiKey.setToken(authApiKey.getToken());
        authApiKey.setIsEnable(1);
        this.authApiKeyRepo.save(authApiKey);
    }

    @Override
    public void disableKey(String headerName, String token) {
        this.authApiKeyRepo.disableKey(headerName, token);
    }
}

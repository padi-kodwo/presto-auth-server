package com.presto.auth.service;


import com.presto.auth.entity.User;

public interface UserStatusManager {
    void updateUserStatus(String username, boolean authSuccessful);
    void blockUser(User user);
}

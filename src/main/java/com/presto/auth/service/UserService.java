package com.presto.auth.service;

import com.presto.auth.dto.CreateUserDto;
import com.presto.auth.dto.EditPasswordDto;
import com.presto.auth.dto.EditUserDto;
import com.presto.auth.dto.SignUpUserDto;
import com.presto.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {

    User getUser(String id);

    Page<User> getAllUsers(Specification<User> spec, Pageable pageable);

    List<User> getUserByIds(List<String> userIds);

    User signUpUser(SignUpUserDto signUpUserDto);

    User createUser(CreateUserDto createUserDto);

    User updateUser(EditUserDto editUserDto);

    void updateUserPassword(EditPasswordDto editPasswordDto);

    void deleteUser(String id);
}

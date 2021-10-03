package com.presto.auth.controller;


import com.presto.auth.domain.response.ApiResponse;
import com.presto.auth.dto.SignUpUserDto;
import com.presto.auth.dto.SocialSignUpUserDto;
import com.presto.auth.dto.UserDto;
import com.presto.auth.entity.User;
import com.presto.auth.interfaces.IsPrestoApp;
import com.presto.auth.service.impl.UserServiceImpl;
import com.presto.auth.util.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    private final UserServiceImpl userService;

    private final ModelMapper modelMapper;

    public AuthController(UserServiceImpl userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @IsPrestoApp
    @PostMapping("/sign_up")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDto> signUpUser(@RequestBody @Valid SignUpUserDto signUpUserDto,
                                           HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: signUpUser");

        User user = userService.signUpUser(signUpUserDto);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        ApiResponse<UserDto> apiResponse= Utils.wrapInApiResponse(userDto, sessionId);

        logger.info("["+ sessionId +"] http response: signUpUser: {}", apiResponse);

        return apiResponse;
    }

    @IsPrestoApp
    @PostMapping("/social/sign_up")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDto> socialSignUpUser(@RequestBody @Valid SocialSignUpUserDto socialSignUpUserDto,
                                                 HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: socialSignUpUser");

        UserDto userDto = modelMapper.map(new User(), UserDto.class);

        ApiResponse<UserDto> apiResponse= Utils.wrapInApiResponse(userDto, sessionId);

        logger.info("["+ sessionId +"] http response: socialSignUpUser: {}", apiResponse);

        return apiResponse;
    }
}

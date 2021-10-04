package com.presto.auth.controller;


import com.presto.auth.domain.response.ApiResponse;
import com.presto.auth.domain.response.PagedContent;
import com.presto.auth.dto.CreateUserDto;
import com.presto.auth.dto.EditUserDto;
import com.presto.auth.dto.UserDto;
import com.presto.auth.entity.User;
import com.presto.auth.service.impl.UserServiceImpl;
import com.presto.auth.spec.ListUserSpec;
import com.presto.auth.spec.SearchUserSpec;
import com.presto.auth.util.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UserServiceImpl userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UserServiceImpl userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/search")
    public ApiResponse<PagedContent<UserDto>> searchUsers(SearchUserSpec searchUserSpec,
                                                          Pageable pageable,
                                                          HttpServletRequest httpServletRequest){
        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: searchUsers");

        Page<User> allUsers = userService.getAllUsers(searchUserSpec, pageable);
        List<UserDto> userDtos = allUsers
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        ApiResponse<PagedContent<UserDto>> apiResponse= Utils.wrapInPagedApiResponse(allUsers, userDtos, sessionId);

        logger.info("["+ sessionId +"] http response: searchUsers: {}", apiResponse);

        return apiResponse;
    }

    @GetMapping("/all")
    public ApiResponse<PagedContent<UserDto>> getAllUsers(ListUserSpec listUserSpec,
                                                  Pageable pageable,
                                                  HttpServletRequest httpServletRequest){

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: getAllUsers");

        Page<User> allUsers = userService.getAllUsers(listUserSpec, pageable);
        List<UserDto> userDtos = allUsers
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        ApiResponse<PagedContent<UserDto>> apiResponse= Utils.wrapInPagedApiResponse(allUsers, userDtos, sessionId);

        logger.info("["+ sessionId +"] http response: getAllUsers: {}", apiResponse);

        return apiResponse;
    }

    @GetMapping("/ids")
    public ApiResponse<List<UserDto>> getUsersByIds(@RequestParam List<String> ids,
                                                    HttpServletRequest httpServletRequest){

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: getUsersByIds", ids);

        List<UserDto> userDtos = userService.getUserByIds(ids)
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        ApiResponse<List<UserDto>> apiResponse= Utils.wrapInApiResponse(userDtos, sessionId);

        logger.info("["+ sessionId +"] http response: getUsersByIds: {}", apiResponse);

        return apiResponse;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ApiResponse<UserDto> getUserById(@PathVariable String id,
                                            HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: getUserById", id);

        User user = userService.getUser(id);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        ApiResponse<UserDto> apiResponse= Utils.wrapInApiResponse(userDto, sessionId);

        logger.info("["+ sessionId +"] http response: getUserById: {}", apiResponse);

        return apiResponse;
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserDto> createUser(@RequestBody @Valid CreateUserDto createUserDto,
                                           HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: createUser", createUserDto);

        User user = userService.createUser(createUserDto);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        ApiResponse<UserDto> apiResponse= Utils.wrapInApiResponse(userDto, sessionId);

        logger.info("["+ sessionId +"] http response: createUser: {}", apiResponse);

        return apiResponse;
    }

    @PutMapping
    @ResponseBody
    public ApiResponse<UserDto> updateUser(@RequestBody @Valid EditUserDto editUserDto,
                                           HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: updateUser", editUserDto);

        User user = userService.updateUser(editUserDto);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        ApiResponse<UserDto> apiResponse= Utils.wrapInApiResponse(userDto, sessionId);

        logger.info("["+ sessionId +"] http response: updateUser: {}", apiResponse);

        return apiResponse;
    }
}

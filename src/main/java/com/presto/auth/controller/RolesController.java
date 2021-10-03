package com.presto.auth.controller;


import com.presto.auth.domain.response.ApiResponse;
import com.presto.auth.domain.response.PagedContent;
import com.presto.auth.dto.RoleDto;
import com.presto.auth.entity.Role;
import com.presto.auth.service.impl.RoleServiceImpl;
import com.presto.auth.spec.RoleSpec;
import com.presto.auth.util.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RolesController {
    private static final Logger logger = LoggerFactory.getLogger(RolesController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleServiceImpl roleService;


    @GetMapping("/all")
    public ApiResponse<PagedContent<RoleDto>> getAllRRoles(RoleSpec roleSpec,
                                                           Pageable pageable,
                                                           HttpServletRequest httpServletRequest){

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: getAllRRoles");

        Page<Role> allRoles = roleService.getAllRoles(roleSpec, pageable);
        List<RoleDto> roleDtos = allRoles
                .stream().
                map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());

        ApiResponse<PagedContent<RoleDto>> apiResponse= Utils.wrapInPagedApiResponse(allRoles, roleDtos, sessionId);


        logger.info("["+ sessionId +"] http response: getAllRRoles: {}", apiResponse);

        return apiResponse;
    }

    @GetMapping(value = "/{code}")
    @ResponseBody
    public ApiResponse<RoleDto> getRoleByCode(@PathVariable int code,
                                            HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("["+ sessionId +"] http request: getRoleByCode ", code);

        Role role = roleService.getRole(code);
        RoleDto roleDto = modelMapper.map(role, RoleDto.class);
        ApiResponse<RoleDto> apiResponse = Utils.wrapInApiResponse(roleDto, sessionId);

        logger.info("["+ sessionId +"] http response: getRoleByCode: {}", apiResponse);

        return apiResponse;
    }
}

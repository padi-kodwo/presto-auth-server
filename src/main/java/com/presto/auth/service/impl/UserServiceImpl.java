package com.presto.auth.service.impl;

import com.presto.auth.config.AuthUserDetails;
import com.presto.auth.domain.TenantContext;
import com.presto.auth.dto.CreateUserDto;
import com.presto.auth.dto.EditPasswordDto;
import com.presto.auth.dto.EditUserDto;
import com.presto.auth.dto.SignUpUserDto;
import com.presto.auth.entity.Role;
import com.presto.auth.entity.User;
import com.presto.auth.enums.UserStatus;
import com.presto.auth.exception.ServiceException;
import com.presto.auth.repository.AccountValidationTokenRepository;
import com.presto.auth.repository.RoleRepository;
import com.presto.auth.repository.UserRepository;
import com.presto.auth.service.UserService;
import com.presto.auth.service.UserStatusManager;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service("user_detail_service")
@CacheConfig(cacheNames = {"users"})
public class UserServiceImpl implements UserService, UserDetailsService, UserStatusManager {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @PersistenceContext
    public EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleServiceImpl roleService;


    private UserRepository userRepository;

    private AccountValidationTokenRepository validationTokenRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository,
                    RoleRepository roleRepository,
                    AccountValidationTokenRepository validationTokenRepository) {

        this.userRepository = userRepository;
        this.validationTokenRepository = validationTokenRepository;
    }

    @Override
    @Transactional
    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ServiceException(100, "user not found by Id: " + id ));
    }

    @Override
    @Transactional
    public Page<User> getAllUsers(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public List<User> getUserByIds(List<String> userIds) {
        Iterable<User> allById = userRepository.findAllById(userIds);

        List<User> foundUsers = new ArrayList<>();
        allById.forEach(foundUsers::add);

        return foundUsers;
    }

    @Override
    @Transactional
    public User createUser(CreateUserDto createUserDto) {

        Optional<User> optionalUser = userRepository.findUserByEmail(createUserDto.getEmail());

        if (optionalUser.isPresent())
            throw new ServiceException(100,
                    "user email already exist");

        List<Role> userRoles = new ArrayList<>();
        createUserDto.getRoles().forEach(roleDto -> userRoles.add(roleService.getRole(roleDto.getCode())));

        User user = modelMapper.map(createUserDto, User.class);
        user.setUserStatus(UserStatus.CLEARED);
        user.setOnboardStatus("created");
        user.setSuccessiveFailedAttempts(0);
        user.setLastLogin(new Timestamp(new Date().getTime()));
        user.setLastSeen(new Timestamp(new Date().getTime()));
        user.setUserStatus(UserStatus.CLEARED);
        user.setRoles(new HashSet<>(userRoles));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User signUpUser(SignUpUserDto signUpUserDto) {

        if (!StringUtils.equals(signUpUserDto.getPassword(), signUpUserDto.getConfirmPassword()))
            throw new ServiceException(100,
                    "password do not match");

        Optional<User> optionalUser = userRepository.findUserByEmail(signUpUserDto.getEmail());

        if (optionalUser.isPresent())
            throw new ServiceException(100,
                    "user email already exist");

        List<Role> userRoles = new ArrayList<>();
        signUpUserDto.getRoles().forEach(roleDto -> userRoles.add(roleService.getRole(roleDto.getCode())));

        User user = modelMapper.map(signUpUserDto, User.class);
        user.setUserStatus(UserStatus.CLEARED);
        user.setOnboardStatus("created");
        user.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));
        user.setLastLogin(new Timestamp(new Date().getTime()));
        user.setLastSeen(new Timestamp(new Date().getTime()));
        user.setUserStatus(UserStatus.CLEARED);
        user.setRoles(new HashSet<>(userRoles));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(EditUserDto editUserDto) {

        List<Role> userRoles = new ArrayList<>();
        editUserDto.getRoles().forEach(roleDto -> userRoles.add(roleService.getRole(roleDto.getCode())));

        User user = getUser(editUserDto.getId());
        user.setPhone(editUserDto.getPhone());
        user.setFirstName(editUserDto.getFirstName());
        user.setLastName(editUserDto.getLastName());
        user.setOtherNames(editUserDto.getOtherNames());

        user.setRoles(new HashSet<>(userRoles));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    //todo: remember to encode password
    @Override
    @Transactional
    public void updateUserPassword(EditPasswordDto editPasswordDto) {
        User user = getUser(editPasswordDto.getId());
        user.setPassword(passwordEncoder.encode(editPasswordDto.getPassword()));

    }
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByPhone(phone);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new AuthUserDetails(user);
        }

        logger.info(">>> USER NOT FOUND: " + phone + " under tenant: " + TenantContext.getCurrentTenant());
        throw new UsernameNotFoundException("Phone: " + phone + " not found");
    }

    @Transactional
    @Override
    public void updateUserStatus(String username, boolean authSuccessful) {
    }

    @Transactional
    @Override
    public void blockUser(User user) {
        user = userRepository.findUserByEmail(user.getEmail())
                .orElseThrow(()->
                        new ServiceException(100,
                                "user not found")
                );

        user.setUserStatus(UserStatus.BLOCKED);

        userRepository.save(user);
    }

    @Transactional
    public long userCount(){
        return userRepository.count();
    }
}
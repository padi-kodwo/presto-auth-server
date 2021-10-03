package com.presto.auth.domain;

import com.presto.auth.dto.SignUpUserDto;
import com.presto.auth.interfaces.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SignUpUserDto userDto = (SignUpUserDto) o;
        return userDto.getPassword().equals(userDto.getConfirmPassword());
    }
}

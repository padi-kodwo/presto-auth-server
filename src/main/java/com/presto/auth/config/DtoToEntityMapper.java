package com.presto.auth.config;


import com.presto.auth.dto.RoleDto;
import com.presto.auth.dto.UserDto;
import com.presto.auth.entity.Role;
import com.presto.auth.entity.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DtoToEntityMapper {

    @Bean
    public ModelMapper modelMapper() {
        PropertyMap<Role, RoleDto> roleMap = new PropertyMap<Role, RoleDto>() {
            protected void configure() {
            }
        };

        PropertyMap<User, UserDto> userMap = new PropertyMap<User, UserDto>() {
            protected void configure() {
            }
        };

        Converter<Timestamp, String> timestampStringConverter = new AbstractConverter<Timestamp, String>() {
            protected String convert(Timestamp source) {

                Date date = new Date(source.getTime());

                DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                return dateFormatUTC.format(date);
            }
        };

        Converter<String, Timestamp> stringTimeStampConverter = new AbstractConverter<String, Timestamp>() {
            protected Timestamp convert(String source) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    Date date = dateFormat.parse(source);
                    return new Timestamp(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(roleMap);
        modelMapper.addMappings(userMap);

        modelMapper.addConverter(timestampStringConverter);
        modelMapper.addConverter(stringTimeStampConverter);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }
}

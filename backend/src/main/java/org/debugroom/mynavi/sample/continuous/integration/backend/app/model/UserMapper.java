package org.debugroom.mynavi.sample.continuous.integration.backend.app.model;

import org.debugroom.mynavi.sample.continuous.integration.common.web.model.UserResource;
import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface UserMapper {

    public static UserResource map(User user){
        return UserResource.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .familyName(user.getFamilyName())
                .loginId(user.getLoginId())
                .isLogin(user.getLogin())
                .address(AddressMapper.map(user.getAddressByUserId()))
                .emailList(user.getEmailsByUserId().stream().map(
                                EmailMapper::map).collect(Collectors.toList()))
                .build();
    }

    public static User mapToEntity(
            org.debugroom.mynavi.sample.continuous.integration.backend.app.model.User user){
        if(Objects.isNull(user.getAddress())){
            user.setAddress(Address.builder().build());
        }
        if(Objects.isNull(user.getEmailList())){
            user.setEmailList(new ArrayList<>());
        }
        return User.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .familyName(user.getFamilyName())
                .loginId(user.getLoginId())
                .isLogin(user.isLogin())
                .addressByUserId(
                        AddressMapper.mapToEntity(user.getAddress()))
                .emailsByUserId(
                        user.getEmailList().stream().map(
                                EmailMapper::mapToEntity).collect(Collectors.toList()))
                .build();
    }

    public static List<User> mapToEntity(
            List<org.debugroom.mynavi.sample.continuous.integration.backend.app.model.User> users){
        return users.stream().map(UserMapper::mapToEntity)
                .collect(Collectors.toList());
    }

    public static List<UserResource> map(List<User> users){
        return users.stream().map(UserMapper::map).collect(Collectors.toList());
    }

}

package com.xuexiang.springbootgrpcapi.utils;

import com.xuexiang.springbootgrpc.XHttpApi;
import com.xuexiang.springbootgrpcapi.model.User;

/**
 *
 *
 * @author XUE
 * @since 2019/4/19 8:51
 */
public final class APIUtils {

    private APIUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static XHttpApi.User toApi(User user) {
        return XHttpApi.User.newBuilder()
                .setUserId(user.getUserId())
                .setLoginName(user.getLoginName())
                .setPassword(user.getName())
                .setName(user.getName())
                .setAge(user.getAge())
                .setGender(user.getGender())
                .setPhone(user.getPhone())
                .build();
    }
}

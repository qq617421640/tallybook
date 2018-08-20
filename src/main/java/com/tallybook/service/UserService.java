package com.tallybook.service;

import com.tallybook.model.User;

/**
 * Created by Administrator on 2018/8/21.
 */
public interface UserService {

    User findOneByPhone(String mobilePhone);

    int login(String mobilePhone, String password);

    void register(String mobilePhone);

    int update(User user);
}

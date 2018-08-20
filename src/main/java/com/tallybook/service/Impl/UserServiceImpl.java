package com.tallybook.service.Impl;

import com.tallybook.mapper.UserMapper;
import com.tallybook.model.User;
import com.tallybook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/8/21.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User findOneByPhone(String mobilePhone) {
        return userMapper.findOneByPhone(mobilePhone);
    }

    @Override
    public int login(String mobilePhone, String password) {
        return userMapper.login(mobilePhone,password);
    }

    @Override
    public void register(String mobilePhone) {
        User user = new User(mobilePhone);
        userMapper.insert(user);
    }

    @Override
    public int update(User user) {
       return userMapper.updatePassword(user);
    }
}

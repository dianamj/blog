package com.zh.blog.service;

import com.zh.blog.po.User;

public interface UserService {

    User checkUser(String username, String password);
}

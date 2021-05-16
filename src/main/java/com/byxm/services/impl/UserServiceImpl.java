package com.byxm.services.impl;

import com.byxm.mapper.UserMapper;
import com.byxm.model.User;
import com.byxm.services.UserService;
import com.byxm.util.TimeUtil;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public User getUser(String username) {

        return userMapper.selectByUsername(username);
    }

    @Override
    public String register(String username, String password) {
        Object o = new SimpleHash("MD5",password,"",1);
        String md5Password = o.toString();
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);
        user.setCreatetime(TimeUtil.getCurrentTime());
        user.setUpdatetime(TimeUtil.getCurrentTime());
        int result = userMapper.insertSelective(user);
        if (result>0){
            return "true";
        }
        return "false";
    }

    @Override
    public int addAmount(String username, Double amount) {
        User user = getUser(username);
        int a = user.getAmount();
        a+=amount;
        user.setAmount(a);
        int result = userMapper.updateByPrimaryKeySelective(user);
        return result;
    }

    @Override
    public int modifyUser(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> getAllUser() {

        return userMapper.selectAll();
    }

    @Override
    public List<User> getUserPage(Integer page, Integer size,String username,String power) {
        PageHelper.startPage(page,size);
        List<User> users = userMapper.manageSelect(username,power);
        return users;
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int getUserCount() {
        return userMapper.selectCount();
    }

    @Override
    public int removeUserByid(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }
}

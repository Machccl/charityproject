package com.byxm.services.impl;

import com.byxm.mapper.OrderMapper;
import com.byxm.model.Order;
import com.byxm.services.OrderService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int addOrder(Order order) {
        int result = orderMapper.insertSelective(order);
        return result;
    }

    @Override
    public List<Order> getOrderByUserName(String username, int page, int size) {
        PageHelper.startPage(page,size," createtime desc");
        List<Order> orders = orderMapper.selectByUserName(username);
        return orders;
    }

    @Override
    public List<Order> getAllByUserName(String username) {
        return orderMapper.selectByUserName(username);
    }

    @Override
    public int getOrderCount() {
        return orderMapper.selectCount();
    }

    @Override
    public List<Order> getOrderPage(Integer page, Integer size, String no, String people, String status) {
        PageHelper.startPage(page,size);
        List<Order> orders = orderMapper.manageSelect(no,people,status);
        return orders;
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int modifyOrder(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public int removeOrderById(Integer id) {
        return orderMapper.deleteByPrimaryKey(id);
    }


}

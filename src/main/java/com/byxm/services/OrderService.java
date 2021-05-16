package com.byxm.services;

import com.byxm.model.Order;

import java.util.List;

public interface OrderService {
    int addOrder(Order order);

    List<Order> getOrderByUserName(String username, int page, int size);

    List<Order> getAllByUserName(String username);

    int getOrderCount();

    List<Order> getOrderPage(Integer page, Integer size, String no, String people, String status);

    Order getOrderById(Integer id);

    int modifyOrder(Order order);

    int removeOrderById(Integer id);
}

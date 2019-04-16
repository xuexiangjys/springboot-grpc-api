package com.xuexiang.springbootgrpcapi.service.impl;

import com.github.pagehelper.PageHelper;
import com.xuexiang.springbootgrpcapi.mapper.OrderInfoMapper;
import com.xuexiang.springbootgrpcapi.model.OrderInfo;
import com.xuexiang.springbootgrpcapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * @author xuexiang
 * @since 2018/7/16 下午12:06
 */
@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;//这里会报错，但是并不会影响

    @Override
    public boolean addOrder(OrderInfo order) {
        return orderInfoMapper.insert(order) > 0;
    }

    @Override
    public boolean deleteOrder(int orderId) {
        return orderInfoMapper.deleteByPrimaryKey(orderId) > 0;
    }

    @Override
    public boolean updateOrder(OrderInfo record) {
        return orderInfoMapper.updateByPrimaryKey(record) > 0;
    }

    @Override
    public List<OrderInfo> findAllOrder(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return orderInfoMapper.selectAll();
    }

    @Override
    public List<OrderInfo> findAllOrder() {
        return orderInfoMapper.selectAll();
    }

    @Override
    public List<OrderInfo> queryOrder(int userId) {
        Condition condition = new Condition(OrderInfo.class);
        condition.createCriteria().andEqualTo("userId", userId);
        return orderInfoMapper.selectByExample(condition);
    }
}

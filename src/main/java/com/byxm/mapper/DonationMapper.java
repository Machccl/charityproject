package com.byxm.mapper;

import com.byxm.model.Donation;

import java.util.List;

public interface DonationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Donation record);

    int insertSelective(Donation record);

    Donation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Donation record);

    int updateByPrimaryKey(Donation record);

    List<Donation> selectAllByUserName(String username);

    int selectCount();
}
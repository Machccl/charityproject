package com.byxm.services.impl;

import com.byxm.mapper.DonationMapper;
import com.byxm.model.Donation;
import com.byxm.model.Order;
import com.byxm.services.DonationService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private DonationMapper donationMapper;

    @Override
    public int createDonation(Donation donation) {
        return donationMapper.insertSelective(donation);
    }

    @Override
    public List<Donation> getDonationByUserName(String username, Integer page, Integer size) {
        PageHelper.startPage(page,size," createtime desc");
        List<Donation> donations = donationMapper.selectAllByUserName(username);
        return donations;
    }

    @Override
    public List<Donation> getAllByUserName(String username) {
        return donationMapper.selectAllByUserName(username);
    }

    @Override
    public int getDonationCount() {
        return donationMapper.selectCount();
    }


}

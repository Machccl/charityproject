package com.byxm.services;

import com.byxm.model.Donation;
import com.byxm.model.Order;

import java.util.List;

public interface DonationService {
    int createDonation(Donation donation);

    List<Donation> getDonationByUserName(String username, Integer page, Integer size);

    List<Donation> getAllByUserName(String username);

    int getDonationCount();
}

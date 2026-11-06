package com.example.service;

import org.springframework.stereotype.Service;
import com.example.domain.*;
import com.example.repository.FlexTimeAccountRepository;

@Service
public class FlexTimeService {

    private final FlexTimeAccountRepository accounts;

    public FlexTimeService(FlexTimeAccountRepository accounts) {
        this.accounts = accounts;
    }

    /** Balance in minutes (0 if the user has no account yet). */
    public int getBalanceMinutes(User user) {
        throw new UnsupportedOperationException("TODO person 3");
    }

    /** Adds (worked minutes - target minutes) of the approved sheet to the account.
     *  Target: 8 h (480 min) per working day (Mon-Fri) of the month. */
    public void applyApprovedSheet(TimeSheet sheet) {
        throw new UnsupportedOperationException("TODO person 3");
    }

    /** Soft rule: true if the balance is below -100 h or above +100 h. */
    public static boolean isOutOfRange(int balanceMinutes) {
        throw new UnsupportedOperationException("TODO person 3");
    }
}
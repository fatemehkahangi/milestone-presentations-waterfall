package com.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.YearMonth;
import org.springframework.transaction.annotation.Transactional;

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
    	return accounts.findByUser(user)
                .map(FlexTimeAccount::getBalanceMinutes)
                .orElse(0);
    }

    /** Adds (worked minutes - target minutes) of the approved sheet to the account.
     *  Target: 8 h (480 min) per working day (Mon-Fri) of the month. */

	@Transactional
	public void applyApprovedSheet(TimeSheet sheet) {
	
	    // 1. Find or create the employee's flex time account
	    User employee = sheet.getEmployee();
	

		FlexTimeAccount account;
		if (accounts.findByUser(employee).isPresent()) {
		    account = accounts.findByUser(employee).get();
		} else {
		    account = new FlexTimeAccount();
		    account.setUser(employee);
		}

	
	    // 2. Calculate total worked minutes
		int workedMinutes = 0;
		
		for (TimeEntry entry : sheet.getEntries()) {
		    workedMinutes += entry.getWorkedMinutes();
		}

	
	    // 3. Count working days (Monday to Friday)
	    YearMonth month = YearMonth.of(sheet.getYear(), sheet.getMonth());
	
	    int workingDays = 0;
	
	    for (int day = 1; day <= month.lengthOfMonth(); day++) {
	        LocalDate date = month.atDay(day);
	        DayOfWeek dayOfWeek = date.getDayOfWeek();
	
	        if (dayOfWeek != DayOfWeek.SATURDAY
	                && dayOfWeek != DayOfWeek.SUNDAY) {
	            workingDays++;
	        }
	    }
	
	    // 4. Calculate the monthly target
	    int targetMinutes = workingDays * 480;
	
	    // 5. Update the flex time balance
	    int newBalance = account.getBalanceMinutes()
	            + workedMinutes - targetMinutes;
	
	    account.setBalanceMinutes(newBalance);
	    account.setLastUpdated(LocalDateTime.now());
	
	    // 6. Save the account
	    accounts.save(account);
	}


    /** Soft rule: true if the balance is below -100 h or above +100 h. */
    public static boolean isOutOfRange(int balanceMinutes) {
    	return balanceMinutes > 6000 || balanceMinutes < -6000;
    }
}
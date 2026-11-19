package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.domain.*;
import com.example.repository.FlexTimeAccountRepository;

@ExtendWith(MockitoExtension.class)
class FlexTimeServiceMockitoTest {

    @Mock private FlexTimeAccountRepository accounts;

    @InjectMocks private FlexTimeService service;

    @Test
    void getBalanceMinutes_returnsAccountBalance() {
        User user = new User();
        FlexTimeAccount account = new FlexTimeAccount();
        account.setBalanceMinutes(120);
        when(accounts.findByUser(user)).thenReturn(Optional.of(account));

        assertEquals(120, service.getBalanceMinutes(user));
    }
}
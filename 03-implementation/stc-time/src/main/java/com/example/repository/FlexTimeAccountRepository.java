package com.example.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.domain.*;

public interface FlexTimeAccountRepository extends JpaRepository<FlexTimeAccount, Long> {
    Optional<FlexTimeAccount> findByUser(User user);
}
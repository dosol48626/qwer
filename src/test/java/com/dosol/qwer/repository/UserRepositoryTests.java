package com.dosol.qwer.repository;

import com.dosol.qwer.domain.user.Role;
import com.dosol.qwer.domain.user.User;
import com.dosol.qwer.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest

public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testBaseEntity() {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .nickname("tester")
                .role(Role.USER)
                .build();

        userRepository.save(user);

    }
}

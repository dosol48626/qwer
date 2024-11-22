package com.dosol.qwer.repository.user;

import com.dosol.qwer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 이름(username)으로 사용자 조회
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    // 닉네임으로 사용자 조회
    Optional<User> findByNickname(String nickname);

    // 기타 필요한 쿼리 메서드를 추가할 수 있습니다.
}

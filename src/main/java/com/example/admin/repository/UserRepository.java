package com.example.admin.repository;

import com.example.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 사용자 상태로 사용자 리스트 조회
    List<User> findByStatus(User.Status status);
}
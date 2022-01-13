package com.raf.user_sevice.repository;

import com.raf.user_sevice.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
}

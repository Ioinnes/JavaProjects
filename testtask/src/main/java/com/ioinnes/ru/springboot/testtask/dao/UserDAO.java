package com.ioinnes.ru.springboot.testtask.dao;

import com.ioinnes.ru.springboot.testtask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {
    List<User> findByStatusAndTimestampStatusAfterOrderByTimestampStatus(String status, Timestamp statusTimestamp);
    List<User> findByStatusOrderByTimestampStatus(String status);
    List<User> findByTimestampStatusAfterOrderByStatusAscTimestampStatusAsc(Timestamp statusTimestamp);
}

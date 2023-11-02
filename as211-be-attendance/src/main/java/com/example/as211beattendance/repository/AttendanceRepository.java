package com.example.as211beattendance.repository;

import com.example.as211beattendance.domain.model.attendance;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AttendanceRepository extends ReactiveCrudRepository<attendance, Integer> {

}

package com.example.as211beattendance.service.impl;

import com.example.as211beattendance.domain.dto.AttemdanceRequestDto;
import com.example.as211beattendance.domain.dto.AttendanceResponseDto;
import com.example.as211beattendance.domain.mapper.AttendanceMapper;
import com.example.as211beattendance.exception.ResourceNotFoundException;
import com.example.as211beattendance.repository.AttendanceRepository;
import com.example.as211beattendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.as211beattendance.domain.mapper.AttendanceMapper.toModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    @Override
    public Mono<AttendanceResponseDto> findById(Integer id) {
        return this.attendanceRepository.findById(id)
                .map(AttendanceMapper::toDto);
    }

    @Override
    public Flux<AttendanceResponseDto> findAll() {
        return this.attendanceRepository.findAll()
                .map(AttendanceMapper::toDto);
    }

    @Override
    public Mono<AttendanceResponseDto> create(AttemdanceRequestDto request) {
        return this.attendanceRepository.save(toModel(request))
                .map(AttendanceMapper::toDto);
    }

    @Override
    public Mono<AttendanceResponseDto> update(AttemdanceRequestDto request, Integer id) {
        return this.attendanceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Asistencia no encontrada")))
                .flatMap(attendance -> this.attendanceRepository.save(toModel(attendance.getId(),request)))
                .map(AttendanceMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return this.attendanceRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Asistencia no encontrada")))
                .flatMap(attendance -> {
                    attendance.setActive("I");
                    return this.attendanceRepository.save(attendance);
                })
                .doOnSuccess(unused -> log.info("se elimino el ID " + id))
                .then();
    }
}

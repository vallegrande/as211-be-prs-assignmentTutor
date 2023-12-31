package com.soa.canete.transaccional_allocation_soa_canete.service.impl;

import com.soa.canete.transaccional_allocation_soa_canete.domain.dto.DataTeenFuncionaryTransaccional;
import com.soa.canete.transaccional_allocation_soa_canete.domain.dto.Funcionary.FuncionaryResponseDto;
import com.soa.canete.transaccional_allocation_soa_canete.domain.dto.Teen.TeenResponseDto;
import com.soa.canete.transaccional_allocation_soa_canete.domain.dto.Transaccional.TransaccionalAllocationRequestDto;
import com.soa.canete.transaccional_allocation_soa_canete.domain.dto.Transaccional.TransaccionalAllocationResponseDto;
import com.soa.canete.transaccional_allocation_soa_canete.domain.mapper.TransaccionalAllocationMapper;
import com.soa.canete.transaccional_allocation_soa_canete.domain.model.TransaccionalAllocation;
import com.soa.canete.transaccional_allocation_soa_canete.exception.ResourceNotFoundException;
import com.soa.canete.transaccional_allocation_soa_canete.repository.TransaccionalAllocationRepository;
import com.soa.canete.transaccional_allocation_soa_canete.service.TransaccionalAllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.soa.canete.transaccional_allocation_soa_canete.domain.mapper.TransaccionalAllocationMapper.toModel;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransaccionalAllocationImpl implements TransaccionalAllocationService {

    @Autowired private WebClient.Builder webClientBuilder;

    final TransaccionalAllocationRepository transaccionalAllocationRepository;

    @Override
    public Mono<DataTeenFuncionaryTransaccional> findById(Integer id_funcionaryteend) {
        Mono<TransaccionalAllocation> family = transaccionalAllocationRepository.findById(id_funcionaryteend);
        return family.flatMap(dataFamily -> {
            Mono<FuncionaryResponseDto> funcionaryResponseDtoMono = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8084/api/funcionaryData/" + dataFamily.getId_funcionary())
                    .retrieve()
                    .bodyToMono(FuncionaryResponseDto.class);
            Mono<TeenResponseDto> teenResponseDtoMono = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/api/teenData/" + dataFamily.getId_adolescente())
                    .retrieve()
                    .bodyToMono(TeenResponseDto.class);
            return funcionaryResponseDtoMono.zipWith(teenResponseDtoMono).map(dataGeneral -> {
                FuncionaryResponseDto legalGuardianResponseDtoData = dataGeneral.getT1();
                TeenResponseDto adolescentResponseDtoData = dataGeneral.getT2();
                DataTeenFuncionaryTransaccional dataTeenFuncionaryTransaccional = new DataTeenFuncionaryTransaccional();
                dataTeenFuncionaryTransaccional.setTransaccionalAllocation(dataFamily);
                dataTeenFuncionaryTransaccional.setTeenResponseDto(adolescentResponseDtoData);
                dataTeenFuncionaryTransaccional.setFuncionaryResponseDto(legalGuardianResponseDtoData);
                return dataTeenFuncionaryTransaccional;
            });
        });
    }

    @Override
    public Flux<DataTeenFuncionaryTransaccional> findAll() {
        Flux<TransaccionalAllocation> family = transaccionalAllocationRepository.findAll();
        return family.flatMap(dataFamily -> {
            Mono<FuncionaryResponseDto> funcionaryResponseDtoMono = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8084/api/funcionaryData/" + dataFamily.getId_funcionary())
                    .retrieve()
                    .bodyToMono(FuncionaryResponseDto.class);
            Mono<TeenResponseDto> teenResponseDtoMono = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/api/teenData/" + dataFamily.getId_adolescente())
                    .retrieve()
                    .bodyToMono(TeenResponseDto.class);
            return funcionaryResponseDtoMono.zipWith(teenResponseDtoMono).map(dataGeneral -> {
                FuncionaryResponseDto legalGuardianResponseDtoData = dataGeneral.getT1();
                TeenResponseDto adolescentResponseDtoData = dataGeneral.getT2();
                DataTeenFuncionaryTransaccional dataTeenFuncionaryTransaccional = new DataTeenFuncionaryTransaccional();
                dataTeenFuncionaryTransaccional.setTransaccionalAllocation(dataFamily);
                dataTeenFuncionaryTransaccional.setTeenResponseDto(adolescentResponseDtoData);
                dataTeenFuncionaryTransaccional.setFuncionaryResponseDto(legalGuardianResponseDtoData);
                return dataTeenFuncionaryTransaccional;
            });
        });
    }

    @Override
    public Mono<TransaccionalAllocation> saveNewDataTransaccional(TransaccionalAllocationRequestDto request) {
        return this.transaccionalAllocationRepository.save(toModel(request));
    }

    @Override
    public Mono<TransaccionalAllocationResponseDto> updateDataTransaccional(TransaccionalAllocationRequestDto request, Integer id) {
        return this.transaccionalAllocationRepository.findById_adolescente(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("El identificador: " + id + "no fue encontrado.")))
                .flatMap(dataFuncionary -> this.transaccionalAllocationRepository.save(toModel(request, dataFuncionary.getId_funcionaryteend())))
                .map(TransaccionalAllocationMapper::toDto);
    }

    @Override
    public Flux<DataTeenFuncionaryTransaccional> findAllIdtutor(Integer idTutor) {
        Flux<TransaccionalAllocation> teen = transaccionalAllocationRepository.findByidtutor(idTutor);
        return teen.flatMap(data ->{
            Mono<FuncionaryResponseDto> funcionaryResponseDtoMono = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8084/api/funcionaryData/" + data.getId_funcionary())
                    .retrieve()
                    .bodyToMono(FuncionaryResponseDto.class);
            Mono<TeenResponseDto> teenResponseDtoMono = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/api/teenData/" + data.getId_adolescente())
                    .retrieve()
                    .bodyToMono(TeenResponseDto.class);
            return funcionaryResponseDtoMono.zipWith(teenResponseDtoMono).map(dataGeneral -> {
                FuncionaryResponseDto legalGuardianResponseDtoData = dataGeneral.getT1();
                TeenResponseDto adolescentResponseDtoData = dataGeneral.getT2();
                DataTeenFuncionaryTransaccional dataTeenFuncionaryTransaccional = new DataTeenFuncionaryTransaccional();
                dataTeenFuncionaryTransaccional.setTransaccionalAllocation(data);
                dataTeenFuncionaryTransaccional.setTeenResponseDto(adolescentResponseDtoData);
                dataTeenFuncionaryTransaccional.setFuncionaryResponseDto(legalGuardianResponseDtoData);
                return dataTeenFuncionaryTransaccional;
            });
        });
    }
}

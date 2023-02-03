package com.bootcamp.java.credit.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.java.credit.domain.Credit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
	Mono<Credit> findByCreditNumber(String creditNumber);
	Mono<Credit> findFirstByOrderById();
	
	Mono<Integer> countByIdClient(String idClient);	
	Mono<Integer> countByIdClientAndIdProduct(String idClient, String idProduct);
	
}

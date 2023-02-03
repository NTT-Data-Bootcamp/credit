package com.bootcamp.java.credit.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import com.bootcamp.java.credit.domain.CreditCard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
	Mono<CreditCard> findByCardNumber(String cardNumber);
	Mono<CreditCard> findFirstByOrderById();
	
	Mono<Integer> countByIdClient(String idClient);	
	Mono<Integer> countByIdClientAndIdProduct(String idClient, String idProduct);
	
	Mono<CreditCard> findTopByCardNumber(String cardNumber);
}

package com.bootcamp.java.credit.web;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.java.credit.domain.CreditCard;
import com.bootcamp.java.credit.service.CreditCardService;
import com.bootcamp.java.credit.web.mapper.CreditCardMapper;
import com.bootcamp.java.credit.web.model.CreditCardModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/creditcard")
public class CreditCardController {
	@Value("${spring.application.name}")
	String name;
	
	@Value("${server.port}")
	String port;
	
	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private CreditCardMapper creditCardMapper;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<CreditCardModel>>> getAll(){
		log.info("getAll executed");
		return Mono.just(ResponseEntity.ok()
			.body(creditCardService.findAll()
					.map(creditCard -> creditCardMapper.entityToModel(creditCard))));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<CreditCardModel>> getById(@PathVariable String id){
		log.info("getById executed {}", id);
		Mono<CreditCard> response = creditCardService.findById(id);
		return response
				.map(creditCard -> creditCardMapper.entityToModel(creditCard))
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("getByCardNumber/{cardNumber}")
	public Mono<ResponseEntity<CreditCardModel>> getByCardNumber(@PathVariable String cardNumber){
		log.info("getById executed {}", cardNumber);
		Mono<CreditCard> response = creditCardService.findByCardNumber(cardNumber);
		return response
				.map(creditCard -> creditCardMapper.entityToModel(creditCard))
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<CreditCardModel>> create(@Valid @RequestBody CreditCardModel request){
		log.info("create executed {}", request);
		return creditCardService.create(creditCardMapper.modelToEntity(request))
				.map(creditCard -> creditCardMapper.entityToModel(creditCard))
				.flatMap(c ->
					Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name,
							port, "credit", c.getId())))
							.body(c)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<CreditCardModel>> updateById(@PathVariable String id, @Valid @RequestBody CreditCardModel request){
		log.info("updateById executed {}:{}", id, request);
		return creditCardService.update(id, creditCardMapper.modelToEntity(request))
				.map(creditCard -> creditCardMapper.entityToModel(creditCard))
				.flatMap(c ->
				Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name,
						port, "creditcard", c.getId())))
						.body(c)))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
		log.info("deleteById executed {}", id);
		return creditCardService.delete(id)
				.map( r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/countCreditCardByIdClient/{idCliente}")
    public Mono<ResponseEntity<Integer>> countCreditCardByIdClient(@PathVariable String idCliente){
        log.info("countCreditCardByIdClient executed {}", idCliente);

        Mono<Integer> response = creditCardService.countByIdClient(idCliente);
        return response
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
	
	/*
	@PutMapping("/cargarconsumo/{id}/{consumo}")
	public Mono<ResponseEntity<CreditCardModel>> cargarconsumo(@PathVariable String id, @PathVariable Double consumo, @Valid @RequestBody CreditCardModel request){
		log.info("cargarconsumo executed {} - {} :{}", id, consumo, request);
		return creditCardService.cargarConsumo(id, consumo,  creditCardMapper.modelToEntity(request))
				.map(creditCard -> creditCardMapper.entityToModel(creditCard))
				.flatMap(c ->
				Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name,
						port, "creditcard", c.getId())))
						.body(c)))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}
	*/
}

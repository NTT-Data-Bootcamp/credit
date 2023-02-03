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

import com.bootcamp.java.credit.domain.Credit;
import com.bootcamp.java.credit.service.CreditService;
import com.bootcamp.java.credit.web.mapper.CreditMapper;
import com.bootcamp.java.credit.web.model.CreditModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/credit")
public class CreditController {
	@Value("${spring.application.name}")
	String name;
	
	@Value("${server.port}")
	String port;
	
	@Autowired
	private CreditService creditService;
	
	@Autowired
	private CreditMapper creditMapper;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<CreditModel>>> getAll(){
		log.info("getAll executed");
		return Mono.just(ResponseEntity.ok()
			.body(creditService.findAll()
					.map(credit -> creditMapper.entityToModel(credit))));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<CreditModel>> getById(@PathVariable String id){
		log.info("getById executed {}", id);
		Mono<Credit> response = creditService.findById(id);
		return response
				.map(credit -> creditMapper.entityToModel(credit))
				.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<CreditModel>> create(@Valid @RequestBody CreditModel request){
		log.info("create executed {}", request);
		return creditService.create(creditMapper.modelToEntity(request))
				.map(credit -> creditMapper.entityToModel(credit))
				.flatMap(c ->
					Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name,
							port, "credit", c.getId())))
							.body(c)))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<CreditModel>> updateById(@PathVariable String id, @Valid @RequestBody CreditModel request){
		log.info("updateById executed {}:{}", id, request);
		return creditService.update(id, creditMapper.modelToEntity(request))
				.map(credit -> creditMapper.entityToModel(credit))
				.flatMap(c ->
				Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name,
						port, "credit", c.getId())))
						.body(c)))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
		log.info("deleteById executed {}", id);
		return creditService.delete(id)
				.map( r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}

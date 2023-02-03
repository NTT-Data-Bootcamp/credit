package com.bootcamp.java.credit.service;


import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.bootcamp.java.credit.domain.CreditCard;
import com.bootcamp.java.credit.repository.CreditCardRepository;
import com.bootcamp.java.credit.web.mapper.CreditCardMapper;
import com.bootcamp.java.credit.web.model.ClientModel;
import com.bootcamp.java.credit.web.model.ProductModel;
import com.bootcamp.java.credit.web.model.ProductParameterModel;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreditCardService {

	@Autowired
	private CreditCardRepository creditCardRepository;
	
	@Autowired
	private CreditCardMapper creditCardMapper;
	
	private WebClient getWebClientClient(){
        log.debug("getWebClientClient executed");
        return WebClient.builder()
                .baseUrl("http://localhost:9050/v1/client")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
	
	private WebClient getWebClientProduct(){
        log.debug("getWebClientProduct executed");
        return WebClient.builder()
                .baseUrl("http://localhost:9051/v1/product")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
	
	private WebClient getWebClientProductParameter(){
        log.debug("getWebClientProductParameter executed");
        return WebClient.builder()
                .baseUrl("http://localhost:9051/v1/productparameter")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
	
	private WebClient getWebClientAccount(){
        log.debug("getWebClientAccount executed");
        return WebClient.builder()
                .baseUrl("http://localhost:9052/v1/account")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

	public Flux<CreditCard> findAll(){
		log.debug("findAll executed");
		return creditCardRepository.findAll();
	}

	public Mono<CreditCard> findById(String creditCardId){
		log.debug("findById executed {}", creditCardId);
		return creditCardRepository.findById(creditCardId);
	}
	
	public Mono<CreditCard> findByCardNumber(String cardNumber){
		log.debug("findById executed {}", cardNumber);
		return creditCardRepository.findByCardNumber(cardNumber);
	}

	public Mono<CreditCard> create(CreditCard creditCard){
		log.debug("create executed {} in service", creditCard);
		
		return getClientById(creditCard.getIdClient())
				.switchIfEmpty(Mono.error(new Exception("Client does not exist")))
				.flatMap(clientModel -> 
					getProductById(creditCard.getIdProduct())
					.switchIfEmpty(Mono.error(new Exception("Product does not exist")))
					.flatMap(productModel -> validateProductParameter(creditCard.getIdProduct(), clientModel.getClientType(), clientModel.getIdClientProfile()))
					//.switchIfEmpty(Mono.error(new Exception("ProductParameter does not exist para 3")))
					.switchIfEmpty(validateProductParameterWithoutClientProfile(creditCard.getIdProduct(), clientModel.getClientType()))
					.switchIfEmpty(Mono.error(new Exception("ProductParameter does not exist para 2")))
					.flatMap(productParameterModel -> {
						Mono<Boolean> isOKQuantityProduct, isOKQuantityAccounts, isOKQuantityCreditCards;
		
						// verificar máximo número de producto account
						Integer maxQuantityProduct = productParameterModel.getMaxQuantityProduct();
						Mono<Integer> quantityAccountByClientProduct = creditCardRepository.countByIdClientAndIdProduct(creditCard.getIdClient(), creditCard.getIdProduct());
						isOKQuantityProduct = quantityAccountByClientProduct.flatMap(quantity -> quantity < maxQuantityProduct ? Mono.just(true) : Mono.just(false) );
						
						
						// verificar account required
						Boolean isAccountRequired = productParameterModel.getAccountRequired();	
						//Mono<Integer> quantityCreditCards = getWebClientCredit().get().uri("creditcard/countCreditCardByIdClient/" + account.getIdClient() ).retrieve().bodyToMono(Integer.class);
						isOKQuantityAccounts = !isAccountRequired  ? Mono.just(true) : getWebClientAccount().get().uri("account/countAccountdByIdClient/" + creditCard.getIdClient() ).retrieve().bodyToMono(Integer.class).flatMap(quantity -> quantity > 0 ? Mono.just(true) : Mono.just(false));
						
						// verificar creditcard required
						Boolean isCardRequired = productParameterModel.getCardRequired();	
						//Mono<Integer> quantityCreditCards = getWebClientCredit().get().uri("creditcard/countCreditCardByIdClient/" + account.getIdClient() ).retrieve().bodyToMono(Integer.class);
						isOKQuantityCreditCards = !isCardRequired  ? Mono.just(true) : creditCardRepository.countByIdClient(creditCard.getIdClient()).flatMap(quantity -> quantity > 0 ? Mono.just(true) : Mono.just(false));
						
						
						isOKQuantityProduct.flatMap(value1 -> {
							if(!value1)	
								return Mono.error(new Exception("Quantity Product >= Maximun quantity of Account permitted"));		
							isOKQuantityAccounts.flatMap(value2 -> {
								if(!value2)	
									return Mono.error(new Exception("account is required"));
								isOKQuantityCreditCards.flatMap(value3 -> {
									if(!value3)	
										return Mono.error(new Exception("credit card is required"));
									
									// setear las fechas
									LocalDate fechaActual = LocalDate.now();
									LocalDate fechaExpira = fechaActual.plusYears(5);
									creditCard.setOpeningDate(fechaActual);
									creditCard.setExpireDate(fechaExpira);
									return creditCardRepository.save(creditCard);						
								});	
								return null;								
							});
							return null;	
						}); //.flatMap(Mono.just(account));						
						//return accountRepository.save(account);					
						return null;
					})
				);
		
		
		//return customerRepository.save(customer);
	}

	public Mono<CreditCard> update(String creditCardId, CreditCard creditCard){
		log.debug("update executed {}:{}", creditCardId, creditCard);
		return creditCardRepository.findById(creditCardId)
		.flatMap(dbCreditCard -> {
			creditCardMapper.update(dbCreditCard, creditCard);
			return creditCardRepository.save(dbCreditCard);
		});
	}

	public Mono<CreditCard> delete(String creditCardId){
		log.debug("delete executed {}", creditCardId);
		return creditCardRepository.findById(creditCardId)
		.flatMap(existingCreditCard -> creditCardRepository.delete(existingCreditCard)
		.then(Mono.just(existingCreditCard)));
		
	}

	
	public Mono<Integer> countByIdClient(String idCliente){
		log.debug("findById executed {}", idCliente);
		return creditCardRepository.countByIdClient(idCliente);
	}

	/*
	public Mono<CreditCard> cargarConsumo(String creditCardId, Double consumo, CreditCard creditCard){
		log.debug("cargarConsumo executed {}:{}", creditCardId,consumo);
		return creditCardRepository.findById(creditCardId)
		.flatMap(dbCreditCard -> {
			 
			if(dbCreditCard.getAmountOwed() + consumo <= dbCreditCard.getSpendingLimit()) {
				dbCreditCard.setAmountOwed(dbCreditCard.getAmountOwed() + consumo);
				dbCreditCard.setAmountAvailable(dbCreditCard.getAmountAvailable() - consumo);
				return creditCardRepository.save(dbCreditCard);

			}
			else {
				return Mono.error(new Exception("El monto a cargar supera el límite de crédito "));
			}
			
		});
	}	*/
	
	
	/////////// METODOS COMPLEMENTARIOS DE LOGICA DEL NEGOCIO ///////////////////////////////////
	private Mono<ClientModel> getClientById(String idClient){
	  log.debug("getClientById executed {}", idClient);
	  /*return getClientByDocument(client)
	          .switchIfEmpty(Mono.error(new InvalidClientException()))
	          .flatMap(Mono::just);*/
	  // Se llama al EndPoint  getById de microservicio Client
	  return getWebClientClient().get().uri("/" + idClient).retrieve()
	          .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new Exception("Client does not exist"))).bodyToMono(ClientModel.class);
	}	
	
	private Mono<ProductModel> getProductById(String idProduct){
		  log.debug("getProductById executed {}", idProduct);
		  
		  // Se llama al EndPoint  getById de microservicio Product
		  return getWebClientProduct().get().uri("/" + idProduct).retrieve()
		          .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new Exception("Product does not exist"))).bodyToMono(ProductModel.class);
		}

	private Mono<ProductParameterModel> validateProductParameter(String idProduct, String clientType, String idClientProfile){
		log.debug("validateProductAndGetProductParameter executed idProduct = {} - clientType = {} - idProfile = {}", idProduct, clientType, idClientProfile);
		// Se llama al EndPoint  getByIdProductAndClientTypeAndIdClientProfileAndActive de microservicio Product -> ProductParameter
		Mono<ProductParameterModel> ppm = getWebClientProductParameter().get().uri("/getByIdProductAndClientTypeAndIdClientProfileAndActive/" + idProduct + "/" + clientType + "/" + idClientProfile).retrieve()
	          //.onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new Exception("ProductParameter does not exists for product = " + idProduct + ", clientType = " + clientType + ", idClientProfile = " + idClientProfile)))                
				//.switchIfEmpty()
				.bodyToMono(ProductParameterModel.class);
		
		return ppm;	
	}	
	
	private Mono<ProductParameterModel> validateProductParameterWithoutClientProfile(String idProduct, String clientType){
		log.debug("validateProductAndGetProductParameter executed idProduct = {} - clientType = {}", idProduct, clientType);
		// Se llama al EndPoint  getByIdProductAndClientTypeAndIdClientProfileAndActive de microservicio Product -> ProductParameter
			
		return getWebClientProductParameter().get().uri("/getByIdProductAndClientTypeAndActiveWithoutClientProfile/" + idProduct + "/" + clientType ).retrieve()
				                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new Exception("ProductParameter does not exists for product = " + idProduct + ", clientType = " + clientType + " (without profile)")))                
				                .bodyToMono(ProductParameterModel.class);
	}
}
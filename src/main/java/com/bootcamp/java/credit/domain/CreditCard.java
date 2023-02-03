package com.bootcamp.java.credit.domain;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@EqualsAndHashCode(of = { "cardNumber" })
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "credit_card")
public class CreditCard {

	@Id
	private String id;
	
	@NotNull
	private String idProduct;
	
	@NotNull
	private String idClient;
	
	@NotNull
	@Indexed(unique = true)
	private String cardNumber;
	
	@NotNull
	private Float amountOwed;
	
	@NotNull
	private Float amountAvailable;
	
	@NotNull
	private Float spendingLimit;
	
	
	private LocalDate openingDate;
	
	private LocalDate updateDate;
	
	private LocalDate expireDate;
	
	@NotNull
	private Integer payDay;
	
	//private String accountNumber;
	
	@NotNull
	private Boolean active;	
	
}



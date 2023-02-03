package com.bootcamp.java.credit.web.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardModel {

	@JsonIgnore
	private String id;	
	
	@NotBlank(message = "idProduct cannot be null or empty")
	private String idProduct;
	
	@NotBlank(message = "IdClient cannot be null or empty")
	private String idClient;
	
	@NotBlank(message = "cardNumber cannot be null or empty")
	private String cardNumber;
	
	@NotNull
	private Float amountOwed;
	
	@NotNull
	private Float amountAvailable;
	
	@NotNull
	private Float spendingLimit;
	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate openingDate;
	
	@JsonIgnore
	private LocalDate updateDate;
	
	private LocalDate expireDate;
	
	@NotNull
	private Integer payDay;
	
	//private String accountNumber;
	
	@NotNull
	private Boolean active;	
	
}

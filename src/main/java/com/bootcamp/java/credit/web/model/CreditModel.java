package com.bootcamp.java.credit.web.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditModel {
	
	
	@JsonIgnore
	private String id;
	
	@NotBlank(message = "idProduct cannot be null or empty")
	private String idProduct;
	
	@NotBlank(message = "idClient cannot be null or empty")
	private String idClient;
	
	@NotBlank(message = "creditNumber cannot be null or empty")
	private String creditNumber;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate openingDate;
	
	@JsonIgnore
	private LocalDate updateDate;
	
	@NotNull
	private Float amountRequested;
	
	@NotNull
	private Float amountDebt; 
	
	@NotNull
	private Integer termInMonths;
	
	@NotNull
	private Float monthlyFee;
	
	@NotNull
	private Integer payDay;
	
	@NotNull
	private Boolean active;
}


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
@EqualsAndHashCode(of = { "creditNumber" })
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "credit")
public class Credit {
	@Id
	private String id;
	
	@NotNull
	private String idProduct;
	
	@NotNull
	private String idClient;
	
	@NotNull
	@Indexed(unique = true)
	private String creditNumber;
	
	private LocalDate openingDate;
	
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

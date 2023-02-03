package com.bootcamp.java.credit.web.model;


import javax.validation.constraints.NotBlank;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductParameterModel {

	@JsonIgnore
	private String id;
	
	
	@NotBlank(message = "IdProduct cannot be null or empty")
	private String idProduct;
	@NotBlank(message = "clientType cannot be null or empty")	
	private String clientType;
	
	private String idClientProfile;
	
	
	private Integer maxQuantityProduct;
    
	// parametros de cuentas
	private Float maintanceCommissionAccount;    
    private Integer minimumOwners;
    private Integer minimumSigners;
    private Float averageMinimumAmountInMonth;
    private Boolean cardRequired;
    
    
    // parametros de transacciones    
    private Long maxTransaccFree;
    private Float commissionByTransaccExtra;
    private Long maxMovementsInMonth; //account
    private Integer dayMovementInMonth; //account
    
    // parametros de credits
    private Boolean accountRequired;
    
    private Boolean active;
    
}

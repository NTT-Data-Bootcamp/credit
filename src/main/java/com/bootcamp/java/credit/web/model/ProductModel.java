package com.bootcamp.java.credit.web.model;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
	
	private String id;
	
	@NotBlank(message = "product´s name cannot be null or empty")
	private String name;
	
	@NotBlank(message = " id of product type cannot be null or empty")
	private String idProductType;
		
	@NotNull
    private Boolean active;
}

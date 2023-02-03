package com.bootcamp.java.credit.web.model;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
public class ClientModel {
	@JsonIgnore
	private String id;

	@NotBlank(message = "Identity Document Type cannot be null or empty")
	private String identityDocumentType;
	
	@NotBlank(message = "Identity Document Number cannot be null or empty")
	private String identityDocumentNumber;

	private String name;

	private String lastName;

	private String businessName;

	@NotBlank(message = "Email cannot be null or empty")
	@Email
	private String email;

	@NotBlank(message = "PhoneNumber cannot be null or empty")
	private String phoneNumber;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	
	
	public String clientType;
	
	public String idClientProfile;
}

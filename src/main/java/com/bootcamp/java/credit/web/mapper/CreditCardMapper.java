package com.bootcamp.java.credit.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.bootcamp.java.credit.domain.CreditCard;
import com.bootcamp.java.credit.web.model.CreditCardModel;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {
	CreditCard modelToEntity(CreditCardModel model);

	CreditCardModel entityToModel(CreditCard event);

	@Mapping(target = "id", ignore = true)
	void update(@MappingTarget CreditCard dbCreditCard, CreditCard creditCard);
}

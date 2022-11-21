package com.aliencat.springboot.nebula.mapper;


import com.aliencat.springboot.nebula.ocean.dto.linkedin.*;
import com.aliencat.springboot.nebula.pojo.*;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * 映射类
 *
 * @author bxx
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NebulaMapper {
    PersonInfoTagDto toPersonInfoTagDto(PersonInfoTag personInfoTag);

    OrganizationTagDto toOrganizationTagDto(OrganizationTag organizationTag);

    PhoneNumberTagDto toPhoneNumberTagDto(PhoneNumberTag phoneNumberTag);

    EmailTagDto toEmailTagDto(EmailTag emailTag);

    AddressTagDto toAddressTagDto(AddressTag addressTag);

    PersonToAccountEdgeDto toPersonToAccountEdgeDto(PersonToAccountEdge personToAccountEdge);

    PersonToOrgEdgeDto toPersonToOrgEdgeDto(PersonToOrgEdge personToOrgEdge);

    PersonToPhoneNumEdgeDto toPersonToPhoneNumEdgeDto(PersonToPhoneNumberEdge personToPhoneNumberEdge);

    PersonToEmailEdgeDto toPersonToEmailEdgeDto(PersonToEmailEdge personToEmailEdge);

    PersonToAddressEdgeDto toPersonToAddressEdgeDto(PersonToAddressEdge personToAddressEdge);

    PersonToPersonEdgeDto toPersonToPersonEdgeDto(PersonToPersonEdge personToPersonEdge);

}

package com.aliencat.springboot.nebula.ocean.dto.linkedin;

import com.aliencat.springboot.nebula.ocean.dto.AccountTag;
import lombok.Data;

import java.util.List;
import java.util.Vector;


@Data
public class LinkedinInfoDto {

    private List<PersonInfoTag> listOfPersonInfoTag= new Vector<>();

    private List<OrganizationTag> listOfOrganizationTag= new Vector<>();
    
    private List<AddressTag> listOfAddressTag= new Vector<>();
    
    private List<EmailTag> listOfEmailTag= new Vector<>();
    
    private List<OrgToAccountEdge> listOfOrgToAccountEdge= new Vector<>();
    
    private List<OrgToOrgEdge> listOfOrgToOrgEdge= new Vector<>();
    
    private List<PersonToAccountEdge> listOfPersonToAccountEdge= new Vector<>();
    
    private List<PersonToAddressEdge> listOfPersonToAddressEdge= new Vector<>();

    private List<OrgToAddressEdge> listOfOrgToAddressEdge= new Vector<>();
    
    private List<PersonToEmailEdge> listOfPersonToEmailEdge= new Vector<>();

    private List<PersonToOrgEdge> listOfPersonToOrgEdge= new Vector<>();
    
    private List<PersonToPersonEdge> listOfPersonToPersonEdge= new Vector<>();

    private List<PersonToPhoneNumberEdge> listOfPersonToPhoneNumberEdge= new Vector<>();
    
    private List<PhoneNumberTag> listOfPhoneNumberTag= new Vector<>();

    private List<AccountTag> listOfAccountTag= new Vector<>();
}

package com.aliencat.springboot.nebula.service.impl;

import com.aliencat.springboot.nebula.config.NebulaConfig;
import com.aliencat.springboot.nebula.config.NebulaProperties;
import com.aliencat.springboot.nebula.mapper.NebulaMapper;
import com.aliencat.springboot.nebula.ocean.common.RtsThread;
import com.aliencat.springboot.nebula.ocean.common.utils.CollectionUtils;
import com.aliencat.springboot.nebula.ocean.dto.AccountTag;
import com.aliencat.springboot.nebula.ocean.dto.linkedin.*;
import com.aliencat.springboot.nebula.ocean.engine.NebulaCondition;
import com.aliencat.springboot.nebula.pojo.*;
import com.aliencat.springboot.nebula.service.NebulaService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.relation.RelationService;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * nebula 服务
 */
@Service
@Slf4j
public class NebulaServiceImpl implements NebulaService {
    @Autowired
    private NebulaConfig nebulaConfig;

    @Resource
    RelationService relationService;

    @Resource
    private NebulaProperties nebulaProperties;

    @Resource
    private NebulaMapper nebulaMapper;

    @Resource
    private RtsThread rtsThread;



    @Override
    public Boolean addOrUpdatePersonInfo(List<PersonVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        ThreadPoolExecutor threadPoolExecutor = rtsThread.getThreadPoolExecutor();
        //定义多线程数量，一般就是任务数量
        final CountDownLatch latch = new CountDownLatch(list.size());
        LinkedinInfoDto dto = new LinkedinInfoDto();
        for (PersonVo vo : list) {
            threadPoolExecutor.execute(new DealPersonVo(latch, vo, dto));
        }
        try {
            latch.await();//等待,不断检测数量是否为0，为零是执行后面的操作
        } catch (InterruptedException e) {
            log.error("Deal  List<PersonVo>  " + e.getMessage());
            return false;
        }
        return true;
    }


    @Data
    @EqualsAndHashCode(callSuper = true)
    class DealPersonVo extends Thread {
        private CountDownLatch latch;
        private PersonVo vo;
        private LinkedinInfoDto dto;


        public DealPersonVo(CountDownLatch latch, PersonVo vo, LinkedinInfoDto dto) {
            this.latch = latch;
            this.vo = vo;
            this.dto = dto;
        }

        @Override
        public void run() {

        }
    }

    @Override
    public List<PersonVo> queryPersonInfo(String name) {
        if (Objects.isNull(name)) {
            return Collections.emptyList();
        }
        String buildSql = NebulaCondition.build().andIn(PersonInfoTag.class, "name", Collections.singleton(name.trim())).buildSql();
        Set<PersonInfoTag> personInfoTags = nebulaConfig.getNebulaGraphMapper().getTagByParam(PersonInfoTag.class, buildSql, null);
        if (!CollectionUtils.isEmpty(personInfoTags)) {
            return personInfoTags.stream().map(personInfoTag -> {
                PersonVo personVo = new PersonVo();
                PersonInfoTagDto personInfoTagDto = nebulaMapper.toPersonInfoTagDto(personInfoTag);
                personVo.setPersonInfoTagDto(personInfoTagDto);
                getPersonInfoByPersonInfoTagId(personInfoTag.getId(), personVo);
                return personVo;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public void getPersonInfoByPersonInfoTagId(String id, PersonVo personVo) {
        Set<PersonToAccountEdge> personToAccountEdgeSet = nebulaConfig.getNebulaGraphMapper().goAllEdge(PersonToAccountEdge.class, null, null, id);
        if (!CollectionUtils.isEmpty(personToAccountEdgeSet)) {
            Set<AccountTag> accountTags = nebulaConfig.getNebulaGraphMapper().fetchVertexTag(AccountTag.class, personToAccountEdgeSet.stream().map(PersonToAccountEdge::getToId).collect(Collectors.toSet()), null);
            Map<String, List<AccountTag>> accountTagMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(accountTags)) {
                accountTagMap = accountTags.stream().
                        filter(o -> o.getId() != null).collect(Collectors.groupingBy(AccountTag::getId));
            }
            List<PersonToAccountEdgeDto> personToAccountEdgeDtoList = new ArrayList<>();
            for (PersonToAccountEdge personToAccountEdge : personToAccountEdgeSet) {
                PersonToAccountEdgeDto personToAccountEdgeDto = nebulaMapper.toPersonToAccountEdgeDto(personToAccountEdge);
                if (accountTagMap != null && accountTagMap.containsKey(personToAccountEdge.getToId())) {
                    AccountTag accountTag = accountTagMap.get(personToAccountEdge.getToId()).get(0);

                }
                personToAccountEdgeDtoList.add(personToAccountEdgeDto);
            }
            personVo.setPersonToAccountEdgeDtoList(personToAccountEdgeDtoList);
        }
        Set<PersonToOrgEdge> personToOrgEdgeSet = nebulaConfig.getNebulaGraphMapper().goAllEdge(PersonToOrgEdge.class, null, null, id);
        if (!CollectionUtils.isEmpty(personToOrgEdgeSet)) {
            Set<OrganizationTag> organizationTags = nebulaConfig.getNebulaGraphMapper().fetchVertexTag(OrganizationTag.class, personToOrgEdgeSet.stream().map(PersonToOrgEdge::getToId).collect(Collectors.toSet()), null);
            Map<String, List<OrganizationTag>> organizationTagMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(organizationTags)) {
                organizationTagMap = organizationTags.stream().
                        filter(o -> o.getId() != null).collect(Collectors.groupingBy(OrganizationTag::getId));
            }

            List<PersonToOrgEdgeDto> personToOrgEdgeDtoList = new ArrayList<>();
            for (PersonToOrgEdge personToOrgEdge : personToOrgEdgeSet) {
                PersonToOrgEdgeDto personToOrgEdgeDto = nebulaMapper.toPersonToOrgEdgeDto(personToOrgEdge);
                if (organizationTagMap != null && organizationTagMap.containsKey(personToOrgEdge.getToId())) {
                    OrganizationTag tag = organizationTagMap.get(personToOrgEdge.getToId()).get(0);
                    OrganizationTagDto dto = nebulaMapper.toOrganizationTagDto(tag);
                    personToOrgEdgeDto.setOrganizationTagDto(dto);
                }
                personToOrgEdgeDtoList.add(personToOrgEdgeDto);
            }
            personVo.setPersonToOrgEdgeDtoList(personToOrgEdgeDtoList);
        }
        Set<PersonToEmailEdge> personToEmailEdgeSet = nebulaConfig.getNebulaGraphMapper().goAllEdge(PersonToEmailEdge.class, null, null, id);
        if (!CollectionUtils.isEmpty(personToEmailEdgeSet)) {
            Set<EmailTag> emailTags = nebulaConfig.getNebulaGraphMapper().fetchVertexTag(EmailTag.class, personToEmailEdgeSet.stream().map(PersonToEmailEdge::getToId).collect(Collectors.toSet()), null);
            Map<String, List<EmailTag>> emailTagMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(emailTags)) {
                emailTagMap = emailTags.stream().
                        filter(o -> o.getId() != null).collect(Collectors.groupingBy(EmailTag::getId));
            }

            List<PersonToEmailEdgeDto> personToEmailEdgeDtoList = new ArrayList<>();
            for (PersonToEmailEdge edge : personToEmailEdgeSet) {
                PersonToEmailEdgeDto personToEmailEdgeDto = nebulaMapper.toPersonToEmailEdgeDto(edge);
                if (emailTagMap != null && emailTagMap.containsKey(edge.getToId())) {
                    EmailTag tag = emailTagMap.get(edge.getToId()).get(0);
                    EmailTagDto dto = nebulaMapper.toEmailTagDto(tag);
                    personToEmailEdgeDto.setEmailTagDto(dto);
                }
                personToEmailEdgeDtoList.add(personToEmailEdgeDto);
            }
            personVo.setPersonToEmailEdgeDtoList(personToEmailEdgeDtoList);
        }
        Set<PersonToAddressEdge> personToAddressEdgeSet = nebulaConfig.getNebulaGraphMapper().goAllEdge(PersonToAddressEdge.class, null, null, id);
        if (!CollectionUtils.isEmpty(personToAddressEdgeSet)) {
            Set<AddressTag> addressTags = nebulaConfig.getNebulaGraphMapper().fetchVertexTag(AddressTag.class, personToAddressEdgeSet.stream().map(PersonToAddressEdge::getToId).collect(Collectors.toSet()), null);
            Map<String, List<AddressTag>> addressTagMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(addressTags)) {
                addressTagMap = addressTags.stream().
                        filter(o -> o.getId() != null).collect(Collectors.groupingBy(AddressTag::getId));
            }

            List<PersonToAddressEdgeDto> personToAddressEdgeList = new ArrayList<>();
            for (PersonToAddressEdge edge : personToAddressEdgeSet) {
                PersonToAddressEdgeDto personToAddressEdgeDto = nebulaMapper.toPersonToAddressEdgeDto(edge);
                if (addressTagMap != null && addressTagMap.containsKey(edge.getToId())) {
                    AddressTag tag = addressTagMap.get(edge.getToId()).get(0);
                    AddressTagDto dto = nebulaMapper.toAddressTagDto(tag);
                    personToAddressEdgeDto.setAddressTagDto(dto);
                }
                personToAddressEdgeList.add(personToAddressEdgeDto);
            }
            personVo.setPersonToAddressEdgeDtoList(personToAddressEdgeList);
        }
        Set<PersonToPhoneNumberEdge> personToPhoneNumberEdgeSet = nebulaConfig.getNebulaGraphMapper().goAllEdge(PersonToPhoneNumberEdge.class, null, null, id);
        if (!CollectionUtils.isEmpty(personToPhoneNumberEdgeSet)) {
            Set<PhoneNumberTag> phoneNumberTags = nebulaConfig.getNebulaGraphMapper().fetchVertexTag(PhoneNumberTag.class, personToPhoneNumberEdgeSet.stream().map(PersonToPhoneNumberEdge::getToId).collect(Collectors.toSet()), null);
            for (PhoneNumberTag phoneNumberTag : phoneNumberTags) {
                String decrypt = null;

                log.info("decrypt" + decrypt);
            }
            Map<String, List<PhoneNumberTag>> phoneNumberTagMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(phoneNumberTags)) {
                phoneNumberTagMap = phoneNumberTags.stream().
                        filter(o -> o.getId() != null).collect(Collectors.groupingBy(PhoneNumberTag::getId));
            }

            List<PersonToPhoneNumEdgeDto> personToPhoneNumEdgeDtoList = new ArrayList<>();
            for (PersonToPhoneNumberEdge edge : personToPhoneNumberEdgeSet) {
                PersonToPhoneNumEdgeDto personToPhoneNumEdgeDto = nebulaMapper.toPersonToPhoneNumEdgeDto(edge);
                if (phoneNumberTagMap != null && phoneNumberTagMap.containsKey(edge.getToId())) {
                    PhoneNumberTag tag = phoneNumberTagMap.get(edge.getToId()).get(0);
                    PhoneNumberTagDto dto = nebulaMapper.toPhoneNumberTagDto(tag);
                    personToPhoneNumEdgeDto.setPhoneNumberTagDto(dto);
                }
                personToPhoneNumEdgeDtoList.add(personToPhoneNumEdgeDto);
            }
            personVo.setPersonToPhoneNumEdgeDtoList(personToPhoneNumEdgeDtoList);
        }
        Set<PersonToPersonEdge> personToPersonEdgeSet = nebulaConfig.getNebulaGraphMapper().goAllEdge(PersonToPersonEdge.class, null, null, id);
        if (!CollectionUtils.isEmpty(personToPersonEdgeSet)) {
            Set<PersonInfoTag> personInfoTags = nebulaConfig.getNebulaGraphMapper().fetchVertexTag(PersonInfoTag.class, personToPersonEdgeSet.stream().map(PersonToPersonEdge::getToId).collect(Collectors.toSet()), null);
            Map<String, List<PersonInfoTag>> personInfoTagMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(personInfoTags)) {
                personInfoTagMap = personInfoTags.stream().
                        filter(o -> o.getId() != null).collect(Collectors.groupingBy(PersonInfoTag::getId));
            }

            List<PersonToPersonEdgeDto> personToPersonEdgeDtoList = new ArrayList<>();
            for (PersonToPersonEdge edge : personToPersonEdgeSet) {
                PersonToPersonEdgeDto personToPersonEdgeDto = nebulaMapper.toPersonToPersonEdgeDto(edge);
                if (personInfoTagMap != null && personInfoTagMap.containsKey(edge.getToId())) {
                    PersonInfoTag tag = personInfoTagMap.get(edge.getToId()).get(0);
                    PersonInfoTagDto dto = nebulaMapper.toPersonInfoTagDto(tag);
                    personToPersonEdgeDto.setPersonInfoTagDto(dto);
                }
                personToPersonEdgeDtoList.add(personToPersonEdgeDto);
            }
            personVo.setPersonToPersonEdgeDtoList(personToPersonEdgeDtoList);
        }


        log.info("");
    }

    //    @Scheduled(cron = "1 * * * * ?")
    public void inittest() {
        List<PersonVo> william_matthew_shinn = queryPersonInfo("william matthew shinn");
        System.out.println("");
    }
}

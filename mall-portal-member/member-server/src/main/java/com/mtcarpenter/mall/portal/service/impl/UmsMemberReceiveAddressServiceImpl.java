package com.mtcarpenter.mall.portal.service.impl;

import com.mtcarpenter.mall.common.UmsMemberReceiveAddressOutput;
import com.mtcarpenter.mall.mapper.UmsMemberReceiveAddressMapper;
import com.mtcarpenter.mall.model.UmsMember;
import com.mtcarpenter.mall.model.UmsMemberReceiveAddress;
import com.mtcarpenter.mall.model.UmsMemberReceiveAddressExample;
import com.mtcarpenter.mall.portal.service.UmsMemberReceiveAddressService;
import com.mtcarpenter.mall.portal.service.UmsMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户地址管理Service实现类
 * Created by macro on 2018/8/28.
 */
@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private UmsMemberReceiveAddressMapper addressMapper;
    @Override
    public int add(UmsMemberReceiveAddress address) {
        UmsMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return addressMapper.insert(address);
    }

    @Override
    public int delete(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.deleteByExample(example);
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address) {
        address.setId(null);
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.updateByExampleSelective(address,example);
    }

    @Override
    public List<UmsMemberReceiveAddressOutput> list(Long memberId) {
        UmsMember currentMember = memberService.getCurrentMember();
        if (memberId != null){
            currentMember = memberService.getById(memberId);
        }
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId());
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = addressMapper.selectByExample(example);
        List<UmsMemberReceiveAddressOutput> receiveAddressOutputs = umsMemberReceiveAddresses.stream().map(u -> {
            UmsMemberReceiveAddressOutput umsMemberReceiveAddressOutput = new UmsMemberReceiveAddressOutput();
            BeanUtils.copyProperties(u, umsMemberReceiveAddresses);
            return umsMemberReceiveAddressOutput;
        }).collect(Collectors.toList());
        return receiveAddressOutputs;
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember currentMember = memberService.getCurrentMember();
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        List<UmsMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(addressList)){
            return addressList.get(0);
        }
        return null;
    }
}

package org.debugroom.mynavi.sample.continuous.integration.bff.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.AddressResource;

@Service
public class FeatureAddressServiceImpl implements FeatureAddressService{

    @Autowired
    SampleService sampleService;

    @Override
    public AddressResource getAddress(Long userId) throws BusinessException {
        return sampleService.getUser(userId).getAddress();
    }

}

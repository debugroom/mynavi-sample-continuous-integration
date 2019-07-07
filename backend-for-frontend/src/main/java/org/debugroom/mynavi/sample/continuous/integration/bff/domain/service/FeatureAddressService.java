package org.debugroom.mynavi.sample.continuous.integration.bff.domain.service;

import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.AddressResource;

public interface FeatureAddressService {

    public AddressResource getAddress(Long userId) throws BusinessException;

}

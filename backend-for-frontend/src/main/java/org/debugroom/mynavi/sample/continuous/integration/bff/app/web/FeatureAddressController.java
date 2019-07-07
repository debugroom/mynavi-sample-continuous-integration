package org.debugroom.mynavi.sample.continuous.integration.bff.app.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.debugroom.mynavi.sample.continuous.integration.bff.app.model.AddUsersForm;
import org.debugroom.mynavi.sample.continuous.integration.bff.app.model.Address;
import org.debugroom.mynavi.sample.continuous.integration.bff.app.model.AddressMapper;
import org.debugroom.mynavi.sample.continuous.integration.bff.app.model.Email;
import org.debugroom.mynavi.sample.continuous.integration.bff.app.model.User;
import org.debugroom.mynavi.sample.continuous.integration.bff.domain.service.FeatureAddressService;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessException;

@Controller
public class FeatureAddressController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    FeatureAddressService featureAddressService;

    @ModelAttribute
    public AddUsersForm setUpForm(){
        AddUsersForm addUsersForm = AddUsersForm.builder()
                .users(new ArrayList<User>(
                        Arrays.asList(
                                User.builder()
                                        .address(new Address())
                                        .emailList(new ArrayList<>(Arrays.asList(new Email())))
                                        .build())))
                .build();
        return addUsersForm;
    }

    @RequestMapping(method = RequestMethod.GET, value="getAddress")
    public String getAdddress(String userId, Model model, Locale locale){
        try{
            model.addAttribute("address", AddressMapper.map(
                    featureAddressService.getAddress(Long.getLong(userId))));
            return "getAddress";
        }catch (BusinessException e){
            model.addAttribute("errorMessageForAddress",
                    messageSource.getMessage(e.getCode(), e.getArgs(), locale));
            return "portal";
        }
    }


}

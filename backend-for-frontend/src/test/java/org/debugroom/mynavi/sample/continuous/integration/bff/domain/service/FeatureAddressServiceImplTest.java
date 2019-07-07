package org.debugroom.mynavi.sample.continuous.integration.bff.domain.service;

import java.util.Arrays;

import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

import org.debugroom.mynavi.sample.continuous.integration.bff.domain.repository.UserResourceRepository;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.AddressResource;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.EmailResource;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.UserResource;

@RunWith(Enclosed.class)
public class FeatureAddressServiceImplTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = {
        FeatureAddressServiceImplTest.UnitTest.Config.class
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    public static class UnitTest{

        @Configuration
        public static class Config{
            @Bean
            FeatureAddressService featureAddressService(){
                return new FeatureAddressServiceImpl();
            }

            @Bean
            SampleService sampleService(){
                return  new SampleServiceImpl();
            }
        }

        @MockBean
        UserResourceRepository userResourceRepositoryMock;

        @Autowired
        FeatureAddressService featureAddressService;

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Before
        public void setUp() throws Exception{
            long userId1 = 0;
            long userId2 = 1;
            long userId3 = 2;
            AddressResource mockAddress1 = AddressResource.builder()
                    .userId(userId1)
                    .zipCode("000-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource mockEmail1 = EmailResource.builder()
                    .userId(userId1)
                    .emailNo(0)
                    .email("taro.mynavi@debugroom.org")
                    .build();
            EmailResource mockEmail2 = EmailResource.builder()
                    .userId(userId1)
                    .emailNo(1)
                    .email("taro.mynavi@test.org")
                    .build();
            UserResource mockUser1 = UserResource.builder()
                    .userId(userId1)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(mockAddress1)
                    .emailList(Arrays.asList(new EmailResource[]{mockEmail1, mockEmail2}))
                    .build();
            AddressResource mockAddress2 = AddressResource.builder()
                    .userId(userId2)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            EmailResource mockEmail3 = EmailResource.builder()
                    .userId(userId2)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            UserResource mockUser2 = UserResource.builder()
                    .userId(userId2)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("hanako.mynavi")
                    .address(mockAddress2)
                    .emailList(Arrays.asList(new EmailResource[]{mockEmail3}))
                    .build();
            AddressResource mockAddress3 = AddressResource.builder()
                    .userId(userId3)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            EmailResource mockEmail4 = EmailResource.builder()
                    .userId(userId3)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            UserResource mockUser3 = UserResource.builder()
                    .userId(userId3)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("hanako.mynavi")
                    .address(mockAddress3)
                    .emailList(Arrays.asList(new EmailResource[]{mockEmail4}))
                    .build();
            when(userResourceRepositoryMock.findOne(userId1)).thenReturn(mockUser1);
            when(userResourceRepositoryMock.findOne(userId2)).thenReturn(mockUser2);
            when(userResourceRepositoryMock.findOne(userId3)).thenThrow(BusinessException.class);
        }

        @Test
        public void getAddressAbnormalTest() throws Exception{
            BusinessException businessException = new BusinessException();
            expectedException.expect(BusinessException.class);
            featureAddressService.getAddress(new Long(2));
        }

    }

}

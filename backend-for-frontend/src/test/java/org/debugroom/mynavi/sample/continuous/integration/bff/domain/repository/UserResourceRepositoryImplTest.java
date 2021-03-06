package org.debugroom.mynavi.sample.continuous.integration.bff.domain.repository;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import org.debugroom.mynavi.sample.continuous.integration.bff.config.TestConfig;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessExceptionResponse;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.SystemException;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.SystemExceptionResponse;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.ValidationError;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.ValidationErrorResponse;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.test.junit.BusinessExceptionMatcher;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.test.junit.SystemExceptionMatcher;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.AddressResource;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.EmailResource;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.UserResource;

@RunWith(Enclosed.class)
public class UserResourceRepositoryImplTest {

    @RunWith(SpringRunner.class)
    @RestClientTest
    @ContextConfiguration(classes = {UnitTest.Config.class,
            TestConfig.UnitTestConfig.class})
    public static class UnitTest{

        @Configuration
        public static class Config{

            @Bean
            RestTemplate restTemplate(){
                return new RestTemplate();
            }

            @Bean
            public UserResourceRepository userResourceRepository(){
                return new UserResourceRepositoryImpl();
            }

        }

        @Autowired
        RestTemplate restTemplate;

        @Autowired
        MessageSource messageSource;

        @Autowired
        ObjectMapper objectMapper;

        @Autowired
        UserResourceRepository userResourceRepository;

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Test
        public void findOneAbnormalTest1() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            String errorCode = "E0001";
            BusinessException businessException = new BusinessException(errorCode,
                    messageSource.getMessage(errorCode, new Long[]{userId},
                            Locale.getDefault()), Long.toString(userId));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    BusinessExceptionResponse.builder()
                            .businessException(businessException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(BusinessException.class);
            expectedException.expect(BusinessExceptionMatcher.builder()
                    .businessException(businessException).build());

            userResourceRepository.findOne(userId);
        }

        @Test
        public void findOneAbnormalTest2() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new Long[]{userId},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    SystemExceptionResponse.builder()
                            .systemException(systemException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findOne(userId);
        }

        @Test
        public void findOneAbnormalTest3() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new Long[]{userId},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(user1);

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findOne(userId);

        }

        @Test
        public void findOneAbnormalTest4() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            String errorCode = "SE0001";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new Long[]{userId},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    SystemExceptionResponse.builder()
                            .systemException(systemException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withServerError().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findOne(userId);
        }

        @Test
        public void findAllAbnormalTest1() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{"/backend/api/v1/users"},
                            Locale.getDefault()), null);

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withBadRequest());

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findAll();

        }

        @Test
        public void findAllAbnormalTest2() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            String errorCode = "SE0001";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{"/backend/api/v1/users"},
                            Locale.getDefault()), null);

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withServerError());

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findAll();

        }

        @Test
        public void saveNormalTest() throws Exception{
            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String jsonRequestBody1 = objectMapper.writeValueAsString(user1);
            String jsonResponseBody1 = objectMapper.writeValueAsString(user1);

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/new"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                    .andExpect(MockRestRequestMatchers.content().string(jsonRequestBody1))
                    .andRespond(MockRestResponseCreators.withSuccess(
                            jsonResponseBody1, MediaType.APPLICATION_JSON));

            UserResource userResource = userResourceRepository.save(user1);

            assertThat(userResource.getFirstName(), is("taro"));
            assertThat(userResource.getFamilyName(), is("mynavi"));
            assertThat(userResource.getLoginId(), is("taro.mynavi"));
            assertThat(userResource.getAddress().getUserId(), is(0L));
            assertThat(userResource.getAddress().getZipCode(), is("100-0000"));
            assertThat(userResource.getAddress().getAddress(), is("Tokyo Chiyoda"));
            assertThat(userResource.getEmailList().get(0).getUserId(), is(0L));
            assertThat(userResource.getEmailList().get(0).getEmailNo(), is(0L));
            assertThat(userResource.getEmailList().get(0).getEmail(), is("taro.mynavi1@debugroom.org"));
            assertThat(userResource.getEmailList().get(1).getUserId(), is(0L));
            assertThat(userResource.getEmailList().get(1).getEmailNo(), is(1L));
            assertThat(userResource.getEmailList().get(1).getEmail(), is("taro.mynavi2@debugroom.org"));

        }

        @Test
        public void saveAbnormalTest1() throws  Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String jsonRequestBody1 = objectMapper.writeValueAsString(user1);
            String errorCode = "E0002";
            BusinessException businessException = new BusinessException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{user1.getLoginId()},
                            Locale.getDefault()), user1.getLoginId());
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    BusinessExceptionResponse.builder()
                            .businessException(businessException)
                    .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/new"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                    .andExpect(MockRestRequestMatchers.content().string(jsonRequestBody1))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(BusinessException.class);
            expectedException.expect(BusinessExceptionMatcher.builder()
                    .businessException(businessException).build());

            userResourceRepository.save(user1);

        }

        @Test
        public void saveAbnormalTest2() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String jsonRequestBody1 = objectMapper.writeValueAsString(user1);
            String errorCode = "BE0001";
            List<ValidationError> validationErrors = new ArrayList<>();
            BusinessException businessException = new BusinessException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{"/backend/api/v1/users/new"},
                            Locale.getDefault()), validationErrors);
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    ValidationErrorResponse.builder()
                            .validationErrors(validationErrors)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/new"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                    .andExpect(MockRestRequestMatchers.content().string(jsonRequestBody1))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(BusinessException.class);
            expectedException.expect(BusinessExceptionMatcher.builder()
                    .businessException(businessException).build());

            userResourceRepository.save(user1);
        }

        @Test
        public void saveAbnormalTest3() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String jsonRequestBody1 = objectMapper.writeValueAsString(user1);
            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{"/backend/api/v1/users/new"},
                            Locale.getDefault()), null);
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    SystemExceptionResponse.builder()
                            .systemException(systemException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/new"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                    .andExpect(MockRestRequestMatchers.content().string(jsonRequestBody1))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.save(user1);

        }

        @Test
        public void saveAbnormalTest4() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String jsonRequestBody1 = objectMapper.writeValueAsString(user1);
            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{"/backend/api/v1/users/new"},
                            Locale.getDefault()), null);
            String jsonResponseBody1 = objectMapper.writeValueAsString(user1);

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/new"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                    .andExpect(MockRestRequestMatchers.content().string(jsonRequestBody1))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.save(user1);

        }

        @Test
        public void saveAbnormalTest5() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String jsonRequestBody1 = objectMapper.writeValueAsString(user1);
            String errorCode = "SE0001";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{"/backend/api/v1/users/new"},
                            Locale.getDefault()), null);

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/new"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                    .andExpect(MockRestRequestMatchers.content().string(jsonRequestBody1))
                    .andRespond(MockRestResponseCreators.withServerError());

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.save(user1);

        }

        @Test
        public void deleteAbnormalTest1() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            String errorCode = "E0001";
            BusinessException businessException = new BusinessException(errorCode,
                    messageSource.getMessage(errorCode, new Long[]{userId},
                            Locale.getDefault()), Long.toString(userId));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    BusinessExceptionResponse.builder()
                            .businessException(businessException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(BusinessException.class);
            expectedException.expect(BusinessExceptionMatcher.builder()
                    .businessException(businessException).build());

            userResourceRepository.delete(userId);
        }

        @Test
        public void deleteAbnormalTest2() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new Long[]{userId},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    SystemExceptionResponse.builder()
                            .systemException(systemException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.delete(userId);
        }

        @Test
        public void deleteAbnormalTest3() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            AddressResource address1 = AddressResource.builder()
                    .userId(userId)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(userId)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(userId)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(userId)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{"/backend/api/v1/users/0"},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(user1);

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.delete(userId);

        }

        @Test
        public void deleteAbnormalTest4() throws Exception{

            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            Long userId = 0L;

            String errorCode = "SE0001";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new Long[]{userId},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    SystemExceptionResponse.builder()
                            .systemException(systemException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo("/backend/api/v1/users/0"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
                    .andRespond(MockRestResponseCreators.withServerError().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.delete(userId);
        }

        @Test
        public void findByLoginIdAbnormalTest1() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            String loginId = "taro.mynavi";

            String errorCode = "E0008";
            BusinessException businessException = new BusinessException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{loginId},
                            Locale.getDefault()), loginId);
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    BusinessExceptionResponse.builder()
                            .businessException(businessException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo(
                            "/backend/api/v1/users/loginId?loginId=taro.mynavi"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(BusinessException.class);
            expectedException.expect(BusinessExceptionMatcher.builder()
                    .businessException(businessException).build());

            userResourceRepository.findByLoginId(loginId);

        }

        @Test
        public void findByLoginIdAbnormalTest2() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            String loginId = "jiro.mynavi";

            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{loginId},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    SystemExceptionResponse.builder()
                            .systemException(systemException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo(
                            "/backend/api/v1/users/loginId?loginId=jiro.mynavi"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findByLoginId(loginId);

        }

        @Test
        public void findByLoginIdAbnormalTest3() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            String loginId = "jiro.mynavi";

            String errorCode = "SE0002";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{loginId},
                            Locale.getDefault()));

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo(
                            "/backend/api/v1/users/loginId?loginId=jiro.mynavi"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withBadRequest().body(
                            "{test:test}"));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findByLoginId(loginId);

        }

        @Test
        public void findByLoginIdAbnormalTest4() throws Exception{


            MockRestServiceServer mockRestServiceServer = MockRestServiceServer
                    .bindTo(restTemplate).build();

            String loginId = "jiro.mynavi";

            String errorCode = "SE0001";
            SystemException systemException = new SystemException(errorCode,
                    messageSource.getMessage(errorCode, new String[]{loginId},
                            Locale.getDefault()));
            String jsonResponseBody1 = objectMapper.writeValueAsString(
                    SystemExceptionResponse.builder()
                            .systemException(systemException)
                            .build());

            mockRestServiceServer
                    .expect(MockRestRequestMatchers.requestTo(
                            "/backend/api/v1/users/loginId?loginId=jiro.mynavi"))
                    .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                    .andRespond(MockRestResponseCreators.withServerError().body(
                            jsonResponseBody1));

            expectedException.expect(SystemException.class);
            expectedException.expect(SystemExceptionMatcher.builder()
                    .systemException(systemException).build());

            userResourceRepository.findByLoginId(loginId);

        }
    }

}

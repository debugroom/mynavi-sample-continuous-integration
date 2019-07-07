package org.debugroom.mynavi.sample.continuous.integration.bff.app.web;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersion.BrowserVersionBuilder;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import org.mockito.Mockito;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import org.debugroom.mynavi.sample.continuous.integration.bff.config.TestConfig;
import org.debugroom.mynavi.sample.continuous.integration.bff.domain.service.OrchestrateService;
import org.debugroom.mynavi.sample.continuous.integration.bff.app.web.selenium.PortalPage;
import org.debugroom.mynavi.sample.continuous.integration.bff.app.web.selenium.SeleniumProperties;
import org.debugroom.mynavi.sample.continuous.integration.bff.domain.repository.UserResourceRepository;
import org.debugroom.mynavi.sample.continuous.integration.bff.domain.service.SampleService;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.test.junit.E2ETest;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessException;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessExceptionResponse;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.ErrorResponse;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.AddressResource;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.EmailResource;
import org.debugroom.mynavi.sample.continuous.integration.common.web.model.UserResource;

@RunWith(Enclosed.class)
public class BackendForFrontendControllerTest {

    @RunWith(SpringRunner.class)
    @WebMvcTest(controllers = BackendForFrontendController.class)
    @ContextConfiguration(classes = {TestConfig.UnitTestConfig.class, WebMvcTestConfig.class})
    @Category(org.debugroom.mynavi.sample.continuous.integration.common.apinfra.test.junit.UnitTest.class)
    public static class UnitTest{

        WebClient webClient;

        @Autowired
        ObjectMapper objectMapper;

        @Autowired
        MockMvc mockMvc;

        @MockBean
        SampleService sampleServiceMock;

        @MockBean
        OrchestrateService orchestrateServiceMock;

        @Before
        public void setUp() throws Exception{

            BrowserVersionBuilder browserVersionBuilder = new BrowserVersionBuilder(BrowserVersion.CHROME);
            browserVersionBuilder.setBrowserLanguage("jp_JP");
            webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
                    .withDelegate(new WebClient(browserVersionBuilder.build()))
                    .build();
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());

            AddressResource address1 = AddressResource.builder()
                    .userId(0)
                    .zipCode("100-0000")
                    .address("Tokyo Chiyoda")
                    .build();

            AddressResource address2 = AddressResource.builder()
                    .userId(1)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
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

            EmailResource email3 = EmailResource.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("hanako.mynavi1@debugroom.org")
                    .build();

            EmailResource email4 = EmailResource.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("hanako.mynavi2@debugroom.org")
                    .build();

            UserResource user1 = UserResource.builder()
                    .userId(0)
                    .firstName("太郎")
                    .familyName("マイナビ")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(new EmailResource[]{email1, email2}))
                    .build();

            UserResource user2 = UserResource.builder()
                    .userId(0)
                    .firstName("花子")
                    .familyName("マイナビ")
                    .loginId("hanako.mynavi")
                    .address(address2)
                    .emailList(Arrays.asList(new EmailResource[]{email3, email4}))
                    .build();

            Mockito.when(sampleServiceMock.getUser(Long.getLong("2")))
                    .thenThrow(new BusinessException("E0001"));
            Mockito.when(sampleServiceMock.getUsers())
                    .thenReturn(Arrays.asList(new UserResource[]{user1, user2}));
            Mockito.when(sampleServiceMock.existsUserOfLoginId("taro.mynavi"))
                    .thenReturn(true);
            Mockito.when(sampleServiceMock.existsUserOfLoginId("hanako.mynavi"))
                    .thenReturn(false);

        }

        @Test
        public void getUsersTest() throws Exception{
            HtmlPage page = webClient.getPage("http://localhost:8080/getUsers");
            assertThat(page.getTitleText(), is("GetUsers"));
            assertThat(page.getElementById("td-firstName-0")
                    .getFirstChild().asText(), is("太郎"));
            assertThat(page.getElementById("td-familyName-0")
                    .getFirstChild().asText(), is("マイナビ"));
            assertThat(page.getElementById("td-loginId-0")
                    .getFirstChild().asText(), is("taro.mynavi"));
            assertThat(page.getElementById("td-address-zipCode-0")
                    .getFirstChild().asText(), is("100-0000"));
            assertThat(page.getElementById("td-address-address-0")
                    .getFirstChild().asText(), is("Tokyo Chiyoda"));
            assertThat(page.getElementById("td-email-0_email-0")
                    .getFirstChild().asText(), is("taro.mynavi1@debugroom.org"));
            assertThat(page.getElementById("td-email-0_email-1")
                    .getFirstChild().asText(), is("taro.mynavi2@debugroom.org"));

            assertThat(page.getElementById("td-firstName-1")
                    .getFirstChild().asText(), is("花子"));
            assertThat(page.getElementById("td-familyName-1")
                    .getFirstChild().asText(), is("マイナビ"));
            assertThat(page.getElementById("td-loginId-1")
                    .getFirstChild().asText(), is("hanako.mynavi"));
            assertThat(page.getElementById("td-address-zipCode-1")
                    .getFirstChild().asText(), is("300-0000"));
            assertThat(page.getElementById("td-address-address-1")
                    .getFirstChild().asText(), is("Tonde Saitama"));
            assertThat(page.getElementById("td-email-1_email-0")
                    .getFirstChild().asText(), is("hanako.mynavi1@debugroom.org"));
            assertThat(page.getElementById("td-email-1_email-1")
                    .getFirstChild().asText(), is("hanako.mynavi2@debugroom.org"));
        }

        @Test
        public void getUserAbnormalTest() throws Exception{
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
            .get("/getUser").requestAttr("userId", 2))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            BusinessExceptionResponse businessExceptionResponse = (BusinessExceptionResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class);

            assertThat(businessExceptionResponse.getBusinessException().getCode(), is("E0001"));

        }

        @Test
        public void isUsableLoginIdNormalTest() throws Exception{
            HtmlPage page = webClient.getPage("http://localhost:8080/portal");
            HtmlButton htmlButton = (HtmlButton)page.getElementById("isUsableLoginIdButton-0");
            HtmlTextInput loginIdInput = (HtmlTextInput)page.getElementById("loginId-0");
            loginIdInput.setText("taro.mynavi");
            HtmlPage updatePage = htmlButton.click();
            webClient.waitForBackgroundJavaScript(10000);
            HtmlElement htmlElement = (HtmlElement)updatePage.getElementById("message-panel");

            assertThat(htmlElement.getFirstChild().asText(), is("使用可能なログインIDです。"));

        }

        @Test
        public void isUsableLoginIdAbrmalTest() throws Exception{
            HtmlPage page = webClient.getPage("http://localhost:8080/portal");
            HtmlButton htmlButton = (HtmlButton)page.getElementById("isUsableLoginIdButton-0");
            HtmlTextInput loginIdInput = (HtmlTextInput)page.getElementById("loginId-0");
            loginIdInput.setText("jiro.mynavi");
            HtmlPage updatePage = htmlButton.click();
            webClient.waitForBackgroundJavaScript(10000);
            HtmlElement htmlElement = (HtmlElement)updatePage.getElementById("message-panel");

            assertThat(htmlElement.getFirstChild().asText(), is("このログインIDは使用できません。"));

        }

        @Test
        public void addUsersInputParamTest() throws Exception{
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.set("users[0].firstName", "taro");
            params.set("users[0].familyName", "mynavi");
            params.set("users[0].loginId", "taro.mynavi");
            params.set("users[0].address.zipCode", "100-0000");
            params.set("users[0].address.address", "Toky　Chiyoda");
            params.set("users[0].emailList[0].email", "taro.mynavi@debugroom.org");
            params.set("users[1].firstName", "hanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanako");
            params.set("users[1].familyName", "mynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavi");
            params.set("users[1].loginId", "hanako.mynavi.hanako.mynavi.hanako.mynavi");
            params.set("users[1].address.zipCode", "100a0000");
            params.set("users[1].address.address", "Tokyo<>Chiyoda");
            params.set("users[1].emailList[0].email", "taro.mynavidebugroom.org");
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/addUsers")
                    .params(params))
                    .andReturn();
            ModelAndView modelAndView = mvcResult.getModelAndView();

            assertThat(modelAndView.getViewName(), is("portal"));
            BindingResult bindingResult = (BindingResult)
                    modelAndView.getModel().get("org.springframework.validation.BindingResult.addUsersForm");

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            assertThat(fieldErrors.size(), is(6));
            fieldErrors.stream().forEach(fieldError -> {
                switch (fieldError.getField()){
                    case  "users[1].firstName" :
                        assertThat(fieldError.getDefaultMessage(), is("1文字以上、50文字以下で入力してください。"));
                        break;
                    case  "users[1].familyName" :
                        assertThat(fieldError.getDefaultMessage(), is("1文字以上、50文字以下で入力してください。"));
                        break;
                    case  "users[1].loginId" :
                        assertThat(fieldError.getDefaultMessage(), is("1文字以上、32文字以下で入力してください。"));
                        break;
                    case  "users[1].emailList[0].email" :
                        assertThat(fieldError.getDefaultMessage(), is("無効なメール形式です。"));
                        break;
                    case  "users[1].address.zipCode" :
                        assertThat(fieldError.getDefaultMessage(), is("無効な文字が含まれています。"));
                        break;
                    case  "users[1].address.address" :
                        assertThat(fieldError.getDefaultMessage(), is("無効な文字が含まれています。"));
                        break;
                    default: fail();
                }
            });

        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = {
            TestConfig.EndToEndTestConfig.class,
            BackendForFrontendControllerTest.EndToEndTest.Config.class
    }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    @Category(E2ETest.class)
    public static class EndToEndTest{

        @Configuration
        public static class Config{

            @Autowired
            SeleniumProperties seleniumProperties;

            @Bean
            @Profile("dev")
            WebDriver webDriver(){
                System.setProperty("webdriver.chrome.driver", seleniumProperties.getChromeDriverPath());
                /*
                 * This option is setting for as follow error in Ubuntu CodeBuild environment.
                 *
                 * Caused by: org.springframework.beans.factory.BeanCreationException:
                 * Error creating bean with name 'webDriver' defined in
                 * org.debugroom.mynavi.sample.continuous.integration.bff.app.web.BackendForFrontendControllerTest$EndToEndTest$Config:
                 * Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException:
                 * Failed to instantiate [org.openqa.selenium.WebDriver]: Factory method 'webDriver' threw exception;
                 * nested exception is org.openqa.selenium.WebDriverException:
                 * unknown error: Chrome failed to start: exited abnormally
                 * See : https://stackoverflow.com/questions/50642308/webdriverexception-unknown-error-devtoolsactiveport-file-doesnt-exist-while-t
                 */
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
                options.addArguments("start-maximized"); // open Browser in maximized mode
                options.addArguments("disable-infobars"); // disabling infobars
                options.addArguments("--disable-extensions"); // disabling extensions
                options.addArguments("--disable-gpu"); // applicable to windows os only
                options.addArguments("--no-sandbox"); // Bypass OS security model
                return new ChromeDriver(options);
            }


        }

        @Value("#{servletContext.contextPath}")
        private String contextPath;

        @Autowired
        SeleniumProperties seleniumProperties;

        @LocalServerPort
        private int port;

        @Autowired(required = false)
        WebDriver webDriver;

        @Autowired
        PortalPage portalPage;

        @Autowired
        UserResourceRepository userResourceRepository;


        @Before
        public void setUp(){
            portalPage.setWebDriver(webDriver);
        }

        @Test
        public void getUsersTest(){
            webDriver.get("http://localhost:" + port + contextPath + "/portal");
            webDriver.findElement(By.id("getUsersButton")).click();
            File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            file.renameTo(new File(seleniumProperties.getEvidencePath() + "/getUsersTest_screenshot.png"));
            webDriver.getPageSource().contains("taro.mynavi");
            webDriver.getPageSource().contains("hanako.mynavi");
            webDriver.getPageSource().contains("jiro.mynavi");
            webDriver.getPageSource().contains("taro.mynavi1");
            webDriver.getPageSource().contains("jiro.mynavi1");
        }

        @Test
        public void addUsers_1_NormalTest(){
            try{
                UserResource userResource1 = userResourceRepository.findByLoginId("taro.mynavi1");
                UserResource userResource2 = userResourceRepository.findByLoginId("jiro.mynavi1");

                userResourceRepository.delete(userResource1.getUserId());
                userResourceRepository.delete(userResource2.getUserId());
            }catch (BusinessException e){
                // if user does not exist, continue to process.
            }
            webDriver.get("http://localhost:" + port + contextPath + "/portal");
            webDriver.findElement(By.id("addFormButton-0")).click();
            portalPage.setAddUserForm1("taro", "mynavi",
                    "taro.mynavi1", "100-0000", "Tokyo　Chiyoda",
                    "taro.mynavi1@debugroom.org");
            portalPage.setAddUserForm2("jiro", "mynavi",
                    "jiro.mynavi1", "300-0000", "Tonde　Saitama",
                    "jiro.mynavi1@debugroom.org");
            webDriver.findElement(By.id("addUsersButton")).click();
            webDriver.getPageSource().contains("taro.mynavi1");
            webDriver.getPageSource().contains("jiro.mynavi1");


            File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            file.renameTo(new File(seleniumProperties.getEvidencePath() + "/addUserNormalTest_screenshot.png"));
        }

        @Test
        public void addUsers_2_AbnormalTest(){
            webDriver.get("http://localhost:" + port + contextPath + "/portal");
            webDriver.findElement(By.id("addFormButton-0")).click();
            portalPage.setAddUserForm1("saburo", "mynavi",
                    "saburo.mynavi1", "100-0000", "Tokyo　Minato",
                    "saburo.mynavi1@debugroom.org");
            portalPage.setAddUserForm2("jiro", "mynavi",
                    "jiro.mynavi1", "300-0000", "Tonde　Saitama",
                    "jiro.mynavi1@debugroom.org");
            webDriver.findElement(By.id("addUsersButton")).click();
            webDriver.getPageSource().contains("使用できないログインIDです。LoginID : jiro.mynavi1");
            File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            file.renameTo(new File(seleniumProperties.getEvidencePath() + "/addUserAbnormalTest_screenshot.png"));
        }

        @After
        public void shutdown(){
            webDriver.quit();
        }



    }

}

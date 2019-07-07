package org.debugroom.mynavi.sample.continuous.integration.bff.app.web;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import org.debugroom.mynavi.sample.continuous.integration.bff.config.TestConfig;
import org.debugroom.mynavi.sample.continuous.integration.bff.domain.service.FeatureAddressService;
import org.debugroom.mynavi.sample.continuous.integration.common.apinfra.exception.BusinessException;


@RunWith(Enclosed.class)
public class FeatureAddressControllerTest {

    @RunWith(SpringRunner.class)
    @WebMvcTest(controllers = FeatureAddressController.class)
    @ContextConfiguration(classes = {TestConfig.UnitTestConfig.class, WebMvcTestConfig.class})
    public static class UnitTest{

        WebClient webClient;

        @Autowired
        MockMvc mockMvc;

        @MockBean
        FeatureAddressService featureAddressServiceMock;

        @Before
        public void setUp() throws Exception{
            BrowserVersion.BrowserVersionBuilder browserVersionBuilder = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME);
            browserVersionBuilder.setBrowserLanguage("jp_JP");
            webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
                    .withDelegate(new WebClient(browserVersionBuilder.build()))
                    .build();
            Mockito.when(featureAddressServiceMock.getAddress(Long.getLong("2")))
                    .thenThrow(new BusinessException("B0003", "", "2"));
        }

        @Test
        public void getAddressAbnormalTest() throws Exception{
            HtmlPage page = webClient.getPage("http://localhost:8080/getAddress?userId=2");
            HtmlElement htmlElement = (HtmlElement)page.getElementById("getAddressFormErrorMessage");
            assertThat(htmlElement.getFirstChild().asText(), is("指定されたユーザは存在しないか、IDが誤っています。ID : 2"));
        }

    }

}

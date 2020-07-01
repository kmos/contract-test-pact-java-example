package io.mosfet.contracts.provider;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import io.mosfet.contracts.provider.domain.Phrase;
import io.mosfet.contracts.provider.domain.PhraseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ProviderApplication.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-contract-test.properties")
@Provider("providerMicroservice")
@PactFolder("../consumer/target/pacts")
public class ProviderContractTest {

    @Value("${server.host}")
    private String serverHost;
    @Value("${server.port}")
    private int serverPort;

    @MockBean
    private PhraseService phraseService;

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget(serverHost, serverPort, "/"));
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("a sentence worked at 1593373360")
    public void sentenceWorkedAt1593373360() {
        when(phraseService.echo(1593373353, "hello!"))
                .thenReturn(new Phrase("hello! sent at: 1593373353 worked at: 1593373360"));
    }

    @State("WITH DSL: a sentence worked at 1593373360")
    public void sentenceWorkedAt1593373360WithDSL() {
        System.out.println("call real method, only String type match is necessary");
        when(phraseService.echo(1593373353, "hello!")).thenCallRealMethod();
    }

}

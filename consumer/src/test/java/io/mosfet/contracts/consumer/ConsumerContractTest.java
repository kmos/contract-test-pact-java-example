package io.mosfet.contracts.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
class ConsumerContractTest {

    private static final String API_ECHO = "/api/echo";

    private static final String echoRequest =
            "{\n" +
            "\t\"timestamp\": \"1593373353\",\n" +
            "\t\"sentence\": \"hello!\"\n" +
            "}";

    private final String echoResponse =
            "{\n" +
            "\t\"phrase\": \"hello! sent at: 1593373353 worked at: 1593373360\"\n" +
            "}";


    private final static Map<String, String> headers;
    private EchoResponse expectedResult;

    static {
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
    }

    @BeforeEach
    public void setUp(MockServer mockServer) {
        expectedResult = new EchoResponse();
        expectedResult.setPhrase("hello! sent at: 1593373353 worked at: 1593373360");

        assertThat(mockServer, is(notNullValue()));
    }

    @Pact(provider = "providerMicroservice", consumer = "consumerMicroservice")
    public RequestResponsePact echoRequest(PactDslWithProvider builder) {

        return builder
                .given("a sentence worked at 1593373360")
                .uponReceiving("an echo request at 1593373353")
                .path(API_ECHO)
                .method("POST")
                .body(echoRequest)
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(echoResponse)
                .toPact();
    }

    @Pact(provider = "providerMicroservice", consumer = "consumerMicroservice")
    public RequestResponsePact echoRequestWithDsl(PactDslWithProvider builder) {

        PactDslJsonBody responseWrittenWithDsl = new PactDslJsonBody()
                .stringType("phrase", "hello! sent at: 1593373353 worked at: 1593373360")
                .close()
                .asBody();

        return builder
                .given("WITH DSL: a sentence worked at 1593373360")
                .uponReceiving("an echo request at 1593373353")
                .path(API_ECHO)
                .method("POST")
                .body(echoRequest)
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(responseWrittenWithDsl)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "echoRequest")
    @DisplayName("given a sentence with a timestamp, when calling provider microservice, than I receive back an echo sentence with a timestamp")
    void givenASentenceWithATimestampWhenCallingProviderThanReturnAnEchoWithATimestamp(MockServer mockServer) throws IOException {
        EchoResponse actualResult = makeMockEchoRequest(mockServer);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @PactTestFor(pactMethod = "echoRequestWithDsl")
    @DisplayName("WITH DSL: given a sentence with a timestamp, when calling provider microservice, than I receive back an echo sentence with a timestamp")
    void givenASentenceWithATimestampWhenCallingProviderThanReturnAnEchoWithATimestampWITHDSL(MockServer mockServer) throws IOException {
        EchoResponse actualResult = makeMockEchoRequest(mockServer);
        assertEquals(expectedResult, actualResult);
    }

    private EchoResponse makeMockEchoRequest(MockServer mockServer) throws IOException {
        BasicHttpEntity bodyRequest = new BasicHttpEntity();
        bodyRequest.setContent(IOUtils.toInputStream(echoRequest, Charset.defaultCharset()));

        HttpResponse httpResponse = Request.Post(mockServer.getUrl() + API_ECHO)
                .body(bodyRequest)
                .execute()
                .returnResponse();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(httpResponse.getEntity().getContent(), EchoResponse.class);
    }

}
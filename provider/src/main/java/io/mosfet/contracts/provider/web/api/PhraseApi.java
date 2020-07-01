package io.mosfet.contracts.provider.web.api;

import io.mosfet.contracts.provider.web.model.EchoRequest;
import io.mosfet.contracts.provider.web.model.EchoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface PhraseApi {

    @RequestMapping(
            value = "/api/echo",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<EchoResponse> echo(@RequestBody EchoRequest echoRequest);
}

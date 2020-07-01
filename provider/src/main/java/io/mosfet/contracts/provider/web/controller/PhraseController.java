package io.mosfet.contracts.provider.web.controller;

import io.mosfet.contracts.provider.domain.Phrase;
import io.mosfet.contracts.provider.domain.PhraseService;
import io.mosfet.contracts.provider.web.api.PhraseApi;
import io.mosfet.contracts.provider.web.model.EchoRequest;
import io.mosfet.contracts.provider.web.model.EchoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class PhraseController implements PhraseApi {

    PhraseService phraseService;

    public ResponseEntity<EchoResponse> echo(EchoRequest echoRequest) {
        Phrase echo = phraseService.echo(echoRequest.getTimestamp(), echoRequest.getSentence());
        return ResponseEntity.ok(new EchoResponse(echo.getSentence()));
    }
}

package io.mosfet.contracts.provider.domain;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@NoArgsConstructor
public class PhraseService {

    public Phrase echo(long timestamp, String sentence) {
        return new Phrase(sentence + " sent at: " + timestamp + " worked at: " + Instant.now().getEpochSecond());
    }
}
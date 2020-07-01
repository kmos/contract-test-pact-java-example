package io.mosfet.contracts.provider.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EchoRequest {
    private long timestamp;
    private String sentence;
}

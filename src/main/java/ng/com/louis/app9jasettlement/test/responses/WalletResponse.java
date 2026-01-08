package ng.com.louis.app9jasettlement.test.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WalletResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("balance")
    private Long balance;
    @JsonProperty("createdAt")
    private Instant createdAt;

}

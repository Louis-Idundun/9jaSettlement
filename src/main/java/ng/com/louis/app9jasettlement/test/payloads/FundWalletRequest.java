package ng.com.louis.app9jasettlement.test.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FundWalletRequest {
    @NotNull
    @Min(1)
    private Long amount;
    @NotNull
    private UUID walletId;
}

package ng.com.louis.app9jasettlement.test.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransferRequest {
    @NotNull(message = "Source wallet ID is required")
    private UUID senderWalletId;

    @NotNull(message = "Destination wallet ID is required")
    private UUID receiverWalletId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Long amount;

    @NotBlank(message = "Idempotency key is required")
    @Size(max = 255, message = "Idempotency key must not exceed 255 characters")
    private String idempotencyKey;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}

package ng.com.louis.app9jasettlement.test.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ng.com.louis.app9jasettlement.test.enums.TransactionType;
import jakarta.validation.constraints.NotNull;



import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionRequest {
    @NotNull(message = "Wallet ID is required")
    private UUID walletId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Long amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotBlank(message = "Idempotency key is required")
    @Size(max = 255, message = "Idempotency key must not exceed 255 characters")
    private String idempotencyKey;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}

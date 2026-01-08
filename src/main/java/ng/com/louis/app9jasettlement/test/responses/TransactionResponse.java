package ng.com.louis.app9jasettlement.test.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ng.com.louis.app9jasettlement.test.enums.TransactionType;
import ng.com.louis.app9jasettlement.test.entities.Transaction;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("walletId")
    private UUID walletId;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("transactionType")
    private TransactionType type;

    @JsonProperty("idempotencyKey")
    private String idempotencyKey;

    @JsonProperty("description")
    private String description;

    @JsonProperty("createdAt")
    private Instant createdAt;


    /**
     * Factory Method to map Transaction Entity → DTO
     */
    public static TransactionResponse fromEntity(Transaction tx) {
        return TransactionResponse.builder()
                .id(tx.getId())
                .walletId(tx.getWallet().getId())
                .amount(tx.getAmount())
                .type(tx.getType())
                .idempotencyKey(tx.getIdempotencyKey())
                .description(tx.getDescription())
                .createdAt(tx.getCreatedAt())
                .build();
    }
}

package ng.com.louis.app9jasettlement.test.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ng.com.louis.app9jasettlement.test.entities.Transfer;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TransferResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("senderWalletId")
    private UUID senderWalletId;

    @JsonProperty("receiverWalletId")
    private UUID receiverWalletId;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("idempotencyKey")
    private String idempotencyKey;

    @JsonProperty("debitTransactionId")
    private UUID debitTransactionId;

    @JsonProperty("creditTransactionId")
    private UUID creditTransactionId;

    @JsonProperty("createdAt")
    private Instant createdAt;


    public static TransferResponse fromEntity(Transfer transfer) {
        return TransferResponse.builder()
                .id(transfer.getId())
                .senderWalletId(transfer.getSenderWallet().getId())
                .receiverWalletId(transfer.getReceiverWallet().getId())
                .amount(transfer.getAmount())
                .idempotencyKey(transfer.getIdempotencyKey())
                .debitTransactionId(
                        transfer.getDebitTransaction() != null ?
                                transfer.getDebitTransaction().getId() : null
                )
                .creditTransactionId(
                        transfer.getCreditTransaction() != null ?
                                transfer.getCreditTransaction().getId() : null
                )
                .createdAt(transfer.getCreatedAt())
                .build();
    }
}

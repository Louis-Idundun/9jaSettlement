package ng.com.louis.app9jasettlement.test.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "transfers", indexes = {
        @Index(name = "idx_transfers_from_wallet", columnList = "sender_wallet_id"),
        @Index(name = "idx_transfers_to_wallet", columnList = "receiver_wallet_id")
})
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_wallet_id", nullable = false)
    private Wallet senderWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_wallet_id", nullable = false)
    private Wallet receiverWallet;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 255)
    private String idempotencyKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_transaction_id")
    private Transaction debitTransaction;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_transaction_id")
    private Transaction creditTransaction;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

}


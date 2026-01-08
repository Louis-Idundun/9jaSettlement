package ng.com.louis.app9jasettlement.test.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@SuperBuilder
@Data
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long balance = 0L;

    @Column(nullable = false)
    private String walletName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Wallet() {
    }

    public void credit(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.balance += amount;
    }

    public void debit(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        if (this.balance < amount) {
            throw new IllegalStateException(
                    String.format("Insufficient balance. Required: %d, Available: %d", amount, this.balance)
            );
        }
        this.balance -= amount;
    }
}


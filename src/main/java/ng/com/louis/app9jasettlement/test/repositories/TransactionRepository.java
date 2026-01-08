package ng.com.louis.app9jasettlement.test.repositories;
import ng.com.louis.app9jasettlement.test.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    boolean existsByIdempotencyKey(String idempotencyKey);

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(UUID walletId);
}


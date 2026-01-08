package ng.com.louis.app9jasettlement.test.services.implementations;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ng.com.louis.app9jasettlement.test.entities.Transaction;
import ng.com.louis.app9jasettlement.test.entities.Transfer;
import ng.com.louis.app9jasettlement.test.entities.Wallet;
import ng.com.louis.app9jasettlement.test.enums.TransactionType;
import ng.com.louis.app9jasettlement.test.exceptions.*;
import ng.com.louis.app9jasettlement.test.payloads.TransactionRequest;
import ng.com.louis.app9jasettlement.test.payloads.TransferRequest;
import ng.com.louis.app9jasettlement.test.responses.TransactionResponse;
import ng.com.louis.app9jasettlement.test.responses.TransferResponse;
import ng.com.louis.app9jasettlement.test.repositories.TransactionRepository;
import ng.com.louis.app9jasettlement.test.repositories.TransferRepository;
import ng.com.louis.app9jasettlement.test.services.TransactionService;
import ng.com.louis.app9jasettlement.test.services.WalletService;
import ng.com.louis.app9jasettlement.test.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TransactionServiceImplementation implements TransactionService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;

    public TransactionServiceImplementation(
            WalletService walletService,
            TransactionRepository transactionRepository,
            TransferRepository transferRepository
    ) {
        this.walletService = walletService;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<?>> processTransaction(TransactionRequest request) {

        Optional<Transaction> existing = transactionRepository.findByIdempotencyKey(request.getIdempotencyKey());

        if (existing.isPresent()) {
            Transaction saved = existing.get();

            boolean matches =
                    saved.getWallet().getId().equals(request.getWalletId()) &&
                            saved.getAmount().equals(request.getAmount()) &&
                            saved.getType().equals(request.getType());

            if (!matches) {
                throw new DuplicateIdempotencyKeyException(request.getIdempotencyKey());
            }

            log.info("Returning existing transaction for idempotency key: {}", request.getIdempotencyKey());

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "TRANSACTION",
                            TransactionResponse.fromEntity(saved),
                            "Duplicate transaction - returning existing result"
                    )
            );
        }

        Wallet wallet = walletService.getWalletWithLock(request.getWalletId());

        if (request.getType() == TransactionType.CREDIT) {
            wallet.credit(request.getAmount());
        } else {
            if (wallet.getBalance() < request.getAmount()) {
                throw new InsufficientBalanceException(request.getWalletId(), request.getAmount(), wallet.getBalance());
            }
            wallet.debit(request.getAmount());
        }

        walletService.saveWallet(wallet);

        Transaction tx = Transaction.builder()
                .wallet(wallet)
                .amount(request.getAmount())
                .type(request.getType())
                .idempotencyKey(request.getIdempotencyKey())
                .description(request.getDescription())
                .build();

        Transaction savedTx = transactionRepository.save(tx);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "TRANSACTION",
                        TransactionResponse.fromEntity(savedTx),
                        "Transaction processed successfully"
                )
        );
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<?>> processTransfer(TransferRequest request) {

        if (request.getSenderWalletId().equals(request.getReceiverWalletId())) {
            throw new SameWalletTransferException(request.getSenderWalletId());
        }

        Optional<Transfer> existing = transferRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existing.isPresent()) {
            Transfer saved = existing.get();

            boolean matches = saved.getSenderWallet().getId().equals(request.getSenderWalletId())
                    && saved.getReceiverWallet().getId().equals(request.getReceiverWalletId())
                    && saved.getAmount().equals(request.getAmount());

            if (!matches) {
                throw new DuplicateIdempotencyKeyException(request.getIdempotencyKey());
            }

            log.info("Returning existing transfer for idempotency key: {}", request.getIdempotencyKey());

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "TRANSFER",
                            TransferResponse.fromEntity(saved),
                            "Duplicate transfer - returning existing result"
                    )
            );
        }

        Wallet senderWallet;
        Wallet receiverWallet;
        if (request.getSenderWalletId().compareTo(request.getReceiverWalletId()) < 0) {
            senderWallet = walletService.getWalletWithLock(request.getSenderWalletId());
            receiverWallet = walletService.getWalletWithLock(request.getReceiverWalletId());
        } else {
            receiverWallet = walletService.getWalletWithLock(request.getReceiverWalletId());
            senderWallet = walletService.getWalletWithLock(request.getSenderWalletId());
        }

        if (senderWallet.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException(senderWallet.getId(), request.getAmount(), senderWallet.getBalance());
        }

        senderWallet.debit(request.getAmount());
        receiverWallet.credit(request.getAmount());

        walletService.saveWallet(senderWallet);
        walletService.saveWallet(receiverWallet);

        Transaction debitTx = Transaction.builder()
                .wallet(senderWallet)
                .amount(request.getAmount())
                .type(TransactionType.DEBIT)
                .idempotencyKey(request.getIdempotencyKey() + "-debit")
                .description("Transfer to wallet " + receiverWallet.getId())
                .build();

        Transaction creditTx = Transaction.builder()
                .wallet(receiverWallet)
                .amount(request.getAmount())
                .type(TransactionType.CREDIT)
                .idempotencyKey(request.getIdempotencyKey() + "-credit")
                .description("Transfer from wallet " + senderWallet.getId())
                .build();

        debitTx = transactionRepository.save(debitTx);
        creditTx = transactionRepository.save(creditTx);

        Transfer transfer = Transfer.builder()
                .senderWallet(senderWallet)
                .receiverWallet(receiverWallet)
                .amount(request.getAmount())
                .idempotencyKey(request.getIdempotencyKey())
                .debitTransaction(debitTx)
                .creditTransaction(creditTx)
                .build();

        Transfer savedTransfer = transferRepository.save(transfer);

        log.info("Transferred {} from {} to {}", request.getAmount(), senderWallet.getId(), receiverWallet.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "TRANSFER",
                        TransferResponse.fromEntity(savedTransfer),
                        "Transfer processed successfully"
                )
        );
    }
}

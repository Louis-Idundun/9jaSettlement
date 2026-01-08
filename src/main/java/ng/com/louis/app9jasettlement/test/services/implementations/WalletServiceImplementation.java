package ng.com.louis.app9jasettlement.test.services.implementations;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ng.com.louis.app9jasettlement.test.entities.Wallet;
import ng.com.louis.app9jasettlement.test.exceptions.WalletNotFoundException;
import ng.com.louis.app9jasettlement.test.payloads.FundWalletRequest;
import ng.com.louis.app9jasettlement.test.payloads.WalletRequest;
import ng.com.louis.app9jasettlement.test.repositories.WalletRepository;
import ng.com.louis.app9jasettlement.test.services.WalletService;
import ng.com.louis.app9jasettlement.test.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class WalletServiceImplementation implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImplementation.class);
    private final WalletRepository walletRepository;

    public WalletServiceImplementation(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<?>> createWallet(WalletRequest walletRequest) {
        if (walletRequest == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("WALLET", null, "Invalid wallet request")
            );
        }

        String firstName = walletRequest.getFirstName() != null ? walletRequest.getFirstName() : "";
        String lastName = walletRequest.getLastName() != null ? walletRequest.getLastName() : "";
        String ownerName = (firstName + " " + lastName).trim().toLowerCase();

        Optional<Wallet> existingWalletOpt = walletRepository.findByWalletName(ownerName);

        if (existingWalletOpt.isPresent()) {
            Wallet existingWallet = existingWalletOpt.get();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ApiResponse.error(
                            "WALLET",
                            "A wallet already exists for owner: " + ownerName +
                                    " with wallet ID: " + existingWallet.getId(),
                            "Wallet already exists for user"
                    )
            );
        }


        Wallet newWallet = new Wallet();
        newWallet.setWalletName(ownerName);
        newWallet.setBalance(0L);

        Wallet savedWallet = walletRepository.save(newWallet);
        logger.info("Created new wallet for {} with id: {}", ownerName, savedWallet.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "WALLET",
                        savedWallet,
                        "Wallet created successfully"
                )
        );
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<?>> fundWallet(FundWalletRequest request) {

        Wallet wallet = walletRepository.findByIdWithLock(request.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException(request.getWalletId()));

        try {
            wallet.credit(request.getAmount());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("WALLET", null, e.getMessage())
            );
        }

        Wallet updatedWallet = walletRepository.save(wallet);
        logger.info("Wallet {} funded with amount {}. New balance: {}", wallet.getId(), request.getAmount(), updatedWallet.getBalance());

        return ResponseEntity.ok(
                ApiResponse.success("WALLET", updatedWallet, "Wallet funded successfully")
        );
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));

        return ResponseEntity.ok(
                ApiResponse.success("WALLET", wallet, "Wallet retrieved successfully")
        );
    }

    @Override
    @Transactional
    public Long getWalletBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
        return wallet.getBalance();
    }


    @Override
    @Transactional
    public Wallet getWalletWithLock(UUID walletId) {
        return walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    @Override
    @Transactional
    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}

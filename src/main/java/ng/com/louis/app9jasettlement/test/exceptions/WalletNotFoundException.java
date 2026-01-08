package ng.com.louis.app9jasettlement.test.exceptions;

import java.util.UUID;

public class WalletNotFoundException extends RuntimeException {

    private final UUID walletId;

    public WalletNotFoundException(UUID walletId) {
        super(String.format("Wallet with id '%s' not found", walletId));
        this.walletId = walletId;
    }

    public UUID getWalletId() {
        return walletId;
    }
}


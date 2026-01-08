package ng.com.louis.app9jasettlement.test.exceptions;

import java.util.UUID;

public class SameWalletTransferException extends RuntimeException {

    private final UUID walletId;

    public SameWalletTransferException(UUID walletId) {
        super(String.format("Cannot transfer to the same wallet: %s", walletId));
        this.walletId = walletId;
    }

    public UUID getWalletId() {
        return walletId;
    }
}

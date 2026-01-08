package ng.com.louis.app9jasettlement.test.exceptions;

import java.util.UUID;


public class InsufficientBalanceException extends RuntimeException {

    private final UUID walletId;
    private final Long requiredAmount;
    private final Long availableBalance;

    public InsufficientBalanceException(UUID walletId, Long requiredAmount, Long availableBalance) {
        super(String.format(
                "Wallet has insufficient balance. Required: %d, Available: %d",
                requiredAmount, availableBalance
        ));
        this.walletId = walletId;
        this.requiredAmount = requiredAmount;
        this.availableBalance = availableBalance;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public Long getRequiredAmount() {
        return requiredAmount;
    }

    public Long getAvailableBalance() {
        return availableBalance;
    }
}


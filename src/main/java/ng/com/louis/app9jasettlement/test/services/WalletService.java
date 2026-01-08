package ng.com.louis.app9jasettlement.test.services;

import ng.com.louis.app9jasettlement.test.entities.Wallet;
import ng.com.louis.app9jasettlement.test.payloads.FundWalletRequest;
import ng.com.louis.app9jasettlement.test.payloads.WalletRequest;
import ng.com.louis.app9jasettlement.test.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface WalletService {
    ResponseEntity<ApiResponse<?>> createWallet (WalletRequest walletRequest);
    ResponseEntity<ApiResponse<?>> fundWallet(FundWalletRequest request);
    ResponseEntity<ApiResponse<?>> getWallet (UUID walletId);
    Long getWalletBalance(UUID walletId);
    Wallet saveWallet (Wallet wallet);
    Wallet getWalletWithLock (UUID walletId);
}

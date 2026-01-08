package ng.com.louis.app9jasettlement.test.controllers;

import jakarta.validation.Valid;
import ng.com.louis.app9jasettlement.test.entities.Wallet;
import ng.com.louis.app9jasettlement.test.payloads.FundWalletRequest;
import ng.com.louis.app9jasettlement.test.payloads.WalletRequest;
import ng.com.louis.app9jasettlement.test.services.WalletService;
import ng.com.louis.app9jasettlement.test.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    @PostMapping("/create/wallet")
    public ResponseEntity<ApiResponse<?>> createWallet(@RequestBody WalletRequest request) {
        return walletService.createWallet(request);
    }

    @PostMapping("/fund/wallet")
    public ResponseEntity<ApiResponse<?>> fundWallet(@Valid @RequestBody FundWalletRequest request) {
        return walletService.fundWallet(request);
    }

    @GetMapping("/get/{walletId}")
    public ResponseEntity<ApiResponse<?>> getWallet (@PathVariable UUID walletId) {
        return walletService.getWallet(walletId);
    }

    @GetMapping("/wallet/{walletId}/balance")
    public ResponseEntity<ApiResponse<?>> getWalletBalance(@PathVariable UUID walletId) {
        Long balance = walletService.getWalletBalance(walletId);
        return ResponseEntity.ok(
                ApiResponse.success("WALLET", balance, "Wallet balance retrieved successfully")
        );
    }

}

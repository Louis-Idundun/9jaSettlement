package ng.com.louis.app9jasettlement.test.controllers;

import ng.com.louis.app9jasettlement.test.payloads.TransactionRequest;
import ng.com.louis.app9jasettlement.test.payloads.TransferRequest;
import ng.com.louis.app9jasettlement.test.services.TransactionService;
import ng.com.louis.app9jasettlement.test.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/process/transaction")
    public ResponseEntity<ApiResponse<?>> processTransaction(@RequestBody TransactionRequest request) {
        return transactionService.processTransaction(request);
    }

    @PostMapping("/process/transfer")
    public ResponseEntity<ApiResponse<?>> processTransfer(@RequestBody TransferRequest request) {
        return transactionService.processTransfer(request);
    }
}

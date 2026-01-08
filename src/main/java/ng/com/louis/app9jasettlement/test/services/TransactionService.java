package ng.com.louis.app9jasettlement.test.services;

import ng.com.louis.app9jasettlement.test.payloads.TransactionRequest;
import ng.com.louis.app9jasettlement.test.payloads.TransferRequest;
import ng.com.louis.app9jasettlement.test.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ApiResponse<?>> processTransaction(TransactionRequest request);
    ResponseEntity<ApiResponse<?>> processTransfer(TransferRequest request);
}

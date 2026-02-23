package learning.demobank_2.controller;

import learning.demobank_2.constant.AppConstants;
import learning.demobank_2.dto.ApiResponse;
import learning.demobank_2.dto.PageResponse;
import learning.demobank_2.model.Transaction;
import learning.demobank_2.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("get-all")
    public ResponseEntity<ApiResponse<PageResponse<Transaction>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        log.info("Fetching paginated transactions| pageNumber: {}, pageSize: {}", pageNumber, pageSize);

        Page<Transaction> transactionPage = transactionService.getAllTransactions(pageNumber, pageSize);
        PageResponse<Transaction> pageResponse = PageResponse.of(transactionPage);

        String message = transactionPage.isEmpty()
                ? AppConstants.NO_TRANSACTIONS_FOUND
                : AppConstants.TRANSACTIONS_RETRIEVED_SUCCESS;
        return buildSuccessResponse(pageResponse, message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable int id){
        log.info("GET/api/transaction/{} - Fetching transaction by id", id);

        Transaction transaction = transactionService.getTransactionById(id);
        return buildSuccessResponse(transaction, AppConstants.TRANSACTIONS_RETRIEVED_SUCCESS);
    }

    private <T> ResponseEntity<ApiResponse<T>> buildSuccessResponse(T data, String message){
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }
}

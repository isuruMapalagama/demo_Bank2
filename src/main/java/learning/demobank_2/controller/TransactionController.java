package learning.demobank_2.controller;

import jakarta.validation.Valid;
import learning.demobank_2.constant.AppConstants;
import learning.demobank_2.model.entity.Transaction;
import learning.demobank_2.model.dto.ApiResponse;
import learning.demobank_2.model.dto.PageResponse;
import learning.demobank_2.model.request.AllTransaction;
import learning.demobank_2.model.request.ExportTransactionDto;
import learning.demobank_2.model.request.TransactionRequest;
import learning.demobank_2.model.request.TransactionSearchDto;
import learning.demobank_2.service.ExcelExportService;
import learning.demobank_2.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final ExcelExportService excelExportService;

// Get all transactions with pagination
    @GetMapping("get-all")
    public ResponseEntity<ApiResponse<PageResponse<AllTransaction>>> getAllTransactions(
            @RequestBody(required = false) AllTransaction allTransaction,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        PageResponse<AllTransaction> pageResponse = PageResponse.of(transactionService.getAllTransactions(allTransaction, pageNumber, pageSize));
        String message = pageResponse.getTransactions().isEmpty()
                ? AppConstants.NO_TRANSACTIONS_FOUND
                : AppConstants.TRANSACTIONS_RETRIEVED_SUCCESS;
        return buildSuccessResponse(pageResponse, message);

    }

// Get transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable int id){
        log.info("GET/api/transaction/{} - Fetching transaction by id", id);

        Transaction transaction = transactionService.getTransactionById(id);
        return buildSuccessResponse(transaction, AppConstants.TRANSACTION_RETRIEVED_SUCCESS);
    }

// Create a new transaction
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TransactionRequest>> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest){
        log.info("POST /api/transaction/create - Creating new transaction");

        TransactionRequest createdTransaction = transactionService.createTransaction(transactionRequest);
        return buildSuccessResponse(createdTransaction, AppConstants.TRANSACTION_CREATED_SUCCESS);
    }

// Search transactions with pagination
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<TransactionSearchDto>>> searchTransactions(
            @RequestBody(required = false) TransactionSearchDto searchDto,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "12") int pageSize) {

        log.info("POST /api/transaction/search - pageNumber: {}, pageSize:{} ", pageNumber, pageSize);

        if (searchDto == null){
            searchDto = new TransactionSearchDto();
        }
        PageResponse<TransactionSearchDto> pageResponse = PageResponse.of(transactionService.searchTransactions(searchDto,pageNumber, pageSize));

        String message = pageResponse.getTransactions().isEmpty()
                ? AppConstants.NO_TRANSACTIONS_FOUND
                : AppConstants.TRANSACTIONS_SEARCH_SUCCESS;

        return buildSuccessResponse(pageResponse, message);
    }

// Export transactions to Excel
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportTransactions(
            @RequestBody(required = false) ExportTransactionDto exportDto){
        log.info("POST /api/transactions/export - Exporting transactions with filters");

        try{
            if (exportDto == null){
                exportDto = new ExportTransactionDto();
            }
            List<ExportTransactionDto> reportTransactions = transactionService.exportTransactions(exportDto);
            ByteArrayInputStream byteArrayInputStream = excelExportService.exportTransactionsToExcelStream(reportTransactions);

            String filename = exportDto.hasFilters() ? "filtered_transactions" : "transactions";
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fullFilename = String.format("%s_%s.xlsx", filename, timestamp);

            InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fullFilename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            log.error("Error exporting transactions: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

//    Update Transactions
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TransactionRequest>> updateTransaction(@PathVariable int id, @Valid @RequestBody TransactionRequest transactionRequest){
        log.info("PUT /api/transaction/update/{} - Updating transaction with id", id);

        TransactionRequest updatedTransaction = transactionService.updateTransaction(id, transactionRequest);
        return buildSuccessResponse(updatedTransaction, AppConstants.TRANSACTION_UPDATED_SUCCESS);
    }

//    Delete Transaction
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable int id) {
        log.info("DELETE /api/transaction/delete/{} - Deleting transaction with id", id);

        transactionService.deleteTransaction(id);
        return buildSuccessResponse(null, AppConstants.TRANSACTION_DELETED_SUCCESS);
    }
    private <T> ResponseEntity<ApiResponse<T>> buildSuccessResponse(T data, String message){
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }
}

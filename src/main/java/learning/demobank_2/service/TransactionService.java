package learning.demobank_2.service;

import jakarta.validation.Valid;
import learning.demobank_2.constant.AppConstants;
import learning.demobank_2.exception.ResourceNotFoundException;
import learning.demobank_2.model.entity.Transaction;
import learning.demobank_2.model.request.ExportTransactionDto;
import learning.demobank_2.model.request.TransactionRequest;
import learning.demobank_2.model.request.TransactionSearchDto;
import learning.demobank_2.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepo;


    public Page<Transaction> getAllTransactions(int pageNumber, int pageSize) {
        log.debug("Fetching transactions - page: {}, size: {}", pageNumber, pageSize);

        validatePaginationParameters(pageNumber, pageSize);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Transaction> transactions = transactionRepo.findAllTransactions(null, null, null, null, null, pageable);
        log.info("Retrieved {} transactions from page {}", transactions.getNumberOfElements(), pageNumber);
        return transactions;
    }

    public Transaction getTransactionById(int id) {
        log.debug("Fetching transaction with id: {}", id);

        return transactionRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Transaction not found with id: {}", id);
                    return new ResourceNotFoundException("Transaction", "id", id);
                });
    }

    public TransactionRequest createTransaction(TransactionRequest transactionRequest) {
        try {
            log.debug("Creating new transaction");

            Transaction transaction = Transaction.builder()
                    .amount(transactionRequest.getAmount())
                    .bankName(transactionRequest.getBankName())
                    .branchName(transactionRequest.getBranchName())
                    .transactionType(transactionRequest.getTransactionType())
                    .debitAccountNumber(transactionRequest.getDebitAccountNumber())
                    .creditAccountNumber(transactionRequest.getCreditAccountNumber())
                    .senderPhoneNumber(transactionRequest.getSenderPhoneNumber())
                    .senderName(transactionRequest.getSenderName())
                    .beneficiaryName(transactionRequest.getBeneficiaryName())
                    .category(transactionRequest.getCategory())
                    .build();
            Transaction savedTransaction = transactionRepo.save(transaction);
            log.info("Transaction created with id: {}", savedTransaction.getId());

            return mapToTransactionRequest(savedTransaction);

        } catch (Exception e) {
            log.error("Error creating transaction: {}", e.getMessage(), e);
            throw e;
        }
    }

    private TransactionRequest mapToTransactionRequest(Transaction savedTransaction) {
        return TransactionRequest.builder()
                .amount(savedTransaction.getAmount())
                .bankName(savedTransaction.getBankName())
                .branchName(savedTransaction.getBranchName())
                .transactionType(savedTransaction.getTransactionType())
                .debitAccountNumber(savedTransaction.getDebitAccountNumber())
                .creditAccountNumber(savedTransaction.getCreditAccountNumber())
                .senderPhoneNumber(savedTransaction.getSenderPhoneNumber())
                .senderName(savedTransaction.getSenderName())
                .beneficiaryName(savedTransaction.getBeneficiaryName())
                .category(savedTransaction.getCategory())
                .build();
    }

    public Page<TransactionSearchDto> searchTransactions(TransactionSearchDto searchDto, int pageNumber, int pageSize) {
        log.debug("Searching transactions with filters - page: {}, size: {}", pageNumber, pageSize);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Transaction> transactionSearchDtos = transactionRepo.searchTransactions(
                searchDto.getBankName(),
                searchDto.getBranchName(),
                searchDto.getTransactionType(),
                searchDto.getSenderName(),
                searchDto.getBeneficiaryName(),
                searchDto.getCategory(),
                pageable
        );
        log.info("Found {} transactions matching search criteria on page {}", transactionSearchDtos.getNumberOfElements(), pageNumber);
        return transactionSearchDtos.map(transaction ->
                TransactionSearchDto.builder()
                        .bankName(transaction.getBankName())
                        .branchName(transaction.getBranchName())
                        .transactionType(transaction.getTransactionType())
                        .senderName(transaction.getSenderName())
                        .beneficiaryName(transaction.getBeneficiaryName())
                        .category(transaction.getCategory())
                        .build()
        );
    }

    private void validatePaginationParameters(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            log.error("Invalid page number: {}", pageNumber);
            throw new IllegalArgumentException(AppConstants.INVALID_PAGE_NUMBER);
        }
        if (pageSize < 1 || pageSize > AppConstants.MAX_PAGE_SIZE) {
            log.error("Invalid page size: {}", pageSize);
            throw new IllegalArgumentException(AppConstants.INVALID_PAGE_SIZE);
        }
    }

    public List<ExportTransactionDto> exportTransactions(ExportTransactionDto exportDto) {
        if (exportDto == null) {
            log.debug("Export DTO is null, fetching all transactions for export");
            exportDto = ExportTransactionDto.builder().build();
        }
        List<Transaction> transactions = transactionRepo.findAllTransactionsForExport(
                exportDto.getBankName(),
                exportDto.getBranchName(),
                exportDto.getTransactionType(),
                exportDto.getSenderName(),
                exportDto.getBeneficiaryName(),
                exportDto.getCategory()
        );
        return transactions.stream()
                .map(t -> ExportTransactionDto.builder()
                        .amount(t.getAmount())
                        .bankName(t.getBankName())
                        .branchName(t.getBranchName())
                        .transactionType(t.getTransactionType())
                        .senderName(t.getSenderName())
                        .beneficiaryName(t.getBeneficiaryName())
                        .category(t.getCategory())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }

    public TransactionRequest updateTransaction(int id, @Valid TransactionRequest request) {
        Transaction existing = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        Transaction updated = Transaction.builder()
                .id(existing.getId())
                .amount(request.getAmount())
                .bankName(request.getBankName())
                .branchName(request.getBranchName())
                .transactionType(request.getTransactionType())
                .debitAccountNumber(request.getDebitAccountNumber())
                .creditAccountNumber(request.getCreditAccountNumber())
                .senderPhoneNumber(request.getSenderPhoneNumber())
                .senderName(request.getSenderName())
                .beneficiaryName(request.getBeneficiaryName())
                .category(request.getCategory())
                .build();
        Transaction savedTransaction = transactionRepo.save(updated);
        log.info("Transaction updated with id: {}", savedTransaction.getId());
        return mapToTransactionRequest(savedTransaction);
    }

    public TransactionRequest deleteTransaction(int id) {
        Transaction existing = transactionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        transactionRepo.delete(existing);
        log.info("Transaction deleted with id: {}", id);
        return mapToTransactionRequest(existing);
    }
}

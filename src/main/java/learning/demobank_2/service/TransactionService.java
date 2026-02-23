package learning.demobank_2.service;

import learning.demobank_2.constant.AppConstants;
import learning.demobank_2.exception.ResourceNotFoundException;
import learning.demobank_2.model.Transaction;
import learning.demobank_2.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepo;

    public Page<Transaction> getAllTransactions(int pageNumber, int pageSize) {
        log.debug("Fetching transactions - page: {}, size: {}", pageNumber, pageSize);

        validatePagintionParameters(pageNumber, pageSize);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Transaction> transactions = transactionRepo.findAllTransactions(null, null,null,null,null,pageable);

//        Transaction.builder()
//                .transactionType("")
//                .branchName("")
//                .build();

        log.info("Retrieved {} transactions from page {}", transactions.getNumberOfElements(), pageNumber);
        return transactions;
    }

    public Transaction getTransactionById(int id){
        log.debug("Fetching transaction with id: {}", id);

        return transactionRepo.findById(id)
                .orElseThrow(()->{
                    log.error("Transaction not found with id: {}", id);
                    return new ResourceNotFoundException("Transaction","id", id);
                });
    }

    private void validatePagintionParameters(int pageNumber, int pageSize){
        if(pageNumber <0){
            log.error("Invalid page number: {}", pageNumber);
            throw new IllegalArgumentException(AppConstants.INVALID_PAGE_NUMBER);
        }
        if(pageSize<1 || pageSize > AppConstants.MAX_PAGE_SIZE){
            log.error("Invalid page size: {}", pageSize);
            throw new IllegalArgumentException(AppConstants.INVALID_PAGE_SIZE);
        }
    }
}




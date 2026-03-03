package learning.demobank_2.repository;

import learning.demobank_2.model.entity.Transaction;
import learning.demobank_2.model.request.TransactionSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository <Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:bankName IS NULL OR t.bankName LIKE %:bankName%) AND " +
            "(:branchName IS NULL OR t.branchName LIKE %:branchName%) AND " +
            "(:transactionType IS NULL OR t.transactionType LIKE %:transactionType%) AND " +
            "(:senderName IS NULL OR t.senderName LIKE %:senderName%) AND " +
            "(:beneficiaryName IS NULL OR t.beneficiaryName LIKE %:beneficiaryName%) " +
            "ORDER BY t.id DESC")
    Page<Transaction> findAllTransactions(
            @Param("bankName") String bankName,
            @Param("branchName") String branchName,
            @Param("transactionType") String transactionType,
            @Param("senderName") String senderName,
            @Param("beneficiaryName") String beneficiaryName,
            Pageable pageable
    );

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:bankName IS NULL OR t.bankName LIKE %:bankName%) AND " +
            "(:branchName IS NULL OR t.branchName LIKE %:branchName%) AND " +
            "(:transactionType IS NULL OR t.transactionType LIKE %:transactionType%) AND " +
            "(:senderName IS NULL OR t.senderName LIKE %:senderName%) AND " +
            "(:beneficiaryName IS NULL OR t.beneficiaryName LIKE %:beneficiaryName%) AND " +
            "(:category IS NULL OR t.category LIKE %:category%) " +
            "ORDER BY t.id DESC")
    Page<Transaction> searchTransactions(
            @Param("bankName") String bankName,
            @Param("branchName") String branchName,
            @Param("transactionType") String transactionType,
            @Param("senderName") String senderName,
            @Param("beneficiaryName") String beneficiaryName,
            @Param("category") String category,
            Pageable pageable
    );
    @Query("SELECT t FROM Transaction t WHERE " +
            "(:bankName IS NULL OR t.bankName LIKE %:bankName%) AND " +
            "(:branchName IS NULL OR t.branchName LIKE %:branchName%) AND " +
            "(:transactionType IS NULL OR t.transactionType LIKE %:transactionType%) AND " +
            "(:senderName IS NULL OR t.senderName LIKE %:senderName%) AND " +
            "(:beneficiaryName IS NULL OR t.beneficiaryName LIKE %:beneficiaryName%) AND " +
            "(:category IS NULL OR t.category LIKE %:category%) " +
            "ORDER BY t.id DESC")
    List<Transaction> findAllTransactionsForExport(
                    @Param("bankName") String bankName,
                    @Param("branchName") String branchName,
                    @Param("transactionType") String transactionType,
                    @Param("senderName") String senderName,
                    @Param("beneficiaryName") String beneficiaryName,
                    @Param("category") String category
            );
}

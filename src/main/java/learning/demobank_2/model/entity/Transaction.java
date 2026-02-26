package learning.demobank_2.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "amount")
    private Double amount;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Column(name = "transaction_type", length = 50)
    private String transactionType;

    @Column(name = "debit_account_number", length = 50)
    private String debitAccountNumber;

    @Column(name = "credit_account_number", length = 50)
    private String creditAccountNumber;

    @Column(name = "sender_phone_number", length = 20)
    private String senderPhoneNumber;

    @Column(name = "sender_name", length = 100)
    private String senderName;

    @Column(name = "beneficiary_name", length = 100)
    private String beneficiaryName;

    @Column(name = "category", length = 50)
    private String category;
}


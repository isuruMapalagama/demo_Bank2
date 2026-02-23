package learning.demobank_2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private Integer id;
    private String branchName;
    private String transactionType;
    private String senderName;
    private String beneficiaryName;
}


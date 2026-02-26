package learning.demobank_2.model.dto;

import lombok.*;

@Getter
@Setter
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


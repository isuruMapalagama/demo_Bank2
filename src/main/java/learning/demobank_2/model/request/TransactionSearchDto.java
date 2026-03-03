package learning.demobank_2.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSearchDto {
    private String bankName;
    private String branchName;
    private String transactionType;
    private String senderName;
    private String beneficiaryName;
    private String category;

}



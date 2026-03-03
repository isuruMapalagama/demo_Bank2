package learning.demobank_2.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExportTransactionDto {

    private Double amount;
    private String bankName;
    private String branchName;
    private String transactionType;
    private String senderName;
    private String beneficiaryName;
    private String category;

    public boolean hasFilters(){
        return (amount != null)  ||
                (bankName != null && !bankName.isEmpty()) ||
                (branchName != null && !branchName.isEmpty()) ||
                (transactionType != null && !transactionType.isEmpty()) ||
                (senderName != null && !senderName.isEmpty()) ||
                (beneficiaryName != null && !beneficiaryName.isEmpty()) ||
                (category != null && !category.isEmpty());
  }
}

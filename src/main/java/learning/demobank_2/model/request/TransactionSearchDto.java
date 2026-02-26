package learning.demobank_2.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

//    public boolean hasFilters(){
//        return (bankName != null && !bankName.isEmpty()) ||
//                (branchName != null && !branchName.isEmpty()) ||
//                (transactionType != null && !transactionType.isEmpty()) ||
//                (senderName != null && !senderName.isEmpty()) ||
//                (beneficiaryName != null && !beneficiaryName.isEmpty()) ||
//                (category != null && !category.isEmpty());
//    }
}



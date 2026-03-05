package learning.demobank_2.model.request;

import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllTransaction {

    private Double amount;
    private String bankName;
    private String branchName;
    private String transactionType;
    private String senderPhoneNumber;
    private String senderName;
    private String beneficiaryName;
    private String category;

}

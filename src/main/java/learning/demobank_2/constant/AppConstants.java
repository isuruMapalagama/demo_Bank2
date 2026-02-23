package learning.demobank_2.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppConstants {

    // Pagination defaults
    public static final int MAX_PAGE_SIZE = 100;

    // Response  messages
    public static final String NO_TRANSACTIONS_FOUND = "No transactions found";


    // Validation messages
    public static final String INVALID_PAGE_NUMBER = "Page number must be 0 or grater than 0";
    public static final String INVALID_PAGE_SIZE = "Page size must be between 1 and " + MAX_PAGE_SIZE;
    public static final String TRANSACTIONS_RETRIEVED_SUCCESS = "Transactions retrieved successfully";


}

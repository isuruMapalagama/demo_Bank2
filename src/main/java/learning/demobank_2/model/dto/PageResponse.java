package learning.demobank_2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> transactions;

//    @JsonProperty("page_number")
//    private int pageNumber;

//    @JsonProperty("page_size")
//    private int pageSize;

//   @JsonProperty("total_elements")
//    private long totalElements;
//
//    @JsonProperty("total_pages")
//    private int totalPages;

//    @JsonProperty("is_last")
//    private boolean isLast;
//
//    @JsonProperty("is_first")
//    private boolean isFirst;

    @JsonProperty("has_next")
    private boolean hasNext;

    @JsonProperty("has_previous")
    private boolean hasPrevious;

// -------------------------------

    public  static <T> PageResponse<T> of(Slice<T> slice){
        return PageResponse.<T>builder()
                .transactions(slice.getContent())
                .hasNext(slice.hasNext())
                .hasPrevious(slice.hasPrevious())
                .build();
    }
}

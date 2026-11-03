package fr.asr.calculator.data.dto.response;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {

    private List<T> content;

    private Integer totalPages;

    private Long totalElements;

    private Integer numberOfElements;

    private Integer size;

    private Integer number;

    private boolean empty;

    private boolean first;

    private boolean last;
}

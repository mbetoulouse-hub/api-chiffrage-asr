package fr.asr.calculator.data.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagedEstimateDTO {

    private Long id;

    private String companyName;

    private String businessSector;

    private Integer buildingCount;

    private Integer roomCount;

    private Status status;

    private String createdBy;

    private Instant createdAt;

    public enum Status {
        DRAFT,
        COMPLETE,
        ;
    }
}

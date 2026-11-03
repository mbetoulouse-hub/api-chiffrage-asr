package fr.asr.calculator.data.dto.error;

import fr.asr.calculator.exception.ExceptionType;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDTO {

    private int status;

    @NotNull
    private ExceptionType error;

    @NotNull
    private Map<String, Object> details;

    private long timestamp;
}

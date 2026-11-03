package fr.asr.calculator.controller;

import fr.asr.calculator.data.dto.EstimateDTO;
import fr.asr.calculator.data.dto.request.CreateEstimateDTO;
import fr.asr.calculator.data.dto.request.SaveEstimateDTO;
import fr.asr.calculator.data.dto.response.FindEstimateDTO;
import fr.asr.calculator.data.dto.response.PageDTO;
import fr.asr.calculator.data.dto.response.PagedEstimateDTO;
import fr.asr.calculator.data.dto.response.SavedEstimateDTO;
import fr.asr.calculator.service.EstimateService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/estimate")
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;

    @PostMapping("/create")
    public EstimateDTO create(
            Authentication authentication, @NotNull @Valid @RequestBody CreateEstimateDTO createProfileDTO) {
        return estimateService.createNewEstimate(authentication.getName(), createProfileDTO);
    }

    @PutMapping("/update")
    public SavedEstimateDTO update(
            Authentication authentication, @NotNull @Valid @RequestBody SaveEstimateDTO saveEstimateDTO) {
        return estimateService.updateEstimate(authentication.getName(), saveEstimateDTO);
    }

    @GetMapping("/get")
    public FindEstimateDTO find(Authentication authentication, @Min(0) @RequestParam Long id) {
        return estimateService.getEstimate(authentication.getName(), id);
    }

    @DeleteMapping("/delete")
    public void delete(Authentication authentication, @Min(1) @RequestParam Long id) {
        estimateService.deleteEstimate(authentication.getName(), id);
    }

    @GetMapping("/all")
    public PageDTO<PagedEstimateDTO> all(
            Authentication authentication,
            @Min(0) @RequestParam Integer page,
            @RequestParam(required = false) String input,
            @RequestParam(required = false) String status) {
        Page<PagedEstimateDTO> profiles =
                estimateService.getAllEstimates(page, authentication.getName(), input, status);

        PageDTO<PagedEstimateDTO> pageDTO = new PageDTO<>();
        pageDTO.setContent(profiles.getContent());
        pageDTO.setTotalPages(profiles.getTotalPages());
        pageDTO.setTotalElements(profiles.getTotalElements());
        pageDTO.setNumberOfElements(profiles.getNumberOfElements());
        pageDTO.setSize(profiles.getSize());
        pageDTO.setNumber(profiles.getNumber());
        pageDTO.setEmpty(profiles.isEmpty());
        pageDTO.setFirst(profiles.isFirst());
        pageDTO.setLast(profiles.isLast());
        return pageDTO;
    }
}

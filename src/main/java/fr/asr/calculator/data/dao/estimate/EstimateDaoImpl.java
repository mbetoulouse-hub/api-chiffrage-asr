package fr.asr.calculator.data.dao.estimate;

import static fr.asr.calculator.exception.ExceptionType.ENTITY_NOT_FOUND;
import static fr.asr.calculator.util.ListUtil.toList;

import fr.asr.calculator.data.entity.Estimate;
import fr.asr.calculator.data.repository.EstimateRepository;
import fr.asr.calculator.data.repository.specification.EstimateSpecifications;
import fr.asr.calculator.exception.functionnal.NotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstimateDaoImpl implements EstimateDao {

    private final EstimateRepository repository;

    @Override
    public List<Estimate> findAll() {
        return toList(repository.findAll());
    }

    @Override
    public Optional<Estimate> findByIdOptional(Long id) {
        return repository.findById(id);
    }

    @Override
    public Estimate findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Estimate", "id", id));
    }

    @Override
    public Estimate save(Estimate entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(Estimate entity) {
        repository.delete(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Page<Estimate> findAll(String input, String status, long profileId, Pageable pageable) {
        Sort sort = Sort.by("createdAt").descending().and(Sort.by("status").ascending());
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Specification<Estimate> spec = Specification.where(EstimateSpecifications.belongsToProfileOrManager(profileId))
                .and(EstimateSpecifications.hasStatusOrAll(status))
                .and(EstimateSpecifications.companyNameContains(input));

        return repository.findAll(spec, sortedPageable);
    }

    @Override
    public Page<Estimate> findAll(String input, String status, Pageable pageable) {
        Sort sort = Sort.by("createdAt").descending().and(Sort.by("status").ascending());
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Specification<Estimate> spec = Specification.where(EstimateSpecifications.hasStatusOrAll(status))
                .and(EstimateSpecifications.companyNameContains(input));

        return repository.findAll(spec, sortedPageable);
    }
}

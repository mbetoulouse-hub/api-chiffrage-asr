package fr.asr.calculator.data.dao.profile;

import static fr.asr.calculator.data.repository.specification.ProfileSpecifications.emailOrCompanyContains;
import static fr.asr.calculator.data.repository.specification.ProfileSpecifications.hasRoleOrAll;
import static fr.asr.calculator.exception.ExceptionType.ENTITY_NOT_FOUND;
import static fr.asr.calculator.util.ListUtil.toList;

import fr.asr.calculator.data.entity.Profile;
import fr.asr.calculator.data.repository.ProfileRepository;
import fr.asr.calculator.exception.functionnal.NotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileDaoImpl implements ProfileDao {

    private final ProfileRepository repository;

    @Override
    public List<Profile> findAll() {
        return toList(repository.findAll());
    }

    @Override
    public Optional<Profile> findByIdOptional(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Profile findById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Profile", "id", id));
    }

    @Override
    public Profile save(Profile entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(Profile entity) {
        repository.delete(entity);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public List<Profile> finAllManager() {
        return repository.findByRole(Profile.Role.MANAGER);
    }

    @Override
    public Page<Profile> findAll(String input, String role, Pageable pageable) {
        Specification<Profile> spec =
                Specification.where(emailOrCompanyContains(input)).and(hasRoleOrAll(role));

        return repository.findAll(spec, pageable);
    }

    @Override
    public Optional<Profile> findByEmailOptional(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Profile findByEmail(String email) {
        return repository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "Profile", "email", email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}

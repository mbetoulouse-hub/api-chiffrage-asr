package fr.asr.calculator.data.dao.refreshtoken;

import static fr.asr.calculator.exception.ExceptionType.ENTITY_NOT_FOUND;
import static fr.asr.calculator.util.ListUtil.toList;

import fr.asr.calculator.data.entity.RefreshToken;
import fr.asr.calculator.data.repository.RefreshTokenRepository;
import fr.asr.calculator.exception.functionnal.NotFoundException;
import fr.asr.calculator.util.DeterministicHashUtil;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenDaoImpl implements RefreshTokenDao {

    private final RefreshTokenRepository repository;

    @Override
    public List<RefreshToken> findAll() {
        return toList(repository.findAll());
    }

    @Override
    public Optional<RefreshToken> findByIdOptional(Integer id) {
        return repository.findById(id);
    }

    @Override
    public RefreshToken findById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "RefreshToken", "id", id));
    }

    @Override
    public RefreshToken save(RefreshToken entity) {
        entity.setToken(DeterministicHashUtil.hash(entity.getToken()));
        return repository.save(entity);
    }

    @Override
    public void delete(RefreshToken entity) {
        repository.delete(entity);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public List<RefreshToken> findExpiredRefreshToken(Instant now) {
        return repository.findByExpiryDateBeforeOrRevokedTrue(now);
    }

    @Override
    public Optional<RefreshToken> findByTokenOptional(String token) {
        return repository.findByToken(DeterministicHashUtil.hash(token));
    }

    @Override
    public RefreshToken findByToken(String token) {
        return repository
                .findByToken(DeterministicHashUtil.hash(token))
                .orElseThrow(() -> new NotFoundException(ENTITY_NOT_FOUND, "type", "RefreshToken"));
    }

    @Override
    public void deleteAll(List<RefreshToken> entity) {
        repository.deleteAll(entity);
    }
}

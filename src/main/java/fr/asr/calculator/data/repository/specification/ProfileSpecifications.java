package fr.asr.calculator.data.repository.specification;

import fr.asr.calculator.data.entity.Profile;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ProfileSpecifications {

    public static Specification<Profile> hasRoleOrAll(String role) {
        return (root, query, cb) -> {
            if (role == null || "ALL".equalsIgnoreCase(role)) {
                return cb.conjunction();
            }
            return cb.equal(root.get("role"), Profile.Role.valueOf(role));
        };
    }

    public static Specification<Profile> emailOrCompanyContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            return cb.or(
                    cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("company")), "%" + keyword.toLowerCase() + "%"));
        };
    }
}

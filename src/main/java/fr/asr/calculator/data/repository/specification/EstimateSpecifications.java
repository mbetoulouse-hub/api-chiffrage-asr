package fr.asr.calculator.data.repository.specification;

import fr.asr.calculator.data.entity.Estimate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class EstimateSpecifications {

    public static Specification<Estimate> belongsToProfileOrManager(long profileId) {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("profile").get("id"), profileId),
                cb.equal(root.get("profile").get("manager").get("id"), profileId));
    }

    public static Specification<Estimate> hasStatusOrAll(String status) {
        return (root, query, cb) -> {
            if (status == null || "ALL".equalsIgnoreCase(status)) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), Estimate.Status.valueOf(status));
        };
    }

    public static Specification<Estimate> companyNameContains(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("companyName")), "%" + keyword.toLowerCase() + "%");
        };
    }
}

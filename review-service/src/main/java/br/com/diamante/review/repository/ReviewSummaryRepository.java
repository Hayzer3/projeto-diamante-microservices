package br.com.diamante.review.repository;

import br.com.diamante.review.model.ReviewSummary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {
    Optional<ReviewSummary> findByDishId(Long dishId);
}

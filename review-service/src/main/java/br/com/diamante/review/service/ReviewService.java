package br.com.diamante.review.service;

import br.com.diamante.review.config.ReviewMessagingConfiguration;
import br.com.diamante.review.dto.RankingEntry;
import br.com.diamante.review.dto.ReviewMessage;
import br.com.diamante.review.model.ReviewSummary;
import br.com.diamante.review.repository.ReviewSummaryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
    private final ConcurrentHashMap<Long, Aggregate> buffer = new ConcurrentHashMap<>();
    private final ReviewSummaryRepository repository;

    public ReviewService(ReviewSummaryRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = ReviewMessagingConfiguration.QUEUE)
    public void receive(ReviewMessage message) {
        Aggregate incoming = new Aggregate(message.dishName(), message.rating(), 1);
        buffer.merge(message.dishId(), incoming, Aggregate::combine);
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void flush() {
        buffer.forEach((dishId, aggregate) -> {
            if (!buffer.remove(dishId, aggregate)) {
                return;
            }

            ReviewSummary summary = repository.findByDishId(dishId)
                    .orElseGet(() -> new ReviewSummary(dishId, aggregate.dishName(), 0, 0));
            summary.add(aggregate.totalRating(), aggregate.count(), aggregate.dishName());
            repository.save(summary);
        });
    }

    public List<RankingEntry> ranking() {
        return repository.findAll().stream()
                .map(summary -> new RankingEntry(
                        summary.getDishId(),
                        summary.getDishName(),
                        summary.getAverage(),
                        summary.getCount()))
                .sorted(Comparator.comparingDouble(RankingEntry::average).reversed())
                .toList();
    }

    private record Aggregate(String dishName, long totalRating, long count) {
        Aggregate combine(Aggregate other) {
            return new Aggregate(
                    other.dishName,
                    totalRating + other.totalRating,
                    count + other.count);
        }
    }
}

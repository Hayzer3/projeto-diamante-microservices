package br.com.diamante.review.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_summaries")
public class ReviewSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long dishId;
    private String dishName;
    private long totalRating;
    private long count;
    protected ReviewSummary() {
    }

    public ReviewSummary(Long dishId, String dishName, long totalRating, long count) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.totalRating = totalRating;
        this.count = count;
    }

    public void add(long ratingTotal, long reviewCount, String currentDishName) {
        totalRating += ratingTotal;
        count += reviewCount;
        dishName = currentDishName;
    }

    public Long getDishId() {
        return dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public double getAverage() {
        if (count == 0) {
            return 0;
        }
        return Math.round((double) totalRating / count * 100.0) / 100.0;
    }

    public long getCount() {
        return count;
    }
}

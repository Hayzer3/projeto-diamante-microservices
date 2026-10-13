package br.com.diamante.review.controller;

import br.com.diamante.review.dto.RankingEntry;
import br.com.diamante.review.service.ReviewService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews/ranking")
    public List<RankingEntry> ranking() {
        return reviewService.ranking();
    }
}

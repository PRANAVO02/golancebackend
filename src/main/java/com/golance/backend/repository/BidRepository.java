package com.golance.backend.repository;

import com.golance.backend.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByBidderId(Long userId);
}

package org.tsumiyoku.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tsumiyoku.domain.Announcement;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByTypeAndStatusOrderByPublishedAtDesc(String type, String status);
}
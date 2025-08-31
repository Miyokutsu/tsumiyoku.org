package org.tsumiyoku.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tsumiyoku.domain.WikiPage;

import java.util.List;
import java.util.Optional;

public interface WikiPageRepository extends JpaRepository<WikiPage, Long> {
    Optional<WikiPage> findBySlug(String slug);

    List<WikiPage> findAllByPublishedTrueOrderByUpdatedAtDesc();
}
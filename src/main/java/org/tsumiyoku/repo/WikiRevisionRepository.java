package org.tsumiyoku.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tsumiyoku.domain.WikiRevision;

public interface WikiRevisionRepository extends JpaRepository<WikiRevision, Long> {
}
package org.tsumiyoku.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tsumiyoku.domain.RoleMapping;

import java.util.Optional;

public interface RoleMappingRepository extends JpaRepository<RoleMapping, Long> {
    Optional<RoleMapping> findByDiscordRoleId(String discordRoleId);
}
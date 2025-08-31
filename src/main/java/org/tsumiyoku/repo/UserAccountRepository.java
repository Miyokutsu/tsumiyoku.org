package org.tsumiyoku.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tsumiyoku.domain.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByDiscordId(String discordId);
}
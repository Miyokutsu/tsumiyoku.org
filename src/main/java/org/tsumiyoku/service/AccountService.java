package org.tsumiyoku.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tsumiyoku.domain.UserAccount;
import org.tsumiyoku.repo.UserAccountRepository;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserAccountRepository users;

    public UserAccount upsertLogin(String discordId, String username, Set<String> roles) {
        UserAccount ua = users.findByDiscordId(discordId).orElseGet(UserAccount::new);
        ua.setDiscordId(discordId);
        ua.setUsername(username);
        ua.setLastLoginAt(Instant.now());
        if (ua.getCreatedAt() == null) ua.setCreatedAt(Instant.now());
        ua.setRoles(roles);
        return users.save(ua);
    }
}
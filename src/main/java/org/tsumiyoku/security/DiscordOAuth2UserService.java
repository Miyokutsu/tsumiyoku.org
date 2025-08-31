package org.tsumiyoku.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tsumiyoku.service.AccountService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DiscordOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountService accounts;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        if (oauth2User == null || oauth2User.getAttributes() == null) {
            throw new IllegalStateException("Discord OAuth2 user info is empty");
        }

        Map<String, Object> attrs = oauth2User.getAttributes();
        String discordId = String.valueOf(attrs.get("id"));
        String username = String.valueOf(attrs.getOrDefault("username", "unknown"));

        // NOTE: To actually fetch guild roles, you'd call Discord API with the token and check guild membership.
        // For now, we accept a custom claim "roles" if present, or default to ROLE_VIEWER.
        Set<String> roles = new HashSet<>();
        Object rolesAttr = attrs.get("roles");
        if (rolesAttr instanceof Collection<?> coll) {
            for (Object r : coll) roles.add("ROLE_" + r.toString().toUpperCase(Locale.ROOT));
        } else {
            roles.add("ROLE_VIEWER");
        }

        // Persist/Update local account
        accounts.upsertLogin(discordId, username, roles);

        // Convert roles to authorities
        List<SimpleGrantedAuthority> auths = roles.stream().map(SimpleGrantedAuthority::new).toList();

        return new DefaultOAuth2User(auths, attrs, "username");
    }
}
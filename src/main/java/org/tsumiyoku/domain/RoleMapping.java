package org.tsumiyoku.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String discordRoleId; // map a Discord role to an app role

    @Column(nullable = false)
    private String appRole; // e.g., ROLE_ADMIN, ROLE_GOV, ROLE_SCI, ROLE_EDITOR
}
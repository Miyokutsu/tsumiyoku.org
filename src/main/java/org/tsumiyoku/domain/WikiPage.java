package org.tsumiyoku.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "ux_slug", columnList = "slug", unique = true),
        @Index(name = "ix_updated", columnList = "updatedAt")
})
public class WikiPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 160)
    private String slug;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String contentMarkdown;

    @Lob
    private String contentHtml;

    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private String lastEditorDiscordId;
}
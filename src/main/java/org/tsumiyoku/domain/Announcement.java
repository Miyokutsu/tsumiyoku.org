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
@Table(indexes = @Index(name = "ix_type_published", columnList = "type,publishedAt"))
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // GOV or SCI (you can extend with an enum + converter if preferred)
    @Column(nullable = false, length = 16)
    private String type;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String contentMarkdown;

    @Lob
    private String contentHtml;

    private String authorDiscordId;
    private Instant createdAt;
    private Instant publishedAt;

    // DRAFT or PUBLISHED
    @Column(nullable = false, length = 16)
    private String status;
}
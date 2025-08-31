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
public class WikiRevision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private WikiPage page;

    @Lob
    private String contentMarkdown;

    private String editorDiscordId;
    private Instant createdAt;
}
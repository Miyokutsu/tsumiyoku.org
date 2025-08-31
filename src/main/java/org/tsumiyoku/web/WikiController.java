package org.tsumiyoku.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tsumiyoku.domain.WikiPage;
import org.tsumiyoku.domain.WikiRevision;
import org.tsumiyoku.repo.WikiPageRepository;
import org.tsumiyoku.repo.WikiRevisionRepository;
import org.tsumiyoku.service.MarkdownService;

import java.time.Instant;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Validated
public class WikiController {

    private final WikiPageRepository pages;
    private final WikiRevisionRepository revs;
    private final MarkdownService md;

    @GetMapping("/wiki/{slug}")
    public String view(@PathVariable String slug, Model model) {
        Optional<WikiPage> opt = pages.findBySlug(slug);
        if (opt.isEmpty()) {
            model.addAttribute("missingSlug", slug);
            return "wiki_missing";
        }
        model.addAttribute("page", opt.get());
        return "wiki_view";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EDITOR','ROLE_ADMIN','ROLE_GOV','ROLE_SCI')")
    @GetMapping("/editor/wiki/{slug}")
    public String edit(@PathVariable String slug, Model model) {
        WikiPage page = pages.findBySlug(slug).orElseGet(() -> {
            WikiPage p = new WikiPage();
            p.setSlug(slug);
            p.setTitle(slug.replace('-', ' '));
            p.setContentMarkdown("");
            p.setContentHtml("");
            p.setCreatedAt(Instant.now());
            p.setUpdatedAt(Instant.now());
            return p;
        });
        model.addAttribute("page", page);
        return "wiki_edit";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EDITOR','ROLE_ADMIN','ROLE_GOV','ROLE_SCI')")
    @PostMapping("/editor/wiki/save")
    public String save(@AuthenticationPrincipal OAuth2User user,
                       @RequestParam @NotBlank String slug,
                       @RequestParam @NotBlank String title,
                       @RequestParam String markdown,
                       @RequestParam(defaultValue = "false") boolean publish) {

        WikiPage page = pages.findBySlug(slug).orElseGet(WikiPage::new);
        boolean isNew = page.getId() == null;

        page.setSlug(slug);
        page.setTitle(title);
        page.setContentMarkdown(markdown);
        page.setContentHtml(md.toHtml(markdown));
        page.setPublished(publish);
        page.setUpdatedAt(Instant.now());
        if (isNew) page.setCreatedAt(Instant.now());
        if (user != null) page.setLastEditorDiscordId(String.valueOf(user.getAttributes().get("id")));
        pages.save(page);

        WikiRevision rev = WikiRevision.builder()
                .page(page)
                .contentMarkdown(markdown)
                .editorDiscordId(user != null ? String.valueOf(user.getAttributes().get("id")) : "unknown")
                .createdAt(Instant.now())
                .build();
        revs.save(rev);

        return "redirect:/wiki/%s".formatted(slug);
    }
}
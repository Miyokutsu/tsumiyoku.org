package org.tsumiyoku.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tsumiyoku.domain.Announcement;
import org.tsumiyoku.repo.AnnouncementRepository;
import org.tsumiyoku.service.MarkdownService;

import java.time.Instant;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementRepository annRepo;
    private final MarkdownService md;

    @GetMapping("/announcements/{type}")
    public String list(@PathVariable String type, Model model) {
        List<Announcement> items = annRepo.findByTypeAndStatusOrderByPublishedAtDesc(type.toUpperCase(), "PUBLISHED");
        model.addAttribute("type", type.toUpperCase());
        model.addAttribute("items", items);
        return "announcements";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GOV','ROLE_SCI')")
    @GetMapping("/editor/announcements/new/{type}")
    public String createForm(@PathVariable String type, Model model) {
        model.addAttribute("type", type.toUpperCase());
        return "ann_edit";
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_GOV','ROLE_SCI')")
    @PostMapping("/editor/announcements/save")
    public String save(@AuthenticationPrincipal OAuth2User user,
                       @RequestParam String type,
                       @RequestParam String title,
                       @RequestParam String markdown,
                       @RequestParam(defaultValue = "DRAFT") String status) {

        Announcement a = Announcement.builder()
                .type(type.toUpperCase())
                .title(title)
                .contentMarkdown(markdown)
                .contentHtml(md.toHtml(markdown))
                .authorDiscordId(user != null ? String.valueOf(user.getAttributes().get("id")) : "unknown")
                .createdAt(Instant.now())
                .status(status.toUpperCase())
                .publishedAt("PUBLISHED".equalsIgnoreCase(status) ? Instant.now() : null)
                .build();
        annRepo.save(a);
        return "redirect:/announcements/%s".formatted(type.toLowerCase());
    }
}
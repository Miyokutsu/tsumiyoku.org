package org.tsumiyoku.service;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).build();

    public String toHtml(String markdown) {
        if (markdown == null) return "";
        Node doc = parser.parse(markdown);
        return renderer.render(doc);
    }
}
package com.application.hub.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @GetMapping(Urls.ORG_URL_TEMPLATE + "/members")
    public List<MemberJson> getMembers(@PathVariable final String organization, final UriComponentsBuilder uriBuilder) {
        final URI reposUrl = uriBuilder
                .path(Urls.USER_URL_TEMPLATE)
                .path("/repos")
                .build(Map.of("username", "myuser"));
        return List.of(new MemberJson(1, reposUrl));
    }

    @GetMapping(Urls.USER_URL_TEMPLATE + "/repos")
    public List<RepositoryJson> getRepositoriesByUser(@PathVariable final String username, final UriComponentsBuilder uriBuilder) {
        final String repoName = "Hello&World";
        final URI languagesUrl = uriBuilder
                .path(Urls.REPO_URL_TEMPLATE)
                .path("/languages")
                .build(Map.of("username", username, "reponame", repoName));
        return List.of(new RepositoryJson(1, repoName, languagesUrl));
    }

    @GetMapping(Urls.REPO_URL_TEMPLATE + "/languages")
    public LanguageJson getLanguagesByUserAndRepository(@PathVariable final String username, @PathVariable final String reponame) {
        return new LanguageJson(Map.of("C", 1234L, "Python", 56789L));
    }
}

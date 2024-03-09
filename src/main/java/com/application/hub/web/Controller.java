package com.application.hub.web;

import com.application.hub.domain.LanguageModel;
import com.application.hub.domain.MemberModel;
import com.application.hub.domain.RepositoryModel;
import com.application.hub.domain.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Controller {
    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping(Urls.ORG_URL_TEMPLATE + "/members")
    public List<MemberJson> getMembers(@PathVariable final String organization, final UriComponentsBuilder uriBuilder) {
        final List<MemberModel> members = service.getMembersByOrganization(organization);

        return members.stream()
                .map(member -> mapToJson(uriBuilder, member))
                .toList();
    }

    @GetMapping(Urls.USER_URL_TEMPLATE + "/repos")
    public List<RepositoryJson> getRepositoriesByUser(@PathVariable final String username, final UriComponentsBuilder uriBuilder) {
        final List<RepositoryModel> repositories = service.getRepositoriesByUser(username);

        return repositories.stream()
                .map(repository -> mapToJson(uriBuilder, username, repository))
                .toList();
    }

    @GetMapping(Urls.REPO_URL_TEMPLATE + "/languages")
    public LanguageJson getLanguagesByUserAndRepository(@PathVariable final String username, @PathVariable final String reponame) {
        final List<LanguageModel> languageModels = service.getLanguagesByRepository(username, reponame);

        return mapToJson(languageModels);
    }

    private static MemberJson mapToJson(UriComponentsBuilder uriBuilder, MemberModel member) {
        final URI reposUrl = uriBuilder
                .path(Urls.USER_URL_TEMPLATE)
                .path("/repos")
                .build(Map.of("username", member.name()));
        return new MemberJson(member.id(), reposUrl);
    }

    private static RepositoryJson mapToJson(UriComponentsBuilder uriBuilder, String username, RepositoryModel repository) {
        final URI languagesUrl = uriBuilder
                .path(Urls.REPO_URL_TEMPLATE)
                .path("/languages")
                .build(Map.of("username", username, "reponame", repository.name()));
        return new RepositoryJson(1, repository.name(), languagesUrl);
    }

    private static LanguageJson mapToJson(List<LanguageModel> languageModels) {
        Map<String, Long> languages = languageModels.stream().collect(Collectors.toMap(LanguageModel::language, LanguageModel::bytes));
        return new LanguageJson(languages);
    }
}

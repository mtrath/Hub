package com.application.hub.web;

import com.application.hub.domain.*;
import org.springframework.http.MediaType;
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

    @GetMapping(value = Urls.ORG_URL_TEMPLATE + "/language-report", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getLanguagesReport(@PathVariable final String organization) {
        final LanguageReport languagesReport = service.getLanguagesReport(organization);

        final StringBuilder builder = new StringBuilder();
        for (LanguageReport.UserReport userReport : languagesReport.getUserReports()) {
            builder.append("- ").append(userReport.username()).append('\n');

            for (LanguageReport.CountedRepositories countedRepositories : userReport.repositoriesPerLanguage()) {
                builder.append("    ").append(countedRepositories.language()).append(' ').append(countedRepositories.repositoriesCount()).append('\n');
            }
        }

        return builder.toString();
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

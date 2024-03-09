package com.application.hub.domain;

import com.application.hub.persistence.Repository;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {
    private final Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
    }

    public List<MemberModel> getMembersByOrganization(final String organization) {
        return repository.findMembersByOrganization(organization);
    }

    public List<RepositoryModel> getRepositoriesByUser(final String username) {
        return repository.findRepositoriesByUser(username);
    }

    public List<LanguageModel> getLanguagesByRepository(final String username, final String repository) {
        return this.repository.findLanguagesByRepository(username, repository);
    }

    public LanguageReport getLanguagesReport(final String organization) {
        final List<MemberModel> members = repository.findMembersByOrganization(organization);

        final List<Pair<String, Map<String, Integer>>> list = members.stream()
                .map(member -> {
                    final List<RepositoryModel> repositories = repository.findRepositoriesByUser(member.name());
                    final Map<String, Integer> countedByLanguage = new HashMap<>();

                    for (RepositoryModel repositoryModel : repositories) {
                        final List<LanguageModel> languages = repository.findLanguagesByRepository(member.name(), repositoryModel.name());
                        for (LanguageModel language : languages) {
                            final Integer currentValue = countedByLanguage.putIfAbsent(language.language(), 1);
                            if (currentValue != null) {
                                countedByLanguage.put(language.language(), currentValue + 1);
                            }
                        }
                    }

                    return Pair.of(member.name(), countedByLanguage);
                })
                .toList();


        return new LanguageReport(list);
    }
}

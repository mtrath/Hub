package com.application.hub.domain;

import com.application.hub.persistence.Repository;

import java.util.List;

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
}

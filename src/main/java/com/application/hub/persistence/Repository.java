package com.application.hub.persistence;

import com.application.hub.domain.LanguageModel;
import com.application.hub.domain.MemberModel;
import com.application.hub.domain.RepositoryModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Repository {
    private static final Map<String, List<MemberModel>> MEMBERS = Map.of(
            "codecentric", List.of(new MemberModel(1, "myuser"))
    );

    private static final Map<String, List<RepositoryModel>> REPOSITORIES = Map.of(
            "myuser", List.of(new RepositoryModel(1, "Hello&World")),
            "anotheruser", List.of(new RepositoryModel(1, "Fancy Tool"))
    );

    private static final Map<String, Map<String, List<LanguageModel>>> LANGUAGES = Map.of(
            "myuser", Map.of("Hello&World",
                    List.of(new LanguageModel("C", 100L), new LanguageModel("Python", 500L))),
            "anotheruser", Map.of("Fancy Tool",
                    List.of(new LanguageModel("Python", 100L)))
    );

    public List<MemberModel> findMembersByOrganization(final String organization) {
        return Optional.ofNullable(MEMBERS.get(organization)).orElse(Collections.emptyList());
    }

    public List<RepositoryModel> findRepositoriesByUser(final String username) {
        return Optional.ofNullable(REPOSITORIES.get(username)).orElse(Collections.emptyList());
    }

    public List<LanguageModel> findLanguagesByRepository(final String username, final String repository) {
        final var repositories = Optional.ofNullable(LANGUAGES.get(username)).orElse(Collections.emptyMap());
        return Optional.ofNullable(repositories.get(repository)).orElse(Collections.emptyList());
    }
}

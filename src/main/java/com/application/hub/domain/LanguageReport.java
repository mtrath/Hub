package com.application.hub.domain;

import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LanguageReport {
    private final List<UserReport> userReports;

    public LanguageReport(List<Pair<String, Map<String, Integer>>> report) {
        userReports = report.stream()
                .map(entry -> {
                    final String username = entry.getFirst();
                    final Map<String, Integer> countedRepositories = entry.getSecond();
                    final List<CountedRepositories> listOfCountedRepositories = mapCountedRepositories(countedRepositories);
                    return new UserReport(username, listOfCountedRepositories);
                })
                .toList();
    }

    public List<UserReport> getUserReports() {
        return userReports;
    }

    private static List<CountedRepositories> mapCountedRepositories(Map<String, Integer> value) {
        return value.entrySet().stream()
                .map(countedRepositories -> new CountedRepositories(countedRepositories.getKey(), countedRepositories.getValue()))
                .toList();
    }

    public record UserReport(String username, List<CountedRepositories> repositoriesPerLanguage) {
    }

    public record CountedRepositories(String language, int repositoriesCount) {
    }
}

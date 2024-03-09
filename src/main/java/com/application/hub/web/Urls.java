package com.application.hub.web;

public final class Urls {
    private Urls() {
    }

    public static final String ORG_URL_TEMPLATE = "/orgs/{organization}";
    public static final String USER_URL_TEMPLATE = "/users/{username}";
    public static final String REPO_URL_TEMPLATE = "/repos/{username}/{reponame}";
}

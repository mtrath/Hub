package com.application.hub.clients;

import feign.Param;
import feign.RequestLine;

public interface HubClient {

    @RequestLine("GET /orgs/{organization}/members")
    String getMembers(@Param final String organization);

    @RequestLine("GET /users/{username}/repos")
    String getRepositories(@Param final String username);

    @RequestLine("GET /repos/{username}/{reponame}/languages")
    String getLanguages(@Param final String username, @Param final String reponame);
}

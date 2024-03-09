package com.application.hub.web;

import java.util.HashMap;
import java.util.Map;

public class LanguageJson extends HashMap<String, Long> {
    public LanguageJson() {
    }

    public LanguageJson(Map<String, Long> languagesMap) {
        super(languagesMap);
    }
}

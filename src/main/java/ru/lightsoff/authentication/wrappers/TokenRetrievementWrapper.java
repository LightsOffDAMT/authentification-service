package ru.lightsoff.authentication.wrappers;

import ru.lightsoff.authentication.services.BaseAuthService;

public class TokenRetrievementWrapper {
    private TokenRetrievementVerdict verdict;
    private String payload;

    public TokenRetrievementWrapper(TokenRetrievementVerdict verdict, String payload) {
        this.verdict = verdict;
        this.payload = payload;
    }

    public TokenRetrievementVerdict getVerdict() {
        return verdict;
    }

    public TokenRetrievementWrapper setVerdict(TokenRetrievementVerdict verdict) {
        this.verdict = verdict;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public TokenRetrievementWrapper setPayload(String payload) {
        this.payload = payload;
        return this;
    }
}

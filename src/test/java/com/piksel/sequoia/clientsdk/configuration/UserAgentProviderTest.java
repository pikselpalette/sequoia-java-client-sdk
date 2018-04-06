package com.piksel.sequoia.clientsdk.configuration;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * You might need to run <code>mvn resources:resources</code> if this test 
 * fails in your IDE.
 */
public class UserAgentProviderTest {

    private static final String SEMVER_REGEX = "(\\d)\\.(\\d)\\.(\\d)([A-Za-z][0-9A-Za-z-]*)?.*";
    private static final String DEFAULT_USER_AGENT_PREFIX = "sequoia-java-client-sdk/";

    @Test
    public void givenAPassthroughUserAgentConfigurer_shouldSetDefaultUserAgent() {
        UserAgentProvider provider = new UserAgentProvider();   
        String userAgent = provider.provideUserAgent(s -> s);
        assertTrue(userAgent.startsWith(DEFAULT_USER_AGENT_PREFIX));
        String versionSubstring = userAgent.substring(DEFAULT_USER_AGENT_PREFIX.length(), userAgent.length());
        assertTrue(versionSubstring.matches(SEMVER_REGEX));
    }
    
    @Test
    public void givenAnOverridingConfigurer_shouldOverride() {
        UserAgentProvider provider = new UserAgentProvider();   
        String userAgent = provider.provideUserAgent(s -> "why so serious?");
        assertTrue(userAgent.startsWith("why so serious?"));
    }
    
    @Test
    public void givenADecoratingConfigurer_shouldDecorate() {
        UserAgentProvider provider = new UserAgentProvider();   
        String userAgent = provider.provideUserAgent(s -> "|" + s + "|");
        assertTrue(userAgent.startsWith("|") && userAgent.endsWith("|"));
    }
}
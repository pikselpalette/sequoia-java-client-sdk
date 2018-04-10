package com.piksel.sequoia.clientsdk.configuration;

/*-
 * #%L
 * Sequoia Java Client SDK
 * %%
 * Copyright (C) 2018 Piksel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.piksel.sequoia.annotations.Internal;

import lombok.extern.slf4j.Slf4j;

/**
 * Generates a user-agent string including the current version. If the 
 * current version is not part of the implementation version of the package
 * then the version <code>unknown-version</code> will be used instead.
 */
@Internal
@Slf4j
public class UserAgentProvider {

    private static final String VERSION_PROPERTIES = "/version.properties";
    private static final String UNKNOWN_VERSION = "unknown-version";
    private static final String DEFAULT_USER_AGENT_STRING_FORMAT = "sequoia-java-client-sdk/%s";

    /**
     * Build the user-agent string from the implementation version of the 
     * module, and allowing the version string to be configured by the supplied 
     * {@link UserAgentConfigurer}. 
     * 
     * @param configurer the configurer to override the useragent string
     * @return the configured user-agent string.
     */
    public String provideUserAgent(UserAgentConfigurer configurer) {
        Preconditions.checkNotNull(configurer, "UserAgentConfigurer must not be null");
        String userAgentString = buildVersionString(configurer);
        log.debug("User-Agent header configured as: [{}]", userAgentString);
        return userAgentString;
    }

    private String buildVersionString(UserAgentConfigurer configurer) {
        String versionString = getVersionString();
        return configurer.configureUserAgentString(
                format(DEFAULT_USER_AGENT_STRING_FORMAT, versionString));
    }

    private String getVersionString() {
        Properties properties = new Properties();
        try {
            loadProperties(properties);
            return (String) properties.get("client-sdk-version");
        } catch (IOException e) {
            log.warn("Unable to locate client sdk version - was there a version.properties on the classpath?");
            return UNKNOWN_VERSION;
        }
    }

    private void loadProperties(Properties properties) throws IOException {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = UserAgentProvider.class.getResourceAsStream(VERSION_PROPERTIES);
            properties.load(resourceAsStream);
        } finally {
            if (resourceAsStream != null) {
                resourceAsStream.close();
            }
        }
    }
    
}

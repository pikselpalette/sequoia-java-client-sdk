package com.piksel.sequoia.clientsdk.resource;

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

import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang.StringUtils.substringBefore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

class UrlQueryStringParser {

    private final String url;

    private UrlQueryStringParser(String url) {
        this.url = url;
    }

    public static UrlQueryStringParser urlParser(String url) {
        return new UrlQueryStringParser(url);
    }

    public Map<String, String> queryString() {
        return Splitter.on("&").withKeyValueSeparator('=').split(substringAfter(urlDecode(url), "?"));
    }

    public List<String> getPathParts() {
        return Lists.newArrayList(Splitter.on("/").split(substringBefore(urlDecode(url), "?")));
    }

    public String replaceFinalPath(String resourceKey) {
        return url.replaceAll(url.split("/")[url.split("/").length - 1].split("\\?")[0], resourceKey);
    }

    private String urlDecode(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ErrorDecodingUrl(e);
        }
    }

    private static final class ErrorDecodingUrl extends RuntimeException {

        private static final long serialVersionUID = 680200987716582877L;

        public ErrorDecodingUrl(Exception e) {
            super(e);
        }

    }
}

package com.piksel.sequoia.clientsdk.criteria;

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

import static java.util.stream.Collectors.joining;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.Multimap;

/**
 * Holds a collection of query strings and values in the order that they are
 * supplied.
 */
final class QueryString extends ForwardingMultimap<String, String> {

    private Multimap<String, String> delegate;

    public QueryString() {
        delegate = ArrayListMultimap.create();
    }

    public QueryString(Map<String, ?> source) {
        if (source != null) {
            source.forEach((k, v) -> put(k, v != null ? v.toString() : null));
        }
    }

    /**
     * The string representation of a query string consists of <code>=</code>
     * separated and URL-safe UTF-8 encoded key and value pairs which are
     * themselves separated by an ampersand.
     */
    @Override
    public String toString() {
        return entries().stream()
                .map((e) -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(joining("&"));
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ErrorEncodingQueryString(e);
        }
    }

    @Override
    protected Multimap<String, String> delegate() {
        return delegate;
    }

    private static final class ErrorEncodingQueryString
            extends RuntimeException {

        private static final long serialVersionUID = -9087730077177477046L;

        public ErrorEncodingQueryString(Exception e) {
            super(e);
        }

    }

}

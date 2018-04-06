package com.piksel.sequoia.clientsdk.resource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UrlQueryStringParserTest {

    @Test
    public void testParser() {
        assertThat(
                UrlQueryStringParser.urlParser("http://my url.com?test=true")
                        .queryString().keySet().iterator().next(),
                is("test"));
        assertThat(
                UrlQueryStringParser.urlParser("http://my url.com?test=true")
                        .queryString().values().iterator().next(),
                is("true"));
    }
}

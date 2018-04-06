package com.piksel.sequoia.clientsdk.resource;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Lists;

public class PageTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void givenARequestForIndexOutsideOfRange_shouldThrowPageNotFoundException() {
        thrown.expect(Page.PageResourceDoesNotExist.class);
        Page<Resource> page = Page.from(aMeta(), anEmptyList());
        page.at(0);
    }
    
    @Test
    public void givenARequestForIndexWithoutBounds_shouldReturnIt() {
        Page<Resource> page = Page.from(aMeta(), newArrayList(aResource()));
        Resource resource = page.at(0);
        assertNotNull(resource);
    }

    private Resource aResource() {
        return new Resource();
    }

    private List<Resource> anEmptyList() {
        return Lists.newArrayList();
    }

    private Meta aMeta() {
        return new Meta();
    }
    
}

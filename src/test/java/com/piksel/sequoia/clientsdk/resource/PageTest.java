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

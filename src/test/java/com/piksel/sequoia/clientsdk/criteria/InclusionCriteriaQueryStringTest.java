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

import static com.piksel.sequoia.clientsdk.criteria.Inclusion.resource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)

public class InclusionCriteriaQueryStringTest {

    QueryStringFactory queryStringFactory = new QueryStringFactory();
    TestCriteria criteria;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class GivenASimpleInclusionExpression {

        public class WithValidResourceName {
            @Test
            public void shouldGenerateAQueryStringWithTheIncludedResource() {
                criteria.include(resource("jobs"));
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("include=jobs&continue=true"));
            }
        }

        public class WithInvalidResourceName {

            @Test
            public void shouldThrowInvalidResourceNameException()
                    throws InvalidResourceNameException {
                thrown.expect(InvalidResourceNameException.class);
                thrown.expectMessage(
                        "Invalid resource name. The resource name must match the pattern: [a-zA-Z]+");
                criteria.include(resource("ñapas"));
            }
        }

    }

    public class GivenMultipleInclusionExpressions {

        public class WithDifferentResourceNames {
            @Test
            public void shouldGenerateAQueryStringWithTheIncludedResources() {
                criteria.include(resource("jobs"), resource("task"),
                        resource("events"));
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(),
                        is("include=jobs%2Ctask%2Cevents&continue=true"));
            }
        }

        public class WithDuplicateResourceNames {
            @Test
            public void shouldGenerateAQueryStringWithoutDuplicatesIncludedResources() {
                criteria.include(resource("jobs"), resource("events"),
                        resource("jobs"));
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("include=jobs%2Cevents&continue=true"));
            }
        }

    }

    public class GivenFieldsByResource {

        public class WithOneResourceName {

            public class WithOneFieldName {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedAndFieldsResources() {
                    criteria.include(resource("jobs").fields("text"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("include=jobs&continue=true&jobs.fields=text"));
                }
            }

            public class WithSeveralFieldNames {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedAndFieldsResources() {
                    criteria.include(resource("jobs").fields("title", "name"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("include=jobs&continue=true&jobs.fields=title%2Cname"));
                }
            }

        }

        public class WithSeveralResourceName {

            public class WithOneFieldName {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedAndFieldsResources() {
                    criteria.include(resource("jobs").fields("title"),
                            resource("events").fields("name"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "include=jobs%2Cevents&events.fields=name&continue=true&jobs.fields=title"));
                }
            }

            public class WithSeveralFieldNames {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedAndFieldsResources() {
                    criteria.include(resource("jobs").fields("name", "title"),
                            resource("events").fields("name", "ref"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "include=jobs%2Cevents&events.fields=name%2Cref&continue=true&jobs.fields=name%2Ctitle"));
                }
            }

        }

    }
    
    public class GivenSortsByResource {

        public class WithOneResourceName {

            public class WithOneSort {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedAndSortsResources() {
                    criteria.include(resource("jobs").orderBy("text"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("jobs.sort=text&include=jobs&continue=true"));
                }
            }

            public class WithSeveralSorts {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedAndSortsResources() {
                    criteria.include(resource("jobs").orderBy("title").asc().orderBy("name").desc());
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("jobs.sort=title%2C-name&include=jobs&continue=true"));
                }
            }

        }

        public class WithSeveralResourceName {

            public class WithOneSort {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedAndSortsResources() {
                    criteria.include(resource("jobs").orderBy("title"),
                            resource("events").orderBy("name"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "jobs.sort=title&include=jobs%2Cevents&continue=true&events.sort=name"));
                }
            }

            public class WithSeveralSorts {
                @Test
                public void shouldGenerateAQueryStringWithTheIncludedFieldsAndSortsResources() {
                    criteria.include(resource("jobs").orderBy("name").orderBy("title").desc(),
                            resource("events").fields("name", "ref"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "jobs.sort=name%2C-title&include=jobs%2Cevents&events.fields=name%2Cref&continue=true"));
                }
            }

        }

    }

}

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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class PaginationCriteriaQueryStringTest {

    QueryStringFactory queryStringFactory = new QueryStringFactory();
    TestCriteria criteria;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class GivenAnPaginationExpression {

        public class WithPerPage {

            public class NumItemsPerPageHigherThanZero {

                @Test
                public void shouldGeneratePerPageQueryString() {
                    criteria.perPage(1000);
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("perPage=1000&continue=true"));
                }

                @Test
                public void shouldGenerateTheLastOnePerPage() {
                    criteria.perPage(1000).perPage(1);
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("perPage=1&continue=true"));
                }
            }

            public class NumItemsPerPageEqualZero {

                @Test
                public void shouldThrowException() {
                    thrown.expect(IllegalArgumentException.class);
                    thrown.expectMessage("it must be higher than 0");
                    criteria.perPage(0);
                }
            }
        }

        public class Count {

            @Test
            public void shouldGenerateCountQueryString() {
                criteria.count();
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("count=true&continue=true"));
            }

        }

        public class Resources {

            @Test
            public void shouldGenerateResourcesQueryString() {
                criteria.skipResources();
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("resources=false&continue=true"));
            }

        }
        
        public class FacetCount {

            @Test
            public void shouldGenerateCountQueryString() {
                criteria.count("type");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("count=type&continue=true"));
            }

        }


    }

}

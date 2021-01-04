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
public class OrderingCriteriaQueryStringTest {

    QueryStringFactory queryStringFactory = new QueryStringFactory();
    TestCriteria criteria;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class GivenAnOrderByExpression {

        public class OnGenericFields {

            public class WithDefaultOrdering {
                @Test
                public void shouldGenerateSingleAscendingOrderFieldInTheQueryString() {
                    criteria.orderBy("field-name");
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=field-name&continue=true"));
                }
            }

            public class WithSingleAscendingOrdering {
                @Test
                public void shouldGenerateSingleAscendingOrderFieldInTheQueryString() {
                    criteria.orderBy("field-name").asc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=field-name&continue=true"));
                }
            }

            public class WithMultipleAscendingOrdering {
                @Test
                public void shouldGenerateMultipleAscendingOrderFieldInTheQueryString() {
                    criteria.orderBy("field-name").orderBy("field-name2");
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("sort=field-name%2Cfield-name2&continue=true"));
                }
            }

            public class WithSingleDescendingOrdering {
                @Test
                public void shouldGenerateSingleDescendingOrderFieldInTheQueryString() {
                    criteria.orderBy("field-name").desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-field-name&continue=true"));
                }
            }

            public class WithMultipleDescendingOrdering {
                @Test
                public void shouldGenerateMultipleDescendingOrderFieldInTheQueryString() {
                    criteria.orderBy("field-name").desc().orderBy("field-name2")
                            .desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("sort=-field-name%2C-field-name2&continue=true"));
                }
            }

            public class WithMixedAscendingAndDescendingOrdering {
                @Test
                public void shouldPreserveTheOrderingOfTheOrderByCriteria() {
                    criteria.orderBy("field-name").asc().orderBy("field-name2")
                            .desc().orderBy("field-name3");
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("sort=field-name%2C-field-name2%2Cfield-name3&continue=true"));
                }
            }

            public class WithDuplicatedFields {

                @Test
                public void shouldNotDuplcateFieldsInTheQueryString() {
                    criteria.orderBy("field-name").orderBy("field-name");
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=field-name&continue=true"));
                }

                @Test
                public void shouldAdoptTheLastOrderFieldInTheQueryString() {
                    criteria.orderBy("field-name").asc().orderBy("field-name")
                            .desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-field-name&continue=true"));
                }

            }

            public class WithUtf8Fields {
                @Test
                public void shouldGenerateUtf8EncodedFieldInTheQueryString() {
                    criteria.orderBy("field-næme");
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("sort=field-n%C3%A6me&continue=true"));
                }
            }
        }

        public class OnCommonFields {

            public class Owner {
                @Test
                public void shouldGenerateAscendingOrderByExpression() {
                    criteria.orderByOwner();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=owner&continue=true"));
                }

                @Test
                public void shouldGenerateDescendingOrderByExpression() {
                    criteria.orderByOwner().desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-owner&continue=true"));
                }
            }

            public class Name {
                @Test
                public void shouldGenerateAscendingOrderByExpression() {
                    criteria.orderByName();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=name&continue=true"));
                }

                @Test
                public void shouldGenerateDescendingOrderByExpression() {
                    criteria.orderByName().desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-name&continue=true"));
                }
            }

            public class CreatedAt {
                @Test
                public void shouldGenerateAscendingOrderByExpression() {
                    criteria.orderByCreatedAt();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=createdAt&continue=true"));
                }

                @Test
                public void shouldGenerateDescendingOrderByExpression() {
                    criteria.orderByCreatedAt().desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-createdAt&continue=true"));
                }
            }

            public class CreatedBy {
                @Test
                public void shouldGenerateAscendingOrderByExpression() {
                    criteria.orderByCreatedBy();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=createdBy&continue=true"));
                }

                @Test
                public void shouldGenerateDescendingOrderByExpression() {
                    criteria.orderByCreatedBy().desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-createdBy&continue=true"));
                }
            }

            public class UpdatedAt {
                @Test
                public void shouldGenerateAscendingOrderByExpression() {
                    criteria.orderByUpdatedAt();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=updatedAt&continue=true"));
                }

                @Test
                public void shouldGenerateDescendingOrderByExpression() {
                    criteria.orderByUpdatedAt().desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-updatedAt&continue=true"));
                }
            }

            public class UpdatedBy {
                @Test
                public void shouldGenerateAscendingOrderByExpression() {
                    criteria.orderByUpdatedBy();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=updatedBy&continue=true"));
                }

                @Test
                public void shouldGenerateDescendingOrderByExpression() {
                    criteria.orderByUpdatedBy().desc();
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("sort=-updatedBy&continue=true"));
                }
            }
        }

    }

    public class GivenAnAscExpressionWithoutAnOrderByFirst {

        @Test
        public void shouldThrowAnUndefinedOrderDirectionExceptionWithTheExpectedErrorMessage()
                throws CriteriaException {
            thrown.expect(UndefinedOrderDirectionException.class);
            thrown.expectMessage("Cannot change ordering of unknown field");
            criteria.asc();
        }
    }
}

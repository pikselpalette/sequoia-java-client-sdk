package com.piksel.sequoia.clientsdk.criteria;

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
                    assertThat(queryString.toString(), is("perPage=1000"));
                }

                @Test
                public void shouldGenerateTheLastOnePerPage() {
                    criteria.perPage(1000).perPage(1);
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("perPage=1"));
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

        public class WithPage {

            public class PageNumberHigherThanZero {

                @Test
                public void shouldGeneratePageQueryString() {
                    criteria.page(1000);
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("page=1000"));
                }

                @Test
                public void shouldGenerateTheLastOnePage() {
                    criteria.page(1000).page(1);
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is("page=1"));
                }
            }

            public class PageNumberEqualToZero {

                @Test
                public void shouldThrowException() {
                    thrown.expect(IllegalArgumentException.class);
                    thrown.expectMessage("it must be higher than 0");
                    criteria.page(0);
                }
            }
        }

        public class Count {

            @Test
            public void shouldGenerateCountQueryString() {
                criteria.count();
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("count=true"));
            }

        }

        public class Resources {

            @Test
            public void shouldGenerateResourcesQueryString() {
                criteria.skipResources();
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("resources=false"));
            }

        }
        
        public class FacetCount {

            @Test
            public void shouldGenerateCountQueryString() {
                criteria.count("type");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("count=type"));
            }

        }


    }

}
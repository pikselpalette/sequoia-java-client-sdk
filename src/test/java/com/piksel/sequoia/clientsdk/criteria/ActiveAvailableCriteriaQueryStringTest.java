package com.piksel.sequoia.clientsdk.criteria;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)

public class ActiveAvailableCriteriaQueryStringTest {

    QueryStringFactory queryStringFactory = new QueryStringFactory();
    TestCriteria criteria;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class GivenActiveField {

        public class WithActiveValue {

            @Test
            public void shouldGenerateAQueryStringWithTActive() {
                criteria.active(true);
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("active=true"));
            }

        }

        public class WithoutActiveField {

            @Test
            public void shouldGenerateAQueryStringWithoutTActive() {
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), not(containsString("active=")));
            }

        }

    }

    public class GivenAvailableField {

        public class WithAvailableValue {

            @Test
            public void shouldGenerateAQueryStringWithTActive() {
                criteria.available(true);
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("available=true"));
            }

        }

        public class WithoutActiveField {

            @Test
            public void shouldGenerateAQueryStringWithoutTActive() {
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), not(containsString("available=")));
            }

        }

    }

}

package com.piksel.sequoia.clientsdk.criteria;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class LangCriteriaQueryStringTest {

    QueryStringFactory queryStringFactory = new QueryStringFactory();
    TestCriteria criteria;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class GivenALanguageExpresion {

        public class LangEqualEn {

            @Test
            public void shouldGenerateLangQueryString() {
                criteria.lang("en");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("lang=en"));
            }

        }

        public class LangEqualEnAndEs {

            @Test
            public void shouldGenerateLangQueryString() {
                criteria.lang("en");
                criteria.lang("es");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("lang=es"));
            }

        }

        public class LangEqualNull {

            @Test
            public void shouldGenerateLangQueryString() {
                criteria.lang(null);
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), isEmptyString());
            }

        }

    }

}
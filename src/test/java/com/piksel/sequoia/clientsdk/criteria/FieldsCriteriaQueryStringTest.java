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

public class FieldsCriteriaQueryStringTest {

    QueryStringFactory queryStringFactory = new QueryStringFactory();
    TestCriteria criteria;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class GivenASingleField {

        public class WithValidFiedlName {

            @Test
            public void shouldGenerateAQueryStringWithTheFieldOption() {
                criteria.fields("title");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("fields=title"));
            }

        }

        public class WithInvalidFieldName {

            @Test
            public void shouldThrowInvalidFieldameException()
                    throws UnsafeFieldNameException {
                thrown.expect(UnsafeFieldNameException.class);
                thrown.expectMessage(
                        "Unsafe field name. The field name must match the pattern: [a-zA-Z0-9]+[\\.]*");
                criteria.fields("ti-tle");
            }

        }

    }

    public class GivenSeveralFields {

        public class WithValidFiedlNames {

            @Test
            public void shouldGenerateAQueryStringWithAFieldOption() {
                criteria.fields("title", "ref");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("fields=title%2Cref"));
            }

            @Test
            public void shouldGenerateAQueryStringWithSeveralFieldOptions() {
                criteria.fields("title").fields("ref");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("fields=title%2Cref"));
            }

            @Test
            public void shouldGenerateAQueryStringWithoutDuplicatedlFieldOptions() {
                criteria.fields("title", "ref", "title");
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("fields=title%2Cref"));
            }
        }

    }

}

package com.piksel.sequoia.clientsdk.criteria;

import static com.piksel.sequoia.clientsdk.criteria.StringExpressionFactory.field;
import static com.piksel.sequoia.clientsdk.criteria.StringExpressionFactory.textSearch;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class SelectionCriteriaQueryStringTest {

    QueryStringFactory queryStringFactory = new QueryStringFactory();
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    TestCriteria criteria;

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class WithUnsafeUrlCharacter {

        public class InTheValuePartOfTheQueryString {

            @Test
            public void shouldUrlEncodeThatCharacterUsingFormEncoding() {
                criteria.add(field("fieldName").equalTo("field value"));
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(),
                        is("withFieldName=field+value"));
            }
        }

        public class InTheKeyPartOfTheQueryString {

            @Test
            public void shouldThrowUnsafeFieldNameException()
                    throws CriteriaException {
                thrown.expect(UnsafeFieldNameException.class);
                thrown.expectMessage(
                        "Unsafe field name. The field name must match the pattern: [a-zA-Z0-9]+");
                criteria.add(field("field name").equalTo("fieldvalue"));
            }
        }
    }

    public class Single {
        @Test
        public void shouldGenerateEqualsQueryString() {
            criteria.add(field("fieldName").equalTo("field-value"));
            QueryString queryString = queryStringFactory
                    .createQueryString(criteria);
            assertThat(queryString.toString(), is("withFieldName=field-value"));
        }
    }

    public class Multiples {
        @Test
        public void shouldGenerateAmpersandSeparatedAndPreservingOrder() {
            criteria.add(field("fieldName").equalTo("field-value"))
                    .add(field("field2Name").equalTo("field2-value"));
            QueryString queryString = queryStringFactory
                    .createQueryString(criteria);
            assertThat(queryString.toString(), is(
                    "withFieldName=field-value&withField2Name=field2-value"));
        }
        
        @Test
        public void shouldGenerateAmpersandSeparatedWithSameFieldMoreThanOne() {
            criteria.add(field("fieldName").equalTo("field-value"))
                    .add(field("fieldName").equalTo("field2-value"));
            QueryString queryString = queryStringFactory
                    .createQueryString(criteria);
            assertThat(queryString.toString(), is(
                    "withFieldName=field-value&withFieldName=field2-value"));
        }
    }

    public class WithUtf8Character {
        @Test
        public void shouldUrlEncodeThatValue() {
            criteria.add(field("fieldName").equalTo("field-vælue"));
            QueryString queryString = queryStringFactory
                    .createQueryString(criteria);
            assertThat(queryString.toString(),
                    is("withFieldName=field-v%C3%A6lue"));
        }
    }

    public class WithLogicalExpression {

        public class OneOrMoreOf {

            public class WithArgs {
                @Test
                public void shouldGenerateLogicalOrExpression() {
                    criteria.add(field("fieldName").oneOrMoreOf("field-Value",
                            "field-value2"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("withFieldName=field-Value%7C%7Cfield-value2"));
                }
            }

            public class WithArrayOfStrings {
                @Test
                public void shouldGenerateLogicalOrExpression() {
                    String[] arguments = new String[] { "field-Value", "field-value2" };
                    criteria.add(field("fieldName").oneOrMoreOf(arguments));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("withFieldName=field-Value%7C%7Cfield-value2"));
                }
            }

            public class WithEmptyArrayOfArguments {
                @Test
                public void shouldThrowInvalidCriterionException() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    String[] arguments = new String[] {};
                    criteria.add(field("fieldName").oneOrMoreOf(arguments));
                }
            }

            public class WithNullArguments {
                @Test
                public void shouldThrowInvalidCriterionException() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").oneOrMoreOf((String) null));
                }
            }

            public class WithOneOfTheElementOfTheArrayEmpty {
                @Test
                public void shouldThrowEmptyValueException() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    String[] arguments = new String[] { "field-value", "" };
                    criteria.add(field("fieldName").oneOrMoreOf(arguments));
                }
            }

        }

        public class LogicalAnd {
            @Test
            public void shouldGenerateEquivalentToAndLogicalExpression() {
                criteria.add(field("fieldName").equalTo("field-Value"))
                        .and(field("fieldName").equalTo("field-value2"));
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is(
                        "withFieldName=field-Value&withFieldName=field-value2"));
            }
        }
    }

    public class WithLikeCriteria {

        public class StartsWith {

            public class WithValidArguments {
                @Test
                public void shouldGenerateWildcard() {
                    criteria.add(field("fieldName").startsWith("field-Value"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("withFieldName=field-Value*"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").startsWith(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").startsWith(""));
                }
            }

        }

        public class EndsWith {

            public class WithValidArguments {
                @Test
                public void shouldGenerateWildcard() {
                    criteria.add(field("fieldName").endsWith("field-Value"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("withFieldName=*field-Value"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").endsWith(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").endsWith(""));
                }
            }

        }

        public class EqualTo {

            public class WithValidArguments {
                @Test
                public void shouldGenerateNotEqualToValueQueryString() {
                    criteria.add(field("fieldName").equalTo("field-Value"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("withFieldName=field-Value"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").equalTo(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").equalTo(""));
                }
            }
        }

        public class NotEqualTo {

            public class WithValidArguments {
                @Test
                public void shouldGenerateNotEqualToValueQueryString() {
                    criteria.add(field("fieldName").notEqualTo("field-Value"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(),
                            is("withFieldName=%21field-Value"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").notEqualTo(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").notEqualTo(""));
                }
            }
        }

        public class Exist {
            @Test
            public void shouldGenerateExistQueryString() {
                criteria.add(field("fieldName").exist());
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("withFieldName=*"));
            }
        }

        public class NotExist {
            @Test
            public void shouldGenerateNotExistQueryString() {
                criteria.add(field("fieldName").notExist());
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("withFieldName=%21*"));
            }
        }

    }

    public class WithTextSearchCriteria {

        public class WithValidArguments {
            @Test
            public void shouldGenerateTextSearchCriteria() {
                criteria.add(textSearch("text search"));
                QueryString queryString = queryStringFactory
                        .createQueryString(criteria);
                assertThat(queryString.toString(), is("q=text+search"));
            }
        }

        public class WithInvalidArguments {
            @Test
            public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                thrown.expect(InvalidCriterionException.class);
                thrown.expectMessage(
                        "Cannot filter for an empty or null value");

                criteria.add(textSearch(null));
            }

            @Test
            public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                thrown.expect(InvalidCriterionException.class);
                thrown.expectMessage(
                        "Cannot filter for an empty or null value");

                criteria.add(textSearch(""));
            }
        }
    }

    public class WithRangeCriteria {

        public class Between {

            public class WithValidArguments {
                @Test
                public void shouldGenerateBetweenRangeQueryString() {
                    criteria.add(field("fieldName").between(
                            "2014-12-25T12:00:00.000Z",
                            "2015-12-25T12:00:00.000Z"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "withFieldName=2014-12-25T12%3A00%3A00.000Z%2F2015-12-25T12%3A00%3A00.000Z"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithAllNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").between(null, null));
                }

                @Test
                public void shouldThrowInvalidCriterionExceptionWithOneNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName")
                            .between("2014-12-25T12:00:00.000Z", null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithAllEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").between("", ""));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithOneEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").between("",
                            "2014-12-25T12:00:00.000Z"));
                }
            }

        }

        public class NotBetween {

            public class WithValidArguments {
                @Test
                public void shouldGenerateNotBetweenRangeQueryString() {
                    criteria.add(field("fieldName").notBetween(
                            "2014-12-25T12:00:00.000Z",
                            "2015-12-25T12:00:00.000Z"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "withFieldName=%212014-12-25T12%3A00%3A00.000Z%2F2015-12-25T12%3A00%3A00.000Z"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithAllNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").notBetween(null, null));
                }

                @Test
                public void shouldThrowInvalidCriterionExceptionWithOneNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName")
                            .notBetween("2014-12-25T12:00:00.000Z", null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithAllEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").notBetween("", ""));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithOneEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").notBetween("",
                            "2014-12-25T12:00:00.000Z"));
                }
            }
        }

        public class LessThan {

            public class WithValidArgument {

                @Test
                public void shouldGenerateLessThanRangeQueryString() {
                    criteria.add(field("fieldName")
                            .lessThan("2014-12-25T12:00:00.000Z"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "withFieldName=%212014-12-25T12%3A00%3A00.000Z%2F"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").lessThan(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").lessThan(""));
                }
            }
        }

        public class LessThanOrEqualTo {

            public class WithValidArgument {

                @Test
                public void shouldGenerateLessThanOrEqualToRangeQueryString() {
                    criteria.add(field("fieldName")
                            .lessThanOrEqualTo("2014-12-25T12:00:00.000Z"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "withFieldName=%2F2014-12-25T12%3A00%3A00.000Z"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").lessThanOrEqualTo(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").lessThanOrEqualTo(""));
                }
            }
        }

        public class GreaterThan {

            public class WithValidArgument {
                @Test
                public void shouldGenerateGreaterThanRangeQueryString() {
                    criteria.add(field("fieldName")
                            .greaterThan("2014-12-25T12:00:00.000Z"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "withFieldName=%21%2F2014-12-25T12%3A00%3A00.000Z"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").greaterThan(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").greaterThan(""));
                }
            }
        }

        public class GreaterThanOrEqualTo {

            public class WithValidArgument {
                @Test
                public void shouldGenerateGreaterThanOrEqualToRangeQueryString() {
                    criteria.add(field("fieldName")
                            .greaterThanOrEqualTo("2014-12-25T12:00:00.000Z"));
                    QueryString queryString = queryStringFactory
                            .createQueryString(criteria);
                    assertThat(queryString.toString(), is(
                            "withFieldName=2014-12-25T12%3A00%3A00.000Z%2F"));
                }
            }

            public class WithInvalidArguments {
                @Test
                public void shouldThrowInvalidCriterionExceptionWithNullArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").greaterThanOrEqualTo(null));
                }

                @Test
                public void shouldInvalidCriterioneExceptionWithEmptyArgument() {
                    thrown.expect(InvalidCriterionException.class);
                    thrown.expectMessage(
                            "Cannot filter for an empty or null value");

                    criteria.add(field("fieldName").greaterThanOrEqualTo(""));
                }
            }
        }

    }

    public class WithUndefinedCriteria {
        @Test
        public void shouldThrowCriteriaException() throws CriteriaException {
            thrown.expect(UndefinedCriterionException.class);
            thrown.expectMessage("Criterion cannot be null");
            Criterion criterion = null;
            criteria.add(criterion);
        }
    }

    private static class TestCriteria extends DefaultCriteria<TestCriteria> {
    }

}
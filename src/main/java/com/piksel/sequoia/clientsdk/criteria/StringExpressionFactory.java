package com.piksel.sequoia.clientsdk.criteria;

import java.util.Arrays;
import java.util.Locale;

import com.google.api.client.util.Preconditions;
import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.RequestExecutionException;

/**
 * Creates an expression for property. 
 */
@PublicEvolving
public class StringExpressionFactory {

    private final String propertyName;

    public StringExpressionFactory(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Check that the arguments is not null and does not contain an empty or
     * null value.
     * 
     * @param args the arguments to be validated
     */
    private static void checkArguments(String... args) {
        try {
            Preconditions.checkArgument(args != null);
            Preconditions.checkArgument(args.length != 0);
            Preconditions.checkArgument(!(Arrays.asList(args).contains(null)
                    || Arrays.asList(args).contains("")));
        } catch (IllegalArgumentException e) {
            throw new InvalidCriterionException(e);
        } catch (Exception e) {
            throw new CriteriaException(e);
        }
    }

    /**
     * Specifies that the field should be equal to the provided value
     * (case sensitive).
     * 
     * <p>Note - query expression is in the form: <code>propertyName=value</code>.
     * 
     * @param value the value that the field should be equal to.
     * @since 1.0.0
     */
    public SimpleExpression equalTo(String value) {
        checkArguments(value);
        return new SimpleExpression(propertyName, value, Operator.EQUALS);
    }

    /**
     * Specifies that the field can be one of the values (OR) or should 
     * contain one or more of the provided fields in the case of arrays. 
     * 
     * <p>Note - query is in the form: <code>propertyName=arg1||arg2</code>
     * 
     * @param args the values to test 
     * @since 1.0.0
     */
    public LogicalExpression oneOrMoreOf(String... args) {
        checkArguments(args);
        return new LogicalExpression(propertyName, args,
                LogicalLocation.ONEORMORE);
    }

    /**
     * Specifies that the field should not be equal to the provided 
     * value.
     * 
     * <p>Note - query expression is in the form of <code>propertyName=!value</code>
     * 
     * @param value value that the field should not be equal to
     * @since 1.0.0
     */
    public SimpleExpression notEqualTo(String value) {
        checkArguments(value);
        return new LikeExpression(propertyName, value, MatchLocation.NEGATION);
    }

    /**
     * Specifies that the field should exist (value can be anything).
     * 
     * <p>The expression is in the form of <code>propertyName=*</code>
     * 
     * @since 1.0.0
     */
    public SimpleExpression exist() {
        return new LikeExpression(propertyName, "", MatchLocation.EXIST);
    }

    /**
     * Specifies that the field should not exist.
     * 
     * <p>Note - query expression is in the form of <code>propertyName=!*</code>
     * 
     * @since 1.0.0
     */
    public SimpleExpression notExist() {
        return new LikeExpression(propertyName, "", MatchLocation.NOTEXIST);
    }

    /**
     * Specifies that the field should start with the given value.
     * 
     * <p>Note - query expression is in the form of <code>propertyName=value*</code>
     * 
     * @param value the value that the field should start with
     * @since 1.0.0
     */
    public LikeExpression startsWith(String value) {
        checkArguments(value);
        return new LikeExpression(propertyName, value, MatchLocation.BEGIN);
    }

    /**
     * Specifies that the field should end with the given value.
     * 
     * <p>Note - query expression is in the form of <code>propertyName=*value</code>.
     * 
     * @param value the value that the string should end with
     * @since 1.0.0
     */
    public LikeExpression endsWith(String value) {
        checkArguments(value);
        return new LikeExpression(propertyName, value, MatchLocation.END);
    }

    /**
     * Specifies that the field should be within a range with the first
     * argument as the lower bound and the second argument as the upper 
     * bound. 
     * 
     * <p>Range expressions are inclusive and work on numeric and 
     * date/timestamp types
     * 
     * <p>Note - query expression is in the form of <code>propertyName=va/vb</code>.
     * 
     * @param from the lower bound for the value
     * @param to the upper bound for the value
     * @since 1.0.0
     */
    public RangeExpression between(String from, String to) {
        checkArguments(from, to);
        return new RangeExpression(propertyName, from, to, RangeLocation.BETWEEN);
    }

    /**
     * Specifies that the field should not be within the range with the 
     * first argument as the lower bound and the second argument as the 
     * upper bound. 
     * 
     * <p>Range expressions are inclusive and work on numeric and 
     * date/timestamp types
     * 
     * <p>Note - query expression is in the form of <code>propertyName=!va/vb</code>.
     * 
     * @param from the lower bound 
     * @param to the upper bound
     * @since 1.0.0
     */
    public RangeExpression notBetween(String from, String to) {
        checkArguments(from, to);
        return new RangeExpression(propertyName, from, to,
                RangeLocation.NOTBETWEEN);
    }

    /**
     * Specifies that the field should be less than the provided value.
     * 
     * <p>Note - query expression in the form of <code>propertyName=!value/</code>
     * 
     * @param value the value that the field should be less than
     * @since 1.0.0
     */
    public RangeExpression lessThan(String value) {
        checkArguments(value);
        return new RangeExpression(propertyName, value, RangeLocation.LESSTHAN);
    }

    /**
     * Specifies that the field should be less or equal to than the 
     * provided value.
     * 
     * <p>Note - query expression in the form of <code>propertyName=/value</code>
     * 
     * @param value the value that the field should be less than or equal to
     * @since 1.0.0
     */
    public RangeExpression lessThanOrEqualTo(String value) {
        checkArguments(value);
        return new RangeExpression(propertyName, value,
                RangeLocation.LESSTHANOREQUALTO);
    }

    /**
     * Specifies that the field should be greater than the provided value.
     * 
     * <p>Note - query expression is in the form of <code>propertyName=!/value</code>
     * 
     * @param value the value that the field should be greater than
     * @since 1.0.0
     */
    public RangeExpression greaterThan(String value) {
        checkArguments(value);
        return new RangeExpression(propertyName, value,
                RangeLocation.GREATERTHAN);
    }

    /**
     * Specifies that the field should be greater than or equal to the 
     * provided value.
     * 
     * <p>Note - query expression is in the form of propertyName=value/
     * 
     * @param value the value that the field should be greater than or equal to
     * @since 1.0.0
     */
    public RangeExpression greaterThanOrEqualTo(String value) {
        checkArguments(value);
        return new RangeExpression(propertyName, value,
                RangeLocation.GREATERTHANOREQUALTO);
    }

    public static StringExpressionFactory newStringExpressionFactory(String propertyName) {
        return new StringExpressionFactory(propertyName);
    }

    /**
     * Specifies a field filter to apply as a criteria to the request.
     * 
     * <p>Field names must path the regular expression: [a-zA-Z0-9]+
     * 
     * <p><b>Note:</b> Not all resource fields are <em>filterable</em>. If a
     * non-filterable field is supplied as a filter an
     * {@link RequestExecutionException} will be thrown.
     * 
     * @param propertyName
     *            the field property name to user as a filter criteria
     * @return a {@link StringExpressionFactory} to construct the field filter
     *         criteria
     * @throws CriteriaException
     *             if the propertyName is not valid
     */
    public static StringExpressionFactory field(String propertyName)
            throws CriteriaException {
        ArgumentValidation.checkFieldName(propertyName);
        return propertyName.contains(".") ? new StringExpressionFactory(propertyName) : new StringExpressionFactory(buildWith(propertyName));
    }

    /**
     * Create a new text search criteria from the provided text.
     * @param text the text to search
     * @return the criteria
     * @throws CriteriaException if the 
     */
    public static TextSearchExpression textSearch(String text)
            throws CriteriaException {
        checkArguments(text);
        return new TextSearchExpression(text, Operator.EQUALS);

    }

    private static String buildWith(String propertyName) {
        return "with" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH)
                + propertyName.substring(1);
    }

}
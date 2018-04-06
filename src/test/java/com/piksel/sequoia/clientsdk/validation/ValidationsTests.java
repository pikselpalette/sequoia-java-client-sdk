package com.piksel.sequoia.clientsdk.validation;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import lombok.Getter;
import lombok.Setter;

import org.junit.Test;

public class ValidationsTests {

    @Getter
    @Setter
    private static final class ResourceWrongDateTimeFormat
            implements Validatable {
        @DateTimeFormat
        private String time;

        @DateTimeFormat(groups = PutValidation.class)
        private String timeGroup;
    }

    @Getter
    @Setter
    private static final class ResourceWrongDurationFormat
            implements Validatable {
        @DurationFormat
        private String duration;

        @DurationFormat(groups = PutValidation.class)
        private String durationGroup;

        @ValidateEnum(enumClazz = MyEnum.class)
        private String myEnum;
    }

    public enum MyEnum {
        TEST
    }

    @Test
    public void testWrongDateTimeFormat() {

        ResourceWrongDateTimeFormat resourceWrongDateTimeFormat = new ResourceWrongDateTimeFormat();
        resourceWrongDateTimeFormat.setTime("time");
        assertThat(resourceWrongDateTimeFormat.isValid(), not(empty()));
        resourceWrongDateTimeFormat.setTimeGroup("time");
        assertThat(resourceWrongDateTimeFormat.isValid(PutValidation.class),
                not(empty()));

    }

    @Test
    public void testWellDateTimeFormat() {

        ResourceWrongDateTimeFormat resourceWrongDateTimeFormat = new ResourceWrongDateTimeFormat();
        resourceWrongDateTimeFormat.setTime("2014-02-15T02:15:03.123Z");
        assertThat(resourceWrongDateTimeFormat.isValid(), is(empty()));
        resourceWrongDateTimeFormat.setTimeGroup("2014-02-15T02:15:03.123Z");
        assertThat(resourceWrongDateTimeFormat.isValid(PutValidation.class),
                is(empty()));

    }

    @Test
    public void testNullDateTimeFormat() {

        ResourceWrongDateTimeFormat resourceWrongDateTimeFormat = new ResourceWrongDateTimeFormat();
        resourceWrongDateTimeFormat.setTime(null);
        assertThat(resourceWrongDateTimeFormat.isValid(), is(empty()));
        resourceWrongDateTimeFormat.setTimeGroup(null);
        assertThat(resourceWrongDateTimeFormat.isValid(PutValidation.class),
                is(empty()));

    }

    @Test
    public void testWrongDuration() {

        ResourceWrongDurationFormat resourceWrongDurationFormat = new ResourceWrongDurationFormat();
        resourceWrongDurationFormat.setDuration("time");
        assertThat(resourceWrongDurationFormat.isValid(), not(empty()));
        resourceWrongDurationFormat.setDurationGroup("time");
        assertThat(resourceWrongDurationFormat.isValid(PutValidation.class),
                not(empty()));

    }

    @Test
    public void testWellDuration() {

        ResourceWrongDurationFormat resourceWrongDurationFormat = new ResourceWrongDurationFormat();
        resourceWrongDurationFormat.setDuration("PT59.96S");
        assertThat(resourceWrongDurationFormat.isValid(), is(empty()));
        resourceWrongDurationFormat.setDurationGroup("PT59.96S");
        assertThat(resourceWrongDurationFormat.isValid(PutValidation.class),
                is(empty()));

    }

    @Test
    public void testNullDuration() {

        ResourceWrongDurationFormat resourceWrongDurationFormat = new ResourceWrongDurationFormat();
        resourceWrongDurationFormat.setDuration(null);
        assertThat(resourceWrongDurationFormat.isValid(), is(empty()));
        resourceWrongDurationFormat.setDurationGroup(null);
        assertThat(resourceWrongDurationFormat.isValid(PutValidation.class),
                is(empty()));

    }

    @Test
    public void testWellEnum() {

        ResourceWrongDurationFormat resourceWrongEnum = new ResourceWrongDurationFormat();
        resourceWrongEnum.setMyEnum("TEST");
        assertThat(resourceWrongEnum.isValid(), is(empty()));

    }

    @Test
    public void testWrongEnum() {

        ResourceWrongDurationFormat resourceWrongEnum = new ResourceWrongDurationFormat();
        resourceWrongEnum.setMyEnum("TEST2");
        assertThat(resourceWrongEnum.isValid(), not(empty()));

    }

}

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

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import de.bechte.junit.runners.context.HierarchicalContextRunner;

@RunWith(HierarchicalContextRunner.class)
public class IntersectCriteriaTest {
    QueryStringFactory queryStringFactory = new QueryStringFactory();
    TestCriteria criteria;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        this.criteria = new TestCriteria();
    }

    public class GivenIntersectCriterias {

        @Test
        public void shouldIncludeSimpleIntersectCriteria() {
            addSimpleCriteria("prop");
            addIntersectCriteria("relatedDoc", "relatedProp");

            QueryString qs = queryStringFactory.createQueryString(criteria);
            assertThat(qs.toString(), is("include=relatedDoc%3Cintersect%3E&prop=value&relatedDoc.relatedProp=value"));
        }

        @Test
        public void shouldIncludeMultipleIntersectCriterias() {
            addSimpleCriteria("prop");
            addIntersectCriteria("relatedDoc", "relatedProp");
            addIntersectCriteria("relatedDoc2", "related2Prop");

            QueryString qs = queryStringFactory.createQueryString(criteria);
            assertThat(qs.toString(),
                    is("include=relatedDoc%3Cintersect%3E%2CrelatedDoc2%3Cintersect%3E&prop=value&relatedDoc.relatedProp=value&relatedDoc2.related2Prop=value"));
        }

        @Test
        public void shouldIncludeIntersectWithIncludes() {
            addSimpleCriteria("prop");
            addIntersectCriteria("relatedDoc", "relatedProp");
            addIncludeCriteria("includedDoc");

            QueryString qs = queryStringFactory.createQueryString(criteria);
            assertThat(qs.toString(),
                    is("include=includedDoc%2CrelatedDoc%3Cintersect%3E&prop=value&relatedDoc.relatedProp=value"));
        }

        @Test
        public void shouldAvoidFacetCountWhenIntersect() {
            addFacetCount("field1");
            addIntersectCriteria("relatedDoc", "relatedProp");

            QueryString qs = queryStringFactory.createQueryString(criteria);
            assertThat(qs.toString(), not(containsString("count=field1")));

        }

        @Test
        public void shouldIncludeFieldsInIntersectWhenIntersect() {
            addSimpleCriteria("prop");
            addIntersectCriteria("relatedDoc", "relatedProp", "retrieveField1", "retrieveField2");

            QueryString qs = queryStringFactory.createQueryString(criteria);
            assertThat(qs.toString(),
                    is("include=relatedDoc%3Cintersect%3E&relatedDoc.fields=retrieveField1%2CretrieveField2&prop=value&relatedDoc.relatedProp=value"));

        }

        @Test
        public void shouldIncludeOrderFieldsInIntersectWhenIntersect() {
            addSimpleCriteria("prop");
            addIntersectCriteriaWitOrder("relatedDoc", "relatedProp", "field1", "field2");

            QueryString qs = queryStringFactory.createQueryString(criteria);
            assertThat(qs.toString(),
                    is("include=relatedDoc%3Cintersect%3E&relatedDoc.sort=field1%2Cfield2&prop=value&relatedDoc.relatedProp=value"));

        }

        @Test
        public void shouldIncludeInclusionInIntersectWhenIntersect() {
            addSimpleCriteria("prop");
            addIntersectCriteriaWitInclusion("relatedDoc", "relatedProp", "relatedRelatedDoc");

            QueryString qs = queryStringFactory.createQueryString(criteria);
            assertThat(qs.toString(),
                    is("include=relatedDoc%3Cintersect%3E&relatedDoc.include=relatedRelatedDoc&prop=value&relatedDoc.relatedRelatedDoc.field=value&relatedDoc.relatedProp=value"));
        }


        private void addSimpleCriteria(String property) {
            criteria.add(new SimpleExpression(property, "value", Operator.EQUALS));
        }

        private void addIntersectCriteria(String docName, String property) {
            TestCriteria intersectCriteria = new TestCriteria();
            intersectCriteria.add(new SimpleExpression(property, "value", Operator.EQUALS));
            criteria.intersect(docName, intersectCriteria);
        }

        private void addIntersectCriteria(String docName, String property, String ... includeFields) {
            TestCriteria intersectCriteria = new TestCriteria();
            intersectCriteria.add(new SimpleExpression(property, "value", Operator.EQUALS));
            intersectCriteria.fields(includeFields);
            criteria.intersect(docName, intersectCriteria);
        }

        private void addIntersectCriteriaWitOrder(String docName, String property, String ... orderFields) {
            TestCriteria intersectCriteria = new TestCriteria();
            intersectCriteria.add(new SimpleExpression(property, "value", Operator.EQUALS));
            for(String field : orderFields) {
                intersectCriteria.add(new Order(field, true));
            }

            criteria.intersect(docName, intersectCriteria);
        }

        private void addIntersectCriteriaWitInclusion(String docName, String property, String ... includedDocs) {
            TestCriteria intersectCriteria = new TestCriteria();
            intersectCriteria.add(new SimpleExpression(property, "value", Operator.EQUALS));
            for(String doc : includedDocs) {
                intersectCriteria.add(Inclusion.resource(doc));
                intersectCriteria.add(new SimpleExpression(doc+".field", "value",  Operator.EQUALS));
            }

            criteria.intersect(docName, intersectCriteria);
        }


        private void addIncludeCriteria(String resourceName) {
            criteria.add(Inclusion.resource(resourceName));
        }

        private void addFacetCount(String fields) {
            criteria.count(fields);
        }
    }

    public class GivenInvalidIntersectCriteria {
        @Test
        public void shouldThrowInvalidResourceNameExceptionWhenNoResourceName() {
            thrown.expect(InvalidResourceNameException.class);
            thrown.expectMessage(
                    "Resource name is mandatory");
            criteria.intersect(null, new TestCriteria().add(new SimpleExpression("prop", "value", Operator.EQUALS)));
        }

        @Test
        public void shouldThrowInvalidCriteriaExceptionWhenNoIntersectCriteria() {
            thrown.expect(CriteriaException.class);
            thrown.expectMessage(
                    "Intersect criteria is mandatory");
            criteria.intersect("name", null);
        }

        @Test
        public void shouldThrowCriteriaExceptionWhenNameIsEmpty() {
            thrown.expect(CriteriaException.class);
            thrown.expectMessage(
                    "Resource name should contain some value");
            criteria.intersect("", new TestCriteria().add(new SimpleExpression("prop", "value", Operator.EQUALS)));
        }

        @Test
        public void shouldThrowCriteriaExceptionWhenCriteriaIsEmpty() {
            thrown.expect(CriteriaException.class);
            thrown.expectMessage(
                    "Intersect criteria should contain any criteria");
            criteria.intersect("name", new TestCriteria());
        }
    }
}

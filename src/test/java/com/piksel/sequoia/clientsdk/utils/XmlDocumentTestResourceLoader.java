package com.piksel.sequoia.clientsdk.utils;

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

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Test resource loader capable of satisfying {@link Document} test resources.
 */
public class XmlDocumentTestResourceLoader implements TestResourceLoader {

    @Override
    public boolean canSatisfy(Field field) {
        return isDocumentType(field);
    }

    @Override
    public Object load(URL testResourceLocation,
            TestResourceContext testResourceContext) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = createDocumentBuilder(builderFactory);
        return parseFileLocation(testResourceLocation, documentBuilder);
    }

    private boolean isDocumentType(Field field) {
        return field.getType().isAssignableFrom(Document.class);
    }

    private Document parseFileLocation(URL testResourceLocation,
            DocumentBuilder documentBuilder) {
        try {
            return documentBuilder.parse(testResourceLocation.getFile());
        } catch (SAXException e) {
            throw new IllegalArgumentException(
                    "Unable to parse XML document at location: "
                            + testResourceLocation,
                    e);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Unable to read XML document at location: "
                            + testResourceLocation,
                    e);
        }
    }

    private DocumentBuilder createDocumentBuilder(
            DocumentBuilderFactory builderFactory) {
        try {
            return builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException(
                    "Unable to create XML document builder", e);
        }
    }
}

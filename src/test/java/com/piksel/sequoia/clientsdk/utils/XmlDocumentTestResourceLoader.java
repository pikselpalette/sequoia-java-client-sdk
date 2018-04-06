package com.piksel.sequoia.clientsdk.utils;

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
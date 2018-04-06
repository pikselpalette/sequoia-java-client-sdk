package com.piksel.sequoia.clientsdk.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Provides the capabilities to load a {@link Schema} as a TestResource.
 */
public class SchemaTestResourceLoader implements TestResourceLoader {

    @Override
    public boolean canSatisfy(Field field) {
        return isSchema(field);
    }

    private boolean isSchema(Field field) {
        return field.getType().isAssignableFrom(Schema.class);
    }

    @Override
    public Object load(URL testResourceLocation,
            TestResourceContext testResourceContext) {
        File schemaFile = new File(testResourceLocation.getFile());
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            return schemaFactory.newSchema(schemaFile);
        } catch (SAXException e) {
            throw new IllegalArgumentException("Failed to create schema", e);
        }
    }
}
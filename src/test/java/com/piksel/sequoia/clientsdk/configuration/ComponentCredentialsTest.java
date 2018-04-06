package com.piksel.sequoia.clientsdk.configuration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ComponentCredentialsTest {

    @Test
    public void shouldNotExposeThePasswordInToStringRepresentation(){
        
        ComponentCredentials creds = new ComponentCredentials("username1", "password1");
        
        assertThat(creds.getPassword(), is("password1"));
        
        String stringRepresentation = creds.toString();
        
        assertThat(stringRepresentation, containsString("username1"));
        assertThat(stringRepresentation, not(containsString("password1")));
    }
    
}

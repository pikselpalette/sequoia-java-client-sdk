package com.piksel.sequoia.clientsdk.resource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.piksel.sequoia.annotations.PublicEvolving;

/**
 * Marks a field should hold indirect (where the relationship is from
 * the target resource to this resource) in the response resource 
 * object.
 * 
 * <p>In the below diagram you can see that the linked resource has 
 * a reference on the field <code>fooRef</code> which refers back to 
 * the source resource.
 * <pre>
 * +--|this|------+                  +--|linked|----+
 * |ref:123       |                  |              |
 * |              |     Indirect     |              |
 * |              &lt;------------------+ fooRef:123   |
 * |              |                  |              |
 * |              |                  |              |
 * |              |                  |              |
 * +--------------+                  +--------------+
 * </pre>
 * 
 * @see DirectRelationship
 */
@PublicEvolving
@Retention(RetentionPolicy.RUNTIME)
public @interface IndirectRelationship {
    
    /**
     * The name of the related resource field name that contains the back 
     * reference to this resource.
     */
    String ref();

    /**
     * 
     */
    String relationship();
}

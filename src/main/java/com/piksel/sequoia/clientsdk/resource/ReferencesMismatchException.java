package com.piksel.sequoia.clientsdk.resource;

import com.piksel.sequoia.annotations.PublicEvolving;
import com.piksel.sequoia.clientsdk.ClientException;

/**
 * Indicates that an update operation was attempted with a reference that 
 * does not match the payload.
 */
@PublicEvolving
public class ReferencesMismatchException extends ClientException {

    private static final long serialVersionUID = 2790753074429742887L;

    private ReferencesMismatchException(String message) {
        super(message);
    }

    /**
     * Create the exception providing the reference used for the update, and the 
     * reference found on the resource.
     */
    public static ReferencesMismatchException thrown(String referenceToUpdate, String resourceReference) {
        return new ReferencesMismatchException(String.format(
                "Reference to update %s doesn't match with the resource reference %s.",
                referenceToUpdate, resourceReference));
    }

}

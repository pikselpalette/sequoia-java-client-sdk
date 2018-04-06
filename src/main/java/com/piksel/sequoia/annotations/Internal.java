package com.piksel.sequoia.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Interface to mark methods within stable, public APIs as an internal developer API.
 *
 * <p>Developer APIs are stable but internal and could change across point
 * releases.
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Public
public @interface Internal {
}
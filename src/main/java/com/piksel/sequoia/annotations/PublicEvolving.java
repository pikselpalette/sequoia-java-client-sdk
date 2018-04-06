package com.piksel.sequoia.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes and methods for public use, but with evolving interfaces.
 *
 * <p>Classes and methods with this annotation are intended for public use and have stable behaviour.
 * However, their interfaces and signatures are not considered to be stable and might be changed
 * across versions.
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR })
@Public
public @interface PublicEvolving {}
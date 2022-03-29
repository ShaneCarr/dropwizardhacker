package org.microsoft.shcarr.security;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// lifting this from dropwizard as an example
// this is the injectible param/annotation ergo @Auth
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface Auth {
    boolean required() default true;
}


/*
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface TokenParam {
    boolean someAttribute() default true;
}

 */
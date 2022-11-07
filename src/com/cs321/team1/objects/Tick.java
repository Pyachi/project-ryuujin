package com.cs321.team1.objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark methods as tick-able.
 * Tick-able methods run once per tick in priority order
 * By convention, all tick-able methods are `public void`,
 * but should never be invoked manually.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tick {
    int priority() default -1;
}

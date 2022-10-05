package com.cs321.team1.objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to mark methods that should run every tick.
 * Priority determines order methods are ran in, lower values run first.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tick {
    int priority() default -1;
}

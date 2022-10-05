package com.cs321.team1.objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark methods that should run every tick
 * Priority determines run order, lower values first
 * Doesn't do anything if used on a method outside GameObject and its subclasses
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tick {
    int priority() default -1;
}

package it.areson.interdimension.commands;

import java.lang.annotation.*;

/**
 * Command name
 *
 * eg. "create chest"
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AresonCommand {
    String value();
}

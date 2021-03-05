package it.areson.interdimension.commands;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface AresonCommand {
    String value();
}

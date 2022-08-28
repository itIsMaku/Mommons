package cz.maku.mommons.worker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BukkitCommand {

    String value();

    String description() default "Another command created by mommons";

    String usage() default "/command";

    String[] aliases() default {};

    String fallbackPrefix() default "mommons";
}

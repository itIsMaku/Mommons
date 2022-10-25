package cz.maku.mommons.worker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BungeeCommand {

    String value();

    String description() default "Another command created with mommons";

    String permission() default "";

    String usage() default "/command";

    String[] aliases() default {};

    String fallbackPrefix() default "mommons";
}

package cz.maku.mommons.worker.annotation.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Download {

    String table();

    String query();

    long period() default 1L;

    long delay() default 0;

}

package cz.maku.mommons.ef.annotation;

import cz.maku.mommons.ef.Repositories;
import cz.maku.mommons.ef.entity.NamePolicy;
import cz.maku.mommons.ef.repository.DefaultRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {

    String name() default "none";

    NamePolicy namePolicy() default NamePolicy.SQL;

    Class<?> repositoryClass() default DefaultRepository.class;
}

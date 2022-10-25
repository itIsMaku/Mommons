package cz.maku.mommons.worker.annotation;

import net.md_5.bungee.api.plugin.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BungeeEvent {

    Class<? extends Event> value();

}

package cz.maku.mommons.worker.annotation;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BukkitEvent {

    Class<? extends Event> value();

    EventPriority priority() default EventPriority.NORMAL;

}

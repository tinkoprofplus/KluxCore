package tech.klux.kluxcore.eventbus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    Event.EventPriority priority()
            default Event.EventPriority.NORMAL;
   }


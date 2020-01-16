package logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import java.util.stream.Collectors

/**
 * Holds the log event context for ThreadLocal.
 * @author Koert Zeilstra
 */
class LogContext<E>(val maxContextSize: Int, val maxEventAge: Int) {
    var events: MutableList<E> = ArrayList<E>()

    fun add(event: E) {
        if (events.size > maxContextSize) {
            val minTimestamp = System.currentTimeMillis() - maxEventAge;
            events = events.stream()
                    .filter {eventItem ->
                        if (eventItem is ILoggingEvent) {
                            eventItem.getTimeStamp() > minTimestamp
                        } else {
                            true
                        }
                    }
                    .collect(Collectors.toList())
        }
        events.add(event)
    }

    /**
     * Clear the stored events.
     */
    fun clearLog() {
        events.clear()
    }

    fun appendTo(appender: Appender<ILoggingEvent>) {
        for (event in events) {
            if (event is ILoggingEvent) {
                appender.doAppend(event as ILoggingEvent)
            }
        }
    }
}

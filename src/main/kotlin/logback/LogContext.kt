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
            garbageCollect2()
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

    /**
     * Garbage collection base on age.
     */
    private fun garbageCollect1() {
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

    /**
     * Garbage collection base on cutting the list in half.
     */
    private fun garbageCollect2() {
        if (events.size > 0) {
            events = events.subList(events.size/2, events.size)
        }
    }
}

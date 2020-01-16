package logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender

/**
 *
 * @author Koert Zeilstra
 */
class LogContext<E> {
    val events = ArrayList<E>()

    fun add(event: E) {
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

package logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.AppenderBase
import org.slf4j.LoggerFactory

/**
 * Logback appender keeps events in memory (per thread), writes these to errorLogger/errorAppender when an error is logged.
 * @author Koert Zeilstra
 */
class LogContextAppender<E> : AppenderBase<E>() {
    private var logContextThreadLocal: ThreadLocal<LogContext<E>> = ThreadLocal()
    private var errorLogger = "incidentLogger"
    private var errorAppender = "incidentAppender"
    var maxContextSize: Int = 50; // maximum number of events in log context
    var maxEventAge: Int = 30 * 1000; // cut off age (in milliseconds) of events when maxContextSize is reached

    /**
     * @param errorLogger Name of logger where events will be logged, if ERROR event is received.
     */
    fun setErrorLogger(errorLogger: String) {
        this.errorLogger = errorLogger
    }

    /**
     * @param errorAppender Name of appender (contained in errorLogger) where events will be logged, if ERROR event is received.
     */
    fun setErrorAppender(errorAppender: String) {
        this.errorAppender = errorAppender
    }

    override fun append(event: E) {
        val logContext = getLogContext()
        if (event is ILoggingEvent) {
            val loggingEvent = event as ILoggingEvent
            val marker = loggingEvent.marker
            if (marker != null && marker.contains(LogContextAppender.MARKER_RESET_SESSION)) {
                logContext.clearLog()
            }
            logContext.add(event)
            if (loggingEvent.level.isGreaterOrEqual(Level.ERROR)) {
                writeLogToIncidentLogger()
                logContext.clearLog()
            }
        } else {
            logContext.add(event)
        }
    }

    /**
     * Write all stored events to errorLogger/errorAppender.
     */
    private fun writeLogToIncidentLogger() {
        val incidentLogger = LoggerFactory.getLogger(errorLogger)
        if (incidentLogger != null) {
            val incidentAppender = (incidentLogger as Logger).getAppender(errorAppender)
            if (incidentAppender != null) {
                val logContext = getLogContext()
                val event0: ILoggingEvent = LoggingEvent(LogContextAppender::class.java.canonicalName, incidentLogger,
                        Level.DEBUG, "Start error dump", null, null)
                incidentAppender.doAppend(event0)
                logContext.appendTo(incidentAppender)
            }
        }
    }

    private fun getLogContext(): LogContext<E> {
        if (logContextThreadLocal.get() == null) {
            logContextThreadLocal.set(LogContext<E>(maxContextSize, maxEventAge))
        }
        return logContextThreadLocal.get()
    }

    companion object {
        const val MARKER_RESET_SESSION = "resetSession"
    }
}

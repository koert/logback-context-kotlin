package example

import logback.LogContextAppender
import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory

/**
 * Test LogContextAppender.
 * @author Koert Zeilstra
 */
fun main() {
    val log = LoggerFactory.getLogger(LogContextAppender::class.java);

    log.debug("debug 1");
    log.info("info 1");
    log.debug(MarkerFactory.getMarker("resetSession"), "reset log context session");
    log.debug("debug 2");
    log.info("info 2");
    log.error("error 2");
}


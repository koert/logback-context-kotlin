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
    log.debug("debug 2a");
    log.debug("debug 2b");
    log.info("info 2a");
    log.info("info 2b");
    log.error("error 2");
}


# j-logger
* Implement slf4j-api
* Support sync and async printer
* Customizable print
  * TextPrinter()
  * GsonPrinter()
  * JacksonPrinter()
* Support LogContext
  * GlobalLogContext
  * ThreadLogContext
  * CoroutineLogContext

## Install
### Gradle Project
1. Add dependency
    ```
    build.gradle.kts
   
    implementation("kr.jadekim:j-logger:$jLoggerVersion")
    ```
## How to use
### Configuration
```
JLog.addPrinter(TextPrinter())
JLog.addAsyncPrinter(GsonPrinter())

JLog.autoClassNamer() // JLog.addInterceptor(ClassLoggerAutoNamer(32))

JLog.defaultLoggerLevel = Level.TRACE
```
### Create logger
```
val logger = JLog.get("loggerName")
val logger = JLog.get(A::class)
val logger = JLog.get(A::class.java)
```
### Logging
```
logger.trace("trace log", extra = mapOf())
logger.debug("debug log", extra = mapOf())
logger.info("info log", extra = mapOf())
logger.warning("warning log", extra = mapOf())
logger.error("error log", extra = mapOf())

try {
    //occur exception
} catch (e: Exception) {
    logger.error("Occur Exception", throwable = e, extra = mapOf())
}
```
### Log with context
```
GlobalLogContext["globalContext"] = "global context"

ThreadLogContext["threadLocalContext"] = "thread local context"

val logContext = CoroutineLogContext().apply {
    set("coroutineContext", "coroutine context")
}
withContext(logContext) {
    logger.sTrace("trace log", extra = mapOf())
    logger.sDebug("debug log", extra = mapOf())
    logger.sInfo("info log", extra = mapOf())
    logger.sWarning("warning log", extra = mapOf())
    logger.sError("error log", extra = mapOf())
}
```
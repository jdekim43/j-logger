# j-logger
[![Download](https://api.bintray.com/packages/jdekim43/maven/j-logger/images/download.svg)](https://bintray.com/jdekim43/maven/j-logger/_latestVersion)
* Implemented slf4j-api
* Support sync and async printer
* Customizable print
  * TextPrinter()
  * GsonPrinter()
  * JacksonPrinter()
* Usable LogContext
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
logger.trace("trace log", meta = mapOf())
logger.debug("debug log", meta = mapOf())
logger.info("info log", meta = mapOf())
logger.warning("warning log", meta = mapOf())
logger.error("error log", meta = mapOf())

try {
    //occur exception
} catch (e: Exception) {
    logger.error("Occur Exception", throwable = e, meta = mapOf())
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
    logger.sTrace("trace log", meta = mapOf())
    logger.sDebug("debug log", meta = mapOf())
    logger.sInfo("info log", meta = mapOf())
    logger.sWarning("warning log", meta = mapOf())
    logger.sError("error log", meta = mapOf())
}
```

## Printers
### TextPrinter
Print logs in text type. `DefaultLogFormatter` does not print `logContext`, and `meta` converts using toString().
#### Constructor Parameter
* formatter: LogFormatter = DefaultLogFormatter
* output: OutputStream = System.out
* printStackTrace: Boolean = true
### GsonPrinter & JacksonPrinter
로그를 Json 형식으로 출력합니다. [Log 클래스](https://github.com/jdekim43/j-logger/blob/master/src/main/kotlin/kr/jadekim/logger/model/Log.kt) 를 Gson 혹은 Jackson 을 사용하여 json 으로 변환하여 출력합니다.
Date 형식이나 meta 와 logContext 의 Serializer 를 변경하고 싶은 경우 Gson 와 ObjectMapper 변경을 통해 설정할 수 있습니다.
#### Constructor Parameter
* gson: Gson = Gson  //or mapper: ObjectMapper = jacksonObjectMapper()
* output: OutputStream = System.out
* printStackTrace: Boolean = true
* traceMaxLength: Int = 12
* useCustomDateSerializer: Boolean = false
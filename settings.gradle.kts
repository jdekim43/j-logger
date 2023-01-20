rootProject.name = "j-logger"

fun use(name: String) {
    include(name)
    project(":$name").name = "${rootProject.name}-$name"
}

use("coroutine")
use("gson")
use("jackson")
use("koin")
use("ktor")
use("okhttp")
use("sentry")
use("slf4j")

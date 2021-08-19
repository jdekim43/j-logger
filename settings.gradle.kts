rootProject.name = "j-logger"

fun use(name: String) {
    include(name)
    project(":$name").name = "${rootProject.name}-$name"
}

use("coroutine")
use("fuel")
use("gson")
use("jackson")
use("koin")
use("ktor")
use("okhttp")
use("slf4j")

package kr.jadekim.logger

import kotlin.reflect.KClass

fun JLog.get(klass: KClass<*>) = get(klass.qualifiedName ?: klass.java.canonicalName)

fun JLog.get(clazz: Class<*>) = get(clazz.canonicalName)
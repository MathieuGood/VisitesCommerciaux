package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

fun <R : Any> R.lazyLogger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(getClassForLogging(this.javaClass).name) }
}

fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}


/*
Exemple 1 : depuis https://stackoverflow.com/questions/34416869/idiomatic-way-of-logging-in-kotlin

// unwrap companion class to enclosing class given a Java Class
fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
    } ?: ofClass
}

fun <R : Any> R.lazyLogger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass).name) }
}
*/


/*
Exemple 2 : depuis https://www.baeldung.com/kotlin/logging

inline fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}

class LoggerDelegate<in R : Any> : ReadOnlyProperty<R, Logger> {
    override fun getValue(thisRef: R, property: KProperty<*>)
            = LoggerFactory.getLogger(getClassForLogging(thisRef.javaClass))
}
 */
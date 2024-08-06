package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties.Companion.LOG
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * La classe `LectureApplicationProperties` est un composant Spring qui gère la lecture des propriétés de l'application.
 *
 * Elle contient les propriétés et méthodes suivantes :
 * @property properties Les propriétés de l'application.
 * @property LOG Le logger utilisé pour enregistrer les informations de débogage.
 * @method onApplicationReadyEvent Méthode appelée lors de l'événement `ApplicationReadyEvent`. Elle imprime toutes les propriétés de l'application.
 * @method printProperties Méthode privée qui imprime les propriétés d'une instance donnée.
 */
@Component
class LectureApplicationProperties(val properties: ApplicationProperties) {

    companion object {
        val LOG by lazyLogger()
    }

    /**
     * Méthode appelée lors de l'événement `ApplicationReadyEvent`.
     * Elle affiche dans les logs toutes les propriétés de l'application.
     */
    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReadyEvent() {
        LOG.debug("Récupération de toutes les propriétés de l'application :")
        printProperties("MainProperties", properties.main)
        printProperties("DemoVendeur", properties.demoVendeur)
        printProperties("DemoResponsable", properties.demoResponsable)
        printProperties("VueSaisieVisiteProperties", properties.vueSaisieVisite)
        printProperties("VueGridVisitesProperties", properties.vueGridVisites)
    }

    /**
     * Méthode privée qui imprime les propriétés d'une instance donnée.
     * @param name Le nom de l'instance.
     * @param propertiesInstance L'instance dont les propriétés doivent être imprimées.
     */
    private fun printProperties(name: String, propertiesInstance: Any) {
        LOG.debug("--> $name")
        propertiesInstance.javaClass.declaredFields.forEach {
            it.isAccessible = true
            LOG.debug("${it.name} : ${it.get(propertiesInstance)}")
        }
    }
}
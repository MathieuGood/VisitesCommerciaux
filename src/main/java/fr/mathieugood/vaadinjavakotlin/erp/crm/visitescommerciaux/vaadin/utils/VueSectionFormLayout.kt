package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Une classe qui étend `FormLayout` de Vaadin pour gérer une section de formulaire avec des fonctionnalités supplémentaires.
 * Cette classe permet de gérer un objet édité, des callbacks de focus et de changement de valeur, et une liste de composants.
 * @param T Le type de l'objet édité dans cette section de formulaire.
 */
open class VueSectionFormLayout<T> : FormLayout() {

    /**
     * L'objet actuellement édité dans cette section de formulaire.
     * Lorsque cet objet est défini, la méthode `onSetObjetEdite` est appelée avec la nouvelle valeur.
     */
    var objetEdite: T? = null
        set(value) {
            field = objetEdite
            onSetObjetEdite(value)
        }

    /**
     * Un callback qui est appelé lorsqu'un composant de la section de formulaire reçoit le focus ou change de valeur.
     * Ce callback prend en paramètres la source du callback (cette instance de `VueSectionFormLayout`) et l'événement de focus ou de changement de valeur.
     */
    var focusValueListenerCallback: (VueSectionFormLayout<T>, Any) -> Unit = { source, event -> }

    private val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)
    var champs: List<Component> = listOf()
    var initialisationEnCours: Boolean = false

    companion object {
        val LOG by lazyLogger()
    }

    open fun onSetObjetEdite(objetEdite: T?) {
    }

    /**
     * Ajoute une liste de composants à la section de formulaire avec un listener de focus ou de changement de valeur.
     * Cette méthode parcourt chaque composant dans la liste donnée, configure un listener de focus ou de changement de valeur sur le composant, puis l'ajoute à la section de formulaire.
     * Le listener de focus ou de changement de valeur appelle le callback `focusValueListenerCallback` avec cette instance de `VueSectionFormLayout` et l'événement de focus ou de changement de valeur.
     * @param components Les composants à ajouter à la section de formulaire. Peut contenir des éléments null.
     */
    fun addWithFocusOrValueListener(vararg components: Component) {
        components.forEach { component ->
            configFocusOrValueListener(component)
            add(component)
        }
    }

    /**
     * Configure un listener de focus ou de changement de valeur sur le composant donné.
     * Cette méthode vérifie le type du composant et ajoute un listener approprié.
     * Si le composant est `Focusable`, un listener de focus est ajouté.
     * Si le composant est un `RadioButtonGroup`, un listener de changement de valeur est ajouté.
     * Si le composant n'est ni `Focusable` ni un `RadioButtonGroup`, cette méthode est appelée récursivement sur tous les enfants du composant.
     * Le listener appelle le callback `focusValueListenerCallback` avec cette instance de `VueSectionFormLayout` et l'événement de focus ou de changement de valeur.
     * @param component Le composant sur lequel configurer le listener. Ne doit pas être null.
     */
    private fun configFocusOrValueListener(component: Component) {
        logger.trace("Ajout composant avec listener : {}", component)
        when (component) {
            is Focusable<*> -> {
                component.addFocusListener { e -> focusValueListenerCallback(this, e) }
                logger.trace("--> FOCUS Listener ajouté sur {}", component)
            }

            is RadioButtonGroup<*> -> {
                logger.trace("--> VALUECHANGE Listener ajouté sur {}", component)
                component.addValueChangeListener { e -> focusValueListenerCallback(this, e) }

            }

            else -> {
                component.children.forEach { child ->
                    configFocusOrValueListener(child)
                }
            }
        }
    }
}
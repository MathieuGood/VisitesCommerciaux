package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.GestionTaillePolice
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.ParametresAccessibilite


/**
 * Cette classe représente un bouton pour ajuster la taille de la police dans l'application.
 * Elle hérite de HorizontalLayout et contient deux boutons, un pour augmenter la taille de la police et un pour la réduire.
 *
 * @property parametresAccessibilite Les paramètres d'accessibilité de l'application contenant la valeur du niveau de police actuel.
 * @property gestionTaillePolice L'objet responsable de la gestion de la taille de la police dans l'application.
 * @property augmenterTailleBtn Le bouton pour augmenter la taille de la police.
 * @property reduireTailleBtn Le bouton pour réduire la taille de la police.
 */
class TaillePoliceButton(
    private val parametresAccessibilite: ParametresAccessibilite, private val gestionTaillePolice: GestionTaillePolice
) : HorizontalLayout() {

    private val augmenterTailleBtn: Button = Button()
    private val reduireTailleBtn: Button = Button()


    init {
        configLayout()
        configBtn()
        add(reduireTailleBtn, augmenterTailleBtn)
    }

    private fun configLayout() {
        isPadding = false
        isMargin = false
        minWidth = "0"
        width = "100%"
        style.set("gap", "0.5em")
    }

    private fun configBtn() {
        augmenterTailleBtn.text = "A+"
        augmenterTailleBtn.style.set("flex", "1")
        augmenterTailleBtn.addClickListener {
            gestionTaillePolice.changerTaillePolice(parametresAccessibilite.niveauTaillePolice + 1)
        }

        reduireTailleBtn.text = "a-"
        reduireTailleBtn.style.set("flex", "1")
        reduireTailleBtn.addClickListener {
            gestionTaillePolice.changerTaillePolice(parametresAccessibilite.niveauTaillePolice - 1)
        }
    }
}
package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils

import com.vaadin.flow.component.UI
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.ParametresAccessibilite

/**
 * Cette classe est responsable de la gestion de la taille de la police dans l'application.
 *
 * @property parametresAccessibilite Les paramètres d'accessibilité actuels de l'application.
 */
class GestionTaillePolice(private var parametresAccessibilite: ParametresAccessibilite) {

    /**
     * Cette méthode valide le niveau de la taille de la police.
     * Si la nouvelle taille de la police est supérieure à 7, elle retourne 7.
     * Si la nouvelle taille de la police est inférieure à 0, elle retourne 0.
     * Sinon, elle retourne la nouvelle taille de la police.
     *
     * @param nouvelleTaillePolice La nouvelle taille de la police à valider.
     * @return Le niveau de la taille de la police validé.
     */
    private fun validerNiveauTaillePolice(nouvelleTaillePolice: Int): Int {
        return when {
            nouvelleTaillePolice > 7 -> 7
            nouvelleTaillePolice < 0 -> 0
            else -> nouvelleTaillePolice
        }
    }

    /**
     * Cette méthode change la taille de la police dans l'application.
     * Elle met à jour le niveau de la taille de la police dans les paramètres d'accessibilité et change la taille de la police sur la page actuelle.
     *
     * @param nouveauNiveauTaillePolice Le nouveau niveau de la taille de la police.
     */
    fun changerTaillePolice(nouveauNiveauTaillePolice: Int) {
        parametresAccessibilite.niveauTaillePolice = validerNiveauTaillePolice(nouveauNiveauTaillePolice)
        val fontSize = when (parametresAccessibilite.niveauTaillePolice) {
            0 -> "var(--lumo-font-size-xxs)"
            1 -> "var(--lumo-font-size-xs)"
            2 -> "var(--lumo-font-size-s)"
            3 -> "var(--lumo-font-size-m)"
            4 -> "var(--lumo-font-size-l)"
            5 -> "var(--lumo-font-size-xl)"
            6 -> "var(--lumo-font-size-xxl)"
            7 -> "var(--lumo-font-size-xxxl)"
            else -> "var(--lumo-font-size-m)"
        }
        UI.getCurrent().page.executeJs("let html = document.querySelector('html');html.style.setProperty('font-size', '$fontSize');")
    }
}
package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.VaadinIcon

/**
 * Cette classe `StatutValidationBadge` est une extension de la classe `Span` de Vaadin.
 * Elle est utilisée pour créer un badge indiquant le statut de validation d'une entité.
 *
 * Elle prend trois paramètres :
 * - `statutValidation` qui est le statut de validation de la visite. Il est de type `StatutValidation` et a une valeur par défaut de `StatutValidation.ENATTENTE`.
 * - `userValidationVisite` qui est le nom de l'utilisateur qui a validé la visite. Il est de type `String?` et peut être null.
 * - `dateValidationVisite` qui est la date de validation de la visite. Elle est de type `String?` et peut être null.
 *
 * @param statutValidation Le statut de validation de la visite.
 * @param userValidationVisite Le nom de l'utilisateur qui a validé la visite.
 * @param dateValidationVisite La date de validation de la visite.
 */
class StatutValidationBadge(
    statutValidation: StatutValidation = StatutValidation.ENATTENTE,
    userValidationVisite: String? = null,
    dateValidationVisite: String? = null
) : Span() {

    init {

        val dateValidationVisiteFormatee = dateValidationVisite?.let {
            val dateParts = it.split("-")
            "${dateParts[2]}/${dateParts[1]}/${dateParts[0]}"
        }
        when (statutValidation) {
            StatutValidation.ENATTENTE -> {
                configBadge(VaadinIcon.CLOCK, "badge", "En attente validation")
            }

            StatutValidation.VALIDE -> {
                configBadge(VaadinIcon.CHECK, "badge success", "$userValidationVisite le $dateValidationVisiteFormatee")
            }
        }
    }

    private fun configBadge(iconeVaadin: VaadinIcon, vaadinTheme: String, texte: String) {
        val icone = iconeVaadin.create()
        icone.style.set("padding", "var(--lumo-space-xs")
        val texteSpan = Span(texte)
        texteSpan.style.set("font-size", "0.85em")
        this.add(icone, texteSpan)
        this.element.themeList.add(vaadinTheme)
    }
}

enum class StatutValidation {
    ENATTENTE, VALIDE
}
package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.icon.Icon
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite

/**
 * Cette classe `EditerVisiteIcon` étend `Icon`.
 * Elle représente une icône d'édition pour une `Visite`.
 *
 * @param visite La `Visite` pour laquelle cette icône d'édition est créée.
 * @param fontSize La taille de la police pour cette icône. Par défaut, elle est de 1.0.
 */
class EditerVisiteIcon(visite: Visite, fontSize: Double = 1.0) : Icon("lumo", "edit") {
    init {
        this.apply {
            style.set("padding", "0px")
            style.set("font-size", "${fontSize}em")
            style.set("cursor", "pointer")
            addClickListener {
                UI.getCurrent().navigate("saisie-visite/${visite.id}")
            }
        }

    }


}
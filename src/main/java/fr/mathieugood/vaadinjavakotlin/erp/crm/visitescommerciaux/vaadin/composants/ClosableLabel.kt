package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.html.Span
import com.vaadin.flow.theme.lumo.LumoUtility

/**
 * Cette classe `ClosableLabel` étend `Span`.
 * Elle représente un label qui peut être fermé par l'utilisateur (désactivation de sa visibilité).
 *
 * @param text Le texte à afficher dans le label.
 */
class ClosableLabel(text: String) : Span() {

    var closeLabelCallback: () -> Unit = {}

    init {
        val label = Span(text)
        val btnClose = Span("X")
        btnClose.addClickListener {
            closeLabelCallback()
            this.isVisible = false
        }

        label.element.style.set("cursor", "default")
        label.addClassName(LumoUtility.Margin.Horizontal.SMALL)
        label.addClassName(LumoUtility.FontSize.SMALL)

        btnClose.element.style.set("cursor", "pointer")
        btnClose.addClassName(LumoUtility.FontSize.SMALL)
        btnClose.addClassName(LumoUtility.FontWeight.BOLD)
        btnClose.addClassName(LumoUtility.Background.TRANSPARENT)
        btnClose.addClassName(LumoUtility.Border.NONE)
        btnClose.addClassName(LumoUtility.TextColor.PRIMARY_CONTRAST)
        btnClose.addClassName(LumoUtility.Padding.NONE)
        btnClose.addClassName(LumoUtility.Margin.End.SMALL)

        style.setHeight("36px")
        addClassName(LumoUtility.Display.FLEX)
        addClassName(LumoUtility.AlignItems.CENTER)
        addClassName(LumoUtility.AlignContent.CENTER)
        addClassName(LumoUtility.Margin.XSMALL)
        addClassName(LumoUtility.Background.PRIMARY)
        addClassName(LumoUtility.TextColor.PRIMARY_CONTRAST)
        addClassName(LumoUtility.BorderRadius.MEDIUM)

        add(label, btnClose)
    }
}
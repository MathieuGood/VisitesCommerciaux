package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.ClientOuProspect

/**
 * Cette classe `ContactCard` étend `HorizontalLayout`.
 * Elle représente une carte de contact pour un `ClientOuProspect`.
 *
 * @property contact Le `ClientOuProspect` pour lequel cette carte de contact est créée.
 * @property selectionCallback Un callback qui est appelé lorsque l'utilisateur sélectionne cette carte de contact.
 * @property modificationCallback Un callback qui est appelé lorsque l'utilisateur modifie cette carte de contact.
 */
class ContactCard(
    private val contact: ClientOuProspect,
    private val selectionCallback: () -> Unit = {},
    private val modificationCallback: () -> Unit = {}
) : HorizontalLayout() {

    private val nom = Span()
    private val adresse1 = Span()
    private val adresse2 = Span()
    private val codePostalVille = Span()
    private val infosContactLayout = VerticalLayout()

    private val btnSelectionner = Button()
    private val btnModifier = Button()
    private val btnLayout = VerticalLayout()


    init {
        addClassName("contact-card")
        configComposants()

        infosContactLayout.add(nom, adresse1, adresse2, codePostalVille)
        btnLayout.add(btnModifier, btnSelectionner)
        add(infosContactLayout, btnLayout)
    }

    private fun configComposants() {
        nom.text = "${contact.nom}"
        adresse1.text = "${contact.adresse1}"
        adresse2.text = "${contact.adresse2}"
        codePostalVille.text = "${contact.codePostal} ${contact.ville}"

        nom.style.apply {
            set("font-weight", "bold")
            set("color", "green")
            set("line-height", "1.2")
            set("margin-bottom", "1px")
        }

        adresse1.style.apply {
            set("font-size", "0.8em")
            set("line-height", "1.2")
            set("margin", "1px 0")
        }
        adresse2.style.apply {
            set("font-size", "0.8em")
            set("line-height", "1.2")
            set("margin", "1px 0")
        }
        codePostalVille.style.apply {
            set("font-size", "0.8em")
            set("line-height", "1.2")
            set("margin-top", "1px")
        }

        configBtn(btnModifier, ButtonVariant.LUMO_CONTRAST)
        btnModifier.text = "Modifier"
        btnModifier.addClickListener { modificationCallback() }

        configBtn(btnSelectionner, ButtonVariant.LUMO_SUCCESS)
        btnSelectionner.text = "Sélectionner"
        btnSelectionner.addClickListener { selectionCallback() }

        infosContactLayout.justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        infosContactLayout.style.set("margin-left", "5px")
        configVerticalLayout(infosContactLayout)

        btnLayout.justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        configVerticalLayout(btnLayout)
        btnLayout.width = "110px"

        style.apply {
            set("border-radius", "5px")
            set("border", "1px solid #e0e0e0")
            set("padding", "5px 5px")
            set("margin", "2px")
            set("margin-right", "7px")
        }
    }

    private fun configBtn(button: Button, theme: ButtonVariant) {
        button.width = "110px"
        button.height = "30px"
        button.addThemeVariants(theme)
        button.style.apply {
            set("padding", "0 8px")
            set("font-size", "0.8em")
            set("margin", "2px 0")
        }
    }

    private fun configVerticalLayout(layout: VerticalLayout) {
        layout.style.apply {
            set("padding", "0")
            set("gap", "0")
        }
    }

}
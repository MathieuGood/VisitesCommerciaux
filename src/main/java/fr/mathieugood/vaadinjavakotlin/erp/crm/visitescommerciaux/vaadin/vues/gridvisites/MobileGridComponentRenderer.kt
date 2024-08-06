package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.gridvisites

import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.*
import java.time.format.DateTimeFormatter

/**
 * Une classe qui représente un renderer de composant pour une grille mobile.
 * Ce composant personnalisé est utilisé pour génére une cellule de grille pour une `Visite` comprenant toutes les informations voulues.
 * Cette classe étend `ComponentRenderer` de Vaadin et est utilisée pour afficher des informations sur une `Visite` dans une grille mobile.
 * Le renderer crée un `HorizontalLayout` pour chaque `Visite` qui contient des informations sur la visite.
 * Deux variables privées, `estResponsable` et `validationVisiteActive`, sont utilisées pour contrôler l'affichage de certaines informations.
 * @param estResponsable Un booléen qui indique si l'utilisateur est responsable. Si `false`, le nom du vendeur n'est pas affiché.
 * @param validationVisiteActive Un booléen qui indique si la validation de la visite est active. Si `false`, le statut de validation n'est pas affiché.
 */
class MobileGridComponentRenderer(
    private val estResponsable: Boolean,
    private val validationVisiteActive: Boolean,
) : ComponentRenderer<HorizontalLayout, Visite>({ visite ->
    val dateVisite = Span("${visite.date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
    val typeContactVisite =
        Span(if (visite.typeContact == "Client") TypeContactBadge(TypeContact.CLIENT) else TypeContactBadge(TypeContact.PROSPECT))
    val contactVisite = Span("${visite.client?.nom ?: visite.prospect?.nom}")
    val familleContactVisite = Span("${visite.client?.famille?.libelle ?: visite.prospect?.famille?.libelle}")
    val vendeurVisite = Span("${visite.vendeur1?.nom}")
    val validationVisite = Span(
        if (visite.dateValidation != null) {
            StatutValidationBadge(
                StatutValidation.VALIDE, visite.userValidation, visite.dateValidation.toString()
            )
        } else {
            StatutValidationBadge(StatutValidation.ENATTENTE, null, null)
        }
    )
    val iconeEditerVisite = EditerVisiteIcon(visite, 2.0)

    if (!estResponsable) vendeurVisite.isVisible = false
    if (!validationVisiteActive) validationVisite.isVisible = false

    // Difficulté à mettre en place une ellipse sur les champs le plus à droite de la colonne 1
    HorizontalLayout(VerticalLayout(vendeurVisite.apply { style.set("font-style", "italic") },
        HorizontalLayout(dateVisite.apply {
            style.set("font-size", "1em")
            style.set("font-weight", "bold")
        }, contactVisite.apply {
            style.set("font-weight", "bold")
            // Non-fonctionnel
            //style.set("white-space", "nowrap")
            //style.set("overflow", "hidden")
            //style.set("text-overflow", "ellipsis")
        }).apply {
            addClassName("ligne1")
            style.set("align-items", "center")
            //setWidthFull()
        },
        HorizontalLayout(typeContactVisite, familleContactVisite.apply {
            // Non-fonctionnel
            //style.set("white-space", "nowrap")
            //style.set("overflow", "hidden")
            //style.set("text-overflow", "ellipsis")
            style.set("font-style", "italic")
        }).apply {
            addClassName("ligne2")
            setWidthFull()
        },
        validationVisite.apply {
            addClassName("ligne3")
            // Non-fonctionnel
            //setWidthFull()
            //style.set("white-space", "nowrap")
            //style.set("overflow", "hidden")
            //style.set("text-overflow", "ellipsis")
        }).apply {
        addClassName("colonne1")
        style.set("row-gap", "0.5em")
        //style.set("flex-grow", "1")
    }, VerticalLayout(
        iconeEditerVisite
    ).apply {
        addClassName("colonne2")
        style.set("align-self", "center")
        style.set("align-items", "center")
        width = "45px"
        // Non-fonctionnel
        //style.set("flex-basis", "45px")
        //style.set("flex-grow", "0")
        //style.set("flex-shrink", "1")
    }).apply {
        style.set("gap", "0")
    }
})
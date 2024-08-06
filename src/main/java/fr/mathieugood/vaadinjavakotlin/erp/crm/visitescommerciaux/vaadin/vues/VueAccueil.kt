package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.gridvisites.FiltresGridVisites
import jakarta.annotation.security.PermitAll
import org.springframework.beans.factory.annotation.Autowired

/**
 * VueAccueil est une classe qui représente la vue d'accueil de l'application CRM Visites.
 * Elle hérite de VerticalLayout et contient plusieurs boutons pour naviguer vers d'autres vues.
 *
 * @property filtresGridVisites Les filtres utilisés pour la grille des visites.
 *
 * @constructor Crée une nouvelle instance de VueAccueil avec les filtres spécifiés pour la grille des visites.
 * @param filtresGridVisites Les filtres à utiliser pour la grille des visites.
 *
 * @PermitAll Cette annotation indique que tous les utilisateurs sont autorisés à accéder à cette vue.
 * @Route Cette annotation définit la route pour accéder à cette vue. La valeur vide signifie que c'est la vue par défaut (vue d'accueil).
 * @PageTitle Cette annotation définit le titre de la page pour cette vue.
 */
@PermitAll
@Route(value = "", layout = TopBarLayout::class)
@PageTitle("Acceuil | CRM Visites")
class VueAccueil(@Autowired private val filtresGridVisites: FiltresGridVisites) : VerticalLayout() {

    init {
        val btnSaisieNouvelleVisite = Button()
        val ouvertureVisiteParIDLayout = HorizontalLayout()
        val btnRechercheVisites = Button()

        btnSaisieNouvelleVisite.text = "Nouvelle visite"
        btnSaisieNouvelleVisite.addClickListener { UI.getCurrent().navigate("saisie-visite/new") }

        btnRechercheVisites.text = "Afficher visites"
        btnRechercheVisites.addClickListener {
            filtresGridVisites.effacerFiltres()
            UI.getCurrent().navigate("grid-visites")
        }

        alignItems = FlexComponent.Alignment.CENTER
        add(btnRechercheVisites, btnSaisieNouvelleVisite, ouvertureVisiteParIDLayout)
    }
}
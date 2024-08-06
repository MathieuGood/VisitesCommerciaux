package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.gridvisites

import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.VaadinSessionScope
import java.time.LocalDate

/**
 * Une classe qui représente les filtres appliqués à la grille des visites dans l'interface utilisateur et qui stocke leurs valeurs.
 * Cette classe est un composant Spring et a une portée de session Vaadin, ce qui signifie qu'une instance est créée pour chaque session utilisateur.
 * Les filtres sont représentés par des variables nullable qui sont initialisées à null.
 * Une méthode `effacerFiltres` est fournie pour réinitialiser tous les filtres à null.
 */
@SpringComponent
@VaadinSessionScope
class FiltresGridVisites {
    /**
     * Le filtre pour la date de début des visites. Peut être null.
     */
    var filtreDateDebut: LocalDate? = null

    /**
     * Le filtre pour la date de fin des visites. Peut être null.
     */
    var filtreDateFin: LocalDate? = null

    /**
     * Le filtre pour la famille des visites. Peut être null.
     */
    var filtreFamille: String? = null

    /**
     * Le filtre pour le type de contact des visites. Peut être null.
     */
    var filtreTypeContact: String? = null

    /**
     * Le filtre pour le contact des visites. Peut être null.
     */
    var filtreContact: String? = null

    /**
     * Le filtre pour le vendeur des visites. Peut être null.
     */
    var filtreVendeur: String? = null

    /**
     * Le filtre pour la validation des visites. Peut être null.
     */
    var filtreValidation: String? = null

    /**
     * Réinitialise tous les filtres à null.
     */
    fun effacerFiltres() {
        filtreDateDebut = null
        filtreDateFin = null
        filtreFamille = null
        filtreTypeContact = null
        filtreContact = null
        filtreVendeur = null
        filtreValidation = null
    }
}
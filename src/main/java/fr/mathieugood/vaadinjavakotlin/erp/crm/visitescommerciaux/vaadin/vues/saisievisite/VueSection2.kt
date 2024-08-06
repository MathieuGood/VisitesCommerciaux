package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.saisievisite

import com.vaadin.flow.component.combobox.MultiSelectComboBox
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.data.binder.BeanValidationBinder
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Concurrent
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.GammeProduit
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security.InfosUtilisateur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.VueSectionFormLayout
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger

/**
 * VueSection2 est une classe qui représente la deuxième section de la vue de saisie de visite.
 * Elle hérite de VueSectionFormLayout<Visite> et contient plusieurs champs et méthodes pour gérer la saisie des informations de la visite.
 *
 * @property visiteService Le service utilisé pour interagir avec les données de visite.
 *
 * @constructor Crée une nouvelle instance de VueSection2 avec les services, le binder, le conteneur parent, et les informations de l'utilisateur spécifiés.
 * @param visiteService Le service à utiliser pour interagir avec les données de visite.
 * @param binder Le binder à utiliser pour la validation des données de la visite.
 * @param conteneurParent Le conteneur parent de cette vue.
 * @param infosUtilisateur Les informations de l'utilisateur connecté.
 */
class VueSection2(
    private val visiteService: VisiteService,
    binder: BeanValidationBinder<Visite>,
    conteneurParent: VueSaisieVisite,
    infosUtilisateur: InfosUtilisateur
) : VueSectionFormLayout<Visite>() {

    private val gammesProduits: MultiSelectComboBox<GammeProduit> = MultiSelectComboBox<GammeProduit>()
    private val concurrents: MultiSelectComboBox<Concurrent> = MultiSelectComboBox<Concurrent>()
    private val commentaires = TextArea()

    companion object {
        val LOG by lazyLogger()
    }

    init {
        champs = listOf(gammesProduits, concurrents, commentaires)
        configComposants()
        addWithFocusOrValueListener(
            gammesProduits, concurrents, commentaires
        )
        binder.bindInstanceFields(this)
    }

    private fun configComposants() {
        gammesProduits.label = ("Gammes de produits")
        gammesProduits.autoExpand = MultiSelectComboBox.AutoExpandMode.BOTH
        gammesProduits.setItems(visiteService.findAllGammeProduits())
        gammesProduits.setItemLabelGenerator { it.libelle }

        concurrents.label = "Fournisseurs actuels"
        concurrents.autoExpand = MultiSelectComboBox.AutoExpandMode.BOTH
        concurrents.setItems(visiteService.findAllConcurrents())
        concurrents.setItemLabelGenerator { it.libelle }

        commentaires.label = "Rapport de visite"
        commentaires.minHeight = "350px"
        // Decrease font size in commentaires
        commentaires.style["font-size"] = "0.9em"
    }


}
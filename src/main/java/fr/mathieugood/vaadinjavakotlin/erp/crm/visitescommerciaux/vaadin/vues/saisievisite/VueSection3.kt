package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.saisievisite

import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.MotifProchaineVisite
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Vendeur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.StatutValidation
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.StatutValidationBadge
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security.InfosUtilisateur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.VueSectionFormLayout
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import java.time.LocalDate
import java.util.*

/**
 * VueSection3 est une classe qui représente la troisième section de la vue de saisie de visite.
 * Elle hérite de VueSectionFormLayout<Visite> et contient plusieurs champs et méthodes pour gérer la saisie des informations de la visite.
 *
 * @property visiteService Le service utilisé pour interagir avec les données de visite.
 * @property binder Le binder utilisé pour la validation des données de la visite.
 *
 * @constructor Crée une nouvelle instance de VueSection3 avec les services, le binder, le conteneur parent, et les informations de l'utilisateur spécifiés.
 * @param visiteService Le service à utiliser pour interagir avec les données de visite.
 * @param binder Le binder à utiliser pour la validation des données de la visite.
 * @param conteneurParent Le conteneur parent de cette vue.
 * @param infosUtilisateur Les informations de l'utilisateur connecté.
 */
class VueSection3(
    private val visiteService: VisiteService,
    private val binder: BeanValidationBinder<Visite>,
    conteneurParent: VueSaisieVisite,
    infosUtilisateur: InfosUtilisateur
) : VueSectionFormLayout<Visite>() {

    companion object {
        val LOG by lazyLogger()
    }

    private var recontact: Checkbox = Checkbox()
    private val ligneDatesRecontact: HorizontalLayout = HorizontalLayout()

    val dateDebutRecontact = DatePicker()
    private val dateFinRecontact = DatePicker()
    private val motifProchaineVisite: ComboBox<MotifProchaineVisite> = ComboBox<MotifProchaineVisite>()
    private val vendeurProchaineVisite: ComboBox<Vendeur> = ComboBox<Vendeur>()

    private var statutValidationBadge: StatutValidationBadge? = null
    private val statutValidationLayout: HorizontalLayout = HorizontalLayout()

    init {
        champs = listOf(recontact, dateDebutRecontact, dateFinRecontact, motifProchaineVisite, vendeurProchaineVisite)

        configComposants()
        addWithFocusOrValueListener(
            recontact, ligneDatesRecontact, motifProchaineVisite, vendeurProchaineVisite, statutValidationLayout
        )
        binder.bindInstanceFields(this)
    }

    /**
     * Cette méthode est appelée lors de la mise à jour de l'objet édité.
     * Elle met à jour le badge de validation de statut en fonction de l'objet édité.
     *
     * Si l'objet édité a une validation d'utilisateur et une date de validation, un badge de validation de statut est créé avec le statut "VALIDE", l'utilisateur de validation et la date de validation.
     * Si l'objet édité n'a pas de validation d'utilisateur ou de date de validation, un badge de validation de statut vide est créé.
     *
     * Le badge de validation de statut est ensuite ajouté au layout de validation de statut.
     *
     * @param objetEdite L'objet édité. Il peut être null.
     */
    override fun onSetObjetEdite(objetEdite: Visite?) {
        super.onSetObjetEdite(objetEdite)
        statutValidationBadge = if (objetEdite?.userValidation != null && objetEdite.dateValidation != null) {
            StatutValidationBadge(
                StatutValidation.VALIDE, objetEdite.userValidation, objetEdite.dateValidation.toString()
            )
        } else {
            StatutValidationBadge()
        }
        statutValidationBadge!!.style.set("margin-top", "var(--lumo-space-s)")
        statutValidationLayout.add(statutValidationBadge)
    }

    private fun configComposants() {
        recontact.label = "Recontacter"
        // Verrouiller champs recontact par défaut et déverouillage si valeur de champRecontacter passe à "Oui"
        verrouillerChampsRecontacter()
        recontact.addValueChangeListener { verrouillerChampsRecontacter() }

        dateDebutRecontact.label = "Date début"
        dateDebutRecontact.locale = Locale.FRANCE
        dateDebutRecontact.value = LocalDate.now()
        // Nécessaire pour que le composant s'adapte à la taille de son conteneur
        dateDebutRecontact.minWidth = "0"
        dateDebutRecontact.width = "100%"
        //
        dateDebutRecontact.addFocusListener {
            val dateVisite = binder.bean.date
            if (dateVisite != null) {
                dateDebutRecontact.min = dateVisite.plusDays(1)
                if (dateDebutRecontact.value != null && !dateDebutRecontact.value.isAfter(dateVisite)) {
                    dateDebutRecontact.value = dateVisite.plusDays(1)
                }
            }
        }
        dateDebutRecontact.addValueChangeListener {
            if (initialisationEnCours) {
                return@addValueChangeListener
            }
            if (dateFinRecontact.value != null && dateDebutRecontact.value != null) {
                if (dateFinRecontact.value.isBefore(dateDebutRecontact.value)) {
                    dateFinRecontact.value = dateDebutRecontact.value
                }
            }
        }

        binder.forField(dateDebutRecontact).withValidator({
            if (recontact.value != true) {
                return@withValidator true
            }
            if (dateDebutRecontact.value == null || binder.bean.date == null) {
                return@withValidator false
            }
            return@withValidator dateDebutRecontact.value.isAfter(binder.bean.date)
        }, "doit être une date postérieure à la date de la visite")
            .bind(Visite::dateDebutRecontact) { it, value -> it.dateDebutRecontact = value }

        dateFinRecontact.label = "Date fin"
        dateFinRecontact.locale = Locale.FRANCE
        dateFinRecontact.value = LocalDate.now()
        // Nécessaire pour que le composant s'adapte à la taille de son conteneur
        dateFinRecontact.minWidth = "0"
        dateFinRecontact.width = "100%"
        //
        dateFinRecontact.addFocusListener {
            if (dateDebutRecontact.value != null) {
                dateFinRecontact.min = dateDebutRecontact.value
            }
        }
        binder.forField(dateFinRecontact).withValidator({
            if (recontact.value != true) {
                return@withValidator true
            }
            if (dateFinRecontact.value == null || dateDebutRecontact.value == null) {
                return@withValidator false
            }
            return@withValidator !dateFinRecontact.value.isBefore(dateDebutRecontact.value)
        }, "ne doit pas être une date antérieure à la date de début de recontact")
            .bind(Visite::dateFinRecontact) { it, value -> it.dateFinRecontact = value }

        ligneDatesRecontact.width = "100%"
        ligneDatesRecontact.add(dateDebutRecontact, dateFinRecontact)

        motifProchaineVisite.label = "Motif prochaine visite"
        motifProchaineVisite.width = "100%"
        motifProchaineVisite.isClearButtonVisible = true
        motifProchaineVisite.setItems(visiteService.findAllMotifsProchaineVisite())
        motifProchaineVisite.setItemLabelGenerator { it.libelle }
        binder.forField(motifProchaineVisite).withValidator({
            if (recontact.value != true) {
                return@withValidator true
            }
            return@withValidator motifProchaineVisite.value != null
        }, "doit être renseigné").bind(Visite::motifProchaineVisite) { it, value -> it.motifProchaineVisite = value }

        vendeurProchaineVisite.label = "Vendeur prochaine visite"
        vendeurProchaineVisite.width = "100%"
        vendeurProchaineVisite.isClearButtonVisible = true
        vendeurProchaineVisite.setItems(visiteService.findAllVendeurs())
        vendeurProchaineVisite.setItemLabelGenerator { it.nom }

        statutValidationLayout.setWidthFull()
        statutValidationLayout.style.set("justify-content", "center")
    }

    /**
     * Cette méthode gère le verrouillage et le déverrouillage des champs de recontact.
     * Si la valeur du champ `recontact` est vraie, les champs `dateDebutRecontact`, `dateFinRecontact`, `motifProchaineVisite` et `vendeurProchaineVisite` sont déverrouillés (leur propriété `isReadOnly` est définie sur false).
     * Si la valeur du champ `recontact` est fausse, les champs `dateDebutRecontact`, `dateFinRecontact`, `motifProchaineVisite` et `vendeurProchaineVisite` sont verrouillés (leur propriété `isReadOnly` est définie sur true) et leurs valeurs sont réinitialisées à null.
     */
    private fun verrouillerChampsRecontacter() {
        if (recontact.value) {
            dateDebutRecontact.isReadOnly = false
            dateFinRecontact.isReadOnly = false
            motifProchaineVisite.isReadOnly = false
            vendeurProchaineVisite.isReadOnly = false
        } else {
            dateDebutRecontact.isReadOnly = true
            dateFinRecontact.isReadOnly = true
            motifProchaineVisite.isReadOnly = true
            vendeurProchaineVisite.isReadOnly = true
            dateDebutRecontact.value = null
            dateFinRecontact.value = null
            motifProchaineVisite.value = null
            vendeurProchaineVisite.value = null
        }
    }

}
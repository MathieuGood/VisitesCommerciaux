package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.saisievisite

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.*
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.SaisieProspectForm
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security.InfosUtilisateur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.VueSectionFormLayout
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import java.time.LocalDate
import java.util.*

/**
 * VueSection1 est une classe qui représente la première section de la vue de saisie de visite.
 * Elle hérite de VueSectionFormLayout<Visite> et contient plusieurs champs et méthodes pour gérer la saisie des informations de la visite.
 *
 * @property visiteService Le service utilisé pour interagir avec les données de visite.
 * @property binder Le binder utilisé pour la validation des données de la visite.
 * @property conteneurParent Le conteneur parent de cette vue, qui est une instance de VueSaisieVisite.
 * @property infosUtilisateur Les informations de l'utilisateur connecté.
 * @property lectureApplicationProperties Les propriétés de l'application lues à partir du fichier de configuration.
 *
 * @constructor Crée une nouvelle instance de VueSection1 avec les services, le binder, le conteneur parent, les informations de l'utilisateur et les propriétés de l'application spécifiés.
 * @param visiteService Le service à utiliser pour interagir avec les données de visite.
 * @param binder Le binder à utiliser pour la validation des données de la visite.
 * @param conteneurParent Le conteneur parent de cette vue.
 * @param infosUtilisateur Les informations de l'utilisateur connecté.
 * @param lectureApplicationProperties Les propriétés de l'application à utiliser.
 */
class VueSection1(
    private val visiteService: VisiteService,
    private val binder: BeanValidationBinder<Visite>,
    private val conteneurParent: VueSaisieVisite,
    private val infosUtilisateur: InfosUtilisateur,
    private val lectureApplicationProperties: LectureApplicationProperties
) : VueSectionFormLayout<Visite>() {

    private val plageJoursAvantAjourdHui: Long =
        lectureApplicationProperties.properties.vueSaisieVisite.plageJoursAvantAjourdhui
    private val plageJoursApresAjourdHui: Long =
        lectureApplicationProperties.properties.vueSaisieVisite.plageJoursApresAjourdhui
    private val creationProspectActive: Boolean =
        lectureApplicationProperties.properties.vueSaisieVisite.creationProspectActive

    private val date = DatePicker()
    private val vendeur1: ComboBox<Vendeur> = ComboBox<Vendeur>()
    private val vendeur2: ComboBox<Vendeur> = ComboBox<Vendeur>()
    private val champTypeContactEtBtnNouveauProspect: HorizontalLayout = HorizontalLayout()
    private val selectClientOuProspect: ComboBox<ClientOuProspect> = ComboBox<ClientOuProspect>()
    private val detailClientOuProspect: Span = Span()
    private val btnCreerProspect: Button = Button()
    private val typeContact: RadioButtonGroup<String> = RadioButtonGroup<String>()
    private var motifVisite: ComboBox<MotifVisite> = ComboBox<MotifVisite>()

    private val nomContact = TextField()
    private val fonctionContact = TextField()

    private var saisieProspectDialog: Dialog = Dialog()
    private val saisieProspectForm = SaisieProspectForm(
        visiteService, lectureApplicationProperties, estBtnDansForm = false

    )

    companion object {
        val LOG by lazyLogger()
    }

    init {
        champs = listOf(
            date,
            vendeur1,
            vendeur2,
            typeContact,
            selectClientOuProspect,
            detailClientOuProspect,
            motifVisite,
            nomContact,
            fonctionContact
        )
        configComposants()
        configSaisieProspectDialog()
        addWithFocusOrValueListener(
            date,
            vendeur1,
            vendeur2,
            motifVisite,
            champTypeContactEtBtnNouveauProspect,
            selectClientOuProspect,
            detailClientOuProspect,
            nomContact,
            fonctionContact
        )
        binder.bindInstanceFields(this)
    }

    private fun setClientOuProspectValue(clientOuProspect: ClientOuProspect) {
        selectClientOuProspect.value = clientOuProspect
    }

    /**
     * Met à jour la liste des prospects dans le ComboBox `selectClientOuProspect`.
     * Cette méthode récupère tous les prospects à partir du service `visiteService` et les définit comme éléments du ComboBox.
     * Le nom du prospect est utilisé comme libellé pour chaque élément du ComboBox.
     */
    private fun majListeProspects() {
        selectClientOuProspect.setItems(visiteService.findAllProspects() as Collection<Prospect?>)
        selectClientOuProspect.setItemLabelGenerator { it.nom }
    }

    private fun configSaisieProspectDialog() {
        saisieProspectDialog.headerTitle = "Création nouveau prospect"
        saisieProspectDialog.isModal = true
        saisieProspectDialog.isCloseOnOutsideClick = false
        saisieProspectDialog.isCloseOnEsc = false
        saisieProspectDialog.maxWidth = "450px"
        saisieProspectDialog.setHeightFull()
        saisieProspectDialog.footer.add(saisieProspectForm.getBtnAnnuler(), saisieProspectForm.getBtnEnregistrer())
        saisieProspectDialog.add(saisieProspectForm)

        saisieProspectForm.modifierFuzzyMatchingCallback = {
            saisieProspectDialog.headerTitle = "Modification prospect"
        }
        saisieProspectForm.existantFuzzyMatchingCallback = {
            setClientOuProspectValue(saisieProspectForm.prospectExistantSelectionne!!)
            saisieProspectDialog.close()
        }
        saisieProspectForm.confirmerFuzzyMatchingCallback = {
            majListeProspects()
            setClientOuProspectValue(saisieProspectForm.getProspect())
            saisieProspectDialog.close()
        }

        saisieProspectForm.btnEnregistrerSuccesCallback = {
            majListeProspects()
            setClientOuProspectValue(saisieProspectForm.getProspect())
            Notification.show(
                "Nouveau propsect ${saisieProspectForm.getProspect().nom} enregistré",
                3000,
                Notification.Position.MIDDLE
            ).addThemeVariants(NotificationVariant.LUMO_SUCCESS)
            saisieProspectDialog.close()
        }

        saisieProspectForm.btnAnnulerClick = { saisieProspectDialog.close() }
    }

    /**
     * Cette méthode est appelée lors de la mise à jour de l'objet édité.
     * Elle met à jour la liste des vendeurs disponibles pour le ComboBox `vendeur2` en fonction de l'objet édité.
     *
     * Si l'objet édité n'est pas null, la liste des vendeurs est filtrée pour exclure le vendeur1 de l'objet édité.
     * Si l'objet édité est null, tous les vendeurs sont ajoutés à la liste.
     *
     * @param objetEdite L'objet édité. Il peut être null.
     */
    override fun onSetObjetEdite(objetEdite: Visite?) {
        super.onSetObjetEdite(objetEdite)
        if (objetEdite != null) vendeur2.setItems(
            visiteService.findAllVendeurs().filter { vendeur -> vendeur != objetEdite.vendeur1 })
        else vendeur2.setItems(visiteService.findAllVendeurs())
    }

    private fun configComposants() {
        date.label = "Date de visite"
        date.locale = Locale.FRANCE
        date.value = LocalDate.now()
        val dateLimiteMax = LocalDate.now().plusDays(plageJoursApresAjourdHui)
        val dateLimiteMin = LocalDate.now().minusDays(plageJoursAvantAjourdHui)
        if (plageJoursApresAjourdHui > 0) {
            date.max = dateLimiteMax
        }
        if (plageJoursAvantAjourdHui > 0) {
            date.min = dateLimiteMin
        }
        date.addValueChangeListener {
            if (initialisationEnCours) {
                LOG.debug("ValueChangeListener sur date | Initialisation en cours")
                return@addValueChangeListener
            }
            if (binder.bean.dateDebutRecontact != null && date.value.isAfter(binder.bean.dateDebutRecontact)) {
                binder.bean.dateDebutRecontact = date.value.plusDays(1)
                val vueSection3 = conteneurParent.sectionsFormLayout.filterIsInstance<VueSection3>().first()
                vueSection3.dateDebutRecontact.value = date.value.plusDays(1)
            }
        }
        binder.forField(date).withValidator({
            if (date.value != null) {
                return@withValidator date.value.isAfter(dateLimiteMin) && date.value.isBefore(dateLimiteMax)
            } else {
                return@withValidator false
            }
        }, "doit être une date comprise entre le $dateLimiteMin et le $dateLimiteMax")
            .bind(Visite::date) { it, value -> it.date = value }

        vendeur1.label = "Vendeur"
        vendeur1.isClearButtonVisible = true

        val vendeursListe: List<Vendeur?> = visiteService.findAllVendeurs()
        if (infosUtilisateur.estResponsable == true && lectureApplicationProperties.properties.main.afficherUniquementVendeursDuResponsable) {
            vendeur1.setItems(vendeursListe.filter { infosUtilisateur.vendeursSousResponsabilite?.contains(it?.code!!) == true })
        } else {
            vendeur1.setItems(vendeursListe)
        }
        if (!infosUtilisateur.estResponsable!!) {
            vendeur1.isReadOnly = true
        }
        vendeur1.setItemLabelGenerator { it.nom }
        vendeur1.addValueChangeListener {
            if (initialisationEnCours) {
                return@addValueChangeListener
            }
            // Retirer le vendeur 1 de la liste des vendeurs 2
            var valeurVendeur2 = vendeur2.value
            if (vendeur2.value == vendeur1.value) {
                valeurVendeur2 = null
            }
            vendeur2.setItems(visiteService.findAllVendeurs().filter { vendeur -> vendeur != vendeur1.value })
            vendeur2.value = valeurVendeur2
        }

        vendeur2.label = "Vendeur 2"
        vendeur2.isClearButtonVisible = true
        vendeur2.setItems(visiteService.findAllVendeurs())
        vendeur2.setItemLabelGenerator { it.nom }

        selectClientOuProspect.isReadOnly = true
        selectClientOuProspect.isClearButtonVisible = true

        typeContact.setItems("Client", "Prospect")
        // Quand le type de contact est modifié, on charge les clients ou les prospects selon le choix de l'utilisateur
        typeContact.addValueChangeListener { type ->
            if (type.value == "Client") {
                binder.bean.prospect = null
                btnCreerProspect.isVisible = false
                selectClientOuProspect.setItems(visiteService.findAllClients()
                    .sortedBy { it?.nom } as Collection<Client?>)
                selectClientOuProspect.setItemLabelGenerator { it.nom }
                binder.forField(selectClientOuProspect)
                    .withValidator({ return@withValidator it != null }, "Le client doit être renseigné")
                    .bind(Visite::client) { it, value -> it.client = value as? Client? }
            } else if (type.value == "Prospect") {
                binder.bean.client = null
                if (creationProspectActive) {
                    btnCreerProspect.isVisible = true
                }
                majListeProspects()
                binder.forField(selectClientOuProspect).withValidator(
                    { return@withValidator it != null }, "Le prospect doit être renseigné"
                ).bind(Visite::prospect) { it, value -> it.prospect = value as? Prospect? }
            }
            selectClientOuProspect.isReadOnly = false
        }

        selectClientOuProspect.addValueChangeListener {
            if (selectClientOuProspect.value != null) {
                majDetailClientOuProspect(selectClientOuProspect.value)
            } else {
                majDetailClientOuProspect(null)
            }
        }

        detailClientOuProspect.style["font-size"] = "0.9em"

        btnCreerProspect.text = "Nouveau"
        btnCreerProspect.height = "30px"
        btnCreerProspect.width = "110px"
        btnCreerProspect.style.set("font-size", "0.95em")
        btnCreerProspect.isVisible = false
        btnCreerProspect.addClickListener {
            saisieProspectForm.newProspect()
            saisieProspectDialog.open()
        }

        champTypeContactEtBtnNouveauProspect.alignItems = FlexComponent.Alignment.BASELINE
        champTypeContactEtBtnNouveauProspect.add(typeContact, btnCreerProspect)

        motifVisite.label = "Motif de visite"
        motifVisite.isClearButtonVisible = true
        motifVisite.setItems(visiteService.findAllMotifsVisite())
        motifVisite.setItemLabelGenerator { it.libelle }

        nomContact.label = "Nom du contact"
        nomContact.isClearButtonVisible = true

        fonctionContact.label = "Fonction du contact"
        fonctionContact.isClearButtonVisible = true
    }

    /**
     * Met à jour les détails du client ou du prospect dans le composant `detailClientOuProspect`.
     * Cette méthode construit une chaîne de caractères contenant les détails du client ou du prospect (adresse, code postal, ville) et l'assigne au texte du composant `detailClientOuProspect`.
     *
     * Si le client ou le prospect est null, le texte du composant `detailClientOuProspect` est vidé.
     *
     * @param clientOuProspect Le client ou le prospect dont les détails doivent être affichés. Il peut être null.
     */
    private fun majDetailClientOuProspect(clientOuProspect: ClientOuProspect?) {
        if (clientOuProspect == null) {
            detailClientOuProspect.text = ""
            return
        }
        val texteDetailClient = StringBuilder()
        texteDetailClient.append(clientOuProspect.adresse1)
        texteDetailClient.append(" | ")
        if (clientOuProspect.adresse2?.isNotEmpty() == true) {
            texteDetailClient.append(clientOuProspect.adresse2)
            texteDetailClient.append(" | ")
        }
        texteDetailClient.append(clientOuProspect.codePostal)
        texteDetailClient.append(" ")
        texteDetailClient.append(clientOuProspect.ville)
        detailClientOuProspect.text = texteDetailClient.toString()
    }
}
package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.*
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone.StringToNumeroTelephoneConverter
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone.TelephoneField
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone.TypeNumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone.ValidationLibPhoneNumber
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.fuzzyMatchClientOuProspect
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.supprimerEspacesInutiles


/**
 * Cette classe `SaisieProspectForm` est utilisée pour créer un formulaire de saisie pour un prospect.
 * Elle hérite de `FormLayout` qui est une disposition de formulaire dans Vaadin.
 *
 * @property visiteService Le service utilisé pour interagir avec les données des visites.
 * @property lectureApplicationProperties Les propriétés de l'application qui sont lues à partir d'un fichier de configuration.
 * @property estBtnDansForm Un indicateur pour savoir si le bouton est dans le formulaire. Par défaut, il est défini sur false.
 */
class SaisieProspectForm(
    private val visiteService: VisiteService,
    private val lectureApplicationProperties: LectureApplicationProperties,
    private val estBtnDansForm: Boolean = false,
) : FormLayout() {

    private val limiteNombreResultatsFuzzyMatching: Int? =
        lectureApplicationProperties.properties.vueSaisieVisite.limiteNombreResultatsFuzzyMatching
    private var fuzzyMatchingActif: Boolean = lectureApplicationProperties.properties.vueSaisieVisite.fuzzyMatchingActif
    private val seuilRatioFuzzyMatching: Int =
        lectureApplicationProperties.properties.vueSaisieVisite.seuilFuzzyMatching
    private val codePaysParDefaut: String = lectureApplicationProperties.properties.vueSaisieVisite.codePaysParDefaut
    private val paysParDefaut: Pays? = visiteService.findAllPays().find { it?.code == codePaysParDefaut }
    private val modeDemo: Boolean = lectureApplicationProperties.properties.main.modeDemo

    private val famille: ComboBox<Famille> = ComboBox<Famille>()
    private val nom: TextField = TextField()
    private val adresse1: TextField = TextField()
    private val adresse2: TextField = TextField()
    private val codePostal: TextField = TextField()
    private val ville: TextField = TextField()
    private val pays: ComboBox<Pays> = ComboBox<Pays>()
    private val telephonePortable: TelephoneField = TelephoneField(
        listePays = visiteService.findAllPays(),
        validateurTelephone = ValidationLibPhoneNumber(visiteService, lectureApplicationProperties),
        typeNumero = TypeNumeroTelephone.PORTABLE,
        validationActive = lectureApplicationProperties.properties.vueSaisieVisite.validationNumeroTelephoneProspectActive,
        formatageActif = lectureApplicationProperties.properties.vueSaisieVisite.formatageNumeroTelephoneProspectActive,
        affichageLibellesPaysEtNumero = lectureApplicationProperties.properties.vueSaisieVisite.affichageLibellesTelephoneField,
        paysParDefaut = paysParDefaut
    )
    private val telephoneFixe: TelephoneField = TelephoneField(
        listePays = visiteService.findAllPays(),
        validateurTelephone = ValidationLibPhoneNumber(visiteService, lectureApplicationProperties),
        typeNumero = TypeNumeroTelephone.FIXE,
        validationActive = lectureApplicationProperties.properties.vueSaisieVisite.validationNumeroTelephoneProspectActive,
        formatageActif = lectureApplicationProperties.properties.vueSaisieVisite.formatageNumeroTelephoneProspectActive,
        affichageLibellesPaysEtNumero = lectureApplicationProperties.properties.vueSaisieVisite.affichageLibellesTelephoneField,
        paysParDefaut = paysParDefaut
    )
    private val btnAnnuler: Button = Button()
    private val btnEnregistrer: Button = Button()
    private val btnAnulerEnregistrerLayout: HorizontalLayout = HorizontalLayout()

    private val champs = listOf(
        famille, adresse1, adresse2, codePostal, ville, pays, telephoneFixe, telephonePortable
    )

    var btnAnnulerClick: () -> Unit = {}
    var btnEnregistrerSuccesCallback: () -> Unit = {}

    private var nomDerniereValeur = ""
    private var estEnModeEdition = false

    // Dialog FuzzyMatching
    private var resultatsFuzzyMatching: List<ClientOuProspect> = listOf()
    private val fuzzyMatchingDialog: Dialog = Dialog()
    private val btnConfirmerFuzzyMatchingDialog = Button()
    private val btnModifierFuzzyMatchingDialog = Button()
    var confirmerFuzzyMatchingCallback: () -> Unit = {}
    var existantFuzzyMatchingCallback: () -> Unit = {}
    var modifierFuzzyMatchingCallback: () -> Unit = {}

    private val binder: Binder<Prospect> = BeanValidationBinder(Prospect::class.java)
    private var nouveauProspect = Prospect()
    var prospectExistantSelectionne: Prospect? = null

    companion object {
        val LOG by lazyLogger()
    }

    init {
        configFormLayout()
        configChamps()
        configBtnEnregistrerAnnuler()
        configFuzzyMatchingDialog()
        configBtnFuzzyMatchingDialog()
        ajoutFocusListenerSurChamps()

        setProspect(nouveauProspect)

        add(
            famille,
            nom,
            adresse1,
            adresse2,
            codePostal,
            ville,
            pays,
            telephoneFixe,
            telephonePortable,
            fuzzyMatchingDialog
        )
        addBtnEnregistrerAnnuler()
        configBinder()
    }

    /**
     * Cette méthode privée `configBinder` est utilisée pour configurer le binder.
     * Le binder est un outil de Vaadin qui lie les champs de formulaire aux propriétés de l'objet.
     *
     * Elle commence par configurer le champ `telephoneFixe` du formulaire pour qu'il utilise un convertisseur `NumeroTelephoneToStringConverter`.
     * Ce convertisseur transforme un numéro de téléphone en chaîne de caractères.
     * Le convertisseur prend en paramètres le service de visite, une instance de `ValidationLibPhoneNumber` pour la validation du numéro de téléphone, et le pays par défaut.
     * Ensuite, elle lie le champ `telephoneFixe` à la propriété `telephoneFixe` de l'objet `Prospect`.
     *
     * Elle fait de même pour le champ `telephonePortable` du formulaire.
     *
     * Enfin, elle lie tous les champs de l'instance actuelle de la classe à leurs propriétés correspondantes dans l'objet `Prospect`.
     */
    private fun configBinder() {
        /*
        binder.forField(telephoneFixe).withConverter(
            StringToNumeroTelephoneConverter(
                visiteService, ValidationLibPhoneNumber(visiteService, lectureApplicationProperties), paysParDefaut
            )
        ).bind(Prospect::telephoneFixe.name)
        binder.forField(telephonePortable).withConverter(
            StringToNumeroTelephoneConverter(
                visiteService, ValidationLibPhoneNumber(visiteService, lectureApplicationProperties), paysParDefaut
            )
        ).bind(Prospect::telephonePortable.name)

        telephoneFixe.ajouterVaadinValidator(binder).bind(Prospect::telephoneFixe.name)
        */

        val validationActive = lectureApplicationProperties.properties.vueSaisieVisite.validationNumeroTelephoneProspectActive

        binder.forField(telephoneFixe).withConverter(
            StringToNumeroTelephoneConverter(
                visiteService, ValidationLibPhoneNumber(visiteService, lectureApplicationProperties), paysParDefaut
            )
        ).withValidator({
            !validationActive || telephoneFixe.value.validite == EtatNumeroTelephone.VALIDE
            /*
            if (!validationActive) return@withValidator true
            TelephoneField.LOG.debug("\u001B[32mFonction withValidator\u001B[0m")
            val validite = telephoneFixe.value.validite
            if (validite == EtatNumeroTelephone.VALIDE) {
                TelephoneField.LOG.debug("  >> Numéro valide")
                true
            } else {
                TelephoneField.LOG.debug("  >> Numéro invalide")
                false
            }*/
        }, "doit être un numéro de téléphone valide")
            .bind(Prospect::telephoneFixe.name)

        binder.forField(telephonePortable).withConverter(
            StringToNumeroTelephoneConverter(
                visiteService, ValidationLibPhoneNumber(visiteService, lectureApplicationProperties), paysParDefaut
            )
        ).bind(Prospect::telephonePortable.name)

        binder.bindInstanceFields(this)
    }

    fun getBtnAnnuler(): Button {
        return btnAnnuler
    }

    fun getBtnEnregistrer(): Button {
        return btnEnregistrer
    }

    private fun addBtnEnregistrerAnnuler() {
        if (estBtnDansForm) {
            btnAnulerEnregistrerLayout.add(btnAnnuler, btnEnregistrer)
            add(btnAnulerEnregistrerLayout)
        }
    }

    /**
     * Cette méthode `newProspect` est utilisée pour créer un nouveau prospect.
     * Elle crée une nouvelle instance de `Prospect` et la définit comme le prospect actuel.
     * Elle définit également le pays du nouveau prospect sur le pays par défaut.
     * Enfin, elle met à jour le bean du binder avec le nouveau prospect.
     */
    fun newProspect() {
        this.nouveauProspect = Prospect()
        this.nouveauProspect.pays = paysParDefaut
        binder.bean = nouveauProspect
    }

    /**
     * Cette méthode privée `setProspect` est utilisée pour définir le prospect actuel.
     * Elle prend en paramètre un objet `Prospect` qui est le nouveau prospect à définir.
     * Elle met à jour le bean du binder avec le nouveau prospect et met à jour le prospect actuel avec le nouveau prospect.
     *
     * @param nouveauProspect Le nouveau prospect à définir.
     */
    private fun setProspect(nouveauProspect: Prospect) {
        binder.bean = nouveauProspect
        this.nouveauProspect = nouveauProspect
    }

    fun getProspect(): Prospect {
        return nouveauProspect
    }

    private fun configFormLayout() {
        setResponsiveSteps(
            ResponsiveStep("0", 1)
        )
    }

    private fun configChamps() {
        famille.label = "Famille"
        famille.isClearButtonVisible = true
        famille.setItems(visiteService.findAllFamilles())
        famille.setItemLabelGenerator { it.libelle }
        nom.label = "Nom"
        adresse1.label = "Adresse 1"
        adresse2.label = "Adresse 2"
        codePostal.label = "Code postal"
        ville.label = "Ville"
        pays.label = "Pays"
        pays.isClearButtonVisible = true
        pays.setItems(visiteService.findAllPays())
        pays.setItemLabelGenerator { it.libelle }
    }


    private fun configBtnEnregistrerAnnuler() {
        btnAnnuler.text = "Annuler"
        btnAnnuler.addThemeVariants(ButtonVariant.LUMO_ERROR)
        btnAnnuler.addClickListener {
            btnAnnulerClick()
            estEnModeEdition = false
        }
        btnEnregistrer.text = "Enregistrer"
        btnEnregistrer.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        btnEnregistrer.addClickListener { actionBtnEnregistrer() }
    }

    private fun ajoutFocusListenerSurChamps() {
        // TODO Pour l'instant, n'ajoute pas de listener sur CustomField car pas directement compatible avec la méthode addFocusListener
        champs.forEach { composant ->
            if (composant is Focusable<*>) {
                composant.addFocusListener {
                    btnConfirmerFuzzyMatchingDialog.isVisible = false
                    if (fuzzyMatchingActif && !estEnModeEdition) {
                        executerFuzzyMatching()
                    }
                    nomDerniereValeur = nom.value
                }
            }
        }
    }

    /**
     * Cette méthode privée `actionBtnEnregistrer` est utilisée pour gérer l'action du bouton Enregistrer.
     * Elle valide d'abord le prospect à enregistrer. Si la validation échoue, elle enregistre un message de débogage et retourne.
     * Si le mode d'édition n'est pas actif, le fuzzy matching est actif et la valeur du champ 'nom' a changé, elle rend le bouton de confirmation du fuzzy matching visible, exécute le fuzzy matching et met à jour la dernière valeur du nom.
     * Ensuite, elle enregistre un message de débogage indiquant l'enregistrement du prospect, désactive le mode d'édition et enregistre le prospect.
     */
    private fun actionBtnEnregistrer() {
        if (!validerProspect()) {
            LOG.debug("Échec de la validation des champs")
            return
        }
        if (!estEnModeEdition && fuzzyMatchingActif && nom.value != nomDerniereValeur) {
            btnConfirmerFuzzyMatchingDialog.isVisible = true
            executerFuzzyMatching()
            nomDerniereValeur = nom.value
            return
        }
        LOG.debug("Enregistrement du prospect ${nouveauProspect.nom}")
        estEnModeEdition = false
        enregistrerProspect()
    }

    /**
     * Cette méthode privée `executerFuzzyMatching` est utilisée pour exécuter le fuzzy matching sur le nom du prospect.
     * Si le fuzzy matching n'est pas actif et que le mode d'édition est actif, elle enregistre un message de débogage indiquant que le fuzzy matching n'est pas actif et ne lance pas le fuzzy matching.
     * Si la valeur du champ 'nom' n'a pas changé, elle enregistre un message de débogage indiquant que la valeur du champ 'nom' est inchangée et ne lance pas le fuzzy matching.
     * Elle exécute ensuite le fuzzy matching sur le nom du prospect avec tous les prospects existants et stocke les résultats dans `resultatsFuzzyMatching`.
     * Si `resultatsFuzzyMatching` n'est pas vide, elle prend les premiers résultats jusqu'à la limite définie par `limiteNombreResultatsFuzzyMatching` si elle est définie.
     * Ensuite, elle met à jour le contenu du dialogue de fuzzy matching avec `resultatsFuzzyMatching` et ouvre le dialogue.
     */
    private fun executerFuzzyMatching() {
        if (!fuzzyMatchingActif && estEnModeEdition) {
            LOG.debug("FuzzyMatching non actif")
            return
        }
        if (nom.value == nomDerniereValeur) {
            LOG.debug("Valeur du champ 'nom' inchangé, pas de lancement du FuzzyMatching")
            return
        }
        resultatsFuzzyMatching = fuzzyMatchClientOuProspect(
            nouveauProspect.nom, visiteService.findAllProspects().toMutableList(), seuilRatioFuzzyMatching
        )
        if (resultatsFuzzyMatching.isNotEmpty()) {
            if (limiteNombreResultatsFuzzyMatching != null) {
                resultatsFuzzyMatching = resultatsFuzzyMatching.take(limiteNombreResultatsFuzzyMatching)
            }
            contenuFuzzyMatchingDialog(resultatsFuzzyMatching)
            fuzzyMatchingDialog.open()
        }
    }

    private fun configFuzzyMatchingDialog() {
        btnConfirmerFuzzyMatchingDialog.text = "Confirmer nouveau prospect"
        btnConfirmerFuzzyMatchingDialog.addThemeVariants(ButtonVariant.LUMO_SUCCESS)

        btnModifierFuzzyMatchingDialog.text = "Retour saisie"
        btnModifierFuzzyMatchingDialog.addThemeVariants(ButtonVariant.LUMO_CONTRAST)

        fuzzyMatchingDialog.isModal = true
        fuzzyMatchingDialog.isCloseOnOutsideClick = false
        fuzzyMatchingDialog.maxWidth = "450px"
        fuzzyMatchingDialog.headerTitle = "Prospects déjà existants"
        val confirmationCreationDialogFooter = VerticalLayout(
            btnModifierFuzzyMatchingDialog, btnConfirmerFuzzyMatchingDialog
        )
        confirmationCreationDialogFooter.alignItems = FlexComponent.Alignment.CENTER
        fuzzyMatchingDialog.footer.add(
            confirmationCreationDialogFooter
        )
        btnConfirmerFuzzyMatchingDialog.isVisible = false
        add(fuzzyMatchingDialog)
    }

    private fun configBtnFuzzyMatchingDialog() {
        btnConfirmerFuzzyMatchingDialog.addClickListener {
            fuzzyMatchingDialog.close()
            enregistrerProspect()
            confirmerFuzzyMatchingCallback()
        }
        btnModifierFuzzyMatchingDialog.addClickListener {
            fuzzyMatchingDialog.close()
            modifierFuzzyMatchingCallback()
        }
    }

    /**
     * Cette méthode privée `contenuFuzzyMatchingDialog` est utilisée pour mettre à jour le contenu du dialogue de fuzzy matching.
     * Elle prend en paramètre une liste de prospects qui sont les résultats positifs du fuzzy matching.
     * Elle commence par supprimer tout le contenu du dialogue de fuzzy matching.
     * Ensuite, elle crée un nouveau `Div` pour contenir les cartes de contact.
     * Pour chaque prospect dans la liste des résultats positifs, elle crée une carte de contact et l'ajoute au `Div`.
     * Chaque carte de contact a deux callbacks : un pour la sélection et un pour la modification.
     * Le callback de sélection met à jour le prospect existant sélectionné, ferme le dialogue de fuzzy matching et appelle le callback existant de fuzzy matching.
     * Le callback de modification met le formulaire en mode d'édition, met à jour le prospect existant sélectionné, définit le prospect du formulaire sur le prospect existant sélectionné, ferme le dialogue de fuzzy matching et appelle le callback de modification de fuzzy matching.
     * Enfin, elle crée un `Scroller` pour le `Div` des cartes de contact, définit la direction du défilement sur vertical et ajoute le `Scroller` au dialogue de fuzzy matching.
     *
     * @param resultatsPositifsProspectsListe La liste des prospects qui sont les résultats positifs du fuzzy matching.
     */
    private fun contenuFuzzyMatchingDialog(resultatsPositifsProspectsListe: List<ClientOuProspect>) {
        fuzzyMatchingDialog.removeAll()
        val cardDiv = Div()

        resultatsPositifsProspectsListe.forEach {
            cardDiv.add(
                ContactCard(it, selectionCallback = {
                    prospectExistantSelectionne = it as Prospect
                    fuzzyMatchingDialog.close()
                    existantFuzzyMatchingCallback()
                }, modificationCallback = {
                    estEnModeEdition = true
                    prospectExistantSelectionne = it as Prospect
                    setProspect(prospectExistantSelectionne!!)
                    fuzzyMatchingDialog.close()
                    modifierFuzzyMatchingCallback()
                })
            )
        }
        val scroller = Scroller(cardDiv)
        scroller.scrollDirection = Scroller.ScrollDirection.VERTICAL
        fuzzyMatchingDialog.add(scroller)
    }

    /**
     * Cette méthode privée `validerProspect` est utilisée pour valider le prospect à enregistrer.
     * Elle vérifie d'abord si le binder est valide. Si c'est le cas, elle supprime les espaces inutiles dans les champs 'nom', 'adresse1', 'adresse2' et 'ville' du prospect, enregistre un message de débogage indiquant que le binder est valide et retourne true.
     * Si le binder n'est pas valide, elle valide le binder, enregistre un message de débogage indiquant que le binder n'est pas valide et retourne false.
     *
     * @return Un booléen indiquant si le prospect est valide ou non.
     */
    private fun validerProspect(): Boolean {
        if (binder.isValid) {
            supprimerEspacesInutiles(
                nouveauProspect.nom, nouveauProspect.adresse1, nouveauProspect.adresse2, nouveauProspect.ville
            )
            LOG.debug("Binder VALIDE pour prospect ${nouveauProspect.nom}")
            return true
        } else {
            binder.validate()
            LOG.debug("Binder NON VALIDE pour prospect ${nouveauProspect.nom}")
            return false
        }
    }

    private fun enregistrerProspect() {
        if (modeDemo) {
            nouveauProspect.code = visiteService.saveProspectWithId(nouveauProspect)
        }
        visiteService.saveProspect(nouveauProspect)
        btnEnregistrerSuccesCallback()
    }
}
package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.saisievisite

import com.vaadin.flow.component.FocusNotifier
import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.HasValidation
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.confirmdialog.ConfirmDialog
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.router.*
import com.vaadin.flow.theme.lumo.LumoUtility
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Vendeur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.ResponsiveVueManager
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security.InfosUtilisateur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.VueSectionFormLayout
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.verifierContraintesEntite
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.TopBarLayout
import jakarta.annotation.security.PermitAll
import org.springframework.beans.factory.annotation.Autowired
import java.time.ZonedDateTime


/**
 * Classe VueSaisieVisite, qui est une extension de VerticalLayout, HasUrlParameter<String> et BeforeLeaveObserver.
 * Cette classe représente la vue de saisie de visite dans l'application CRM Visites.
 * Elle est accessible à tous les utilisateurs (annotation @PermitAll).
 * Elle est mappée à l'URL "saisie-visite" (annotation @Route).
 * Le titre de la page est "Saisie de visite | CRM Visites" (annotation @PageTitle).
 *
 * @property visiteService Le service utilisé pour gérer les visites.
 * @property infosUtilisateur Les informations sur l'utilisateur actuellement connecté.
 * @constructor Crée une nouvelle instance de VueSaisieVisite.
 * @param visiteService Le service à utiliser pour gérer les visites.
 * @param infosUtilisateur Les informations sur l'utilisateur actuellement connecté.
 * @param lectureApplicationProperties Les propriétés de l'application à lire.
 */
@PermitAll
@Route(value = "saisie-visite", layout = TopBarLayout::class)
@PageTitle("Saisie de visite | CRM Visites")
class VueSaisieVisite(
    private var visiteService: VisiteService,
    @Autowired private val infosUtilisateur: InfosUtilisateur,
    lectureApplicationProperties: LectureApplicationProperties
) : VerticalLayout(), HasUrlParameter<String>, BeforeLeaveObserver {

    private val validationVisiteActive: Boolean = lectureApplicationProperties.properties.main.validationVisiteActive
    private val afficherVisitesQuandUserEstVendeur2: Boolean =
        lectureApplicationProperties.properties.main.afficherVisitesQuandUserEstVendeur2
    private val afficherUniquementVisitesDesVendeursDuResponsable: Boolean =
        lectureApplicationProperties.properties.main.afficherUniquementVisitesDesVendeursDuResponsable
    private val afficherVisitesQuandUserEstVendeur2DuResponsable: Boolean =
        lectureApplicationProperties.properties.main.afficherVisitesQuandUserEstVendeur2DuResponsable
    private val modeDemo: Boolean = lectureApplicationProperties.properties.main.modeDemo

    private val estResponsable: Boolean? = infosUtilisateur.estResponsable
    private val userConnecte: String? = infosUtilisateur.nomComplet
    private val vendeurConnecte: Vendeur? = infosUtilisateur.instanceVendeur

    private var estNouvelleVisite: Boolean? = null
    private var indexSectionActive: Int = 0
    private var composantFocusableActif: Focusable<*>? = null

    private var verticalLayoutPrincipalVueMobile: VerticalLayout = VerticalLayout()

    var sectionsFormLayout: List<VueSectionFormLayout<Visite>> = listOf()
    private val boutonsNavigation = mutableListOf<HorizontalLayout>()
    private var btnEnregistrerVueMobile: Button = Button()
    private val btnValiderVueMobile: Button = Button()
    private val btnValiderVueDesktop: Button = Button()
    private var btnEnregistrerVueDesktop: Button = Button()
    private val btnRetourAccueilVueMobile: Button = Button()
    private val btnRetourAccueilVueDesktop: Button = Button()
    private val col1VueDesktop = VerticalLayout()
    private val col2VueDesktop = VerticalLayout()
    private val ligneBasVueDesktop = HorizontalLayout()
    private val horizontalLayoutPrincipalVueDesktop = HorizontalLayout()
    private val horizontalLayoutBtnVueDesktop = HorizontalLayout()

    private val binder = BeanValidationBinder(Visite::class.java)
    private var visite = Visite()
    private var visiteNonModifiee = Visite()

    private val section1 = VueSection1(visiteService, binder, this, infosUtilisateur, lectureApplicationProperties)
    private val section2 = VueSection2(visiteService, binder, this, infosUtilisateur)
    private val section3 = VueSection3(visiteService, binder, this, infosUtilisateur)

    private val responsiveVueManager = ResponsiveVueManager()

    companion object {
        val LOG by lazyLogger()
    }

    init {
        sectionsFormLayout = listOf(section1, section2, section3)

        configResponsiveVueManager()
        configBeforeUnloadEvent()
        configLayoutGlobal()
        configLayoutPrincipalVueMobile()
        formaterSections()

        // Initialisation Vue Mobile
        configBtnEnregistrer(btnEnregistrerVueMobile)
        configBtnRetourAccueil(btnRetourAccueilVueMobile)
        configBtnValider(btnValiderVueMobile)
        ajoutBtnNavigationSections()

        // Initialisation Vue Desktop
        configBtnEnregistrer(btnEnregistrerVueDesktop)
        configBtnRetourAccueil(btnRetourAccueilVueDesktop)
        configBtnValider(btnValiderVueDesktop)
        ajoutLayoutVueDesktop()

        assignationListener(sectionsFormLayout)
    }

    private fun configResponsiveVueManager() {
        responsiveVueManager.vueMobileCallback = { statutVisibilite -> visibiliteVueMobile(statutVisibilite) }
        responsiveVueManager.vueDesktopCallback = { statutVisibilite -> visibiliteVueDesktop(statutVisibilite) }
    }

    /**
     * Définit le paramètre de l'URL pour cette vue.
     * Cette méthode est appelée par le routeur Vaadin lors de la navigation vers cette vue si un paramètre d'URL est présent.
     *
     * Si le paramètre est `null` ou "new", une nouvelle visite est initialisée et `estNouvelleVisite` est défini à `true`.
     *
     * Si le paramètre n'est pas `null` et n'est pas "new", il est converti en Long et utilisé pour rechercher une visite existante par son ID.
     * Si une visite correspondante est trouvée, elle est définie comme la visite actuelle et `estNouvelleVisite` est défini à `false`.
     * Si aucune visite correspondante n'est trouvée, l'utilisateur est redirigé vers la page d'accueil.
     *
     * Si l'utilisateur n'est pas autorisé à consulter la visite trouvée, il est également redirigé vers la page d'accueil.
     *
     * @param event L'événement de navigation qui a conduit à l'appel de cette méthode.
     * @param parameter Le paramètre d'URL pour cette vue, qui est soit `null`, soit "new", soit l'ID d'une visite existante.
     */
    override fun setParameter(event: BeforeEvent?, @OptionalParameter parameter: String?) {
        if (parameter == null || parameter == "new") {
            LOG.debug("Pas de paramètre de route dans VueSaisieVisite, initialisation nouvelle visite")
            estNouvelleVisite = true
            newVisite()
            return
        }

        estNouvelleVisite = false
        val visiteId = parameter.toLong()
        val visiteTrouvee = visiteService.findVisiteById(visiteId)
        LOG.debug("Visite demandée : ${visiteTrouvee.get().date} ${visiteTrouvee.get().vendeur1?.nom}")

        if (visiteTrouvee.isEmpty) {
            LOG.debug("Pas de visite trouvée avec l'id $parameter")
            UI.getCurrent().navigate("")
            return
        }

        if (!verifierSiUtilisateurAutoriseAConsulterVisite(visiteTrouvee.get())) {
            UI.getCurrent().navigate("")
            return
        }

        LOG.debug("\n\nVisite trouvée par ID : $visiteId")
        LOG.debug(visiteTrouvee.get().getDetails())
        setVisite(visiteTrouvee.get())
    }

    /**
     * Vérifie si l'utilisateur est autorisé à consulter la visite spécifiée.
     * Cette méthode vérifie si l'utilisateur connecté est un responsable ou un vendeur et détermine si l'utilisateur est autorisé à consulter la visite en fonction de plusieurs critères.
     *
     * Si l'utilisateur est un responsable :
     * - Si l'option `afficherUniquementVisitesDesVendeursDuResponsable` est activée, la méthode vérifie si le vendeur1 ou le vendeur2 de la visite est sous la responsabilité du responsable connecté.
     * - Si l'option `afficherUniquementVisitesDesVendeursDuResponsable` n'est pas activée, toutes les visites sont affichées.
     *
     * Si l'utilisateur est un vendeur :
     * - La méthode vérifie si le vendeur connecté est le vendeur1 ou le vendeur2 de la visite.
     *
     * @param visite La visite à vérifier.
     * @return `true` si l'utilisateur est autorisé à consulter la visite, `false` sinon.
     */
    private fun verifierSiUtilisateurAutoriseAConsulterVisite(visite: Visite): Boolean {
        val vendeur1Visite = visite.vendeur1
        val vendeur2Visite = visite.vendeur2

        if (infosUtilisateur.estResponsable!!) {
            LOG.debug("Responsable connecté : ${infosUtilisateur.nomComplet}")

            if (afficherUniquementVisitesDesVendeursDuResponsable) {
                LOG.debug("Affichage uniquement des visites des vendeurs sous responsabilité du responsable connecté")
                // Si le vendeur1 de la visite est sous la responsabilité du responsable connecté, alors on peut afficher la visite
                if (infosUtilisateur.vendeursSousResponsabilite!!.contains(vendeur1Visite?.code!!)) {
                    LOG.debug("Vendeur1 de la visite sous responsabilité du responsable connecté")
                    return true
                }
                // Si le vendeur2 de la visite est sous la responsabilité du responsable connecté, alors on peut afficher la visite
                if (afficherVisitesQuandUserEstVendeur2DuResponsable && infosUtilisateur.vendeursSousResponsabilite!!.contains(
                        vendeur2Visite?.code!!
                    )
                ) {
                    LOG.debug("Vendeur2 de la visite sous responsabilité du responsable connecté")
                    return true
                }
                LOG.debug("Vendeur1 et Vendeur2 de la visite non sous responsabilité du responsable connecté. PAS D'AFFICHAGE VISITE")
                return false
            }
            LOG.debug("Affichage de toutes les visites car responsable sans restrictions")
            return true
        }

        LOG.debug("Vendeur connecté : ${infosUtilisateur.nomComplet}")

        if (vendeur1Visite == vendeurConnecte) {
            LOG.debug("Vendeur1 de la visite est le vendeur connecté")
            return true
        }

        if (afficherVisitesQuandUserEstVendeur2 && vendeur2Visite == vendeurConnecte) {
            LOG.debug("Vendeur2 de la visite est le vendeur connecté")
            return true
        }
        LOG.debug("Vendeur1 et Vendeur2 de la visite non égaux au vendeur connecté. PAS D'AFFICHAGE VISITE")
        return false
    }


    /**
     * Méthode appelée avant de quitter la vue actuelle.
     * Cette méthode vérifie si des modifications ont été apportées à la visite en cours d'édition.
     * Si des modifications ont été apportées, un dialogue de confirmation est affiché pour demander à l'utilisateur s'il souhaite sauvegarder les modifications, ne pas les sauvegarder ou rester sur la page.
     * Si l'utilisateur choisit de sauvegarder les modifications, la méthode `verifierSaisieEtEnregistrerVisite` est appelée.
     * Si l'utilisateur choisit de ne pas sauvegarder les modifications, la navigation se poursuit sans sauvegarder les modifications.
     * Si l'utilisateur choisit de rester sur la page, le dialogue est fermé et la navigation est annulée.
     * Si aucune modification n'a été apportée à la visite, la navigation se poursuit normalement.
     *
     * @param event L'événement de navigation qui a conduit à l'appel de cette méthode.
     */
    override fun beforeLeave(event: BeforeLeaveEvent?) {
        LOG.debug("BeforeLeaveEvent : Vérification des changements dans la visite")
        binder.validate()
        LOG.debug("Visite actuelle : $visite")
        LOG.debug(visite.getDetails())
        LOG.debug("Visite non modifiée : $visiteNonModifiee")
        LOG.debug(visiteNonModifiee.getDetails())
        if (visite != visiteNonModifiee) {
            LOG.debug("  >> BeforeLeaveEvent : Modifications dans la visite")
            val action = event!!.postpone()
            val dialog = ConfirmDialog()
            dialog.setText("Certaines modifications n'ont pas été enregistrées. Voulez-vous vraiment quitter la page ?")
            dialog.setCancelButton("Rester sur la page") { dialog.close() }
            dialog.setRejectButton("Ne pas sauvegarder") { action.proceed() }
            dialog.setConfirmButton("Sauvegarder les modifications") { verifierSaisieEtEnregistrerVisite() }
            dialog.setCancelable(true)
            dialog.open()
        } else {
            LOG.debug("  >> BeforeLeaveEvent : Pas de changements dans la visite")
        }
    }

    /**
     * Configure l'événement 'beforeunload' pour la page actuelle.
     * Cet événement est déclenché lorsque l'utilisateur tente de quitter la page.
     * Un message de confirmation est affiché, demandant à l'utilisateur s'il souhaite vraiment quitter la page.
     * Cette méthode utilise l'API JavaScript pour ajouter l'événement 'beforeunload' à la fenêtre du navigateur.
     */
    private fun configBeforeUnloadEvent() {
        UI.getCurrent().page.executeJs(
            """
        window.addEventListener('beforeunload', function(e) {
            e.returnValue = 'Quitter la page ?';
            return ''; 
        });
        """
        )
    }

    /**
     * Crée une nouvelle visite.
     * Si l'utilisateur connecté n'est pas un responsable, le vendeur1 de la nouvelle visite est défini comme étant l'utilisateur connecté.
     * Ensuite, la nouvelle visite est définie comme la visite actuelle en appelant la méthode `setVisite`.
     */
    private fun newVisite() {
        val nouvelleVisite = Visite()
        if (!infosUtilisateur.estResponsable!!) {
            nouvelleVisite.vendeur1 = infosUtilisateur.instanceVendeur
        }
        setVisite(nouvelleVisite)
    }

    /**
     * Définit la visite actuelle et initialise les sections du formulaire avec cette visite.
     * Cette méthode copie également la visite pour pouvoir détecter ultérieurement si des modifications ont été apportées.
     * Pendant l'initialisation des sections du formulaire, le drapeau `initialisationEnCours` est défini à `true` pour chaque section pour indiquer que l'initialisation est en cours.
     * Après l'initialisation, le drapeau `initialisationEnCours` est défini à `false` pour chaque section.
     *
     * @param visite La visite à définir comme visite actuelle.
     */
    private fun setVisite(visite: Visite) {
        this.visite = visite
        visiteNonModifiee = visite.copy()

        sectionsFormLayout.forEach {
            it.initialisationEnCours = true
            it.objetEdite = visite
        }
        binder.bean = visite
        sectionsFormLayout.forEach { it.initialisationEnCours = false }
    }

    /**
     * Vérifie la saisie de la visite et l'enregistre si elle est valide.
     * Cette méthode effectue plusieurs vérifications avant d'enregistrer la visite :
     * - Elle vérifie si le binder Vaadin pour la visite est valide. Si ce n'est pas le cas, elle valide le binder et retourne sans enregistrer la visite.
     * - Elle vérifie si les contraintes JPA pour la visite sont valides. Si ce n'est pas le cas, elle retourne sans enregistrer la visite.
     * - Si la visite est nouvelle (c'est-à-dire si `dateCreation` est `null`), elle définit `dateCreation` et `userCreation` pour la visite.
     * - Si la visite n'est pas nouvelle, elle définit `dateDerniereModification` et `userDerniereModification` pour la visite.
     * - Ensuite, elle enregistre la visite en utilisant `visiteService.saveVisite`.
     * - Si l'application est en mode démo et que la visite est nouvelle, elle enregistre également la visite avec un ID en utilisant `visiteService.saveVisiteWithId`.
     * - Après avoir enregistré la visite, elle met à jour `visiteNonModifiee` pour correspondre à la visite enregistrée, afin de ne pas afficher le message de confirmation pour quitter la page.
     * - Enfin, elle affiche une notification indiquant que la visite a été enregistrée avec succès et navigue vers la page d'accueil.
     */
    private fun verifierSaisieEtEnregistrerVisite() {
        if (!binder.isValid) {
            binder.validate()
            LOG.debug("Binder Vaadin pour Visite NON VALIDE.\n-> Pas d'enregistrement de la visite ")
            return
        }

        if (!verifierContraintesEntite(visite)) {
            LOG.debug("Contraintes JPA pour Visite NON VALIDE.\n-> Pas d'enregistrement de la visite ")
            return
        }

        if (visite.dateCreation == null) {
            visite.dateCreation = ZonedDateTime.now()
            visite.userCreation = userConnecte
        } else {
            visite.dateDerniereModification = ZonedDateTime.now()
            visite.userDerniereModification = userConnecte
        }

        LOG.debug("Binder Visite VALIDE\n-> Enregistrement de la visite ${visite.id} ${visite.date} ${visite.vendeur1?.nom}")
        LOG.debug("\nDétails visite enregistrée :\n${visite.getDetails()}")

        if (modeDemo && estNouvelleVisite == true) {
            visite.code = visiteService.saveVisiteWithId(visite)
            LOG.debug("Visite enregistrée en mode démo avec le code ${visite.code}")
        }
        visiteService.saveVisite(visite)

        // Mise à jour de la valeur de visiteNonModifiee pour ne pas afficher le message de confirmation pour quitter la page
        visiteNonModifiee = visite.copy()

        Notification.show("Visite enregistrée avec succès", 3000, Notification.Position.MIDDLE)
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS)
        UI.getCurrent().navigate("")
    }

    /**
     * Assignation des écouteurs d'événements aux sections du formulaire.
     * Cette méthode parcourt chaque section du formulaire et assigne un écouteur d'événements de focus.
     * Lorsqu'un événement de focus est déclenché dans une section, l'index de la section active est mis à jour et le composant qui a déclenché l'événement est stocké.
     *
     * @param sections La liste des sections du formulaire à laquelle assigner les écouteurs d'événements.
     */
    private fun assignationListener(sections: List<VueSectionFormLayout<Visite>>) {
        sections.forEachIndexed { index, section ->
            section.focusValueListenerCallback = { formSection, event ->
                indexSectionActive = index
                if (event is FocusNotifier.FocusEvent<*> && event.source is Focusable<*>) {
                    composantFocusableActif = event.source as? Focusable<*>
                }
                LOG.debug("Section $index : $formSection")
            }
        }
    }

    private fun configLayoutGlobal() {
        defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        addClassName("layout-global")
        addClassName(LumoUtility.Padding.Top.XSMALL)
        addClassName(LumoUtility.Gap.XSMALL)
        //verticalLayoutPrincipalVueMobile.style.set("padding-top", "0px")
        verticalLayoutPrincipalVueMobile.style.set("row-gap", "0")
        //verticalLayoutPrincipalVueMobile.style.set("align-items", "flex-start")
        setSizeFull()
    }

    private fun configLayoutPrincipalVueMobile() {
        verticalLayoutPrincipalVueMobile.setSizeFull()
        verticalLayoutPrincipalVueMobile.addClassName("main-mobile-vertical")
        verticalLayoutPrincipalVueMobile.justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        verticalLayoutPrincipalVueMobile.maxWidth = "620px"
        verticalLayoutPrincipalVueMobile.style.set("padding-top", "0")
        add(verticalLayoutPrincipalVueMobile)
    }

    private fun configBtnEnregistrer(btn: Button) {
        btn.text = "Enregistrer"
        btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        btn.addClickListener {
            LOG.debug("Clic btnEnregistrer: Enregistrement de la fiche de visite")
            verifierSaisieEtEnregistrerVisite()
        }
    }

    private fun configBtnValider(btn: Button) {
        btn.text = "Valider la visite"
        btn.addThemeVariants(ButtonVariant.LUMO_CONTRAST)
        btn.addClickListener {
            LOG.debug("Clic btnValider")
            if (visite.userValidation.isNullOrBlank() || visite.dateValidation == null) {
                LOG.debug("Validation visite")
                validerVisite()
            } else {
                Notification.show(
                    "La visite a déjà été validée par ${visite.userValidation} le ${visite.dateValidation}",
                    3000,
                    Notification.Position.MIDDLE
                ).addThemeVariants(NotificationVariant.LUMO_WARNING)
            }
        }
    }

    private fun configBtnRetourAccueil(btn: Button) {
        btn.text = "Retour"
        btn.addThemeVariants(ButtonVariant.LUMO_ERROR)
        btn.addClickListener {
            LOG.debug("Clic btnRetour")
            navigationRetour()
        }
    }

    /**
     * Valide la visite actuelle.
     * Cette méthode affiche une boîte de dialogue de confirmation demandant à l'utilisateur s'il souhaite valider la visite.
     * Si l'utilisateur confirme, la visite est validée en définissant `userValidation` et `dateValidation` pour la visite, et la visite est enregistrée en appelant la méthode `verifierSaisieEtEnregistrerVisite`.
     * Une notification est également affichée pour indiquer que la visite a été validée avec succès.
     * Si l'utilisateur annule, la boîte de dialogue est fermée et la visite n'est pas validée.
     */
    private fun validerVisite() {
        val dialog = ConfirmDialog()
        dialog.setText("Confirmez-vous la validation la visite ?")
        dialog.setConfirmButton("Valider") {
            Notification.show("Visite validée par $userConnecte", 3000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS)
            dialog.close()
            visite.userValidation = userConnecte
            visite.dateValidation = java.time.LocalDate.now()
            verifierSaisieEtEnregistrerVisite()
        }
        dialog.setCancelButton("Annuler") { dialog.close() }
        dialog.open()
    }


    /**
     * Formate les sections du formulaire.
     * Cette méthode parcourt chaque section du formulaire et définit sa taille à pleine taille.
     * Elle définit également les étapes de réponse pour chaque section à une colonne à partir de zéro pixels.
     */
    private fun formaterSections() {
        sectionsFormLayout.forEach {
            it.setSizeFull()
            it.setResponsiveSteps(FormLayout.ResponsiveStep("0", 1))
        }
    }

    /**
     * Cache toutes les sections du formulaire sauf une.
     * Cette méthode parcourt chaque section du formulaire et la rend invisible.
     * Ensuite, elle rend visible la section à l'index spécifié.
     *
     * @param indexSectionVisible L'index de la section à rendre visible.
     */
    private fun cacherToutesSectionsSaufUne(indexSectionVisible: Int) {
        sectionsFormLayout.forEach { it.isVisible = false }
        sectionsFormLayout[indexSectionVisible].isVisible = true
    }

    /**
     * Affiche toutes les sections du formulaire.
     * Cette méthode parcourt chaque section du formulaire et la rend visible.
     */
    private fun afficherToutesSections() {
        sectionsFormLayout.forEach { it.isVisible = true }
    }

    /**
     * Ajoute les sections du formulaire à la vue mobile.
     * Cette méthode ajoute le layout principal de la vue mobile au layout global,
     * puis ajoute chaque section du formulaire au layout principal de la vue mobile.
     */
    private fun ajoutSectionsVueMobile() {
        add(verticalLayoutPrincipalVueMobile)
        sectionsFormLayout.forEach { section -> verticalLayoutPrincipalVueMobile.add(section) }
    }

    /**
     * Retire les sections du formulaire de la vue mobile.
     * Cette méthode parcourt chaque section du formulaire et la retire du layout principal de la vue mobile.
     * Ensuite, elle retire le layout principal de la vue mobile du layout global.
     */
    private fun retraitSectionsVueMobile() {
        sectionsFormLayout.forEach { section -> verticalLayoutPrincipalVueMobile.remove(section) }
        remove(verticalLayoutPrincipalVueMobile)
    }

    /**
     * Ajoute les sections du formulaire à la vue de bureau (desktop).
     * Cette méthode effectue plusieurs opérations :
     * - Elle crée une liste des blocs de la vue de bureau, qui comprend `col1VueDesktop`, `col2VueDesktop` et `ligneBasVueDesktop`.
     * - Elle parcourt chaque bloc de la vue de bureau et définit sa largeur maximale à 620 pixels. Si le bloc est une instance de VerticalLayout, elle définit également son padding-top à 0.
     * - Elle ajoute `col1VueDesktop` et `col2VueDesktop` à `horizontalLayoutPrincipalVueDesktop`.
     * - Elle ajoute des classes CSS à `horizontalLayoutBtnVueDesktop` pour le formater correctement.
     * - Elle ajoute les boutons `btnRetourAccueilVueDesktop` et `btnEnregistrerVueDesktop` à `horizontalLayoutBtnVueDesktop`.
     * - Si l'utilisateur connecté est un responsable et que la validation de la visite est activée, elle ajoute également `btnValiderVueDesktop` à `horizontalLayoutBtnVueDesktop`.
     * - Enfin, elle ajoute `horizontalLayoutPrincipalVueDesktop`, `ligneBasVueDesktop` et `horizontalLayoutBtnVueDesktop` au layout global de la vue.
     */
    private fun ajoutLayoutVueDesktop() {
        val blocsVueDesktop = listOf(col1VueDesktop, col2VueDesktop, ligneBasVueDesktop)
        blocsVueDesktop.forEach {
            it.maxWidth = "620px"
            if (it is VerticalLayout) {
                it.style.set("padding-top", "0")
            }
        }
        horizontalLayoutPrincipalVueDesktop.add(col1VueDesktop, col2VueDesktop)
        horizontalLayoutBtnVueDesktop.addClassName("btn-layout-vue-desktop")
        horizontalLayoutBtnVueDesktop.addClassName(LumoUtility.Padding.Top.SMALL)
        horizontalLayoutBtnVueDesktop.add(btnRetourAccueilVueDesktop, btnEnregistrerVueDesktop)
        if (estResponsable!! && validationVisiteActive) {
            horizontalLayoutBtnVueDesktop.add(btnValiderVueDesktop)
        }
        add(horizontalLayoutPrincipalVueDesktop, ligneBasVueDesktop, horizontalLayoutBtnVueDesktop)
    }

    /**
     * Modifie la visibilité des layouts principaux de la vue de bureau.
     * Cette méthode définit la visibilité des layouts `horizontalLayoutPrincipalVueDesktop` et `horizontalLayoutBtnVueDesktop` en fonction du statut de visibilité passé en paramètre.
     *
     * @param statutVisibilite Le statut de visibilité à appliquer aux layouts principaux de la vue de bureau.
     */
    private fun visibiliteLayoutVueDesktop(statutVisibilite: Boolean) {
        horizontalLayoutPrincipalVueDesktop.isVisible = statutVisibilite
        horizontalLayoutBtnVueDesktop.isVisible = statutVisibilite
    }

    /**
     * Ajoute les sections du formulaire à la vue de bureau.
     * Cette méthode ajoute la première et la deuxième section du formulaire à `col1VueDesktop` et `col2VueDesktop` respectivement,
     * et ajoute la troisième section du formulaire à `ligneBasVueDesktop`.
     */
    private fun ajoutSectionsVueDesktop() {
        col1VueDesktop.add(sectionsFormLayout[0])
        col2VueDesktop.add(sectionsFormLayout[1])
        ligneBasVueDesktop.add(sectionsFormLayout[2])
    }

    /**
     * Retire les sections du formulaire de la vue de bureau.
     * Cette méthode retire toutes les sections du formulaire de `col1VueDesktop` et `col2VueDesktop`.
     */
    private fun retraitSectionVueDesktop() {
        col1VueDesktop.removeAll()
        col2VueDesktop.removeAll()
    }

    /**
     * Navigue vers une section spécifique du formulaire en fonction du type de navigation fourni.
     * Cette méthode détermine la section cible en fonction du type de navigation (suivant ou précédent) et vérifie si la section cible existe.
     * Si la section cible existe, elle rend la section actuelle invisible, rend la section cible visible et met à jour l'index de la section active.
     * Si la section cible n'existe pas, elle enregistre un message de journalisation indiquant que la section demandée n'existe pas.
     *
     * @param typeNavigation Le type de navigation à effectuer, qui peut être soit `TypeNavigation.SUIVANT` pour naviguer vers la section suivante, soit `TypeNavigation.PRECEDENT` pour naviguer vers la section précédente.
     */
    private fun naviguerVersSection(typeNavigation: TypeNavigation) {
        val sectionCible: Int
        val estSectionExistante: Boolean

        if (typeNavigation == TypeNavigation.SUIVANT) {
            sectionCible = indexSectionActive + 1
            estSectionExistante = sectionCible < sectionsFormLayout.size
        } else {
            sectionCible = indexSectionActive - 1
            estSectionExistante = sectionCible >= 0
        }

        if (estSectionExistante) {
            LOG.debug("Navigation de la section $indexSectionActive à la section $sectionCible")
            sectionsFormLayout[indexSectionActive].isVisible = false
            sectionsFormLayout[sectionCible].isVisible = true
            indexSectionActive = sectionCible
        } else {
            LOG.debug("Impossible d'afficher la section ${typeNavigation.description} $sectionCible demandée car inexistante")
        }
    }

    private fun creerBtnNavigation(type: TypeNavigation): Button {
        val button = Button()
        button.text = type.description
        button.addThemeVariants(type.theme)
        button.addClickListener {
            if (type == TypeNavigation.SUIVANT && !estSectionAvecSaisieValide()) {
                return@addClickListener
            }
            naviguerVersSection(type)
        }
        return button
    }

    /**
     * Gère la navigation de retour.
     * Cette méthode vérifie si la visite actuelle est une nouvelle visite.
     * Si c'est le cas, elle navigue vers la page d'accueil.
     * Sinon, elle navigue vers la page "grid-visites".
     */
    private fun navigationRetour() {
        if (estNouvelleVisite!!) {
            UI.getCurrent().navigate("")
        } else {
            UI.getCurrent().navigate("grid-visites")
        }
    }

    /**
     * Vérifie si la section actuelle du formulaire est valide.
     * Cette méthode effectue plusieurs opérations :
     * - Elle valide le binder Vaadin pour la visite.
     * - Elle parcourt chaque champ de la section actuelle du formulaire. Si le champ est une instance de HasValidation et qu'il est invalide, elle retourne `false`.
     * - Elle efface les messages d'erreur de la validation pour que les champs de la page suivante (pas encore remplis) ne soient pas marqués comme invalides.
     * - Si tous les champs de la section actuelle sont valides, elle retourne `true`.
     *
     * @return `true` si tous les champs de la section actuelle sont valides, `false` sinon.
     */
    private fun estSectionAvecSaisieValide(): Boolean {
        binder.validate()
        LOG.debug("\n--------------------------------\nValidation des champs de la section $indexSectionActive")
        sectionsFormLayout[indexSectionActive].champs.forEach { champ ->
            if (champ is HasValidation) {
                if (champ.isInvalid) {
                    LOG.debug("\u001B[31m${champ} -> INVALIDE\u001B[0m")
                    return false
                } else {
                    LOG.debug("\u001B[32m${champ} -> VALIDE\u001B[0m")
                }
            }
        }
        // Effacement des messages d'erreur de la validation pour que les champs de la page suivante (pas encore remplis) ne soient pas marqués comme invalides
        binder.bean = binder.bean
        return true
    }

    /**
     * Ajoute des boutons de navigation aux sections du formulaire.
     * Cette méthode parcourt chaque section du formulaire et ajoute un layout horizontal contenant les boutons de navigation appropriés.
     * Pour la première section, un bouton "Retour accueil" est ajouté.
     * Pour toutes les sections sauf la première, un bouton "Précédent" est ajouté.
     * Pour toutes les sections sauf la dernière, un bouton "Suivant" est ajouté.
     * Pour la dernière section, un bouton "Enregistrer" est ajouté. Si l'utilisateur connecté est un responsable et que la validation de la visite est activée, un bouton "Valider" est également ajouté.
     * Chaque layout horizontal de boutons est ensuite ajouté à la section correspondante et à la liste `boutonsNavigation`.
     */
    private fun ajoutBtnNavigationSections() {
        sectionsFormLayout.mapIndexed { indexSection, section ->
            val horizontalButtonLayoutVueMobile = HorizontalLayout()
            horizontalButtonLayoutVueMobile.justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            horizontalButtonLayoutVueMobile.addClassName(LumoUtility.Padding.Top.SMALL)
            // Pour la première page, ajouter le bouton retour accueil
            if (indexSection == 0) {
                horizontalButtonLayoutVueMobile.add(btnRetourAccueilVueMobile)
            }
            // Pour toutes les pages sauf la première, ajouter bouton Précédent
            if (indexSection > 0) {
                horizontalButtonLayoutVueMobile.add(creerBtnNavigation(TypeNavigation.PRECEDENT))
            }
            // Pour toutes les pages sauf la dernière, ajouter le bouton Suivant
            if (indexSection != sectionsFormLayout.size - 1) {
                horizontalButtonLayoutVueMobile.add(creerBtnNavigation(TypeNavigation.SUIVANT))
            }
            // Sur la dernière page, ajouter le bouton enregistrer
            if (indexSection == sectionsFormLayout.size - 1) {
                horizontalButtonLayoutVueMobile.add(btnEnregistrerVueMobile)
                if (estResponsable!! && validationVisiteActive) {
                    horizontalButtonLayoutVueMobile.add(btnValiderVueMobile)
                }
            }
            section.add(horizontalButtonLayoutVueMobile)
            boutonsNavigation.add(horizontalButtonLayoutVueMobile)
        }
    }

    /**
     * Modifie la visibilité des boutons de navigation des sections du formulaire.
     * Cette méthode parcourt chaque layout horizontal de boutons dans la liste `boutonsNavigation` et définit sa visibilité en fonction du statut de visibilité passé en paramètre.
     *
     * @param statutVisibilite Le statut de visibilité à appliquer aux boutons de navigation des sections du formulaire.
     */
    private fun visibiliteBtnNavigationSections(statutVisibilite: Boolean) {
        boutonsNavigation.forEach { it.isVisible = statutVisibilite }
    }

    /**
     * Modifie la visibilité de la vue mobile.
     * Cette méthode gère l'activation et la désactivation de la vue mobile en fonction du statut de visibilité passé en paramètre.
     *
     * @param statutVisibilite Le statut de visibilité à appliquer à la vue mobile.
     * Si `statutVisibilite` est `true` :
     * - La vue mobile est activée.
     * - Les sections du formulaire sont ajoutées à la vue mobile.
     * - Toutes les sections du formulaire sont cachées sauf la section active.
     * - Les boutons de navigation des sections du formulaire sont rendus visibles.
     * - Le focus est donné au composant actif.
     *
     * Si `statutVisibilite` est `false` :
     * - La vue mobile est désactivée.
     * - La largeur maximale du layout global est réinitialisée à sa valeur par défaut.
     * - Les sections du formulaire sont retirées de la vue mobile.
     * - Toutes les sections du formulaire sont rendues visibles.
     * - Les boutons de navigation des sections du formulaire sont rendus invisibles.
     */
    private fun visibiliteVueMobile(statutVisibilite: Boolean) {
        if (statutVisibilite) {
            // Activer Vue Mobile
            LOG.debug("Visibilité Vue Mobile activée")
            ajoutSectionsVueMobile()
            cacherToutesSectionsSaufUne(indexSectionActive)
            visibiliteBtnNavigationSections(true)
            composantFocusableActif?.focus()
        } else {
            // Désactiver Vue Mobile
            LOG.debug("Visibilité Vue Mobile désactivée")
            maxWidth = null
            retraitSectionsVueMobile()
            afficherToutesSections()
            visibiliteBtnNavigationSections(false)
        }
    }

    /**
     * Modifie la visibilité de la vue de bureau (desktop).
     * Cette méthode gère l'activation et la désactivation de la vue de bureau en fonction du statut de visibilité passé en paramètre.
     *
     * @param statutVisibilite Le statut de visibilité à appliquer à la vue de bureau.
     * Si `statutVisibilite` est `true` :
     * - La vue de bureau est activée.
     * - Les sections du formulaire sont ajoutées à la vue de bureau.
     * - Les layouts principaux de la vue de bureau sont rendus visibles.
     * - Le focus est donné au composant actif.
     *
     * Si `statutVisibilite` est `false` :
     * - La vue de bureau est désactivée.
     * - Les sections du formulaire sont retirées de la vue de bureau.
     * - Les layouts principaux de la vue de bureau sont rendus invisibles.
     */
    private fun visibiliteVueDesktop(statutVisibilite: Boolean) {
        if (statutVisibilite) {
            // Activer Vue Desktop
            LOG.debug("Visibilité Vue Desktop activée")
            ajoutSectionsVueDesktop()
            visibiliteLayoutVueDesktop(true)
            composantFocusableActif?.focus()
        } else {
            // Désactiver Vue Desktop
            LOG.debug("Visibilité Vue Desktop désactivée")
            retraitSectionVueDesktop()
            visibiliteLayoutVueDesktop(false)
        }
    }
}


/**
 * Enumération TypeNavigation, qui définit les types de navigation possibles dans l'application.
 * Chaque type de navigation a une description et un thème associé.
 *
 * @property description La description du type de navigation, qui est affichée sur le bouton de navigation correspondant.
 * @property theme Le thème du bouton de navigation, qui est utilisé pour styliser le bouton de navigation correspondant.
 *
 * @see ButtonVariant
 *
 * @constructor Crée une nouvelle instance de TypeNavigation avec la description et le thème spécifiés.
 * @param description La description du type de navigation.
 * @param theme Le thème du bouton de navigation.
 */
enum class TypeNavigation(
    val description: String, val theme: ButtonVariant
) {
    /**
     * Le type de navigation "Précédent", qui est utilisé pour naviguer vers la section précédente du formulaire.
     * La description est "Retour" et le thème est `ButtonVariant.LUMO_ERROR`.
     */
    PRECEDENT("Retour", ButtonVariant.LUMO_ERROR),

    /**
     * Le type de navigation "Suivant", qui est utilisé pour naviguer vers la section suivante du formulaire.
     * La description est "Suite" et le thème est `ButtonVariant.LUMO_PRIMARY`.
     */
    SUIVANT("Suite", ButtonVariant.LUMO_PRIMARY)
}
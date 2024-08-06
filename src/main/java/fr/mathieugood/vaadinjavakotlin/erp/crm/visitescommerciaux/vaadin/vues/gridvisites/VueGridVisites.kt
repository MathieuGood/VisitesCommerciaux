package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.gridvisites

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasSize
import com.vaadin.flow.component.HasValue
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.shared.HasClearButton
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.*
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security.InfosUtilisateur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.TopBarLayout
import jakarta.annotation.security.PermitAll
import org.springframework.beans.factory.annotation.Autowired
import java.time.format.DateTimeFormatter

@PermitAll
@Route(value = "grid-visites", layout = TopBarLayout::class)
@PageTitle("Consultation visites | CRM Visites")
/**
 * Une classe qui représente la vue de la grille des visites dans l'interface utilisateur.
 * Cette classe étend `VerticalLayout` de Vaadin et utilise plusieurs services et propriétés pour gérer l'affichage et l'interaction avec les visites.
 * @param visiteService Le service utilisé pour interagir avec les données des visites.
 * @param filtresGridVisites Les filtres actuellement appliqués à la grille des visites.
 * @param infosUtilisateur Les informations sur l'utilisateur actuellement connecté.
 * @param lectureApplicationProperties Les propriétés de l'application, utilisées pour configurer certains aspects de la vue.
 */
class VueGridVisites(
    private val visiteService: VisiteService,
    @Autowired private val filtresGridVisites: FiltresGridVisites,
    @Autowired private val infosUtilisateur: InfosUtilisateur,
    private val lectureApplicationProperties: LectureApplicationProperties,
) : VerticalLayout() {
    private val triInitialFrontendActif: Boolean = lectureApplicationProperties.properties.main.triFrontendActif
    private val afficherVisitesQuandUserEstVendeur2: Boolean =
        lectureApplicationProperties.properties.main.afficherVisitesQuandUserEstVendeur2
    private val afficherUniquementVisitesDesVendeursDuResponsable: Boolean =
        lectureApplicationProperties.properties.main.afficherUniquementVisitesDesVendeursDuResponsable
    private val afficherVisitesQuandUserEstVendeur2DuResponsable: Boolean =
        lectureApplicationProperties.properties.main.afficherVisitesQuandUserEstVendeur2DuResponsable
    private val validationVisiteActive: Boolean = lectureApplicationProperties.properties.main.validationVisiteActive
    private val ouvrirVisiteAuClicSurLigneMobile: Boolean =
        lectureApplicationProperties.properties.vueGridVisites.ouvrirVisiteAuClicSurLigneMobile

    // Paramètres de l'utilisateur connecté
    private var estResponsable: Boolean? = infosUtilisateur.estResponsable
    private var codeVendeurConnecte: String? = infosUtilisateur.codeVendeur
    private var listeVendeursRattachesAuResponsable: List<String>? = infosUtilisateur.vendeursSousResponsabilite

    private val gridVisites: Grid<Visite> = Grid(Visite::class.java, false)
    private lateinit var dataProvider: ListDataProvider<Visite>

    private val topHorizontalLayout: HorizontalLayout = HorizontalLayout()

    private val nombreResultats: Span = Span()

    private val filtresDesktopHorizontalLayout: HorizontalLayout = HorizontalLayout()
    private val filtresDesktopLayoutLigne1: HorizontalLayout = HorizontalLayout()
    private val filtresDesktopLayoutLigne2: HorizontalLayout = HorizontalLayout()

    private val filtresMobileDialog: Dialog = Dialog()
    private val filtresMobileHorizontalLayout: HorizontalLayout = HorizontalLayout()
    private val filtresActifsMobileHorizontalLayout: HorizontalLayout = HorizontalLayout()
    private val filtresMobileDialogVerticalLayout: VerticalLayout = VerticalLayout()
    private val btnAfficherFiltresMobile: Button = Button()
    private val btnEffacerFiltresMobile: Button = Button()
    private val btnEffacerFiltresDekstop: Button = Button()

    private val champFiltreDateDebut: DatePicker = DatePicker()
    private val champFiltreDateFin: DatePicker = DatePicker()
    private val champFiltreFamille: ComboBox<String> = ComboBox()
    private val champFiltreTypeContact: ComboBox<String> = ComboBox()
    private val champFiltreContact: TextField = TextField()
    private val champFiltreVendeur: ComboBox<String> = ComboBox()
    private val champFiltreValidation: ComboBox<String> = ComboBox()

    private val filtres: MutableMap<Component, Any?> = mutableMapOf(
        champFiltreDateDebut to filtresGridVisites.filtreDateDebut,
        champFiltreDateFin to filtresGridVisites.filtreDateFin,
        champFiltreFamille to filtresGridVisites.filtreFamille,
        champFiltreTypeContact to filtresGridVisites.filtreTypeContact,
        champFiltreContact to filtresGridVisites.filtreContact,
        champFiltreVendeur to filtresGridVisites.filtreVendeur,
        champFiltreValidation to filtresGridVisites.filtreValidation
    )

    private var ouvrirVisiteComponentRenderer: ComponentRenderer<Icon, Visite>? = null
    private var statutValidationComponentRenderer: ComponentRenderer<StatutValidationBadge, Visite>? = null
    private var typeContactComponentRenderer: ComponentRenderer<Span, Visite>? = null

    private val responsiveVueManager = ResponsiveVueManager()


    init {
        configData()
        setSizeFull()
        configDesktopComponentRenderers()
        configGrid()
        configStyleChampsFiltres()
        configValueChangeListenerChampsFiltres()
        configTopHorizontalLayout()
        configFiltresMobileHorizontalLayout()
        configFiltresActifsMobileHorizontalLayout()
        configFiltresDekstop()
        configFiltresMobileDialog()
        addRowClickListener()
        configLayoutGlobal()
        restaurerFiltres()
        appliquerFiltres()
    }

    private fun configLayoutGlobal() {
        responsiveVueManager.setBreakpoint(800)
        responsiveVueManager.vueMobileCallback = { statutActivation -> activerVueMobile(statutActivation) }
        responsiveVueManager.vueDesktopCallback = { statutActivation -> activerVueDesktop(statutActivation) }

        isSpacing = false

        topHorizontalLayout.style.set("row-gap", "0.2em")
        topHorizontalLayout.add(
            filtresMobileHorizontalLayout, filtresDesktopHorizontalLayout, nombreResultats
        )
        add(topHorizontalLayout, filtresActifsMobileHorizontalLayout, gridVisites)
    }

    private fun configTopHorizontalLayout() {
        nombreResultats.style.set("margin-top", "5px")
        topHorizontalLayout.setWidthFull()
        topHorizontalLayout.style.set("row-gap", "0")
        topHorizontalLayout.style.set("margin-bottom", "10px")
        topHorizontalLayout.style.set("flex-wrap", "wrap")
        topHorizontalLayout.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
    }

    private fun configFiltresMobileHorizontalLayout() {
        filtresMobileHorizontalLayout.style.set("flex-wrap", "wrap")
        filtresMobileHorizontalLayout.add(btnAfficherFiltresMobile, btnEffacerFiltresMobile)
    }

    private fun configFiltresActifsMobileHorizontalLayout() {
        filtresActifsMobileHorizontalLayout.setWidthFull()
        filtresActifsMobileHorizontalLayout.style.set("gap", "0")
        filtresActifsMobileHorizontalLayout.style.set("flex-wrap", "wrap")
        filtresActifsMobileHorizontalLayout.style.set("row-gap", "0.2em")
        filtresActifsMobileHorizontalLayout.style.set("margin-bottom", "10px")
    }


    private fun configFiltresMobileDialog() {
        btnAfficherFiltresMobile.text = "Filtrer"
        btnAfficherFiltresMobile.addClickListener { filtresMobileDialog.open() }

        btnEffacerFiltresMobile.text = "Effacer filtres"
        btnEffacerFiltresMobile.isVisible = false

        filtresMobileDialog.isCloseOnOutsideClick = true
        filtresMobileDialog.isCloseOnEsc = true
        filtresMobileDialog.isModal = true
        filtresMobileDialog.add(filtresMobileDialogVerticalLayout)
        filtresMobileDialog.footer.add(Button("Fermer") { filtresMobileDialog.close() })

        filtresMobileDialogVerticalLayout.alignItems = FlexComponent.Alignment.CENTER
        filtresMobileDialogVerticalLayout.defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
    }

    private fun configFiltresDekstop() {
        filtresDesktopHorizontalLayout.style.set("flex-wrap", "wrap")
        filtresDesktopHorizontalLayout.add(filtresDesktopLayoutLigne1, filtresDesktopLayoutLigne2)
    }

    private fun activerVueMobile(estActive: Boolean) {
        if (!estActive) return

        topHorizontalLayout.style.set("justify-content", "space-between")
        filtresMobileHorizontalLayout.isVisible = true
        filtresActifsMobileHorizontalLayout.isVisible = true
        btnAfficherFiltresMobile.isVisible = true
        btnEffacerFiltresMobile.isVisible = filtresActifs()
        filtresDesktopHorizontalLayout.isVisible = false
        gridVisites.removeAllColumns()
        gridVisites.addThemeVariants(GridVariant.LUMO_COMPACT)
        ajoutColonnesVueMobile()

        filtres.forEach { (champFiltre, _) ->
            filtresMobileDialogVerticalLayout.add(champFiltre)
            if (champFiltre is HasSize) champFiltre.width = "220px"
        }

        filtresDesktopLayoutLigne1.removeAll()
        filtresDesktopLayoutLigne2.removeAll()

        gridVisites.element.executeJs("this.shadowRoot.querySelector('thead').style.display = 'none';")
    }

    private fun activerVueDesktop(estActive: Boolean) {
        if (!estActive) return

        filtresMobileHorizontalLayout.isVisible = false
        filtresActifsMobileHorizontalLayout.isVisible = false
        topHorizontalLayout.style.set("justify-content", "flex-start")
        filtresMobileDialog.close()
        btnAfficherFiltresMobile.isVisible = false
        btnEffacerFiltresMobile.isVisible = false
        filtresDesktopHorizontalLayout.isVisible = true
        gridVisites.removeAllColumns()
        gridVisites.removeThemeVariants(GridVariant.LUMO_COMPACT)
        ajoutColonnesVueDesktop()

        filtresMobileDialogVerticalLayout.removeAll()
        filtres.forEach { (champFiltre, _) ->
            if (champFiltre is HasSize) champFiltre.width = "160px"
        }

        listOf(
            champFiltreDateDebut, champFiltreDateFin, champFiltreFamille, champFiltreTypeContact
        ).forEach { filtresDesktopLayoutLigne1.add(it) }

        listOf(
            champFiltreContact, champFiltreVendeur, champFiltreValidation, btnEffacerFiltresDekstop
        ).forEach { filtresDesktopLayoutLigne2.add(it) }

        gridVisites.element.executeJs("this.shadowRoot.querySelector('thead').style.display = 'flex';")
    }

    private fun configDesktopComponentRenderers() {
        ouvrirVisiteComponentRenderer = ComponentRenderer<Icon, Visite> { visite ->
            EditerVisiteIcon(visite)
        }
        statutValidationComponentRenderer = ComponentRenderer<StatutValidationBadge, Visite> { visite ->
            if (visite.dateValidation != null) {
                StatutValidationBadge(
                    StatutValidation.VALIDE, visite.userValidation, visite.dateValidation.toString()
                )
            } else {
                StatutValidationBadge(StatutValidation.ENATTENTE, null, null)
            }
        }
        typeContactComponentRenderer = ComponentRenderer<Span, Visite> { visite ->
            if (visite.typeContact == "Client") {
                TypeContactBadge(TypeContact.CLIENT)
            } else {
                TypeContactBadge(TypeContact.PROSPECT)
            }
        }
    }

    private fun configGrid() {
        gridVisites.element.style.set("font-size", "0.95em")
        gridVisites.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT)
        majAffichageNombreResultats()
    }

    private fun configData() {
        var visites: List<Visite?> = queryVisitesData()
        if (triInitialFrontendActif) {
            visites = visites.sortedByDescending { it?.date }
        }
        dataProvider = ListDataProvider(visites)
        gridVisites.dataProvider = dataProvider
    }

    private fun configStyleChampsFiltres() {
        configBtnEffacerFiltres(btnEffacerFiltresDekstop)
        configBtnEffacerFiltres(btnEffacerFiltresMobile)

        filtres.forEach { (champFiltre, _) ->
            champFiltre.style.setFontSize("0.9em")
            champFiltre.style["--vaadin-input-field-value-color"] = "blue"
            if (champFiltre is HasClearButton) champFiltre.isClearButtonVisible = true
        }

        val dateMinimum = dataProvider.items.mapNotNull { it.date }.minOrNull()
        val dateMaximum = dataProvider.items.mapNotNull { it.date }.maxOrNull()

        champFiltreDateDebut.placeholder = "Date début"
        champFiltreDateDebut.min = dateMinimum
        champFiltreDateDebut.max = dateMaximum

        champFiltreDateFin.placeholder = "Date fin"
        champFiltreDateFin.min = dateMinimum
        champFiltreDateFin.max = dateMaximum

        champFiltreFamille.placeholder = "Famille"
        champFiltreFamille.setItems(visiteService.findAllFamilles().mapNotNull { it?.libelle }.distinct())

        champFiltreTypeContact.placeholder = "Client/Prospect"
        champFiltreTypeContact.setItems(listOf("Client", "Prospect"))

        champFiltreContact.placeholder = "Nom Client/Prospect"
        champFiltreContact.valueChangeMode = ValueChangeMode.LAZY

        champFiltreVendeur.placeholder = "Vendeur"
        champFiltreVendeur.setItems(visiteService.findAllVendeurs().mapNotNull { it?.nom }.distinct())

        champFiltreValidation.placeholder = "Validation"
        champFiltreValidation.setItems(listOf("En attente", "Validé"))

        if (!estResponsable!!) champFiltreVendeur.isVisible = false
        if (!validationVisiteActive) champFiltreValidation.isVisible = false
    }

    /**
     * Récupère les données des visites en fonction des paramètres de l'utilisateur connecté.
     * Cette méthode vérifie si l'utilisateur est un responsable et récupère les visites en conséquence.
     * Si l'utilisateur est un responsable, cette méthode vérifie ensuite si seules les visites des vendeurs rattachés au responsable doivent être affichées.
     * Si c'est le cas, elle vérifie si les visites où l'utilisateur est vendeur2 doivent également être affichées.
     * Si l'utilisateur n'est pas un responsable, cette méthode vérifie si les visites où l'utilisateur est vendeur2 doivent être affichées.
     * @return Une liste de visites qui correspondent aux paramètres de l'utilisateur connecté.
     */
    private fun queryVisitesData(): List<Visite?> {
        if (estResponsable!!) {
            return if (afficherUniquementVisitesDesVendeursDuResponsable) {
                if (afficherVisitesQuandUserEstVendeur2DuResponsable) {
                    // Afficher les visites des vendeurs rattachés au responsable, qu'ils soient vendeur1 ou vendeur2
                    visiteService.findVisitesByCodesVendeurs1Et2(listeVendeursRattachesAuResponsable!!)
                } else {
                    // Afficher les visites des vendeurs rattachés au responsable, uniquement quand ils sont vendeur1
                    visiteService.findVisitesByCodesVendeurs(listeVendeursRattachesAuResponsable!!)
                }
            } else {
                // Afficher toutes les visites présentes en base de donnée
                visiteService.findAllVisites()
            }
        }

        return if (afficherVisitesQuandUserEstVendeur2) {
            // Afficher les visites du vendeur connecté, qu'il soit vendeur1 ou vendeur2
            visiteService.findVisitesByCodeVendeurOnVendeur1AndVendeur2(codeVendeurConnecte!!)
        } else {
            // Afficher les visites du vendeur connecté, uniquement quand il est vendeur1
            visiteService.findVisitesByCodeVendeur(codeVendeurConnecte!!)
        }
    }

    /**
     * Met à jour l'affichage du nombre de résultats dans la grille des visites.
     * Cette méthode récupère le nombre de visites actuellement affichées dans la grille (en tenant compte des filtres appliqués) et met à jour le texte du composant `nombreResultats` en conséquence.
     * Si le nombre de résultats est supérieur à 1, un "s" est ajouté à la fin du mot "résultat" pour indiquer le pluriel.
     */
    private fun majAffichageNombreResultats() {
        val nombreResultats = dataProvider.size(Query(dataProvider.filter))
        this.nombreResultats.text = "$nombreResultats résultat"
        if (nombreResultats > 1) this.nombreResultats.text += "s"
    }

    /**
     * Configure les écouteurs de changement de valeur pour les champs de filtre.
     * Chaque champ de filtre a un écouteur qui met à jour la valeur correspondante dans `filtresGridVisites` lorsque la valeur du champ change.
     * Pour les champs de date de début et de fin, des vérifications supplémentaires sont effectuées pour s'assurer que la date de début n'est pas après la date de fin et vice versa.
     * Si c'est le cas, la valeur de l'autre champ de date est mise à jour pour correspondre à la valeur modifiée.
     * Enfin, pour chaque champ de filtre, un écouteur est ajouté qui appelle `appliquerFiltres` chaque fois que la valeur du champ change.
     * Cela garantit que les filtres sont appliqués immédiatement après la modification d'un champ de filtre.
     */
    private fun configValueChangeListenerChampsFiltres() {
        champFiltreDateDebut.addValueChangeListener {
            filtresGridVisites.filtreDateDebut = it.value
            if (it.value != null && filtresGridVisites.filtreDateFin != null && it.value.isAfter(filtresGridVisites.filtreDateFin)) {
                champFiltreDateFin.value = it.value
            }
        }
        champFiltreDateFin.addValueChangeListener {
            filtresGridVisites.filtreDateFin = it.value
            if (it.value != null && filtresGridVisites.filtreDateDebut != null && it.value.isBefore(filtresGridVisites.filtreDateDebut)) {
                champFiltreDateDebut.value = it.value
            }
        }
        champFiltreFamille.addValueChangeListener { filtresGridVisites.filtreFamille = it.value }
        champFiltreTypeContact.addValueChangeListener { filtresGridVisites.filtreTypeContact = it.value }
        champFiltreContact.addValueChangeListener { filtresGridVisites.filtreContact = it.value }
        champFiltreVendeur.addValueChangeListener { filtresGridVisites.filtreVendeur = it.value }
        champFiltreValidation.addValueChangeListener { filtresGridVisites.filtreValidation = it.value }

        filtres.forEach { (champFiltre, _) ->
            if (champFiltre is HasValue<*, *>) {
                champFiltre.addValueChangeListener {
                    appliquerFiltres()
                }
            }
        }
    }

    /**
     * Restaure les valeurs des filtres à partir des données de l'objet `filtresGridVisites`.
     * Cette méthode est utilisée pour initialiser les champs de filtre avec les valeurs précédemment appliquées.
     * Chaque champ de filtre est mis à jour avec la valeur correspondante dans `filtresGridVisites`.
     * Pour le champ de filtre de contact, si la valeur dans `filtresGridVisites` est `null`, le champ est initialisé avec une chaîne vide.
     */
    private fun restaurerFiltres() {
        champFiltreDateDebut.value = filtresGridVisites.filtreDateDebut
        champFiltreDateFin.value = filtresGridVisites.filtreDateFin
        champFiltreFamille.value = filtresGridVisites.filtreFamille
        champFiltreTypeContact.value = filtresGridVisites.filtreTypeContact
        champFiltreContact.value = if (filtresGridVisites.filtreContact != null) {
            filtresGridVisites.filtreContact
        } else {
            ""
        }
        champFiltreVendeur.value = filtresGridVisites.filtreVendeur
        champFiltreValidation.value = filtresGridVisites.filtreValidation
    }

    /**
     * Configure le bouton pour effacer les filtres.
     * Cette méthode définit le texte du bouton, ajoute une variante de thème pour changer son apparence et ajoute un écouteur de clic.
     * Lorsque le bouton est cliqué, tous les filtres sont effacés, les champs de filtre sont réinitialisés et l'affichage du nombre de résultats est mis à jour.
     * Enfin, le bouton pour effacer les filtres est rendu invisible.
     * @param button Le bouton à configurer.
     */
    private fun configBtnEffacerFiltres(button: Button) {
        button.text = "Effacer filtres"
        button.addThemeVariants(ButtonVariant.LUMO_ERROR)
        button.addClickListener {
            dataProvider.clearFilters()
            filtres.forEach { (champFiltre, _) -> if (champFiltre is HasValue<*, *>) champFiltre.clear() }
            btnEffacerFiltresMobile.isVisible = false
            btnEffacerFiltresDekstop.isEnabled = false
            majAffichageNombreResultats()
        }
    }

    /**
     * Vérifie si des filtres sont actuellement actifs.
     * Cette méthode parcourt tous les champs de filtre et vérifie si une valeur non nulle et non vide est définie pour chaque champ.
     * Si une telle valeur est trouvée pour un champ, la méthode retourne `true`, indiquant qu'au moins un filtre est actif.
     * Si aucune valeur de filtre n'est trouvée, la méthode retourne `false`, indiquant qu'aucun filtre n'est actif.
     * @return `true` si au moins un filtre est actif, `false` sinon.
     */
    private fun filtresActifs(): Boolean {
        filtres.forEach { (champFiltre, _) ->
            if (champFiltre is HasValue<*, *> && champFiltre.value != null && champFiltre.value.toString()
                    .isNotBlank()
            ) return true
        }
        return false
    }

    /**
     * Applique les filtres sélectionnés à la grille des visites.
     * Cette méthode efface d'abord tous les filtres existants de la grille, puis ajoute un nouveau filtre basé sur les valeurs actuelles des champs de filtre.
     * Pour chaque champ de filtre, si une valeur non nulle est définie, la méthode vérifie si chaque visite correspond à la valeur du filtre.
     * Si une visite ne correspond pas à une valeur de filtre, elle est exclue de la grille.
     * Après l'application des filtres, la méthode met à jour l'affichage du bouton pour effacer les filtres et l'affichage du nombre de résultats.
     */
    private fun appliquerFiltres() {
        dataProvider.clearFilters()
        dataProvider.addFilter { visite ->
            var matches = true

            if (filtresGridVisites.filtreDateDebut != null) {
                matches =
                    matches && (visite.date?.isAfter(filtresGridVisites.filtreDateDebut) ?: false || visite.date?.isEqual(
                        filtresGridVisites.filtreDateDebut
                    ) ?: false)
            }

            if (filtresGridVisites.filtreDateFin != null) {
                matches =
                    matches && (visite.date?.isBefore(filtresGridVisites.filtreDateFin) ?: false || visite.date?.isEqual(
                        filtresGridVisites.filtreDateFin
                    ) ?: false)
            }

            if (filtresGridVisites.filtreFamille != null) {
                val familleLibelle = visite.client?.famille?.libelle ?: visite.prospect?.famille?.libelle
                matches = matches && (familleLibelle == filtresGridVisites.filtreFamille)
            }

            if (filtresGridVisites.filtreTypeContact != null) {
                matches = matches && (visite.typeContact == filtresGridVisites.filtreTypeContact)
            }

            if (filtresGridVisites.filtreContact != null) {
                matches =
                    matches && ((visite.client?.nom?.contains(filtresGridVisites.filtreContact!!, ignoreCase = true)
                        ?: false) || (visite.prospect?.nom?.contains(
                        filtresGridVisites.filtreContact!!, ignoreCase = true
                    ) ?: false))
            }

            if (filtresGridVisites.filtreVendeur != null) {
                matches =
                    matches && ((visite.vendeur1?.nom == filtresGridVisites.filtreVendeur) || (visite.vendeur2?.nom == filtresGridVisites.filtreVendeur))
            }

            if (filtresGridVisites.filtreValidation != null) {
                matches =
                    matches && ((filtresGridVisites.filtreValidation == "En attente" && visite.dateValidation == null) || (filtresGridVisites.filtreValidation == "Validé" && visite.dateValidation != null))
            }

            matches
        }

        if (responsiveVueManager.getEstVueMobile() == true) {
            btnEffacerFiltresMobile.isVisible = filtresActifs()
        }
        btnEffacerFiltresDekstop.isEnabled = filtresActifs()
        majAffichageNombreResultats()
        majAffichagesFiltresActifsMobile()
    }

    /**
     * Met à jour l'affichage des filtres actifs pour la vue mobile.
     * Cette méthode parcourt tous les champs de filtre et vérifie si une valeur non nulle et non vide est définie pour chaque champ.
     * Si une telle valeur est trouvée pour un champ, un label est créé avec la valeur du champ et ajouté à `filtresActifsMobileHorizontalLayout`.
     * Pour les champs de type `DatePicker`, la valeur est formatée en une chaîne de caractères représentant la date et préfixée par "Début" ou "Fin" selon le champ.
     * Pour les autres types de champs, la valeur est simplement convertie en chaîne de caractères.
     * Chaque label créé est un `ClosableLabel`, ce qui signifie qu'il a un bouton de fermeture associé.
     * Lorsque ce bouton est cliqué, la valeur du champ de filtre correspondant est effacée, ce qui déclenche la mise à jour des filtres.
     */
    private fun majAffichagesFiltresActifsMobile() {
        filtresActifsMobileHorizontalLayout.removeAll()
        filtres.forEach { (champFiltre, _) ->
            if (champFiltre is HasValue<*, *> && champFiltre.value != null && champFiltre.value.toString()
                    .isNotBlank()
            ) {
                val valeurChamp: String = if (champFiltre is DatePicker) {
                    val dateString = champFiltre.value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
                    if (champFiltre == champFiltreDateDebut) {
                        "Début $dateString"
                    } else {
                        "Fin $dateString"
                    }
                } else {
                    champFiltre.value.toString()
                }
                val closableLabel = ClosableLabel(valeurChamp)
                closableLabel.closeLabelCallback = { champFiltre.clear() }
                filtresActifsMobileHorizontalLayout.add(closableLabel)
            }
        }
    }

    /**
     * Navigue vers la page de la visite correspondant à l'ID donné.
     * Si l'ID de la visite est `null`, une notification d'erreur est affichée.
     * Sinon, la méthode navigue vers la page de saisie de la visite correspondant à l'ID.
     * @param visiteID L'ID de la visite pour laquelle naviguer.
     */
    private fun naviguerVersVisiteID(visiteID: Int?) {
        if (visiteID == null) {
            Notification.show("Visite non trouvée", 3000, Notification.Position.MIDDLE).addThemeVariants(
                NotificationVariant.LUMO_ERROR
            )
        }
        UI.getCurrent().navigate("saisie-visite/${visiteID}")
    }


    /**
     * Ajoute des écouteurs d'événements de clic sur les lignes de la grille des visites.
     * Cette méthode ajoute deux types d'écouteurs :
     * - Un écouteur de double-clic, qui navigue vers la page de la visite correspondant à l'ID de la visite de la ligne double-cliquée.
     * - Un écouteur de clic simple, qui est seulement actif si la vue mobile est active et que la propriété `ouvrirVisiteAuClicSurLigneMobile` est `true`. Dans ce cas, un clic simple navigue également vers la page de la visite correspondant à l'ID de la visite de la ligne cliquée.
     */
    private fun addRowClickListener() {
        gridVisites.addItemDoubleClickListener { event ->
            val visiteID = event.item.id
            naviguerVersVisiteID(visiteID?.toInt())
        }

        // Vérifie si la vue mobile est active et que la propriété `ouvrirVisiteAuClicSurLigneMobile` est `true`
        gridVisites.addItemClickListener { event ->
            if (ouvrirVisiteAuClicSurLigneMobile && responsiveVueManager.getEstVueMobile() == true) {
                val visiteID = event.item.id
                naviguerVersVisiteID(visiteID?.toInt())
            }
        }
    }

    /**
     * Ajoute des colonnes à la grille des visites pour la vue de bureau.
     * Cette méthode ajoute plusieurs colonnes à la grille des visites, chacune correspondant à un champ de visite.
     * Chaque colonne est configurée avec un rendu de composant, un en-tête et un comparateur pour le tri.
     * Les colonnes ajoutées sont : "edit", "date", "typeContact", "codeClientProspect", "Client/Prospect", "Nom contact", "Vendeur" (si l'utilisateur est un responsable), "Validation" (si la validation de la visite est active) et "famille".
     * Enfin, la méthode configure la largeur de chaque colonne en fonction de son ID.
     * Les colonnes avec un ID ont une largeur automatique et une croissance flexible de 0, tandis que les autres colonnes ont une croissance flexible de 1.
     * Note : Les valeurs dans les colonnes sont des chaînes de caractères et non des objets. Par exemple, si on veut filtrer par Famille client/prospect et que deux familles ont le même nom, il n'y aura qu'un filtre.
     */
    private fun ajoutColonnesVueDesktop() {
        // TODO Attention, les valeurs dans les colonnes sont des String et non des objets. Par exemple, si on veut filtrer par Famille client/prospect et que deux familles ont le même nom, il n'y aura qu'un filtre.
        gridVisites.addColumn(ouvrirVisiteComponentRenderer).setId("edit")

        gridVisites.addColumn { it.date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }.setHeader("Date")
            .setComparator { v1, v2 -> v1.date?.compareTo(v2.date) ?: 0 }.setId("date")

        gridVisites.addColumn(typeContactComponentRenderer).setComparator { v1, v2 ->
            val v1TypeContact = if (v1.typeContact == "Client") "Client" else "Prospect"
            val v2TypeContact = if (v2.typeContact == "Client") "Client" else "Prospect"
            v1TypeContact.compareTo(v2TypeContact)
        }.setId("typeContact")

        gridVisites.addColumn {
            if (it.typeContact == "Client") {
                it.client?.code
            } else {
                it.prospect?.code
            }
        }.setComparator { v1, v2 ->
            val v1TypeContact = if (v1.typeContact == "Client") v1.client?.code ?: 0 else v1.prospect?.code ?: 0
            val v2TypeContact = if (v2.typeContact == "Client") v2.client?.code ?: 0 else v2.prospect?.code ?: 0
            v1TypeContact.compareTo(v2TypeContact)
        }.setHeader("Code").setId("codeClientProspect")

        gridVisites.addColumn {
            if (it.typeContact == "Client") {
                it.client?.nom
            } else {
                it.prospect?.nom
            }
        }.setComparator { v1, v2 ->
            val v1Contact = if (v1.typeContact == "Client") v1.client?.nom else v1.prospect?.nom
            val v2Contact = if (v2.typeContact == "Client") v2.client?.nom else v2.prospect?.nom
            v1Contact?.compareTo(v2Contact ?: "") ?: 0
        }.setHeader("Client/Prospect")

        gridVisites.addColumn {
            it.nomContact
        }.setComparator { v1, v2 ->
            v1.nomContact?.compareTo(v2.nomContact ?: "") ?: 0
        }.setHeader("Nom contact")

        if (estResponsable!!) {
            gridVisites.addColumn { it.vendeur1?.nom }.setComparator { v1, v2 ->
                val v1Vendeur = v1.vendeur1?.nom
                val v2Vendeur = v2.vendeur1?.nom
                v1Vendeur?.compareTo(v2Vendeur ?: "") ?: 0
            }.setHeader("Vendeur")
        }

        if (validationVisiteActive) {
            gridVisites.addColumn(statutValidationComponentRenderer).setComparator { v1, v2 ->
                val v1ValidationDate = v1.dateValidation
                val v2ValidationDate = v2.dateValidation
                when {
                    v1ValidationDate == null -> if (v2ValidationDate == null) 0 else -1
                    v2ValidationDate == null -> 1
                    else -> v1ValidationDate.compareTo(v2ValidationDate)
                }
            }.setHeader("Validation")
        }

        gridVisites.addColumn {
            if (it.typeContact == "Client") {
                it.client?.famille?.libelle
            } else {
                it.prospect?.famille?.libelle
            }
        }.setComparator { v1, v2 ->
            val v1Famille =
                if (v1.typeContact == "Client") v1.client?.famille?.libelle else v1.prospect?.famille?.libelle
            val v2Famille =
                if (v2.typeContact == "Client") v2.client?.famille?.libelle else v2.prospect?.famille?.libelle
            v1Famille?.compareTo(v2Famille ?: "") ?: 0
        }.setHeader("Famille").setId("famille")

        gridVisites.columns.forEach {
            // Les colonnes avec un id ont une règle de largeur différente
            if (it.id != null && it.id.isPresent) {
                it.isAutoWidth = true
                it.flexGrow = 0
            } else {
                it.flexGrow = 1
            }
        }
    }

    /**
     * Ajoute une colonne à la grille des visites pour la vue mobile.
     * Cette méthode crée une nouvelle colonne avec un `MobileGridComponentRenderer` qui est configuré avec les valeurs de `estResponsable` et `validationVisiteActive`.
     * La colonne créée est ajoutée à la grille des visites.
     * Ensuite, la méthode configure la largeur de la colonne pour qu'elle prenne toute la largeur disponible.
     */
    private fun ajoutColonnesVueMobile() {
        val col = gridVisites.addColumn(MobileGridComponentRenderer(estResponsable!!, validationVisiteActive))
        //col.isAutoWidth = true
        col.flexGrow = 1
    }
}


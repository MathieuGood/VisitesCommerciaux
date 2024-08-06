package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.contextmenu.SubMenu
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.theme.lumo.LumoUtility
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.dummydata.DummyDataInjector
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.TaillePoliceButton
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security.InfosUtilisateur
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security.SecurityService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.GestionTaillePolice
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import org.springframework.beans.factory.annotation.Autowired

/**
 * TopBarLayout est une classe qui représente la barre supérieure de l'application CRM Visites.
 * Elle hérite de AppLayout, une classe de Vaadin qui permet de créer des mises en page d'application.
 *
 * @property securityService Le service utilisé pour gérer la sécurité de l'application, notamment la déconnexion.
 * @property visiteService Le service utilisé pour interagir avec les données de visite.
 * @property lectureApplicationProperties Les propriétés de l'application lues à partir des fichiers de configuration.
 * @property infosUtilisateur Les informations de l'utilisateur connecté.
 *
 * @constructor Crée une nouvelle instance de TopBarLayout avec les services et les propriétés spécifiés.
 * @param securityService Le service de sécurité à utiliser.
 * @param visiteService Le service de visite à utiliser.
 * @param lectureApplicationProperties Les propriétés de l'application à utiliser.
 * @param infosUtilisateur Les informations de l'utilisateur connecté.
 *
 * @Autowired Cette annotation est utilisée pour l'injection automatique des beans par Spring. Ici, elle est utilisée pour injecter les informations de l'utilisateur connecté.
 */
class TopBarLayout(
    private val securityService: SecurityService,
    private val visiteService: VisiteService,
    private val lectureApplicationProperties: LectureApplicationProperties,
    @Autowired private val infosUtilisateur: InfosUtilisateur,
    @Autowired private val parametresAccessibilite: ParametresAccessibilite
) : AppLayout() {

    private val btnRetour: Button = Button()
    private val etiquetteNomUtilisateur: Span = Span()
    private val dialogDeconnexion: Dialog = Dialog()
    private val header: HorizontalLayout = HorizontalLayout()
    private val texteHeader: Span = Span()
    private val btnMenuDebug: MenuBar = MenuBar()
    private val btnMenuUser: MenuBar = MenuBar()
    private val gestionTaillePolice = GestionTaillePolice(parametresAccessibilite)

    companion object {
        val LOG by lazyLogger()
    }

    init {
        if (lectureApplicationProperties.properties.main.modeDemo) {
            DummyDataInjector(visiteService, lectureApplicationProperties).injectData()
        }

        configTaillePolice()
        configMenuDebug()
        configMenuUser()
        configDialogDeconnexion()
        configHeader()
        addToNavbar(header)

        // TODO À supprimer avant la mise en production (cache les outils de développement de Vaadin)
        //val removeDevToolsGizmo = Html(" <style>vaadin-dev-tools {display: none}</style>")
        //addToNavbar(removeDevToolsGizmo)
    }

    private fun configTaillePolice() {
        gestionTaillePolice.changerTaillePolice(parametresAccessibilite.niveauTaillePolice)
    }

    private fun configDialogDeconnexion() {
        dialogDeconnexion.headerTitle = "Déconnexion"
        val btnConfirmerDeconnexion = Button("Confirmer") { securityService.logout() }
        btnConfirmerDeconnexion.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        val btnAnnulerDeconnexion = Button("Annuler") { dialogDeconnexion.close() }
        dialogDeconnexion.add(Span("Êtes-vous sûr de vouloir vous déconnecter ?"))
        dialogDeconnexion.footer.add(btnAnnulerDeconnexion, btnConfirmerDeconnexion)
    }

    /**
     * Cette méthode privée est utilisée pour configurer le menu de débogage.
     * Elle crée un élément de menu principal "Debug" et un sous-menu pour cet élément.
     * Dans le sous-menu, elle ajoute un élément "Print Entities" qui, lorsqu'il est sélectionné, imprime toutes les entités en utilisant la classe DummyDataInjector.
     * Si l'application n'est pas en mode de débogage (déterminé par les propriétés de l'application), le menu n'est pas affiché.
     *
     * @see DummyDataInjector pour plus d'informations sur l'impression de toutes les entités.
     */
    private fun configMenuDebug() {
        val mainItem: MenuItem = btnMenuDebug.addItem("Debug")
        val subMenu: SubMenu = mainItem.subMenu
        subMenu.addItem("Print Entities") {
            DummyDataInjector(visiteService, lectureApplicationProperties).printAllEntities()
        }

        if (!lectureApplicationProperties.properties.main.modeDebug) {
            btnMenuDebug.isVisible = false
        }
    }

    private fun configMenuUser() {
        val mainItem: MenuItem = btnMenuUser.addItem(Icon(VaadinIcon.USER))
        val subMenu: SubMenu = mainItem.subMenu

        val btnDeconnexion = Button("Déconnexion")
        btnDeconnexion.setWidthFull()
        btnDeconnexion.icon = VaadinIcon.SIGN_OUT.create()
        btnDeconnexion.addThemeVariants(ButtonVariant.LUMO_ERROR)
        btnDeconnexion.addClickListener { dialogDeconnexion.open() }

        val btnAide = Button("Aide")
        btnAide.setWidthFull()
        btnAide.icon = VaadinIcon.QUESTION.create()
        btnAide.addClickListener {
            LOG.debug("Clic pour ouverture 'Aide'")
            // TODO Implémenter l'aide
        }

        val btnAPropos = Button("À propos")
        btnAPropos.setWidthFull()
        btnAPropos.icon = VaadinIcon.EXCLAMATION_CIRCLE.create()
        btnAPropos.addClickListener {
            LOG.debug("Click pour ouverture 'À Propos'")
            // TODO Implémenter À propos
        }
        val btnTaillePolice = TaillePoliceButton(parametresAccessibilite, gestionTaillePolice)

        subMenu.addItem(btnAide)
        subMenu.addItem(btnAPropos)
        subMenu.addItem(btnTaillePolice)
        subMenu.addItem(btnDeconnexion)
    }

    private fun configHeader() {
        btnRetour.icon = VaadinIcon.HOME.create()
        btnRetour.addClickListener {
            LOG.debug("Clic btnRetourAccueil")
            UI.getCurrent().navigate("")
        }

        texteHeader.text = "CRM Visites"
        header.expand(texteHeader)

        etiquetteNomUtilisateur.text = infosUtilisateur.nomComplet

        header.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Width.FULL
        )

        header.add(
            btnRetour, texteHeader, btnMenuDebug, etiquetteNomUtilisateur, btnMenuUser
        )
    }
}

package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.dummydata.DummyDataInjector
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.GestionTaillePolice
import org.springframework.beans.factory.annotation.Autowired


/**
 * Cette classe représente la vue de connexion de l'application.
 * Elle est mappée à la route "login" et contient un formulaire de connexion.
 *
 * @property visiteService Le service utilisé pour gérer les visites dans l'application.
 * @property lectureApplicationProperties Les propriétés de l'application, utilisées pour déterminer si l'application est en mode démo.
 * @property parametresAccessibilite Les paramètres d'accessibilité actuels de l'application.
 *
 * @see VerticalLayout pour plus d'informations sur la classe parente.
 * @see BeforeEnterObserver pour plus d'informations sur l'interface implémentée.
 */
@Route("login")
@PageTitle("Connexion | CRM Visites")
class VueLogin(
    private val visiteService: VisiteService,
    private val lectureApplicationProperties: LectureApplicationProperties,
    @Autowired private val parametresAccessibilite: ParametresAccessibilite
) : VerticalLayout(), BeforeEnterObserver {
    private val loginForm = LoginForm()
    private val gestionTaillePolice = GestionTaillePolice(parametresAccessibilite)

    init {
        injecterDonneesDemo()
        configTaillePolice()
        configLayout()
        configLoginForm()
        add(H1("CRM Visites"), loginForm)

        // TODO À supprimer avant la mise en production (cache les outils de développement de Vaadin)
        //val removeDevToolsGizmo = Html(" <style>vaadin-dev-tools {display: none}</style>")
        //add(removeDevToolsGizmo)
    }

    private fun configLayout() {
        addClassName("vue-login")
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = JustifyContentMode.CENTER
    }

    private fun configTaillePolice() {
        gestionTaillePolice.changerTaillePolice(parametresAccessibilite.niveauTaillePolice)
    }

    /**
     * Cette méthode privée est utilisée pour injecter des données de démonstration dans l'application.
     * Elle vérifie d'abord si l'application est en mode démo en utilisant les propriétés de l'application.
     * Si l'application est en mode démo, elle utilise la classe DummyDataInjector pour injecter des données factices.
     * Cette injection de données est utile pour tester l'application sans avoir à saisir manuellement des données.
     *
     * @see DummyDataInjector pour plus d'informations sur l'injection de données factices.
     */
    private fun injecterDonneesDemo() {
        if (lectureApplicationProperties.properties.main.modeDemo) {
            DummyDataInjector(visiteService, lectureApplicationProperties).injectData()
        }
    }

    /**
     * Cette méthode est appelée avant l'entrée dans la vue.
     * Elle vérifie si le paramètre "error" est présent dans les paramètres de requête de l'URL.
     * Si le paramètre "error" est présent, elle définit la propriété isError du formulaire de connexion à true.
     * Cela permet d'afficher un message d'erreur sur le formulaire de connexion.
     *
     * @param beforeEnterEvent L'événement déclenché avant l'entrée dans la vue.
     * @see BeforeEnterEvent pour plus d'informations sur les événements avant l'entrée dans une vue.
     */
    override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
        if (beforeEnterEvent.location.queryParameters.parameters.containsKey("error")) {
            loginForm.isError = true
        }
    }

    private fun configLoginForm() {
        val i18n = LoginI18n.createDefault()

        val i18nForm = i18n.form
        i18nForm.title = "Connexion"
        i18nForm.username = "Nom d'utilisateur"
        i18nForm.password = "Mot de passe"
        i18nForm.submit = "Se connecter"
        i18nForm.forgotPassword = "Mot de passe oublié ?"
        i18n.form = i18nForm

        val i18nErrorMessage = i18n.errorMessage
        i18nErrorMessage.title = "Nom d'utilisateur ou mot de passe incorrect"
        i18nErrorMessage.message =
            "Vérifiez que votre nom d'utilisateur et votre mot de passe sont corrects et réessayez."
        i18nErrorMessage.username = "Veuillez renseigner votre nom d'utilisateur"
        i18nErrorMessage.password = "Veuillez entrer votre mot de passe"
        i18n.errorMessage = i18nErrorMessage

        loginForm.setI18n(i18n)
        loginForm.action = "login"
    }

}

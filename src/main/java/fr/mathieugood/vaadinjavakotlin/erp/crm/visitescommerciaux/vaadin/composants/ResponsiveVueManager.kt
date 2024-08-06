package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.UI
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger

/**
 * Cette classe `ResponsiveVueManager` gère les vues réactives en fonction de la largeur de la fenêtre du navigateur.
 * Elle définit des callbacks pour les vues mobiles et de bureau, et met à jour la vue en fonction de la largeur actuelle de la fenêtre.
 *
 * @property estVueMobile Un indicateur pour savoir si la vue mobile est active.
 * @property breakpoint La largeur de la fenêtre à laquelle la vue doit passer de bureau à mobile et vice versa.
 * @property vueMobileCallback Un callback qui est appelé lorsque la vue mobile est activée.
 * @property vueDesktopCallback Un callback qui est appelé lorsque la vue de bureau est activée.
 */
class ResponsiveVueManager {

    private var estVueMobile: Boolean? = null
    private var breakpoint: Int = 800
    var vueMobileCallback: (estActive: Boolean) -> Unit = {}
    var vueDesktopCallback: (estActive: Boolean) -> Unit = {}

    companion object {
        val LOG by lazyLogger()
    }

    init {
        UI.getCurrent().page.executeJs(
            """
            window.getViewportWidth = function() {
                return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
            }
        """
        )

        majVueEnFonctionDeLargeurActuelle()
        gestionLargeurPage()
    }

    /**
     * Cette méthode privée `majVueEnFonctionDeLargeurActuelle` est utilisée pour mettre à jour la vue en fonction de la largeur actuelle de la fenêtre du navigateur.
     * Elle exécute un script JavaScript pour obtenir la largeur du viewport, puis active la vue appropriée (mobile ou de bureau) en fonction de cette largeur.
     */
    private fun majVueEnFonctionDeLargeurActuelle() {
        UI.getCurrent().page.executeJs("return window.getViewportWidth();").then(Int::class.java) { largeur ->
            LOG.debug("majVueEnFonctionDeLargeurActuelle AVEC JS : $largeur")
            activerVueSelonLargeurViewport(largeur)
        }
    }

    fun getEstVueMobile(): Boolean? {
        return estVueMobile
    }

    fun setBreakpoint(breakpoint: Int) {
        this.breakpoint = breakpoint
    }

    /**
     * Cette méthode privée `gestionLargeurPage` est utilisée pour gérer le redimensionnement de la page.
     * Elle ajoute un écouteur de redimensionnement de la fenêtre du navigateur à la page actuelle.
     * Lorsque la fenêtre est redimensionnée, elle met à jour la variable `estVueMobile` et active la vue appropriée (mobile ou de bureau) en fonction de la nouvelle largeur de la fenêtre.
     */
    private fun gestionLargeurPage() {
        UI.getCurrent().page.addBrowserWindowResizeListener { e ->
            LOG.debug("Taille Viewport largeur=${e.width} hauteur=${e.height}")
            activerVueSelonLargeurViewport(e.width)
        }
    }

    /**
     * Cette méthode privée `activerVueSelonLargeurViewport` est utilisée pour activer la vue appropriée en fonction de la largeur du viewport.
     * Elle détermine si la vue mobile doit être activée en comparant la largeur du viewport au point de rupture.
     * Si la largeur du viewport est inférieure ou égale au point de rupture, la vue mobile est activée.
     * Sinon, la vue de bureau est activée.
     *
     * @param largeurViewport La largeur actuelle du viewport.
     */
    private fun activerVueSelonLargeurViewport(largeurViewport: Int) {
        val estVueMobileNouveau: Boolean = largeurViewport <= breakpoint
        LOG.debug("activerVueSelonLargeurViewport : $largeurViewport")
        LOG.debug("  >> estVueMobile : $estVueMobile")
        LOG.debug("  >> estVueMobileNouveau : $estVueMobileNouveau")
        if (estVueMobileNouveau != estVueMobile) {
            estVueMobile = estVueMobileNouveau
            if (estVueMobileNouveau) {
                LOG.debug("    >> Activation Vue MOBILE : viewport $largeurViewport <= breakpoint $breakpoint")
                vueDesktopCallback(false)
                vueMobileCallback(true)
            } else {
                LOG.debug("    >> Activation Vue DESKTOP : viewport $largeurViewport > breakpoint $breakpoint")
                vueMobileCallback(false)
                vueDesktopCallback(true)
            }
        }
    }
}
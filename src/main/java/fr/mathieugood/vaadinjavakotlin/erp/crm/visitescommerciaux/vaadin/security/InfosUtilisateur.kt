package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security

import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.VaadinSessionScope
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Vendeur


/**
 * @SpringComponent Cette annotation indique que cette classe est un composant Spring.
 * Cela signifie que Spring va automatiquement créer une instance de cette classe et la gérer.
 *
 * @VaadinSessionScope Cette annotation indique que le composant Spring est lié à la portée de la session Vaadin.
 * Cela signifie qu'une nouvelle instance de cette classe sera créée pour chaque session utilisateur.
 *
 * La classe InfosUtilisateur est utilisée pour stocker et gérer les informations de l'utilisateur connecté.
 * Elle utilise le service de sécurité pour récupérer les informations de l'utilisateur.
 *
 * @property securityService Le service de sécurité utilisé pour récupérer les informations de l'utilisateur.
 *
 * @constructor Crée une nouvelle instance de InfosUtilisateur avec le service de sécurité spécifié.
 * @param securityService Le service de sécurité à utiliser.
 */
@SpringComponent
@VaadinSessionScope
class InfosUtilisateur(private val securityService: SecurityService) {

    var _instanceVendeur: Vendeur? = null
    val instanceVendeur: Vendeur?
        get() {
            if (_instanceVendeur == null) {
                _instanceVendeur = securityService.vendeurInstance
            }
            return _instanceVendeur
        }

    var _codeVendeur: String? = null
    val codeVendeur: String?
        get() {
            if (_codeVendeur == null) {
                _codeVendeur = instanceVendeur?.code
            }
            return _codeVendeur
        }

    var _nomComplet: String? = null
    val nomComplet: String?
        get() {
            if (_nomComplet == null) {
                _nomComplet = instanceVendeur?.nom
            }
            return _nomComplet
        }

    var _estResponsable: Boolean? = null
    val estResponsable: Boolean?
        get() {
            if (_estResponsable == null) {
                _estResponsable = securityService.estResponsable
            }
            return _estResponsable
        }

    var _vendeursSousResponsabilite: List<String>? = null
    val vendeursSousResponsabilite: List<String>?
        get() {
            if (estResponsable == true) {
                if (_vendeursSousResponsabilite == null) {
                    _vendeursSousResponsabilite = securityService.vendeursSousResponsabilite
                }
                return _vendeursSousResponsabilite
            } else {
                return emptyList()
            }
        }

}
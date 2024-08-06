package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository.UtilisateurRepository
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

/**
 * Cette classe `AuthentificationUtilisateur` est un composant Spring qui implémente `AuthenticationProvider`.
 * Elle est utilisée pour gérer l'authentification des utilisateurs.
 *
 * Elle prend en paramètre un objet `UtilisateurRepository` qui est utilisé pour récupérer les informations des utilisateurs à partir de la base de données.
 *
 * @param utilisateurRepository Le repository utilisé pour récupérer les informations des utilisateurs.
 */
@Component
class AuthentificationUtilisateur(val utilisateurRepository: UtilisateurRepository) : AuthenticationProvider {

    companion object {
        val LOG by lazyLogger()
    }

    /**
     * Cette méthode `authenticate` est utilisée pour authentifier un utilisateur.
     *
     * Elle prend en paramètre un objet `Authentication` qui contient les informations d'authentification de l'utilisateur.
     * Elle récupère le nom d'utilisateur et le mot de passe à partir de cet objet.
     * Ensuite, elle utilise le `UtilisateurRepository` pour récupérer l'utilisateur correspondant à partir de la base de données.
     * Si l'utilisateur n'est pas trouvé ou si le mot de passe est incorrect, elle renvoie null.
     * Sinon, elle crée une nouvelle instance de `UsernamePasswordAuthenticationToken` avec le nom d'utilisateur, le mot de passe et la liste des rôles de l'utilisateur, puis la renvoie.
     *
     * @param authentication L'objet contenant les informations d'authentification de l'utilisateur.
     * @return Un objet `Authentication` si l'authentification est réussie, null sinon.
     */
    override fun authenticate(authentication: Authentication): Authentication? {
        val nomUtilisateur: String = authentication.principal.toString()
        val motDePasse: String = authentication.credentials.toString()
        val utilisateur = utilisateurRepository.findByNomUtilisateur(nomUtilisateur)
        LOG.debug("Authentification -> username : $nomUtilisateur")

        if (utilisateur.isEmpty) {
            LOG.debug("Utilisateur absent de la base de données")
            return null
        }
        if (utilisateur.get().motDePasse != motDePasse) {
            LOG.debug("Mot de passe incorrect")
            return null
        }

        LOG.debug("Mot de passe correct")
        val role: String = if (utilisateur.get().estReponsable == true) "RESPONSABLE" else "USER"
        val listRole: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(role))
        return UsernamePasswordAuthenticationToken(nomUtilisateur, motDePasse, listRole)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
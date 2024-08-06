package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Cette interface `UtilisateurRepository` sert de repository pour l'entité `Utilisateur`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Utilisateur? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface UtilisateurRepository : JpaRepository<Utilisateur?, Long?> {

    /**
     * Cette fonction `findByNomUtilisateur` est utilisée pour trouver un utilisateur par son nom.
     *
     * @param nomUtilisateur Le nom de l'utilisateur à rechercher.
     * @return Optional<Utilisateur?> Retourne un `Optional` qui peut contenir l'utilisateur si trouvé.
     */
    fun findByNomUtilisateur(nomUtilisateur: String?): Optional<Utilisateur?>
}
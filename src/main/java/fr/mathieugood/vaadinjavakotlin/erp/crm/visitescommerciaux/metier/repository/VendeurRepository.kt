package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Vendeur
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * Cette interface `VendeurRepository` sert de repository pour l'entité `Vendeur`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Vendeur? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface VendeurRepository : JpaRepository<Vendeur?, Long?> {
    /**
     * Cette fonction `findByCode` est utilisée pour trouver un vendeur par son code.
     *
     * @param code Le code du vendeur à rechercher.
     * @return Optional<Vendeur?> Retourne un `Optional` qui peut contenir le vendeur si trouvé.
     */
    fun findByCode(code: String?): Optional<Vendeur?>
}


package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Pays
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `PaysRepository` sert de repository pour l'entité `Pays`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Pays? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface PaysRepository : JpaRepository<Pays, Long> {
    fun findByCode(code: String?): Pays?
}

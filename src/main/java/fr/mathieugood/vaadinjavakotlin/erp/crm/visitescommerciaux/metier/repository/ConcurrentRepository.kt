package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Concurrent
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `ConcurrentRepository` sert de repository pour l'entité `Concurrent`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Concurrent? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface ConcurrentRepository : JpaRepository<Concurrent?, Long?>

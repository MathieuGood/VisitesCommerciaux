package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Client
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `ClientRepository` sert de repository pour l'entité `Client`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Client? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface ClientRepository : JpaRepository<Client?, Long?>

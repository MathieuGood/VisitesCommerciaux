package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Prospect
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `ProspectRepository` sert de repository pour l'entité `Prospect`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Prospect? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface ProspectRepository : JpaRepository<Prospect?, Long?>

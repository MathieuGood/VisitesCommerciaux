package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Famille
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `FamilleRepository` sert de repository pour l'entité `Famille`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Famille? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface FamilleRepository : JpaRepository<Famille?, Long?>
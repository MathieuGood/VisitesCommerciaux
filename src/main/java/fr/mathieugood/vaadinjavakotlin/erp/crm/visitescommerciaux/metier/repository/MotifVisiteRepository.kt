package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.MotifVisite
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `MotifVisiteRepository` sert de repository pour l'entité `MotifVisite`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property MotifVisite? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface MotifVisiteRepository : JpaRepository<MotifVisite?, Long?>

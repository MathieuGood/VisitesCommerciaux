package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository


import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.MotifProchaineVisite
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `MotifProchaineVisiteRepository` sert de repository pour l'entité `MotifProchaineVisite`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property MotifProchaineVisite? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface MotifProchaineVisiteRepository : JpaRepository<MotifProchaineVisite?, Long?>
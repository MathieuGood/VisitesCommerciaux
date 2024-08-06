package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.GammeProduit
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `GammeProduitRepository` sert de repository pour l'entité `GammeProduit`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property GammeProduit? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface GammeProduitRepository : JpaRepository<GammeProduit?, Long?>

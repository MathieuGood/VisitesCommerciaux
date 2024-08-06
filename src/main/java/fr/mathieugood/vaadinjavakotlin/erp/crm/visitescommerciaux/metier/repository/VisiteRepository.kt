package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Cette interface `VisiteRepository` sert de repository pour l'entité `Visite`.
 * Elle hérite de `JpaRepository` qui fournit des méthodes pour les opérations CRUD.
 *
 * @property Visite? Le type de l'entité pour laquelle ce repository est utilisé.
 * @property Long? Le type de l'identifiant de l'entité.
 */
interface VisiteRepository : JpaRepository<Visite?, Long?> {
    /**
     * Cette fonction `findAllVisitesByVendeur1Code` est utilisée pour récupérer toutes les visites où le code du vendeur 1 est égal à `codeVendeur`.
     *
     * @param codeVendeur Le code du vendeur à rechercher.
     * @return List<Visite?> Retourne une liste de `Visite` qui peuvent être null.
     */
    fun findAllVisitesByVendeur1Code(codeVendeur: String): List<Visite?>

    /**
     * Cette fonction `findAllVisitesByVendeur1CodeOrVendeur2Code` est utilisée pour récupérer toutes les visites où le code du vendeur 1 ou le code du vendeur 2 est égal à `codeVendeur1` ou `codeVendeur2`.
     *
     * @param codeVendeur1 Le code du premier vendeur à rechercher.
     * @param codeVendeur2 Le code du deuxième vendeur à rechercher.
     * @return List<Visite?> Retourne une liste de `Visite` qui peuvent être null.
     */
    fun findAllVisitesByVendeur1CodeOrVendeur2Code(codeVendeur1: String, codeVendeur2: String): List<Visite?>

    /**
     * Cette fonction `findAllVisitesByVendeur1CodeInOrVendeur2CodeIn` est utilisée pour récupérer toutes les visites où le code du vendeur 1 ou le code du vendeur 2 est dans une liste de codes vendeurs.
     *
     * @param codesVendeurs1 La liste des codes du premier vendeur à rechercher.
     * @param codesVendeurs2 La liste des codes du deuxième vendeur à rechercher.
     * @return List<Visite?> Retourne une liste de `Visite` qui peuvent être null.
     */
    fun findAllVisitesByVendeur1CodeInOrVendeur2CodeIn(
        codesVendeurs1: List<String>, codesVendeurs2: List<String>
    ): List<Visite?>

    /**
     * Cette fonction `findAllVisitesByVendeur1CodeIn` est utilisée pour récupérer toutes les visites où le code du vendeur 1 est dans une liste de codes vendeurs.
     *
     * @param codesVendeurs La liste des codes du vendeur à rechercher.
     * @return List<Visite?> Retourne une liste de `Visite` qui peuvent être null.
     */
    fun findAllVisitesByVendeur1CodeIn(codesVendeurs: List<String>): List<Visite?>
}
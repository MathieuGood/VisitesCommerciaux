package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import jakarta.validation.Validation


/**
 * Vérifie les contraintes de validation JPA sur une instance d'entité `Visite`.
 * Cette méthode utilise la bibliothèque Jakarta Validation pour créer un validateur et vérifier les contraintes sur l'instance d'entité.
 * Si des violations de contraintes sont trouvées, elles sont imprimées dans la console et la méthode renvoie false.
 * Si aucune violation de contrainte n'est trouvée, la méthode renvoie true.
 * @param instanceEntite L'instance d'entité `Visite` à vérifier. Ne doit pas être null.
 * @return Un booléen indiquant si l'instance d'entité respecte toutes les contraintes de validation JPA (true) ou non (false).
 */
fun verifierContraintesEntite(instanceEntite: Visite): Boolean {
    val validator = Validation.buildDefaultValidatorFactory().validator
    val violations = validator.validate(instanceEntite)
    if (violations.isNotEmpty()) {
        println("Violations de contraintes JPA pour l'enregistrement de l'entité  ${instanceEntite}:")
        violations.forEachIndexed { index, violation ->
            println("  -> Violation ${index + 1} : ${violation.propertyPath} ${violation.message}")
        }
        return false
    } else {
        return true
    }
}
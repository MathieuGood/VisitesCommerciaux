package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.ClientOuProspect
import me.xdrop.fuzzywuzzy.FuzzySearch


/**
 * Supprime les espaces inutiles au début et à la fin de chaque chaîne de caractères dans le tableau donné.
 * Cette méthode utilise la fonction `trim()` de Kotlin pour supprimer les espaces inutiles.
 * @param strings Le tableau de chaînes de caractères dont les espaces inutiles doivent être supprimés. Peut contenir des éléments null.
 */
fun supprimerEspacesInutiles(vararg strings: String?) {
    strings.map { it?.trim() }
}

/**
 * Effectue une correspondance floue (fuzzy match) entre une chaîne de caractères et une liste de `ClientOuProspect`.
 * Cette méthode utilise la bibliothèque FuzzySearch pour calculer le ratio de correspondance entre la chaîne de caractères et le nom de chaque `ClientOuProspect` dans la liste.
 * Si le ratio de correspondance est supérieur au seuil donné, le `ClientOuProspect` est ajouté à la liste des résultats positifs.
 * Les résultats positifs sont triés par ratio de correspondance décroissant et renvoyés.
 * @param stringAComparer La chaîne de caractères à comparer. Peut être null.
 * @param listePourComparaisons La liste de `ClientOuProspect` à comparer. Peut contenir des éléments null.
 * @param seuilRatio Le seuil de ratio de correspondance pour considérer un `ClientOuProspect` comme un résultat positif.
 * @return Une liste de `ClientOuProspect` qui ont un ratio de correspondance supérieur au seuil donné, triés par ratio de correspondance décroissant.
 */
fun fuzzyMatchClientOuProspect(
    stringAComparer: String?, listePourComparaisons: MutableList<ClientOuProspect?>, seuilRatio: Int
): List<ClientOuProspect> {
    val mapResultatsPositifs = mutableListOf<Pair<ClientOuProspect, Int>>()
    listePourComparaisons.forEach { itemPourComparaison ->
        val ratioResultatFuzzyMatch = FuzzySearch.ratio(
            stringAComparer?.lowercase(), itemPourComparaison?.nom?.lowercase()
        )
        if (ratioResultatFuzzyMatch > seuilRatio) {
            println("\u001B[32mCorrespondance (ratio $ratioResultatFuzzyMatch) : ${itemPourComparaison?.nom}\u001B[0m")
            mapResultatsPositifs.add(itemPourComparaison as ClientOuProspect to ratioResultatFuzzyMatch)

        } else {
            println("\u001B[31mPas de correspondance (ratio $ratioResultatFuzzyMatch) : ${itemPourComparaison?.nom}\u001B[0m")
        }
    }
    mapResultatsPositifs.sortByDescending { it.second }
    val listeResultatsPositifs = mapResultatsPositifs.map { it.first }
    return listeResultatsPositifs
}
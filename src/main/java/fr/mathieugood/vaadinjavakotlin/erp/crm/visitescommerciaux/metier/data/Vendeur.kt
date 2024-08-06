package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import java.util.*

/**
 * Cette classe `Vendeur` représente un vendeur dans le système.
 *
 * @property id L'identifiant unique du vendeur.
 * @property code Le code unique du vendeur.
 * @property nom Le nom du vendeur. Il ne peut pas être vide.
 * @property dateSortie La date de sortie du vendeur. Elle ne peut pas être vide.
 */
@Entity
class Vendeur {
    @Id
    @GeneratedValue
    var id: Long? = null

    var code: String? = null

    var nom: @NotBlank String? = null

    var dateSortie: @NotBlank Date? = null

    // TODO Ajouter une propriété pour stocker les départements auxquels le vendeur est rattaché ?
    // TODO Ajouter dans SVisitesCommerciaux.properties la possibilité d'activer ou désactiver cette prise en compte

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vendeur

        if (id != other.id) return false
        if (code != other.code) return false
        if (nom != other.nom) return false
        if (dateSortie != other.dateSortie) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (nom?.hashCode() ?: 0)
        result = 31 * result + (dateSortie?.hashCode() ?: 0)
        return result
    }
}

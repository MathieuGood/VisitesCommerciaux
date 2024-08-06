package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

/**
 * Cette classe `GammeProduit` représente une gamme de produits dans le système.
 *
 * @property id L'identifiant unique de la gamme de produits.
 * @property code Le code unique de la gamme de produits.
 * @property libelle Le libellé de la gamme de produits.
 */
@Entity
class GammeProduit {
    @Id
    @GeneratedValue
    var id: Long? = null

    var code: String? = null

    var libelle: @NotBlank String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GammeProduit

        if (id != other.id) return false
        if (code != other.code) return false
        if (libelle != other.libelle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (libelle?.hashCode() ?: 0)
        return result
    }
}

package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

/**
 * Cette classe `Famille` représente une famille dans le système.
 *
 * @property id L'identifiant unique de la famille.
 * @property code Le code unique de la famille.
 * @property libelle Le libellé de la famille.
 */
@Entity
class Famille {

    @Id
    @GeneratedValue
    var id: Long? = null

    var code: String? = null

    var libelle: @NotBlank String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Famille

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

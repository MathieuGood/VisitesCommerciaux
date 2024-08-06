package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

/**
 * Cette classe `Pays` représente un pays dans le système.
 *
 * @property code Le code unique du pays. Il peut être null.
 */
@Entity
class Pays(var code: String? = null) {

    @Id
    @GeneratedValue
    var id: Long? = null

    @NotBlank
    var libelle: String? = null

    override fun toString(): String {
        return "Pays(code=$code, id=$id, libelle=$libelle)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pays

        if (code != other.code) return false
        if (id != other.id) return false
        if (libelle != other.libelle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (libelle?.hashCode() ?: 0)
        return result
    }
}
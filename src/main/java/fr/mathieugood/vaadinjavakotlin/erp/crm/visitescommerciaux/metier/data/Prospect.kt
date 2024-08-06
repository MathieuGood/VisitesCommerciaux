package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*


/**
 * Cette classe `Prospect` représente un prospect dans le système.
 * Un prospect est un client potentiel qui n'a pas encore effectué d'achat.
 * Elle hérite de la classe `ClientOuProspect`.
 *
 * @see ClientOuProspect
 */
@Entity
class Prospect : ClientOuProspect {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    override var code: Long? = null

    @NotNull
    @ManyToOne
    override var famille: Famille? = null

    @NotBlank
    override var nom: String? = null

    @NotBlank
    override var adresse1: String? = null

    override var adresse2: String? = null

    @NotBlank
    override var codePostal: String? = null

    @NotBlank
    override var ville: String? = null

    @NotNull
    @ManyToOne
    override var pays: Pays? = null

    var telephoneFixe: String? = null

    var telephonePortable: String? = null

    var dateCreation: Date? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Prospect

        if (id != other.id) return false
        if (code != other.code) return false
        if (famille != other.famille) return false
        if (nom != other.nom) return false
        if (adresse1 != other.adresse1) return false
        if (adresse2 != other.adresse2) return false
        if (codePostal != other.codePostal) return false
        if (ville != other.ville) return false
        if (pays != other.pays) return false
        if (telephoneFixe != other.telephoneFixe) return false
        if (telephonePortable != other.telephonePortable) return false
        if (dateCreation != other.dateCreation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (famille?.hashCode() ?: 0)
        result = 31 * result + (nom?.hashCode() ?: 0)
        result = 31 * result + (adresse1?.hashCode() ?: 0)
        result = 31 * result + (adresse2?.hashCode() ?: 0)
        result = 31 * result + (codePostal?.hashCode() ?: 0)
        result = 31 * result + (ville?.hashCode() ?: 0)
        result = 31 * result + (pays?.hashCode() ?: 0)
        result = 31 * result + (telephoneFixe?.hashCode() ?: 0)
        result = 31 * result + (telephonePortable?.hashCode() ?: 0)
        result = 31 * result + (dateCreation?.hashCode() ?: 0)
        return result
    }
}

package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * Cette classe `Client` est une entité qui étend `ClientOuProspect`.
 * Elle représente un client dans le système.
 *
 * @property id L'identifiant unique du client.
 * @property code Le code unique du client.
 * @property famille La famille à laquelle appartient le client.
 * @property nom Le nom du client.
 * @property adresse1 L'adresse principale du client.
 * @property adresse2 L'adresse secondaire du client (facultatif).
 * @property codePostal Le code postal de l'adresse du client.
 * @property ville La ville de l'adresse du client.
 * @property pays Le pays du client.
 * @property telephoneFixe Le numéro de téléphone fixe du client (facultatif).
 * @property telephonePortable Le numéro de téléphone portable du client (facultatif).
 */
@Entity
class Client : ClientOuProspect {
    @Id
    @GeneratedValue
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

    @ManyToOne
    override var pays: Pays? = null

    var telephoneFixe: String? = null

    var telephonePortable: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Client

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
        return result
    }
}

package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import jakarta.persistence.*

/**
 * Cette classe `Utilisateur` représente un utilisateur authentifié dans l'application web.
 *
 * @property id L'identifiant unique de l'utilisateur.
 * @property codeVendeur Le code unique du vendeur associé à l'utilisateur.
 * @property nomUtilisateur Le nom de l'utilisateur.
 * @property motDePasse Le mot de passe de l'utilisateur.
 * @property estReponsable Indique si l'utilisateur est responsable ou non.
 * @property vendeursSousResponsabilite La liste des vendeurs sous la responsabilité de l'utilisateur.
 */
@Entity
class Utilisateur {

    @Id
    @GeneratedValue
    var id: Long? = null

    var codeVendeur: String? = null

    var nomUtilisateur: String? = null

    var motDePasse: String? = null

    var estReponsable: Boolean? = null

    @OneToMany(fetch = FetchType.EAGER)
    var vendeursSousResponsabilite: List<Vendeur?> = listOf()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Utilisateur

        if (id != other.id) return false
        if (codeVendeur != other.codeVendeur) return false
        if (nomUtilisateur != other.nomUtilisateur) return false
        if (motDePasse != other.motDePasse) return false
        if (estReponsable != other.estReponsable) return false
        if (vendeursSousResponsabilite != other.vendeursSousResponsabilite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (codeVendeur?.hashCode() ?: 0)
        result = 31 * result + (nomUtilisateur?.hashCode() ?: 0)
        result = 31 * result + (motDePasse?.hashCode() ?: 0)
        result = 31 * result + (estReponsable?.hashCode() ?: 0)
        result = 31 * result + vendeursSousResponsabilite.hashCode()
        return result
    }
}
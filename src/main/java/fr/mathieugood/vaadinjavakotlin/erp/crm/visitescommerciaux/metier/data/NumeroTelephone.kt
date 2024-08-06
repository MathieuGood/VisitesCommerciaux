package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger


/**
 * Cette classe `NumeroTelephone` représente un numéro de téléphone dans le système.
 *
 * @property numero Le numéro de téléphone. Il peut être null.
 * @property indicatif L'indicatif du pays. Il peut être null.
 * @property pays Le pays associé au numéro de téléphone. Il peut être null.
 */
class NumeroTelephone(
    val numero: String? = null,
    val indicatif: String? = null,
    val pays: Pays? = null,
    var validite: EtatNumeroTelephone = if(numero.isNullOrBlank() && indicatif.isNullOrBlank()) EtatNumeroTelephone.VIDE else EtatNumeroTelephone.INCONNU
) {

    companion object {
        val LOG by lazyLogger()
    }

    /**
     * Cette fonction `getNumeroComplet` est utilisée pour obtenir le numéro de téléphone complet avec l'indicatif.
     *
     * @return Le numéro de téléphone complet avec l'indicatif. Si le numéro ou l'indicatif est null, retourne le numéro seul ou une chaîne vide.
     */
    fun getNumeroComplet(): String {
        val numeroOut: String = numero ?: ""
        var indicatifOut: String = indicatif ?: ""
        if (!indicatif.isNullOrBlank() && !indicatif.startsWith("+")) {
            indicatifOut = "+${indicatif}"
        }
        val numeroComplet = if(numeroOut.isNotBlank()) "$indicatifOut$numeroOut" else ""
        LOG.debug("getNumeroComplet : $numeroComplet")
        return numeroComplet
    }

    override fun toString(): String {
        return "NumeroTelephone(indicatif=$indicatif, numero=$numero, pays=$pays, validite=$validite)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NumeroTelephone

        if (numero != other.numero) return false
        if (indicatif != other.indicatif) return false
        if (pays != other.pays) return false
        if (validite != other.validite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numero?.hashCode() ?: 0
        result = 31 * result + (indicatif?.hashCode() ?: 0)
        result = 31 * result + (pays?.hashCode() ?: 0)
        result = 31 * result + validite.hashCode()
        return result
    }
}


enum class EtatNumeroTelephone {
    INCONNU, VALIDE, INVALIDE, VIDE
}
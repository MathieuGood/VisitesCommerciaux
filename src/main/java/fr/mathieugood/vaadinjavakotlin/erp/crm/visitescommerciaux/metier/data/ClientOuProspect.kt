package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

/**
 * Cette interface `ClientOuProspect` représente un client ou un prospect dans le système.
 *
 * @property code Le code unique du client ou du prospect.
 * @property famille La famille à laquelle appartient le client ou le prospect.
 * @property nom Le nom du client ou du prospect.
 * @property adresse1 L'adresse principale du client ou du prospect.
 * @property adresse2 L'adresse secondaire du client ou du prospect (facultatif).
 * @property codePostal Le code postal de l'adresse du client ou du prospect.
 * @property ville La ville de l'adresse du client ou du prospect.
 * @property pays Le pays du client ou du prospect.
 */
interface ClientOuProspect {
    var code: Long?
    var famille: Famille?
    var nom: String?
    var adresse1: String?
    var adresse2: String?
    var codePostal: String?
    var ville: String?
    var pays: Pays?
}
package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone

import com.google.i18n.phonenumbers.Phonenumber
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.EtatNumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.NumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Pays

/**
 * L'interface `ValidateurTelephone` définit les méthodes nécessaires pour la validation et la manipulation des numéros de téléphone.
 * Cette interface a été créée dans le but de permettre l'utilisation de différentes librairies de validation de numéros de téléphone.
 */
interface ValidateurTelephone {

    /**
     * Formate un numéro de téléphone pour l'affichage.
     * Cette méthode prend en entrée une instance de `NumeroTelephone` représentant un numéro de téléphone.
     * Elle renvoie une chaîne de caractères représentant le numéro de téléphone formaté pour l'affichage.
     * @param numeroTelephone Le numéro de téléphone à formater.
     * @return Le numéro de téléphone formaté pour l'affichage.
     */
    fun formaterNumeroTelephone(numeroTelephone: NumeroTelephone): String

    /**
     * Convertit un code de pays en un indicatif téléphonique.
     * Cette méthode prend en entrée une chaîne de caractères représentant un code de pays.
     * Elle renvoie une chaîne de caractères représentant l'indicatif téléphonique correspondant au code de pays.
     * @param codePays Le code de pays à convertir en indicatif téléphonique.
     * @return L'indicatif téléphonique correspondant au code de pays.
     */
    fun codePaysVersIndicatif(codePays: String?): String?

    /**
     * Convertit un indicatif téléphonique en un code de pays.
     * Cette méthode prend en entrée une chaîne de caractères représentant un indicatif téléphonique.
     * Elle renvoie une chaîne de caractères représentant le code de pays correspondant à l'indicatif téléphonique, ou `null` si aucune correspondance n'a été
     * trouvée.
     * @param indicatif L'indicatif téléphonique à convertir en code de pays.
     * @return Le code de pays correspondant à l'indicatif téléphonique.
     */
    fun indicatifVersCodePays(indicatif: String): String?

    /**
     * Convertit une chaîne de caractères en une instance de `NumeroTelephone`.
     * Cette méthode prend en entrée une chaîne de caractères représentant un numéro de téléphone.
     * Elle renvoie une instance de `NumeroTelephone` représentant le numéro de téléphone.
     * @param numeroChaine Le numéro de téléphone à convertir.
     * @return Une instance de `NumeroTelephone` représentant le numéro de téléphone.
     */
    fun numeroChaineVersNumeroTelephone(numeroChaine: String, paysParDefaut: Pays?): NumeroTelephone

    /**
     * Convertit une chaîne de caractères en une instance de `Phonenumber.PhoneNumber`.
     * Cette méthode prend en entrée une chaîne de caractères représentant un numéro de téléphone.
     * Elle renvoie une instance de `Phonenumber.PhoneNumber` représentant le numéro de téléphone, ou null si la conversion échoue.
     * @param numero Le numéro de téléphone à convertir.
     * @return Une instance de `Phonenumber.PhoneNumber` représentant le numéro de téléphone, ou null si la conversion échoue.
     */
    fun parseNumeroChaineVersPhoneNumber(numero: String, codePaysParDefaut: String?): Phonenumber.PhoneNumber?

    /**
     * Met à jour la validité d'un numéro de téléphone.
     * Cette méthode prend en entrée une instance de `NumeroTelephone` représentant un numéro de téléphone.
     * @param numeroTelephone Le numéro de téléphone dont la validité doit être mise à jour.
     * @return L'état de la validité mise à jour.
     */
    fun miseAJourValiditeNumeroTelephone(numeroTelephone: NumeroTelephone): EtatNumeroTelephone
}
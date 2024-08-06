package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.EtatNumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.NumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Pays
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger

/**
 * Classe de validation des numéros de téléphone en utilisant la bibliothèque PhoneNumber de Google.
 * Cette classe implémente l'interface `ValidateurTelephone` pour fournir des méthodes de validation et de formatage des numéros de téléphone.
 * Elle utilise un `VisiteService` pour accéder aux données de visite et un `LectureApplicationProperties` pour lire les propriétés de l'application.
 * @property visiteService Le service de visite utilisé pour accéder aux données de visite.
 * @property lectureApplicationProperties L'objet utilisé pour lire les propriétés de l'application.
 * @property phoneNumberUtil L'instance de PhoneNumberUtil utilisée pour valider et formater les numéros de téléphone.
 * @property codePaysParDefaut Le code du pays par défaut utilisé lors de la validation des numéros de téléphone.
 */
class ValidationLibPhoneNumber(
    private val visiteService: VisiteService,
    private val lectureApplicationProperties: LectureApplicationProperties
) : ValidateurTelephone {

    private val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
    //private val codePaysParDefaut: String = lectureApplicationProperties.properties.vueSaisieVisite.codePaysParDefaut

    companion object {
        val LOG by lazyLogger()
    }

    /**
     * Convertit un `NumeroTelephone` en `PhoneNumber` de la bibliothèque PhoneNumber de Google.
     * Cette méthode tente de parser le numéro de téléphone et le code pays de l'objet `NumeroTelephone` en un `PhoneNumber`.
     * Si le parsing réussit, le `PhoneNumber` est renvoyé.
     * Si une `NumberParseException` est levée pendant le parsing, la méthode renvoie null et log l'erreur.
     * @param numeroTelephone L'objet `NumeroTelephone` à convertir en `PhoneNumber`. Ne doit pas être null.
     * @return Un `PhoneNumber` représentant le numéro de téléphone, ou null si le parsing échoue.
     */
    private fun parseNumeroTelephoneVersPhoneNumber(numeroTelephone: NumeroTelephone): Phonenumber.PhoneNumber? {
        LOG.debug("parseNumeroTelephoneVersPhoneNumber($numeroTelephone)")
//        LOG.debug("Fonction parseNumeroTelephone")
//        LOG.debug("  > Numéro à parser : ${numeroTelephone.numero}")
        try {
            val parsedNumero = phoneNumberUtil.parse(numeroTelephone.numero, numeroTelephone.pays?.code)
            LOG.debug("    > Numéro parsé : ${parsedNumero.nationalNumber}")
            return parsedNumero
        } catch (e: NumberParseException) {
            LOG.debug("    > NumberParseException: ${e.message}")
            return null
        }
    }

    /**
     * Convertit une chaîne de caractères représentant un numéro de téléphone en `PhoneNumber` de la bibliothèque PhoneNumber de Google.
     * Cette méthode tente de parser la chaîne de caractères en un `PhoneNumber`.
     * Si le parsing réussit, le `PhoneNumber` est renvoyé.
     * Si une `NumberParseException` est levée pendant le parsing, la méthode renvoie null et log l'erreur.
     * @param numero La chaîne de caractères représentant le numéro de téléphone à convertir en `PhoneNumber`. Ne doit pas être null.
     * @return Un `PhoneNumber` représentant le numéro de téléphone, ou null si le parsing échoue.
     */
    override fun parseNumeroChaineVersPhoneNumber(numero: String, codePaysParDefaut: String?): Phonenumber.PhoneNumber? {
        LOG.debug("parseNumeroChaineVersPhoneNumber($numero, $codePaysParDefaut)")
//        LOG.debug("Fonction parseNumeroTelephone")
//        LOG.debug("  > Numéro à parser : $numero")
        try {
            val parsedNumero = phoneNumberUtil.parse(numero, codePaysParDefaut)
//            LOG.debug("    > Numéro parsé : ${parsedNumero.nationalNumber}")
            return parsedNumero
        } catch (e: NumberParseException) {
//            LOG.debug("    > NumberParseException: ${e.message}")
            return null
        }
    }

    /**
     * Met à jour la validité d'un `NumeroTelephone` en utilisant la bibliothèque PhoneNumber de Google.
     * Cette méthode tente de parser le numéro de téléphone et le code pays de l'objet `NumeroTelephone` en un `PhoneNumber`.
     * Si le parsing réussit, la validité du `NumeroTelephone` est mise à jour en fonction de la validité du `PhoneNumber`.
     * Si une `NumberParseException` est levée pendant le parsing, la validité du `NumeroTelephone` est mise à jour à `EtatNumeroTelephone.INVALIDE`.
     * @param numeroTelephone L'objet `NumeroTelephone` dont la validité doit être mise à jour. Ne doit pas être null.
     * @return L'état de la validité mise à jour.
     */
    override fun miseAJourValiditeNumeroTelephone(numeroTelephone: NumeroTelephone) : EtatNumeroTelephone {
        LOG.debug("miseAJourValiditeNumeroTelephone($numeroTelephone)")
        val parsedPhoneNumber = parseNumeroTelephoneVersPhoneNumber(numeroTelephone)
        if (parsedPhoneNumber == null) {
            LOG.debug("  > Numéro invalide, impossible de le parser")
            numeroTelephone.validite = EtatNumeroTelephone.INVALIDE
        } else {
            val statutValidation = phoneNumberUtil.isValidNumber(parsedPhoneNumber)
            LOG.debug("  > Statut validation : $statutValidation")
            numeroTelephone.validite = if (statutValidation) EtatNumeroTelephone.VALIDE else EtatNumeroTelephone.INVALIDE
        }
        return numeroTelephone.validite
    }


    /**
     * Formate un `NumeroTelephone` en utilisant la bibliothèque PhoneNumber de Google.
     * Cette méthode tente de parser le `NumeroTelephone` en un `PhoneNumber`.
     * Si le parsing réussit, le `PhoneNumber` est formaté en utilisant le format national et renvoyé.
     * Si une `NumberParseException` est levée pendant le parsing, la méthode renvoie le numéro de téléphone original du `NumeroTelephone`.
     * @param numeroTelephone L'objet `NumeroTelephone` à formater. Ne doit pas être null.
     * @return Une chaîne de caractères représentant le numéro de téléphone formaté, ou le numéro de téléphone original si le parsing échoue.
     */
    override fun formaterNumeroTelephone(numeroTelephone: NumeroTelephone): String {
        LOG.debug("formaterNumeroTelephone($numeroTelephone)")
        val parsedPhoneNumber = parseNumeroTelephoneVersPhoneNumber(numeroTelephone)
        if (parsedPhoneNumber == null) {
//            LOG.debug("  > Numéro invalide, impossible de le parser")
            return numeroTelephone.numero ?: ""
        }
        val numeroFormate = phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
//        LOG.debug("Numéro à formater : ${numeroTelephone.numero} | Numéro formaté : $numeroFormate")
        return numeroFormate
    }


    /**
     * Convertit un code pays en indicatif téléphonique en utilisant la bibliothèque PhoneNumber de Google.
     * Cette méthode tente de récupérer l'indicatif pour le code pays donné.
     * Si l'indicatif est trouvé, il est renvoyé sous forme de chaîne de caractères avec un "+" en préfixe.
     * Si aucun indicatif n'est trouvé pour le code pays, la méthode renvoie null et log l'information.
     * @param codePays Le code pays pour lequel l'indicatif doit être récupéré. Peut être null.
     * @return Une chaîne de caractères représentant l'indicatif pour le code pays donné, ou null si aucun indicatif n'est trouvé.
     */
    override fun codePaysVersIndicatif(codePays: String?): String? {
        LOG.debug("codePaysVersIndicatif($codePays)")
        val indicatif = phoneNumberUtil.getCountryCodeForRegion(codePays?.uppercase())
        return if (indicatif != 0) {
//            LOG.debug("  >> Indicatif trouvé : +$indicatif")
            "+$indicatif"
        } else {
//            LOG.debug("  >> Pas d'indicatif trouvé")
            null
        }
    }

    /**
     * Convertit un indicatif téléphonique en code pays en utilisant la bibliothèque PhoneNumber de Google.
     * Cette méthode tente de récupérer le code pays pour l'indicatif donné.
     * Si le code pays est trouvé, il est renvoyé.
     * Si aucun code pays n'est trouvé pour l'indicatif ou si l'indicatif n'était pas un entier, la méthode renvoie `null`.
     * @param indicatif L'indicatif pour lequel le code pays doit être récupéré. Ne doit pas être null.
     * @return Une chaîne de caractères représentant le code pays pour l'indicatif donné, ou `null` si aucun code pays n'est trouvé.
     */
    override fun indicatifVersCodePays(indicatif: String): String? {
//        LOG.debug("Indicatif vers code pays : $indicatif")
        var codePays: String? = null
        val indicatifNumerique = indicatif.toIntOrNull()
        if(indicatifNumerique != null) {
            codePays = phoneNumberUtil.getRegionCodeForCountryCode(indicatif.toInt())
            if(codePays == "ZZ")
                codePays = null
        }
        LOG.debug("indicatifVersCodePays : $indicatif - Code pays : $codePays")
        return codePays
    }

    /**
     * Convertit une chaîne de caractères représentant un numéro de téléphone en `NumeroTelephone`.
     * Cette méthode tente de parser la chaîne de caractères en un `PhoneNumber` en utilisant la bibliothèque PhoneNumber de Google.
     * Si le parsing réussit, un `NumeroTelephone` est créé avec le numéro de téléphone, l'indicatif et le pays correspondants.
     * Si le parsing échoue, la méthode tente de déterminer si la chaîne de caractères est un indicatif ou un numéro de téléphone sans indicatif.
     * Si la chaîne de caractères est un indicatif, un `NumeroTelephone` invalide est créé avec cet indicatif.
     * Si la chaîne de caractères est un numéro de téléphone sans indicatif, l'indicatif par défaut est ajouté et un nouveau parsing est tenté.
     * Si ce second parsing échoue, un `NumeroTelephone` invalide est créé avec le numéro de téléphone original.
     * @param numeroChaine La chaîne de caractères représentant le numéro de téléphone à convertir en `NumeroTelephone`. Ne doit pas être null.
     * @return Un `NumeroTelephone` représentant le numéro de téléphone, ou un `NumeroTelephone` invalide si le parsing échoue.
     */
    override fun numeroChaineVersNumeroTelephone(numeroChaine: String, paysParDefaut: Pays?): NumeroTelephone {
        // Parsing chaîne pour séparer numéro et indicatif
        LOG.debug("numeroChaineVersNumeroTelephone($numeroChaine, $paysParDefaut)")
        var numeroParse = parseNumeroChaineVersPhoneNumber(numeroChaine, paysParDefaut?.code)
        // Cas où le parsing n'a pas pu se faire : pas de préfixe ou juste un préfixe
        if (numeroParse == null) {
//            LOG.debug("Premier parsing impossible")
            // Dans le cas où la chaîne démarre par un "+", est constituée ou de "00"+3 chiffres, ou d'un à trois chiffres, on considère que c'est juste le préfixe
            if (numeroChaine.startsWith("+") || Regex("^00\\\\d{1,3}\$").matches(numeroChaine) || Regex("^\\d{1,3}$").matches(numeroChaine))
            {
//                LOG.debug("  >> CAS : Juste le préfixe")
                val codePays = indicatifVersCodePays(numeroChaine)
                val pays = if(codePays != null) visiteService.findPaysByCode(codePays) else null
//                LOG.debug("  >> Conversion du préfixe $numeroChaine vers code pays : $codePays")
                val numeroTelephone = NumeroTelephone(null, numeroChaine, pays, EtatNumeroTelephone.INVALIDE)
                LOG.debug(numeroTelephone.toString())
                return numeroTelephone
            } else {
//                LOG.debug("  >> CAS : Numéro sans préfixe")
                var indicatifParDefaut: String? = null
                if(paysParDefaut != null) {
                    indicatifParDefaut = codePaysVersIndicatif(paysParDefaut.code)
//                    LOG.debug(" >> Ajout du préfixe par défaut $indicatifParDefaut à $numeroChaine")
                    numeroParse = parseNumeroChaineVersPhoneNumber("$indicatifParDefaut$numeroChaine", paysParDefaut.code)
                }
                if (numeroParse == null) {
//                    LOG.debug("  >> Numéro invalide")
                    val numeroTelephone =
                        NumeroTelephone(numeroChaine, indicatifParDefaut, null, EtatNumeroTelephone.INVALIDE)
//                    LOG.debug(numeroTelephone.toString())
                    return numeroTelephone
                }
            }
        }
        val numeroSeul = numeroParse.nationalNumber.toString()
        val indicatif = numeroParse.countryCode.toString()
        val codePays = indicatifVersCodePays(indicatif)
        val pays = visiteService.findPaysByCode(codePays)
        val validite = if(numeroSeul.isNotBlank() && indicatif.isNotBlank() && pays != null)
            EtatNumeroTelephone.VALIDE
        else
            EtatNumeroTelephone.INCONNU
        val numeroTelephone = NumeroTelephone(numeroSeul, indicatif, pays, validite)
        return numeroTelephone
    }

}
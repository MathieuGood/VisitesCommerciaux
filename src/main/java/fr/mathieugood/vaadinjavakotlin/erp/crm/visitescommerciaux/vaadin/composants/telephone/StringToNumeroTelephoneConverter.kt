package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone

import com.vaadin.flow.data.binder.Result
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.converter.Converter
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.NumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Pays
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger

/**
 * Classe de conversion entre `NumeroTelephone` et `String`.
 * Elle est nécessaire au bon fonctionnement du composant `TelephoneField`, héritant lui même de CustomField.
 * Cette classe implémente l'interface `Converter` de Vaadin pour convertir des objets `NumeroTelephone` en chaînes de caractères et vice versa.
 * Elle utilise un `VisiteService` pour accéder aux données de visite, un `ValidateurTelephone` pour valider et formater les numéros de téléphone, et un `Pays` par défaut pour les numéros de téléphone sans pays spécifié.
 * @property visiteService Le service de visite utilisé pour accéder aux données de visite.
 * @property validateurTelephone Le validateur de téléphone utilisé pour valider et formater les numéros de téléphone.
 * @property paysParDefaut Le pays par défaut à utiliser pour les numéros de téléphone sans pays spécifié.
 */
class StringToNumeroTelephoneConverter(
    private val visiteService: VisiteService,
    private val validateurTelephone: ValidateurTelephone,
    private val paysParDefaut: Pays?
) : Converter<NumeroTelephone, String> {

    companion object {
        val LOG by lazyLogger()
    }

    /**
     * Convertit une instance de `NumeroTelephone` en une chaîne de caractères.
     * Cette méthode est utilisée pour convertir la valeur du modèle (un objet `NumeroTelephone`) en une chaîne de caractères pour la présentation.
     * Si la valeur est null, une chaîne vide est renvoyée.
     * Sinon, la méthode `getNumeroComplet` de l'objet `NumeroTelephone` est appelée pour obtenir une représentation en chaîne de caractères du numéro de téléphone.
     * @param value L'objet `NumeroTelephone` à convertir. Peut être null.
     * @param context Le contexte de la valeur. Non utilisé dans cette méthode.
     * @return Un objet `Result` contenant la chaîne de caractères représentant le numéro de téléphone, ou une chaîne vide si la valeur est null.
     */
    override fun convertToModel(value: NumeroTelephone?, context: ValueContext?): Result<String> {
        LOG.debug("\u001B[32mconvertToModel($value)\u001B[0m")
//        LOG.debug("\u001B[32mFonction convertToModel : Conversion de l'objet NumeroTelephone en chaîne\u001B[0m")
//        LOG.debug("  > Objet NumeroTelephone : ${value.toString()}")
//        LOG.debug("  > Conversion de l'objet NumeroTelephone en chaîne : ${value?.getNumeroComplet()}")
        //LOG.debug("\u001B[31mFin de la fonction convertToModel\u001B[0m")
        // Problème : au chargement d'un numéro de téléphone vide, le champ numéro est rempli avec l'indicatif pays type +33
        // TODO S'il y a un préfixe contenu dans le numero, tester si le pays correspond à l'indicatif
        //      Si oui, retourner le numero complet tel quel
        //      Si non, retourner le numero sans le prefixe
        return Result.ok(value?.getNumeroComplet() ?: "")
    }

    /**
     * Convertit une chaîne de caractères en une instance de `NumeroTelephone`.
     * Cette méthode est utilisée pour convertir la valeur de présentation (une chaîne de caractères) en un objet `NumeroTelephone` pour le modèle.
     * Si la valeur est null ou vide, un nouvel objet `NumeroTelephone` est créé avec un numéro vide, l'indicatif du pays par défaut et le pays par défaut.
     * Sinon, la méthode `numeroChaineVersNumeroTelephone` du validateur de téléphone est appelée pour convertir la chaîne de caractères en un objet `NumeroTelephone`.
     * @param value La chaîne de caractères à convertir. Peut être null.
     * @param context Le contexte de la valeur. Non utilisé dans cette méthode.
     * @return Un objet `NumeroTelephone` représentant la valeur du modèle.
     */
    override fun convertToPresentation(value: String?, context: ValueContext?): NumeroTelephone {
        LOG.debug("\u001B[32mconvertToPresentation($value)\u001B[0m")
//        LOG.debug("\u001B[32mFonction convertToPresentation : Conversion de la chaîne '$value' en objet NumeroTelephone\u001B[0m")
        return if (value.isNullOrBlank()) {
            //LOG.debug("\u001B[31mFin de la fonction convertToPresentation\u001B[0m")
            NumeroTelephone()
            /*
            NumeroTelephone(
                numero = null,
                indicatif = validateurTelephone.codePaysVersIndicatif(paysParDefaut?.code),
                pays = paysParDefaut
            )*/
        } else {
            validateurTelephone.numeroChaineVersNumeroTelephone(value, paysParDefaut)
        }
    }
}
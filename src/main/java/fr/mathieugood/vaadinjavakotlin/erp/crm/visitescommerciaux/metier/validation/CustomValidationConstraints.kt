package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.validation

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Visite
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import kotlin.reflect.KClass

/**
 * Cette classe `CustomValidationConstraints` contient des annotations et des validateurs de contraintes personnalisés.
 * Chaque annotation et validateur définissent une contrainte spécifique qui peut être appliquée à l'entité `Visite`.
 */
class CustomValidationConstraints {

    /**
     * Cette annotation `DateVisiteDansPlage` est utilisée pour valider que la date de visite est dans une plage de dates spécifique.
     * Elle est appliquée à l'entité `Visite`.
     *
     * @property message Le message d'erreur à afficher si la validation échoue.
     * @property groups Les groupes de validation auxquels cette contrainte appartient.
     * @property payload Les informations de charge utile qui peuvent être associées à cette contrainte.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @Constraint(validatedBy = [DateVisiteValidator::class])
    annotation class DateVisiteDansPlage(
        val message: String = "La date de visite ",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = [],
    )

    /**
     * Cette classe `DateVisiteValidator` implémente `ConstraintValidator` pour la contrainte `DateVisiteDansPlage`.
     * Elle valide si la date de visite est dans une plage de dates spécifique.
     *
     * @property plageJoursAvantAjourdhui Le nombre de jours avant aujourd'hui pour la plage de dates.
     * @property plageJoursApresAjourdhui Le nombre de jours après aujourd'hui pour la plage de dates.
     */
    class DateVisiteValidator : ConstraintValidator<DateVisiteDansPlage, Visite?> {
        // TODO : Récupérer la valeur de plageJoursAvantAjourdHui depuis le fichier application.properties (voir pour utiliser Spring Constraint ValidatorFactory)
        private val plageJoursAvantAjourdhui: Long = 60
        private val plageJoursApresAjourdhui: Long = 60

        /**
         * Cette méthode `isValid` vérifie si la date de visite est dans la plage de dates spécifiée.
         *
         * @param visite L'objet `Visite` à valider.
         * @param context Le contexte de `ConstraintValidator`.
         * @return Boolean Retourne `true` si la date de visite est dans la plage de dates, sinon `false`.
         */
        override fun isValid(visite: Visite?, context: ConstraintValidatorContext): Boolean {
            if (visite?.date == null) {
                return false
            }
            val dateLimiteMin = LocalDate.now().minusDays(plageJoursAvantAjourdhui)
            val dateLimiteMax = LocalDate.now().plusDays(plageJoursApresAjourdhui)
            return visite.date!!.isAfter(dateLimiteMin) && visite.date!!.isBefore(dateLimiteMax)
        }
    }


    /**
     * Cette annotation `VendeursDifferents` est utilisée pour valider que les vendeurs 1 et 2 sont différents.
     * Elle est appliquée à l'entité `Visite`.
     *
     * @property message Le message d'erreur à afficher si la validation échoue.
     * @property groups Les groupes de validation auxquels cette contrainte appartient.
     * @property payload Les informations de charge utile qui peuvent être associées à cette contrainte.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @Constraint(validatedBy = [DifferentVendeursValidator::class])
    annotation class VendeursDifferents(
        val message: String = "Vendeur 1 et 2 doivent être différents",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
    )

    /**
     * Cette classe `DifferentVendeursValidator` implémente `ConstraintValidator` pour la contrainte `VendeursDifferents`.
     * Elle valide si les vendeurs 1 et 2 sont différents.
     */
    class DifferentVendeursValidator : ConstraintValidator<VendeursDifferents, Visite?> {
        /**
         * Cette méthode `isValid` vérifie si les vendeurs 1 et 2 sont différents.
         *
         * @param visite L'objet `Visite` à valider.
         * @param context Le contexte de `ConstraintValidator`.
         * @return Boolean Retourne `true` si les vendeurs 1 et 2 sont différents, sinon `false`.
         */
        override fun isValid(visite: Visite?, context: ConstraintValidatorContext): Boolean {
            return visite?.vendeur1 != visite?.vendeur2
        }
    }


    /**
     * Cette annotation `ProspectNonNull` est utilisée pour valider que le prospect est renseigné.
     * Elle est appliquée à l'entité `Visite`.
     *
     * @property message Le message d'erreur à afficher si la validation échoue.
     * @property groups Les groupes de validation auxquels cette contrainte appartient.
     * @property payload Les informations de charge utile qui peuvent être associées à cette contrainte.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @Constraint(validatedBy = [ProspectManquantValidator::class])
    annotation class ProspectNonNull(
        val message: String = "Prospect doit être renseigné",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
    )

    /**
     * Cette classe `ProspectManquantValidator` implémente `ConstraintValidator` pour la contrainte `ProspectNonNull`.
     * Elle valide si le prospect est renseigné lorsque le type de contact est "Prospect".
     */
    class ProspectManquantValidator : ConstraintValidator<ProspectNonNull, Visite?> {
        /**
         * Cette méthode `isValid` vérifie si le prospect est renseigné lorsque le type de contact est "Prospect".
         *
         * @param visite L'objet `Visite` à valider.
         * @param context Le contexte de `ConstraintValidator`.
         * @return Boolean Retourne `true` si le prospect est renseigné lorsque le type de contact est "Prospect", sinon `true`.
         */
        override fun isValid(visite: Visite?, context: ConstraintValidatorContext): Boolean {
            return if (visite?.typeContact == "Prospect") {
                visite.prospect != null
            } else {
                true
            }
        }
    }


    /**
     * Cette annotation `ClientNotNull` est utilisée pour valider que le client est renseigné.
     * Elle est appliquée à l'entité `Visite`.
     *
     * @property message Le message d'erreur à afficher si la validation échoue.
     * @property groups Les groupes de validation auxquels cette contrainte appartient.
     * @property payload Les informations de charge utile qui peuvent être associées à cette contrainte.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @Constraint(validatedBy = [ClientManquantValidator::class])
    annotation class ClientNotNull(
        val message: String = "Prospect doit être renseigné",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
    )

    /**
     * Cette classe `ClientManquantValidator` implémente `ConstraintValidator` pour la contrainte `ClientNotNull`.
     * Elle valide si le client est renseigné lorsque le type de contact est "Client".
     */
    class ClientManquantValidator : ConstraintValidator<ClientNotNull, Visite?> {
        /**
         * Cette méthode `isValid` vérifie si le client est renseigné lorsque le type de contact est "Client".
         *
         * @param visite L'objet `Visite` à valider.
         * @param context Le contexte de `ConstraintValidator`.
         * @return Boolean Retourne `true` si le client est renseigné lorsque le type de contact est "Client", sinon `true`.
         */
        override fun isValid(visite: Visite?, context: ConstraintValidatorContext): Boolean {
            return if (visite?.typeContact == "Client") {
                visite.client != null
            } else {
                true
            }
        }
    }


    /**
     * Cette annotation `DateDebutRecontactNotNull` est utilisée pour valider que la date de début de recontact est renseignée et postérieure à la date de visite.
     * Elle est appliquée à l'entité `Visite`.
     *
     * @property message Le message d'erreur à afficher si la validation échoue.
     * @property groups Les groupes de validation auxquels cette contrainte appartient.
     * @property payload Les informations de charge utile qui peuvent être associées à cette contrainte.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @Constraint(validatedBy = [DateDebutRecontactValidator::class])
    annotation class DateDebutRecontactNotNull(
        val message: String = "Une date postérieure à la date de visite doit être renseignée",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
    )

    /**
     * Cette classe `DateDebutRecontactValidator` implémente `ConstraintValidator` pour la contrainte `DateDebutRecontactNotNull`.
     * Elle valide si la date de début de recontact est renseignée et postérieure à la date de visite.
     */
    class DateDebutRecontactValidator : ConstraintValidator<DateDebutRecontactNotNull, Visite?> {
        /**
         * Cette méthode `isValid` vérifie si la date de début de recontact est renseignée et postérieure à la date de visite.
         *
         * @param visite L'objet `Visite` à valider.
         * @param context Le contexte de `ConstraintValidator`.
         * @return Boolean Retourne `true` si la date de début de recontact est renseignée et postérieure à la date de visite, sinon `true`.
         */
        override fun isValid(visite: Visite?, context: ConstraintValidatorContext): Boolean {
            if (visite?.recontact != true) {
                return true
            }
            return visite.dateDebutRecontact != null && visite.dateDebutRecontact!!.isAfter(visite.date)
        }
    }


    /**
     * Cette annotation `DateFinRecontactNotNull` est utilisée pour valider que la date de fin de recontact est renseignée et postérieure à la date de début de recontact.
     * Elle est appliquée à l'entité `Visite`.
     *
     * @property message Le message d'erreur à afficher si la validation échoue.
     * @property groups Les groupes de validation auxquels cette contrainte appartient.
     * @property payload Les informations de charge utile qui peuvent être associées à cette contrainte.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @Constraint(validatedBy = [DateFinRecontactValidator::class])
    annotation class DateFinRecontactNotNull(
        val message: String = "Une date postérieure à la date de début de recontact doit être renseignée",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
    )

    /**
     * Cette classe `DateFinRecontactValidator` implémente `ConstraintValidator` pour la contrainte `DateFinRecontactNotNull`.
     * Elle valide si la date de fin de recontact est renseignée et postérieure à la date de début de recontact.
     */
    class DateFinRecontactValidator : ConstraintValidator<DateFinRecontactNotNull, Visite?> {
        /**
         * Cette méthode `isValid` vérifie si la date de fin de recontact est renseignée et postérieure à la date de début de recontact.
         *
         * @param visite L'objet `Visite` à valider.
         * @param context Le contexte de `ConstraintValidator`.
         * @return Boolean Retourne `true` si la date de fin de recontact est renseignée et postérieure à la date de début de recontact, sinon `true`.
         */
        override fun isValid(visite: Visite?, context: ConstraintValidatorContext): Boolean {
            if (visite?.recontact != true) {
                return true
            }
            return visite.dateFinRecontact != null && visite.dateFinRecontact!!.isAfter(visite.dateDebutRecontact)
        }
    }


    /**
     * Cette annotation `MotifProchaineVisiteNotNull` est utilisée pour valider que le motif de la prochaine visite est renseigné.
     * Elle est appliquée à l'entité `Visite`.
     *
     * @property message Le message d'erreur à afficher si la validation échoue.
     * @property groups Les groupes de validation auxquels cette contrainte appartient.
     * @property payload Les informations de charge utile qui peuvent être associées à cette contrainte.
     */
    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    @Constraint(validatedBy = [MotifProchaineVisiteValidator::class])
    annotation class MotifProchaineVisiteNotNull(
        val message: String = "Un motif pour la prochaine visite doit être renseigné",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
    )

    /**
     * Cette classe `MotifProchaineVisiteValidator` implémente `ConstraintValidator` pour la contrainte `MotifProchaineVisiteNotNull`.
     * Elle valide si le motif de la prochaine visite est renseigné lorsque le recontact est vrai.
     */
    class MotifProchaineVisiteValidator : ConstraintValidator<MotifProchaineVisiteNotNull, Visite?> {
        /**
         * Cette méthode `isValid` vérifie si le motif de la prochaine visite est renseigné lorsque le recontact est vrai.
         *
         * @param visite L'objet `Visite` à valider.
         * @param context Le contexte de `ConstraintValidator`.
         * @return Boolean Retourne `true` si le motif de la prochaine visite est renseigné lorsque le recontact est vrai, sinon `true`.
         */
        override fun isValid(visite: Visite?, context: ConstraintValidatorContext): Boolean {
            if (visite?.recontact != true) {
                return true
            }
            return visite.motifProchaineVisite != null
        }
    }

}


package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.validation.CustomValidationConstraints
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Cette classe `Visite` représente une visite dans le système.
 *
 * @property dateCreation La date de création de la visite.
 * @property userCreation L'utilisateur qui a créé la visite.
 * @property dateDerniereModification La date de la dernière modification de la visite.
 * @property userDerniereModification L'utilisateur qui a effectué la dernière modification de la visite.
 * @property id L'identifiant unique de la visite.
 * @property code Le code de la visite.
 * @property date La date de la visite.
 * @property vendeur1 Le premier vendeur associé à la visite.
 * @property vendeur2 Le deuxième vendeur associé à la visite.
 * @property typeContact Le type de contact de la visite (Client ou Prospect).
 * @property client Le client associé à la visite.
 * @property prospect Le prospect associé à la visite.
 * @property motifVisite Le motif de la visite.
 * @property nomContact Le nom du contact de la visite.
 * @property fonctionContact La fonction du contact de la visite.
 * @property gammesProduits Les gammes de produits associées à la visite.
 * @property concurrents Les concurrents associés à la visite.
 * @property commentaires Les commentaires ou le rapport de la visite.
 * @property recontact Indique si la visite doit être recontactée.
 * @property dateDebutRecontact La date de début du recontact de la visite.
 * @property dateFinRecontact La date de fin du recontact de la visite.
 * @property motifProchaineVisite Le motif de la prochaine visite.
 * @property vendeurProchaineVisite Le vendeur de la prochaine visite.
 * @property userValidation L'utilisateur qui a validé la visite.
 * @property dateValidation La date de validation de la visite.
 */
@Entity
@CustomValidationConstraints.DateVisiteDansPlage
@CustomValidationConstraints.VendeursDifferents
@CustomValidationConstraints.ProspectNonNull
@CustomValidationConstraints.ClientNotNull
@CustomValidationConstraints.DateDebutRecontactNotNull
@CustomValidationConstraints.DateFinRecontactNotNull
@CustomValidationConstraints.MotifProchaineVisiteNotNull
class Visite {

    var dateCreation: ZonedDateTime? = null
    var userCreation: String? = null
    var dateDerniereModification: ZonedDateTime? = null
    var userDerniereModification: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    // Numéro ordre : ATT 0
    var code: Long? = null

    // Date visite : ATT 1
    @NotNull
    var date: LocalDate? = null

    // Vendeur : ATT 2
    @NotNull
    @ManyToOne
    var vendeur1: Vendeur? = null

    // Vendeur 2 : ATT 13
    @ManyToOne
    var vendeur2: Vendeur? = null

    // Type contact : Client ou Prospect : ATT 3
    @NotEmpty
    var typeContact: String? = null

    // Numéro client : ATT 4
    @ManyToOne
    var client: Client? = null

    // Numéro prospect : ATT 5
    @ManyToOne
    var prospect: Prospect? = null

    // Motif de visite : ATT 7
    @NotNull
    @ManyToOne
    var motifVisite: MotifVisite? = null

    // Contact : ATT 9 (multivalué)
    @NotEmpty
    var nomContact: String? = null

    // Fonction contact : ATT 9 (multivalué)
    @NotEmpty
    var fonctionContact: String? = null

    // Gammes de produits : ATT 15
    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    var gammesProduits: Set<GammeProduit?>? = null

    // Fournisseurs actuels : ATT 6
    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    var concurrents: Set<Concurrent?>? = null

    // Commentaires / rapport visite : ATT 10
    var commentaires: String? = ""

    // Recontacter (O/N)
    @NotNull
    var recontact: Boolean? = false

    // Date début recontacter : ATT 12
    var dateDebutRecontact: LocalDate? = null

    // Date fin recontacter : ATT ???
    var dateFinRecontact: LocalDate? = null

    // Motif prochaine visite
    @ManyToOne
    var motifProchaineVisite: MotifProchaineVisite? = null

    // Vendeur prochaine visite : ATT 17
    @ManyToOne
    var vendeurProchaineVisite: Vendeur? = null

    // User valid : ATT 23
    var userValidation: String? = null

    // Date validation : ATT 24
    var dateValidation: LocalDate? = null


    /**
     * Cette méthode génère une chaîne de caractères contenant les détails de la visite.
     * Elle utilise un StringBuilder pour construire la chaîne de caractères.
     * Chaque détail est ajouté à la chaîne de caractères avec une nouvelle ligne à la fin.
     *
     * @return Une chaîne de caractères contenant les détails de la visite.
     */
    fun getDetails(): String {
        val details = StringBuilder()
        details.append("----------------------------------------------\n")
        details.append("Visite ID : ${this.id}\n")
        details.append("Code visite ERP: ${this.code}\n")
        details.append("Date : ${this.date}\n")
        details.append("Vendeur 1 : ${this.vendeur1?.nom}\n")
        details.append("Vendeur 2 : ${this.vendeur2?.nom}\n")
        details.append("Type contact (P/C) : ${this.typeContact}\n")
        details.append("Client : ${this.client?.nom} | ${this.client?.adresse1} | ${this.client?.codePostal} | ${this.client?.ville} | ${this.client?.pays?.libelle} | ${this.client?.telephoneFixe} | ${this.client?.telephonePortable}\n")
        details.append("Prospect : ${this.prospect?.nom} | ${this.prospect?.adresse1} | ${this.prospect?.codePostal} | ${this.prospect?.ville} | ${this.prospect?.pays?.libelle} | ${this.prospect?.telephoneFixe} | ${this.prospect?.telephonePortable}\n")
        details.append("Motif visite : ${this.motifVisite?.libelle}\n")
        details.append("Nom contact : ${this.nomContact}\n")
        details.append("Fonction contact : ${this.fonctionContact}\n")
        details.append("Gammes de produits : \n")
        this.gammesProduits?.forEach { details.append(" -> ${it?.libelle}\n") }
        details.append("Fournisseurs actuels : \n")
        this.concurrents?.forEach { details.append(" -> ${it?.libelle}\n") }
        details.append("Commentaires / Rapport : ${this.commentaires}\n")
        details.append("Recontact (O/N) : ${this.recontact}\n")
        details.append("Date début recontact : ${this.dateDebutRecontact}\n")
        details.append("Date fin recontact : ${this.dateFinRecontact}\n")
        details.append("Motif prochaine visite : ${this.motifProchaineVisite?.libelle}\n")
        details.append("Vendeur prochaine visite : ${this.vendeurProchaineVisite?.nom}\n")
        details.append("Date création : ${this.dateCreation}\n")
        details.append("Date dernière modification : ${this.dateDerniereModification}\n")
        details.append("----------------------------------------------")
        return details.toString()
    }

    /**
     * Cette méthode crée une copie de l'objet Visite actuel.
     * Elle copie toutes les propriétés de l'objet actuel dans un nouvel objet Visite.
     *
     * @return Une copie de l'objet Visite actuel.
     */
    fun copy(): Visite {
        // Creation d'une copie de la visite
        val visiteCopy = Visite()
        visiteCopy.dateCreation = this.dateCreation
        visiteCopy.userCreation = this.userCreation
        visiteCopy.dateDerniereModification = this.dateDerniereModification
        visiteCopy.userDerniereModification = this.userDerniereModification
        visiteCopy.id = this.id
        visiteCopy.code = this.code
        visiteCopy.date = this.date
        visiteCopy.vendeur1 = this.vendeur1
        visiteCopy.vendeur2 = this.vendeur2
        visiteCopy.typeContact = this.typeContact
        visiteCopy.client = this.client
        visiteCopy.prospect = this.prospect
        visiteCopy.motifVisite = this.motifVisite
        visiteCopy.nomContact = this.nomContact
        visiteCopy.fonctionContact = this.fonctionContact
        visiteCopy.gammesProduits = this.gammesProduits
        visiteCopy.concurrents = this.concurrents
        visiteCopy.commentaires = this.commentaires
        visiteCopy.recontact = this.recontact
        visiteCopy.dateDebutRecontact = this.dateDebutRecontact
        visiteCopy.dateFinRecontact = this.dateFinRecontact
        visiteCopy.motifProchaineVisite = this.motifProchaineVisite
        visiteCopy.vendeurProchaineVisite = this.vendeurProchaineVisite
        visiteCopy.userValidation = this.userValidation
        visiteCopy.dateValidation = this.dateValidation
        return visiteCopy
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Visite

        if (dateCreation != other.dateCreation) return false
        if (userCreation != other.userCreation) return false
        if (dateDerniereModification != other.dateDerniereModification) return false
        if (userDerniereModification != other.userDerniereModification) return false
        if (id != other.id) return false
        if (code != other.code) return false
        if (date != other.date) return false
        if (vendeur1 != other.vendeur1) return false
        if (vendeur2 != other.vendeur2) return false
        if (typeContact != other.typeContact) return false
        if (client != other.client) return false
        if (prospect != other.prospect) return false
        if (motifVisite != other.motifVisite) return false
        if (nomContact != other.nomContact) return false
        if (fonctionContact != other.fonctionContact) return false
        if (gammesProduits != other.gammesProduits) return false
        if (concurrents != other.concurrents) return false
        if (commentaires != other.commentaires) return false
        if (recontact != other.recontact) return false
        if (dateDebutRecontact != other.dateDebutRecontact) return false
        if (dateFinRecontact != other.dateFinRecontact) return false
        if (motifProchaineVisite != other.motifProchaineVisite) return false
        if (vendeurProchaineVisite != other.vendeurProchaineVisite) return false
        if (userValidation != other.userValidation) return false
        if (dateValidation != other.dateValidation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dateCreation?.hashCode() ?: 0
        result = 31 * result + (userCreation?.hashCode() ?: 0)
        result = 31 * result + (dateDerniereModification?.hashCode() ?: 0)
        result = 31 * result + (userDerniereModification?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (vendeur1?.hashCode() ?: 0)
        result = 31 * result + (vendeur2?.hashCode() ?: 0)
        result = 31 * result + (typeContact?.hashCode() ?: 0)
        result = 31 * result + (client?.hashCode() ?: 0)
        result = 31 * result + (prospect?.hashCode() ?: 0)
        result = 31 * result + (motifVisite?.hashCode() ?: 0)
        result = 31 * result + (nomContact?.hashCode() ?: 0)
        result = 31 * result + (fonctionContact?.hashCode() ?: 0)
        result = 31 * result + (gammesProduits?.hashCode() ?: 0)
        result = 31 * result + (concurrents?.hashCode() ?: 0)
        result = 31 * result + (commentaires?.hashCode() ?: 0)
        result = 31 * result + (recontact?.hashCode() ?: 0)
        result = 31 * result + (dateDebutRecontact?.hashCode() ?: 0)
        result = 31 * result + (dateFinRecontact?.hashCode() ?: 0)
        result = 31 * result + (motifProchaineVisite?.hashCode() ?: 0)
        result = 31 * result + (vendeurProchaineVisite?.hashCode() ?: 0)
        result = 31 * result + (userValidation?.hashCode() ?: 0)
        result = 31 * result + (dateValidation?.hashCode() ?: 0)
        return result
    }


}

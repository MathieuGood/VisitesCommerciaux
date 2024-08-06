package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * La classe `ApplicationProperties` est une classe de configuration qui contient plusieurs sous-classes de propriétés.
 * Elle est annotée avec `@ConfigurationProperties` pour indiquer à Spring Boot qu'elle doit être utilisée pour lier les propriétés de configuration.
 *
 * @property main Les propriétés principales de l'application.
 * @property demoVendeur Les propriétés pour le compte de démonstration du vendeur.
 * @property demoResponsable Les propriétés pour le compte de démonstration du responsable.
 * @property vueSaisieVisite Les propriétés pour la vue de saisie de visite.
 * @property vueGridVisites Les propriétés pour la vue de grille des visites.
 *
 * @ConfigurationProperties Indique à Spring Boot que cette classe doit être utilisée pour lier les propriétés de configuration.
 */
@ConfigurationProperties
data class ApplicationProperties(
    val main: MainProperties = MainProperties(),
    val demoVendeur: UtilisateurDemo = UtilisateurDemo(),
    val demoResponsable: UtilisateurDemo = UtilisateurDemo(),
    val vueSaisieVisite: VueSaisieVisiteProperties = VueSaisieVisiteProperties(),
    val vueGridVisites: VueGridVisitesProperties = VueGridVisitesProperties()
)

/**
 * La classe `MainProperties` contient les propriétés principales de l'application.
 *
 * @property validationVisiteActive  Un booléen qui indique si la validation des visites est activée.
 * @property afficherVisitesQuandUserEstVendeur2 Un booléen qui indique si les visites du vendeur doivent être affichées quand il est renseigné comme vendeur 2.
 * @property afficherUniquementVisitesDesVendeursDuResponsable Un booléen qui indique si seules les visites des vendeurs associés au responsable connecté doivent être affichées.
 * @property afficherUniquementVendeursDuResponsable Un booléen qui indique si seuls les vendeurs (dans la saisie visite) associés au responsable commercial doivent être affichés.
 * @property afficherVisitesQuandUserEstVendeur2DuResponsable Un booléen qui indique si les visites des vendeurs associés au responsable connecté doivent être affichées quand le vendeur est renseigné comme vendeur 2.
 * @property niveauTaillePolice Le niveau de la taille de la police utilisé dans l'application (valeur entre 0 et 7).
 * @property modeDebug Un booléen qui indique si le mode test est actif.
 * @property modeDemo Un booléen qui indique si le mode démonstration est actif.
 */
data class MainProperties(
    val triFrontendActif: Boolean = true,
    val validationVisiteActive: Boolean = true,
    val afficherVisitesQuandUserEstVendeur2: Boolean = true,
    val afficherUniquementVisitesDesVendeursDuResponsable: Boolean = true,
    val afficherUniquementVendeursDuResponsable: Boolean = true,
    val afficherVisitesQuandUserEstVendeur2DuResponsable: Boolean = true,
    val niveauTaillePolice: Int = 3,
    val modeDebug: Boolean = false,
    val modeDemo: Boolean = false
)


/**
 * La classe `UtilisateurDemo` représente un utilisateur de démonstration dans l'application.
 *
 * Elle contient les propriétés suivantes :
 * @property nomUtilisateur Le nom d'utilisateur pour le compte de démonstration. Il peut être `null`.
 * @property motDePasse Le mot de passe pour le compte de démonstration. Il peut être `null`.
 * @property estResponsable Un booléen qui indique si le compte de démonstration est un compte responsable.
 * @property codeVendeur Le code du vendeur pour le compte de démonstration. Il peut être `null`.
 * @property codesVendeursSousResponsabilite Les codes des vendeurs qui sont sous la responsabilité du responsable de démonstration. Il peut être `null`.
 */
data class UtilisateurDemo(
    val nomUtilisateur: String? = null,
    val motDePasse: String? = null,
    val estResponsable: Boolean = false,
    val codeVendeur: String? = null,
    var codesVendeursSousResponsabilite: String? = null
)

/**
 * La classe `VueSaisieVisiteProperties` contient les propriétés pour la vue de saisie de visite.
 *
 * @property plageJoursAvantAjourdhui Nombre de jours avant aujourd'hui pour définir la plage autorisée de saisie de nouvelle visite (ou de modification de date d'une visite existante).
 * @property plageJoursApresAjourdhui Nombre de jours après aujourd'hui pour définir la plage autorisée de saisie de nouvelle visite (ou de modification de date d'une visite existante).
 * @property creationProspectActive Activer la création des prospects.
 * @property fuzzyMatchingActif Fuzzy matching sur les noms de prospects.
 * @property seuilFuzzyMatching Niveau du seuil du fuzzy matching (entier entre 1 et 100).
 * @property limiteNombreResultatsFuzzyMatching Limite du nombre de résultats renvoyés par le fuzzy matching (entier de la valeur du nombre de résultats ou `null` pour aucune limite).
 * @property codePaysParDefaut Code pays utilisé par défaut pour les adresses.
 * @property validationNumeroTelephoneProspectActive Activer la validation des numéros de téléphone des prospects.
 * @property formatageNumeroTelephoneProspectActive Activer le formatage des numéros de téléphone des prospects.
 * @property affichageLibellesTelephoneField Afficher les libellés du Custom Field TelephoneField.
 */
data class VueSaisieVisiteProperties(
    val plageJoursAvantAjourdhui: Long = 60,
    val plageJoursApresAjourdhui: Long = 60,
    val creationProspectActive: Boolean = true,
    val fuzzyMatchingActif: Boolean = true,
    val seuilFuzzyMatching: Int = 70,
    val limiteNombreResultatsFuzzyMatching: Int? = null,
    val codePaysParDefaut: String = "FR",
    val validationNumeroTelephoneProspectActive: Boolean = true,
    val formatageNumeroTelephoneProspectActive: Boolean = true,
    val affichageLibellesTelephoneField: Boolean = false,
)

/**
 * La classe `VueGridVisitesProperties` contient les propriétés pour la vue de grille des visites.
 *
 * @property ouvrirVisiteAuClicSurLigneMobile En mode mobile, ouvrir la visite au clic simple sur la ligne du grid.
 */
data class VueGridVisitesProperties(
    val ouvrirVisiteAuClicSurLigneMobile: Boolean = true,
)
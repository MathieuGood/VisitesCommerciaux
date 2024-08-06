package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.*
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * La classe `VisiteService` est un service Spring qui fournit des méthodes pour interagir avec les différentes entités de l'application.
 *
 * Elle utilise plusieurs repositories pour accéder aux données de l'application :
 * - `clientRepository` pour les clients
 * - `concurrentRepository` pour les concurrents
 * - `gammeProduitRepository` pour les gammes de produits
 * - `motifVisiteRepository` pour les motifs de visite
 * - `motifProchaineVisiteRepository` pour les motifs de prochaine visite
 * - `paysRepository` pour les pays
 * - `prospectRepository` pour les prospects
 * - `vendeurRepository` pour les vendeurs
 * - `visiteRepository` pour les visites
 * - `familleRepository` pour les familles
 * - `utilisateurRepository` pour les utilisateurs
 *
 * @Service Indique que cette classe est un service Spring. Spring va automatiquement créer une instance de cette classe et la gérer.
 */
@Service
class VisiteService(
    lectureApplicationProperties: LectureApplicationProperties,
    private val clientRepository: ClientRepository,
    private val concurrentRepository: ConcurrentRepository,
    private val gammeProduitRepository: GammeProduitRepository,
    private val motifVisiteRepository: MotifVisiteRepository,
    private val motifProchaineVisiteRepository: MotifProchaineVisiteRepository,
    private val paysRepository: PaysRepository,
    private val prospectRepository: ProspectRepository,
    private val vendeurRepository: VendeurRepository,
    private val visiteRepository: VisiteRepository,
    private val familleRepository: FamilleRepository,
    private val utilisateurRepository: UtilisateurRepository
) {

    private val triActif: Boolean = lectureApplicationProperties.properties.main.triFrontendActif

    fun findAllClients(): List<Client?> {
        val result = clientRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.nom }
        } else {
            result
        }
    }

    fun findAllConcurrents(): List<Concurrent?> {
        val result = concurrentRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.libelle }
        } else {
            return result
        }
    }

    fun findAllFamilles(): List<Famille?> {
        val result = familleRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.libelle }
        } else {
            return result
        }
    }

    fun findAllGammeProduits(): List<GammeProduit?> {
        val result = gammeProduitRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.libelle }
        } else {
            return result
        }
    }

    fun findAllMotifsVisite(): List<MotifVisite?> {
        val result = motifVisiteRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.libelle }
        } else {
            return result
        }
    }

    fun findAllMotifsProchaineVisite(): List<MotifProchaineVisite?> {
        val result = motifProchaineVisiteRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.libelle }
        } else {
            return result
        }
    }

    fun findAllPays(): List<Pays?> {
        val result = paysRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.libelle }
        } else {
            return result
        }
    }

    fun findPaysByCode(code: String?): Pays? {
        return paysRepository.findByCode(code)
    }

    fun findAllProspects(): List<Prospect?> {
        val result = prospectRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.nom }
        } else {
            return result
        }
    }

    fun findAllVendeurs(): List<Vendeur?> {
        val result = vendeurRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.nom }
        } else {
            return result
        }
    }

    fun findVendeurByCode(code: String): Optional<Vendeur?> {
        return vendeurRepository.findByCode(code)
    }

    fun findUtilisateurByNomUtilisateur(username: String): Optional<Utilisateur?> {
        return utilisateurRepository.findByNomUtilisateur(username)
    }

    fun findAllUtilisateurs(): MutableList<Utilisateur?> {
        return utilisateurRepository.findAll()
    }

    fun findAllVisites(): List<Visite?> {
        val result = visiteRepository.findAll()
        return if (triActif) {
            result.sortedBy { it?.date }
        } else {
            return result
        }
    }

    fun findVisitesByCodeVendeur(codeVendeur: String): List<Visite?> {
        return visiteRepository.findAllVisitesByVendeur1Code(codeVendeur)
    }

    fun findVisitesByCodeVendeurOnVendeur1AndVendeur2(codeVendeur: String): List<Visite?> {
        return visiteRepository.findAllVisitesByVendeur1CodeOrVendeur2Code(codeVendeur, codeVendeur)
    }

    fun findVisitesByCodesVendeurs(codesVendeurs: List<String>): List<Visite?> {
        return visiteRepository.findAllVisitesByVendeur1CodeIn(codesVendeurs)
    }

    fun findVisitesByCodesVendeurs1Et2(codesVendeurs: List<String>): List<Visite?> {
        return visiteRepository.findAllVisitesByVendeur1CodeInOrVendeur2CodeIn(codesVendeurs, codesVendeurs)
    }

    fun findVisiteById(idVisite: Long): Optional<Visite?> {
        return visiteRepository.findById(idVisite)
    }

    fun saveClient(client: Client) {
        clientRepository.save(client)
    }

    fun saveConcurrent(concurrent: Concurrent) {
        concurrentRepository.save(concurrent)
    }

    fun saveFamile(famille: Famille) {
        familleRepository.save(famille)
    }

    fun saveGammeProduit(gammeProduit: GammeProduit) {
        gammeProduitRepository.save(gammeProduit)
    }

    fun saveMotifVisite(motifVisite: MotifVisite) {
        motifVisiteRepository.save(motifVisite)
    }

    fun saveMotifProchaineVisite(motifProchaineVisite: MotifProchaineVisite) {
        motifProchaineVisiteRepository.save(motifProchaineVisite)
    }

    fun savePays(pays: Pays) {
        paysRepository.save(pays)
    }

    fun saveProspect(prospect: Prospect) {
        prospectRepository.save(prospect)
    }

    /**
     * Cette méthode `saveProspectWithId` est utilisée pour sauvegarder un prospect dans la base de données et retourner son identifiant.
     * Méthode uniquement utilisée en mode démo car inutile en production où le code prospect est fourni par la base Universe.
     *
     * Elle commence par sauvegarder le prospect en utilisant `prospectRepository.save(prospect)`.
     * Ensuite, elle retourne l'identifiant du prospect qui vient d'être sauvegardé.
     *
     * @param prospect Le prospect à sauvegarder.
     * @return L'identifiant du prospect qui vient d'être sauvegardé.
     */
    @Transactional
    fun saveProspectWithId(prospect: Prospect): Long? {
        prospectRepository.save(prospect)
        return prospect.id
    }

    fun saveUtilisateur(utilisateur: Utilisateur) {
        utilisateurRepository.save(utilisateur)
    }

    fun saveVendeur(vendeur: Vendeur) {
        vendeurRepository.save(vendeur)
    }

    fun saveVisite(visite: Visite) {
        visiteRepository.save(visite)
    }

    /**
     * Cette méthode `saveVisiteWithId` est utilisée pour sauvegarder une visite dans la base de données et retourner son identifiant.
     * Méthode uniquement utilisée en mode démo car inutile en production où le code visite est fourni par la base Universe.
     *
     * Elle commence par sauvegarder la visite en utilisant `visiteRepository.save(visite)`.
     * Ensuite, elle retourne l'identifiant de la visite qui vient d'être sauvegardée.
     *
     * @param visite La visite à sauvegarder.
     * @return L'identifiant de la visite qui vient d'être sauvegardée.
     */
    @Transactional
    fun saveVisiteWithId(visite: Visite): Long? {
        visiteRepository.save(visite)
        return visite.id
    }

}

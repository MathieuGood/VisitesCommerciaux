package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.dummydata

import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.*
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Cette classe `DummyDataInjector` est utilisée pour injecter des données factices dans l'application.
 *
 * @property visiteService Le service qui fournit des méthodes pour interagir avec les données de visite.
 * @property lectureApplicationProperties Le service qui fournit des méthodes pour lire les propriétés de l'application.
 */
class DummyDataInjector(
    private val visiteService: VisiteService, private val lectureApplicationProperties: LectureApplicationProperties
) {

    companion object {
        val LOG by lazyLogger()
    }

    fun injectData() {
        if (visiteService.findAllVendeurs().isEmpty()) {
            injectMotifsVisite()
            injectMotifsProchaineVisite()
            injectFamilles()
            injectPays()
            injectClients()
            injectVendeurs()
            injectProspects()
            injectConcurrents()
            injectGammesProduits()
            repeat(200) { injectRandomVisite() }
        }
        if (visiteService.findAllUtilisateurs().isEmpty()) {
            creationUtilisateurs()
        }
    }

    private fun creationUtilisateurs() {
        val vendeur = Utilisateur()
        vendeur.nomUtilisateur = lectureApplicationProperties.properties.demoVendeur.nomUtilisateur
        vendeur.motDePasse = lectureApplicationProperties.properties.demoVendeur.motDePasse
        vendeur.estReponsable = lectureApplicationProperties.properties.demoVendeur.estResponsable
        vendeur.codeVendeur = lectureApplicationProperties.properties.demoVendeur.codeVendeur
        visiteService.saveUtilisateur(vendeur)

        val responsable = Utilisateur()
        responsable.nomUtilisateur = lectureApplicationProperties.properties.demoResponsable.nomUtilisateur
        responsable.motDePasse = lectureApplicationProperties.properties.demoResponsable.motDePasse
        responsable.estReponsable = lectureApplicationProperties.properties.demoResponsable.estResponsable
        responsable.codeVendeur = lectureApplicationProperties.properties.demoResponsable.codeVendeur
        val vendeursSousResponsabilite: List<String> =
            lectureApplicationProperties.properties.demoResponsable.codesVendeursSousResponsabilite?.split(",")
                ?: emptyList()
        responsable.vendeursSousResponsabilite = visiteService.findAllVendeurs().filter {
            it?.code in vendeursSousResponsabilite
        }
        visiteService.saveUtilisateur(responsable)
    }

    private fun injectFamilles() {
        val famille = famillesMap.map { (code, nom) ->
            Famille().apply {
                this.code = code
                this.libelle = nom
            }
        }
        famille.forEach { visiteService.saveFamile(it) }
    }

    private fun injectPays() {
        val pays = paysMap.map { (code, nom) ->
            Pays().apply {
                this.code = code
                this.libelle = nom
            }
        }
        pays.forEach { visiteService.savePays(it) }
    }

    private fun injectRandomVisite() {
        visiteService.saveVisite(generateRandomVisite())
    }

    private fun generateRandomVisite(): Visite {
        val visite = Visite()
        val dateVisite = LocalDate.now().minusDays((0..30).random().toLong())
        val dateDebutRecontact = dateVisite.plusDays((1..30).random().toLong())
        val dateFinRecontact = dateDebutRecontact.plusDays((1..30).random().toLong())

        visite.code = (1..20000).random().toLong()
        visite.date = dateVisite
        visite.vendeur1 = visiteService.findAllVendeurs()[(0..6).random()]
        val vendeur2 = visiteService.findAllVendeurs()[(0..13).random()]
        if (vendeur2 != visite.vendeur1) {
            visite.vendeur2 = vendeur2
        } else {
            visite.vendeur2 = visiteService.findAllVendeurs()[(7..13).random()]
        }
        visite.motifVisite = visiteService.findAllMotifsVisite()[(0..12).random()]
        visite.typeContact = if ((0..1).random() == 0) "Client" else "Prospect"
        if (visite.typeContact == "Client") {
            visite.client = visiteService.findAllClients()[(0..48).random()]
        } else {
            visite.prospect = visiteService.findAllProspects()[(0..17).random()]
        }
        visite.nomContact = nomsPrenomsListe[nomsPrenomsListe.indices.random()]
        visite.fonctionContact = "Poissonier"
        visite.gammesProduits = setOf(
            visiteService.findAllGammeProduits()[(0..5).random()]
        )
        visite.concurrents = setOf(
            visiteService.findAllConcurrents()[(0..10).random()]
        )
        visite.commentaires = "Aimerait pouvoir être livré deux fois par semaine pour éviter les ruptures de stock."
        visite.recontact = true
        visite.dateDebutRecontact = dateDebutRecontact
        visite.dateFinRecontact = dateFinRecontact
        visite.motifProchaineVisite = visiteService.findAllMotifsProchaineVisite()[(0..4).random()]
        visite.vendeurProchaineVisite = visiteService.findAllVendeurs()[(0..13).random()]
        visite.userValidation = if ((0..1).random() == 0) "Christophe Muller" else null
        if (visite.userValidation != null) {
            visite.dateValidation = dateVisite.plusDays((4..8).random().toLong())
        }
        visite.dateCreation = ZonedDateTime.now()
        return visite
    }

    private fun injectMotifsVisite() {
        val motifsVisite = motifsVisiteMap.map { (code, nom) ->
            MotifVisite().apply {
                this.code = code
                this.libelle = nom
            }
        }
        motifsVisite.forEach { visiteService.saveMotifVisite(it) }
    }

    private fun injectMotifsProchaineVisite() {
        val motifsProchaineVisite = motifsProchaineVisiteMap.map { (code, nom) ->
            MotifProchaineVisite().apply {
                this.code = code
                this.libelle = nom
            }
        }
        motifsProchaineVisite.forEach { visiteService.saveMotifProchaineVisite(it) }
    }

    private fun injectGammesProduits() {
        val gammesProduits = gammesProduitsMap.map { (code, nom) ->
            GammeProduit().apply {
                this.code = code
                this.libelle = nom
            }
        }
        gammesProduits.forEach { visiteService.saveGammeProduit(it) }
    }

    private fun injectConcurrents() {
        val concurrents = concurrentsMap.map { (code, nom) ->
            Concurrent().apply {
                this.code = code
                this.libelle = nom
            }
        }
        concurrents.forEach { visiteService.saveConcurrent(it) }
    }

    private fun injectProspects() {
        prospectsListe.forEach { prospectInfo ->
            val prospectData = prospectInfo.split(" | ")
            val code = (0..999).random().toLong()
            val nom = prospectData[0]
            val adresse1 = prospectData[1]
            val codePostalVille = prospectData[2].split(" ")
            val adresse2 = ""
            val codePostal = codePostalVille[0]
            val ville = codePostalVille.drop(1).joinToString(" ")
            val telephoneFixe = prospectData[3]
            val famille = visiteService.findAllFamilles()[(0..4).random()]
            val prospect = Prospect().apply {
                this.code = code
                this.famille = famille
                this.nom = nom
                this.adresse1 = adresse1
                this.adresse2 = adresse2
                this.codePostal = codePostal
                this.ville = ville
                this.pays = visiteService.findAllPays().filter { it?.code == "FR" }[0]
                this.telephoneFixe = telephoneFixe
            }
            visiteService.saveProspect(prospect)
        }
    }

    private fun injectClients() {
        clientsListe.forEach { clientInfo ->
            val clientData = clientInfo.split(" | ")
            val code = (0..999).random().toLong()
            val nom = clientData[0]
            val adresse1 = clientData[1]
            val codePostalVille = clientData[3].split(" ")
            val adresse2 = clientData[2]
            val codePostal = codePostalVille[0]
            val ville = codePostalVille.drop(1).joinToString(" ")
            val famille = visiteService.findAllFamilles()[(0..4).random()]
            val client = Client().apply {
                this.code = code
                this.famille = famille
                this.nom = nom
                this.adresse1 = adresse1
                this.adresse2 = adresse2
                this.codePostal = codePostal
                this.ville = ville
                this.pays = visiteService.findAllPays().filter { it?.code == "FR" }[0]
            }
            visiteService.saveClient(client)
        }
    }

    private fun injectVendeurs() {
        val vendeurs = vendeursMap.map { (code, nom) ->
            Vendeur().apply {
                this.code = code
                this.nom = nom
            }
        }
        vendeurs.forEach { visiteService.saveVendeur(it) }
    }

    fun printAllEntities() {
        LOG.debug("VENDEURS :")
        visiteService.findAllVendeurs().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.code} | ${it.nom} ")
            }
        }
        LOG.debug("FAMILLES :")
        visiteService.findAllFamilles().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.code} | ${it.libelle} ")
            }
        }
        LOG.debug("CLIENTS :")
        visiteService.findAllClients().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.nom} | ${it.adresse1} | ${it.codePostal} | ${it.ville}")
            }
        }
        LOG.debug("PROSPECTS :")
        visiteService.findAllProspects().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.nom} | ${it.adresse1} | ${it.codePostal} | ${it.ville} | ${it.famille?.libelle} | ${it.telephoneFixe} | ${it.telephonePortable}")
            }
        }
        LOG.debug("MOTIFS VISITES :")
        visiteService.findAllMotifsVisite().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.code} | ${it.libelle}")
            }
        }
        LOG.debug("CONCURRENTS :")
        visiteService.findAllConcurrents().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.code} | ${it.libelle}")
            }
        }
        LOG.debug("GAMMES PRODUITS :")
        visiteService.findAllGammeProduits().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.code} | ${it.libelle}")
            }
        }
        LOG.debug("MOTIFS PROCHAINE VISITE :")
        visiteService.findAllMotifsProchaineVisite().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.code} | ${it.libelle}")
            }
        }
        LOG.debug("PAYS :")
        visiteService.findAllPays().forEach {
            if (it != null) {
                LOG.debug("${it.id} | ${it.code} | ${it.libelle}")
            }
        }
        LOG.debug("VISITES :")
        visiteService.findAllVisites().forEach {
            LOG.debug(it?.getDetails())
        }
    }
}
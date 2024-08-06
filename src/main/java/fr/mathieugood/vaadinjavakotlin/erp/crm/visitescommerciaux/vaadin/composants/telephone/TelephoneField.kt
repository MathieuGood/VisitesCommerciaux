package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants.telephone

import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.EtatNumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.NumeroTelephone
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Pays
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Prospect
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.utils.lazyLogger

/**
 * Classe représentant un champ de formulaire pour un numéro de téléphone.
 * Cette classe étend `CustomField<NumeroTelephone>` pour créer un champ de formulaire personnalisé pour les numéros de téléphone.
 * Elle utilise une liste de pays, un validateur de téléphone, un type de numéro de téléphone, et des options pour la validation, le formatage et l'affichage des libellés.
 * @property listePays La liste des pays disponibles pour le champ de formulaire.
 * @property validateurTelephone Le validateur de téléphone utilisé pour valider les numéros de téléphone.
 * @property typeNumero Le type de numéro de téléphone pour ce champ de formulaire. Par défaut, c'est `TypeNumeroTelephone.GENERIQUE`.
 * @property validationActive Un booléen indiquant si la validation est activée pour ce champ de formulaire. Par défaut, c'est vrai.
 * @property formatageActif Un booléen indiquant si le formatage est activé pour ce champ de formulaire. Par défaut, c'est faux.
 * @property affichageLibellesPaysEtNumero Un booléen indiquant si les libellés des pays et des numéros doivent être affichés. Par défaut, c'est faux.
 */
class TelephoneField(
    private val listePays: List<Pays?>,
    private val validateurTelephone: ValidateurTelephone,
    private val typeNumero: TypeNumeroTelephone = TypeNumeroTelephone.GENERIQUE,
    private val validationActive: Boolean = true,
    private val formatageActif: Boolean = false,
    private val affichageLibellesPaysEtNumero: Boolean = false,
    private val paysParDefaut: Pays?
) : CustomField<NumeroTelephone>() {

    private val numeroTelephoneField: TextField = TextField()
    private val paysField: Select<Pays> = Select()

    companion object {
        val LOG by lazyLogger()
    }

    init {
        configChamps()
        configLayout()
    }

    private fun configChamps() {
        label = typeNumero.libelle
        paysField.width = "8em"
        paysField.setItems(listePays.filter { validateurTelephone.codePaysVersIndicatif(it?.code) != null })
        paysField.setItemLabelGenerator { "${it.code} | ${validateurTelephone.codePaysVersIndicatif(it.code)}" }
        numeroTelephoneField.setWidthFull()
        if (affichageLibellesPaysEtNumero) {
            paysField.label = "Pays"
            numeroTelephoneField.label = "Numéro"
        }
    }

    private fun configLayout() {
        val layout = HorizontalLayout(paysField, numeroTelephoneField)
        layout.isSpacing = false
        layout.themeList.set("spacing-s", true)
        add(layout)
    }

    /**
     * Récupère un numéro de téléphone.
     * Cette méthode renvoie une instance de `NumeroTelephone` avec le numéro et le pays récupérés à partir des champs du formulaire.
     * @return Une instance de `NumeroTelephone` avec le numéro et le pays.
     */
    private fun getNumeroTelephone(): NumeroTelephone {
        return NumeroTelephone(numero = numeroTelephoneField.value, pays = paysField.value)
    }

    /**
     * Ajoute un validateur Vaadin au champ de numéro de téléphone.
     * Cette méthode configure un validateur pour le champ de numéro de téléphone dans le formulaire.
     * Le validateur vérifie si le numéro de téléphone est valide en fonction du pays sélectionné.
     * Si la validation est désactivée, le validateur renvoie toujours vrai.
     * Si le numéro de téléphone est vide, le validateur renvoie vrai.
     * Sinon, le validateur vérifie la validité du numéro de téléphone et renvoie vrai si le numéro est valide, faux sinon.
     * @param binder Le binder Vaadin utilisé pour lier les données du formulaire.
     * @return Un objet Binder.BindingBuilder configuré avec le validateur.
     */
    fun ajouterVaadinValidator(
        binder: Binder<Prospect>
    ): Binder.BindingBuilder<Prospect, String> {
        return binder.forField(numeroTelephoneField).withValidator({
            if (!validationActive) return@withValidator true
            LOG.debug("\u001B[32mFonction withValidator\u001B[0m")
            LOG.debug("Validation du numéro de téléphone ${getNumeroTelephone().numero} avec code pays ${getNumeroTelephone().pays?.code}")
            LOG.debug("  >> VALEUR DE IT : $it")
            if (it == null || it.isEmpty()) {
                //LOG.debug("\u001B[31mFin fonction withValidator\u001B[0m")
                return@withValidator true
            }
            val validite = if(getNumeroTelephone().validite == EtatNumeroTelephone.INCONNU)
                validateurTelephone.miseAJourValiditeNumeroTelephone(getNumeroTelephone())
            else
                getNumeroTelephone().validite
            if (validite == EtatNumeroTelephone.VALIDE) {
                LOG.debug("  >> Numéro valide")
                //LOG.debug("\u001B[31mFin fonction withValidator\u001B[0m")
                return@withValidator true
            } else {
                LOG.debug("  >> Numéro invalide")
                //LOG.debug("\u001B[31mFin fonction withValidator\u001B[0m")
                return@withValidator false
            }
        }, "doit être un numéro de téléphone valide")
    }


    /**
     * Définit la valeur de présentation du champ de numéro de téléphone.
     * Cette méthode est appelée lorsque le champ de numéro de téléphone doit être mis à jour avec une nouvelle valeur.
     * Si le numéro de téléphone n'est pas null et que le formatage est activé, le numéro de téléphone est formaté avant d'être défini.
     * Le champ du pays est également mis à jour avec le pays du numéro de téléphone.
     * @param numeroTelephone Le numéro de téléphone à définir. Peut être null.
     */
    override fun setPresentationValue(numeroTelephone: NumeroTelephone?) {
        LOG.debug("\u001B[32mFonction setPresentationValue($numeroTelephone)\u001B[0m")
        //numeroTelephoneField.value = numeroTelephone?.numero ?: ""
        if (numeroTelephone != null && formatageActif) {
            numeroTelephoneField.value = validateurTelephone.formaterNumeroTelephone(numeroTelephone)
        } else {
            numeroTelephoneField.value = numeroTelephone?.numero?:""
        }
        paysField.value = numeroTelephone?.pays?:paysParDefaut
        LOG.debug("  >> numeroTelephoneField : ${numeroTelephoneField.value}")
        LOG.debug("  >> paysField : ${paysField.value}")
    }

    /**
     * Génère la valeur du modèle à partir des valeurs des champs du formulaire.
     * Cette méthode est appelée lorsque la valeur du modèle doit être mise à jour avec les valeurs actuelles des champs du formulaire.
     * Si le numéro de téléphone commence par "+", le numéro de téléphone est converti en une instance de `NumeroTelephone` en utilisant le validateur de téléphone.
     * Sinon, une nouvelle instance de `NumeroTelephone` est créée avec le numéro de téléphone, l'indicatif et le pays récupérés à partir des champs du formulaire.
     * @return Une instance de `NumeroTelephone` représentant la valeur du modèle.
     */
    override fun generateModelValue(): NumeroTelephone {
        LOG.debug("\u001B[32mFonction generateModelValue\u001B[0m")
        LOG.debug("  >> Numéro : ${numeroTelephoneField.value}")
        val modelValue: NumeroTelephone
        val valeurSaisie = numeroTelephoneField.value?:""

        if (valeurSaisie.trim().startsWith("+")) {
            LOG.debug("Numéro commence par + : ${numeroTelephoneField.value}")
            modelValue = validateurTelephone.numeroChaineVersNumeroTelephone(numeroTelephoneField.value, paysParDefaut)
        } else {
            LOG.debug("Numéro ne commence pas par + : ${numeroTelephoneField.value}")
            modelValue = NumeroTelephone(
                numero = numeroTelephoneField.value,
                indicatif = validateurTelephone.codePaysVersIndicatif(paysField.value.code),
                pays = paysField.value
            )
            validateurTelephone.miseAJourValiditeNumeroTelephone(modelValue)
        }

        // Si le pays a été trouvé depuis numeroTelephoneField.value, il faut changer ici la valeur de paysField.value (car la fonction setPresentationValue
        // ne sera pas rappelée)
        if(modelValue.pays != null && paysField.value != modelValue.pays)
            paysField.value = modelValue.pays
        // Si on a un numéro de téléphone valide, on fait le formatage s'il y a lieu
        if (modelValue.validite == EtatNumeroTelephone.VALIDE && !modelValue.numero.isNullOrBlank() && formatageActif)
            numeroTelephoneField.value = validateurTelephone.formaterNumeroTelephone(modelValue)

        LOG.debug("  >> ModelValue : $modelValue")
        return modelValue
    }
}

/**
 * Enumération représentant les différents types de numéros de téléphone.
 * Chaque type de numéro de téléphone est associé à un libellé.
 * @property libelle Le libellé associé au type de numéro de téléphone.
 * @enum GENERIQUE Un numéro de téléphone générique.
 * @enum FIXE Un numéro de téléphone fixe.
 * @enum PORTABLE Un numéro de téléphone portable.
 * @enum FAX Un numéro de fax.
 */
enum class TypeNumeroTelephone(val libelle: String) {
    GENERIQUE("Téléphone"),
    FIXE("Téléphone fixe"),
    PORTABLE("Téléphone portable"),
    FAX("Fax")
}
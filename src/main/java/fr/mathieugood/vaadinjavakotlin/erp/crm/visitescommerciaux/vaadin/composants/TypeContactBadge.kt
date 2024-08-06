package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.composants

import com.vaadin.flow.component.html.Span

/**
 * Cette classe `TypeContactBadge` est une extension de la classe `Span` de Vaadin.
 * Elle est utilisée pour créer un badge mentionnant le type de contact (Client ou Prospect).
 *
 * Elle prend en paramètre un objet `TypeContact` qui est le type de contact pour lequel le badge est créé.
 *
 * @param typeContact Le type de contact pour lequel le badge est créé.
 */
class TypeContactBadge(typeContact: TypeContact) : Span() {
    init {
        text = typeContact.libelle
        element.themeList.add(typeContact.theme)
        style.set("font-size", "0.8em")
        style.setMinWidth("62px")

    }
}


/**
 * Cette énumération `TypeContact` définit les types de contacts possibles.
 * Chaque type de contact a un libellé et un thème associés.
 *
 * Elle a deux valeurs possibles :
 * - `CLIENT` qui représente un client. Son libellé est "Client" et son thème est "badge success primary".
 * - `PROSPECT` qui représente un prospect. Son libellé est "Prospect" et son thème est "badge contrast primary".
 */
enum class TypeContact(val libelle: String, val theme: String) {
    CLIENT("Client", "badge success primary"),
    PROSPECT("Prospect", "badge contrast primary")
}
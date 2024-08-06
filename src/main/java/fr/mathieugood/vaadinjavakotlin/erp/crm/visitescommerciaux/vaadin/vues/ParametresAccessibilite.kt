package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues

import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.VaadinSessionScope
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.LectureApplicationProperties

/**
 * Cette classe représente les paramètres d'accessibilité pour l'application.
 * Elle est liée à la portée de la session Vaadin, ce qui signifie qu'une nouvelle instance est créée pour chaque session utilisateur.
 *
 * @property niveauTaillePolice Le niveau de taille de police utilisé dans l'application. Il est initialisé à 3 par défaut.
 */
@SpringComponent
@VaadinSessionScope
class ParametresAccessibilite(lectureApplicationProperties: LectureApplicationProperties) {
    var niveauTaillePolice: Int = lectureApplicationProperties.properties.main.niveauTaillePolice
}
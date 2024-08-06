package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Utilisateur;
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.metier.data.Vendeur;
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.services.VisiteService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * La classe `SecurityService` est un composant Spring qui fournit des méthodes pour gérer la sécurité de l'application.
 * Elle utilise `AuthenticationContext` pour gérer l'authentification de l'utilisateur et `VisiteService` pour interagir avec la base de données.
 *
 * @Component Indique que cette classe est un composant Spring. Spring va automatiquement créer une instance de cette classe et la gérer.
 */
@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;
    private final VisiteService visiteService;


    public SecurityService(AuthenticationContext authenticationContext, VisiteService visiteService) {
        this.authenticationContext = authenticationContext;
        this.visiteService = visiteService;
    }

    public UsernamePasswordAuthenticationToken getAuthenticatedUser() {
        return (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Cette méthode `getUtilisateurInstance` est utilisée pour obtenir une instance de `Utilisateur` correspondant à l'utilisateur connecté.
     * <p>
     * Elle utilise d'abord la méthode `getAuthenticatedUser` pour obtenir l'utilisateur authentifié.
     * Ensuite, elle récupère le nom d'utilisateur à partir de cet utilisateur authentifié.
     * Enfin, elle utilise le `visiteService` pour trouver l'utilisateur correspondant à ce nom d'utilisateur dans la base de données.
     * Si aucun utilisateur n'est trouvé, elle crée et renvoie une nouvelle instance de `Utilisateur`.
     *
     * @return Une instance de `Utilisateur` correspondant à l'utilisateur authentifié, ou une nouvelle instance de `Utilisateur` si aucun utilisateur n'est trouvé.
     */
    public Utilisateur getUtilisateurInstance() {
        UsernamePasswordAuthenticationToken user = getAuthenticatedUser();
        String username = user.getPrincipal().toString();
        return visiteService.findUtilisateurByNomUtilisateur(username).orElse(new Utilisateur());
    }

    /**
     * Cette méthode `getUserCode` est utilisée pour obtenir le code vendeur de l'utilisateur connecté.
     * <p>
     * Elle utilise la méthode `getUtilisateurInstance` pour obtenir une instance de `Utilisateur`,
     * puis elle appelle la méthode `getCodeVendeur` sur cette instance pour obtenir le code de l'utilisateur.
     *
     * @return Le code de l'utilisateur.
     */
    public String getUserCode() {
        return getUtilisateurInstance().getCodeVendeur();
    }

    /**
     * Cette méthode `getEstResponsable` est utilisée pour déterminer si l'utilisateur est responsable ou non.
     * <p>
     * Elle utilise la méthode `getUtilisateurInstance` pour obtenir une instance de `Utilisateur`,
     * puis elle appelle la méthode `getEstReponsable` sur cette instance pour obtenir l'information.
     *
     * @return Un booléen indiquant si l'utilisateur est responsable ou non.
     */
    public Boolean getEstResponsable() {
        return getUtilisateurInstance().getEstReponsable();
    }

    /**
     * Cette méthode `getVendeurInstance` est utilisée pour obtenir une instance de `Vendeur`.
     * <p>
     * Elle utilise d'abord la méthode `getUserCode` pour obtenir le code vendeur de l'utilisateur connecté.
     * Ensuite, elle utilise le `visiteService` pour trouver le vendeur correspondant à ce code dans la base de données.
     * Si aucun vendeur n'est trouvé, elle crée et renvoie une nouvelle instance de `Vendeur`.
     *
     * @return Une instance de `Vendeur` correspondant au code vendeur de l'utilisateur, ou une nouvelle instance de `Vendeur` si aucun vendeur n'est trouvé.
     */
    public Vendeur getVendeurInstance() {
        String codeVendeur = getUserCode();
        return visiteService.findVendeurByCode(codeVendeur).orElse(new Vendeur());
    }

    /**
     * Cette méthode `getVendeursSousResponsabilite` est utilisée pour obtenir la liste des codes vendeurs sous la responsabilité de l'utilisateur.
     * <p>
     * Elle commence par créer une nouvelle liste vide `listeCodesVendeursSousResponsabilite`.
     * Ensuite, elle utilise la méthode `getUtilisateurInstance` pour obtenir une instance de `Utilisateur`,
     * puis elle appelle la méthode `getVendeursSousResponsabilite` sur cette instance pour obtenir la liste des vendeurs sous la responsabilité de l'utilisateur.
     * Pour chaque vendeur dans cette liste, elle ajoute le code du vendeur à la `listeCodesVendeursSousResponsabilite`.
     *
     * @return Une liste de chaînes de caractères représentant les codes des vendeurs sous la responsabilité de l'utilisateur.
     */
    public List<String> getVendeursSousResponsabilite() {
        List<String> listeCodesVendeursSousResponsabilite = new java.util.ArrayList<>(List.of());
        getUtilisateurInstance().getVendeursSousResponsabilite().forEach(vendeur -> listeCodesVendeursSousResponsabilite.add(vendeur.getCode()));
        return listeCodesVendeursSousResponsabilite;
    }

    /**
     * Cette méthode `logout` est utilisée pour déconnecter l'utilisateur actuellement authentifié.
     * <p>
     * Elle utilise le `authenticationContext` pour effectuer l'opération de déconnexion.
     */
    public void logout() {
        authenticationContext.logout();
    }

}

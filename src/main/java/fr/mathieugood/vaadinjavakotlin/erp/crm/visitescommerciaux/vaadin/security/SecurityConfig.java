package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux.vaadin.vues.VueLogin;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Cette classe `SecurityConfig` est une configuration Spring Security qui étend `VaadinWebSecurity`.
 * Elle est utilisée pour configurer les paramètres de sécurité de l'application.
 * <p>
 * Elle est annotée avec `@EnableWebSecurity` pour activer la sécurité Web Spring Security.
 * Elle est également annotée avec `@Configuration` pour indiquer qu'il s'agit d'une classe de configuration Spring.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    private final AuthentificationUtilisateur authentificationUtilisateur;

    public SecurityConfig(AuthentificationUtilisateur authentificationUtilisateur) {
        this.authentificationUtilisateur = authentificationUtilisateur;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, VueLogin.class);
        // À décommenter pour désactiver la protection CSRF et visualiser le contenu de la console H2 dans le navigateur
        // http.csrf().disable();
        // http.headers().frameOptions().disable();
        http.authenticationProvider(authentificationUtilisateur);
    }
}

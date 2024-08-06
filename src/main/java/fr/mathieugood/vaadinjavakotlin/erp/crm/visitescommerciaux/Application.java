package fr.mathieugood.vaadinjavakotlin.erp.crm.visitescommerciaux;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * La classe `Application` est la classe principale de l'application Spring Boot.
 * Elle est annotée avec plusieurs annotations pour configurer l'application :
 *
 * @SpringBootApplication Indique que c'est une application Spring Boot.
 * @Theme(value = "svisitescommerciaux") Définit le thème de l'application.
 * @Configuration Indique que cette classe contient des beans de configuration Spring.
 * @PropertySource(value = "classpath:/VisitesCommerciaux.properties", ignoreResourceNotFound = true) Charge les propriétés de l'application à partir du fichier spécifié.
 * @EnableConfigurationProperties(ApplicationProperties.class) Active le support pour les propriétés de configuration.
 * <p>
 * Elle implémente `AppShellConfigurator` qui est une interface de Vaadin pour configurer l'application.
 */
@SpringBootApplication
@Theme(value = "svisitescommerciaux")
@Configuration
@PropertySource(value = "classpath:/VisitesCommerciaux.properties", ignoreResourceNotFound = true)
@EnableConfigurationProperties(ApplicationProperties.class)
public class Application implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

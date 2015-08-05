package runkoserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import static runkoserver.libraries.Links.*;
import runkoserver.service.PersonUserDetailsService;

/**
 * Security configuration using Spring-security.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    
    /**
     * Sets required authentications for views.
     * @param http HttpSecurity class
     * @throws Exception error
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ei päästetä käyttäjää mihinkään sovelluksen resurssiin ilman
        // kirjautumista
        http.authorizeRequests()
                .antMatchers(LINK_CONTENT_INDEX + LINK_CONTENT_SIMPLEFORM).authenticated()
                .antMatchers(FILE_CSS, LINK_HOME, LINK_CONTENT_INDEX + "/*").permitAll()
                .anyRequest().authenticated();
        
        http.csrf().disable();

        // tarjotaan mahdollisuus kirjautumiseen ja annetaan kaikille
        // pääsy kirjautumissivulle
        http.formLogin()
                .loginPage(LINK_LOGIN).permitAll();
    }
    
    /**
     * Configures authentication service.
     */
    @Configuration
    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
        
        @Autowired
        PersonUserDetailsService personUserDetailsService;
        
        /**
         * Initializes authentication with selected service.
         * @param auth AuthenticationManagerBuilder. Can be set with different authentication services.
         * @throws Exception Throws usernameNotFound exception
         */
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(personUserDetailsService);
//            // käyttäjällä jack, jonka salasana on bauer, on rooli USER
//            auth.inMemoryAuthentication()
//                    .withUser("jack").password("bauer").roles("USER");
        }
    }
}

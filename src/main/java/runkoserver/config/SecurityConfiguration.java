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
        // don't let user in any applications resources without login
        http.authorizeRequests()
                .antMatchers(FOLDER_CSS, LINK_HOME).permitAll()
                .antMatchers(LINK_CONTENT + LINK_CONTENT_SIMPLEFORM, LINK_CONTENT + LINK_CONTENT_FANCYFORM).authenticated()
                .antMatchers(FOLDER_CSS, LINK_HOME, LINK_CONTENT + "/*").permitAll()
                .anyRequest().authenticated();

        http.csrf().disable();

        // offers chance to login and give everybody access to loginpage
        http.formLogin()
                .defaultSuccessUrl(LINK_FRONTPAGE, true)
                .loginPage(LINK_LOGIN).permitAll();

        // configurations to logout
        http.logout()
		.logoutSuccessUrl(LINK_LOGIN);
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
         *
         * @param auth AuthenticationManagerBuilder. Can be set with different
         * authentication services.
         * @throws Exception Throws usernameNotFound exception
         */
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(personUserDetailsService);
//            // user jack, which password is bauer, is role USER
//            auth.inMemoryAuthentication()
//                    .withUser("jack").password("bauer").roles("USER");
        }
    }
}

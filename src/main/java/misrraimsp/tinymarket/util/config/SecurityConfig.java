package misrraimsp.tinymarket.util.config;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.service.UserServer;
import misrraimsp.tinymarket.util.auth.CustomAuthenticationFailureHandler;
import misrraimsp.tinymarket.util.auth.CustomAuthenticationSuccessHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServer userServer;
    private final MessageSource messageSource;

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(messageSource);
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // authenticate through login form
                .formLogin()
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler(customAuthenticationFailureHandler())

                // set logout page
                .and()
                .logout()
                .logoutSuccessUrl("/login")

                // authorization
                .and()
                .authorizeRequests()
                    // protect role_admin resources
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                    // protect role_user resources
                .antMatchers("/user/**")
                .access("hasRole('ROLE_USER')")
                    // publicly open
                .antMatchers("/", "/login", "/register")
                .access("permitAll")

                // enable csrf protection
                .and()
                .csrf()
                //.ignoringAntMatchers("/h2-console/**") // DEV - Make H2-Console non-secured
                //.ignoringAntMatchers("/**") // Testing

                /* DEV - Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                */
        ;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userServer)
                .passwordEncoder(encoder())
        ;
    }

}

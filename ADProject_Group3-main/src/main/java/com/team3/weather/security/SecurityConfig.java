package com.team3.weather.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {
        private CustomUserDetailsService userDetailsService;
        private RequestCache nullRequestCache = new NullRequestCache();
        private RoleBasedSuccessHandler roleBasedSuccessHandler;

        private static final String[] WHITE_LIST_URLS = {
                        "/login",
                        "/images/**",
                        "/css/**",
                        "/js/**",
                        "/register/**",
                        "/api/**"
        };

        @Autowired
        public SecurityConfig(CustomUserDetailsService userDetailsService,
                        RoleBasedSuccessHandler roleBasedSuccessHandler) {
                this.userDetailsService = userDetailsService;
                this.roleBasedSuccessHandler = roleBasedSuccessHandler;
        }

        // Add listener below to the configuration to keep Spring Security updated about
        // session lifecycle events:
        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
                return new HttpSessionEventPublisher();
        }

        @Bean
        public static PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(10);
                // default BCryptEncoder strength is 10
                // return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8(); //alternative
                // return NoOpPasswordEncoder.getInstance(); // temporary for testing
        }

        // delete when migrating to mySQL server
        @Bean
        WebSecurityCustomizer webSecurityCustomizer() {
                return web -> web.ignoring()
                                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
        }
        //

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(
                                new ClearSiteDataHeaderWriter(Directive.COOKIES, Directive.STORAGE));

                http
                                // Cross Site Request Forgery Protection, disabled for development
                                .csrf((csrf) -> csrf.disable())
                                // Requests other than those whitelisted will redirect to login page
                                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                                                .requestMatchers(WHITE_LIST_URLS)
                                                .permitAll()
                                                .anyRequest().authenticated())
                                // Disable cache storage of user's original request before authentication
                                .requestCache((cache) -> cache
                                                .requestCache(nullRequestCache))
                                // Configure the login page for authentication
                                .formLogin((formLogin) -> formLogin
                                                .loginPage("/login")
                                                .successHandler(roleBasedSuccessHandler)
                                                .loginProcessingUrl("/login")
                                                .failureUrl("/login?error")
                                                .permitAll())
                                // Clear cookies, storage, belonging to the owning website
                                .logout((logout) -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll()
                                                .addLogoutHandler(clearSiteData))
                                // Prevent a user from logging in multiple times - a second login will cause the
                                // first to be invalidated
                                .sessionManagement((session) -> session
                                                .sessionFixation((sessionFixation) -> sessionFixation
                                                                .newSession())
                                                .maximumSessions(1)
                                                .expiredUrl("/login?expired"));

                return http.build();
        }

        public void configure(AuthenticationManagerBuilder builder) throws Exception {
                builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }
}

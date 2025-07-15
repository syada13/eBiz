package com.ecommerce.eCom.security;


import com.ecommerce.eCom.model.AppRole;
import com.ecommerce.eCom.model.Role;
import com.ecommerce.eCom.model.User;
import com.ecommerce.eCom.repositories.RoleRepository;
import com.ecommerce.eCom.repositories.UserRepository;
import com.ecommerce.eCom.security.jwt.AuthEntryPointJwt;
import com.ecommerce.eCom.security.jwt.AuthTokenFilter;
import com.ecommerce.eCom.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();

    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
         DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
         daoAuthenticationProvider.setUserDetailsService(userDetailsService);
         daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
         return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain fiilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                                exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth)->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/admin/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()

                        .anyRequest().authenticated());

       http.authenticationProvider(daoAuthenticationProvider());
       http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().
                requestMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,PasswordEncoder passwordEncoder){
        return args -> {
            // Retrieve or create roles
           Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                   .orElseGet(() -> {
                       Role newUserRole = new Role(AppRole.ROLE_USER);
                       return roleRepository.save(newUserRole);
                   });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });


            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole,sellerRole,adminRole);

            // Create users if not already present
            if(!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com",
                        passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if(!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com",
                        passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if(!userRepository.existsByUserName("admin1")) {
                User admin1 = new User("admin1", "admin1@example.com",
                        passwordEncoder.encode("adminPass"));
                userRepository.save(admin1);
            }

            // Update roles for an existing users
            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUserName("admin1").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }
}

    package com.eewms.config;

    import com.eewms.services.impl.CustomUserDetailsService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final CustomUserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())   // Tắt CSRF (tạm ổn nếu bạn không dùng API REST)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()
                            .requestMatchers("/account/info", "/account/update-profile").authenticated()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                            .requestMatchers("/staff/**").hasAnyRole("ADMIN", "MANAGER", "STAFF")
                            .anyRequest().authenticated()
                    )

                    .formLogin(form -> form
                            .loginPage("/login")
                            .loginProcessingUrl("/do-login")
                            .defaultSuccessUrl("/dashboard", true)
                            .failureUrl("/login?error=true")
                            .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout=true")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .permitAll()
                    )
                    .userDetailsService(userDetailsService);

            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
    }



    //import lombok.RequiredArgsConstructor;
    //import org.modelmapper.ModelMapper;
    //import org.springframework.context.annotation.*;
    //import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    //import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    //import org.springframework.security.crypto.password.PasswordEncoder;
    //import org.springframework.web.servlet.config.annotation.*;
    //
    //@Configuration
    //@EnableWebSecurity
    //@RequiredArgsConstructor
    //public class SecurityConfig implements WebMvcConfigurer {
    //
    //
    //    @Value("${jwt.signerKey}")
    //    private String signerKey;
    //    @Bean
    //    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //        http
    //                .csrf(csrf -> csrf.disable())
    //                .sessionManagement(sm ->
    //                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //                )
    //                .authorizeHttpRequests(auth -> auth
    //                        .requestMatchers("/api/auth/**").permitAll()
    //                        .requestMatchers("/api/settings/**").permitAll()
    //                        .anyRequest().authenticated()
    //                );
    //        http.oauth2ResourceServer(oauth2 ->
    //                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));
    //
    //        http.csrf(AbstractHttpConfigurer::disable);
    //        return http.build();
    //    }
    //    @Bean
    //    JwtDecoder jwtDecoder(){
    //        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(),"HS256");
    //        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256)
    //                .build();
    //    }
    //    @Bean
    //    public ModelMapper modelMapper() {
    //        return new ModelMapper();
    //    }
    //
    //    @Bean
    //    public PasswordEncoder passwordEncoder() {
    //        return new BCryptPasswordEncoder();
    //    }
    //}

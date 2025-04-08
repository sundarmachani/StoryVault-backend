package com.daily.my_dairy.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class JwtAuthSecurityConfiguration {

  /**
   * Configures the Spring Security filter chain. - Disables CSRF for stateless APIs. - Permits
   * unauthenticated access to the `/authenticate` endpoint. - Requires authentication for all other
   * endpoints. - Configures JWT-based authentication for OAuth2 resource server.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/authenticate", "/register")
            .permitAll() // Allow unauthenticated access to /authenticate
            .anyRequest().authenticated() // All other requests require authentication
        )
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Enable JWT authentication
        .cors(cors -> cors.configurationSource(
            corsConfigurationSource())); // Apply custom CORS configuration

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
        List.of("http://localhost:5173", "https://storyvault.vercel.app")); // Allowed origins
    configuration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed methods
    configuration.setAllowedHeaders(List.of("*")); // Allow all headers
    configuration.setAllowCredentials(true); // Allow credentials (if needed)

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Configures an in-memory user store with two users: "sundar" and "admin". - Passwords are
   * encoded using BCryptPasswordEncoder.
   */
  @Bean
  public UserDetailsService userDetailsService() {
    var user = User.withUsername("sundar")
        .password(passwordEncoder().encode("password"))
        .roles("USER")
        .build();

    var admin = User.withUsername("admin")
        .password(passwordEncoder().encode("password"))
        .roles("ADMIN", "USER")
        .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  /**
   * Provides a BCryptPasswordEncoder bean for encoding passwords.
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Generates an RSA key pair for signing and verifying JWT tokens.
   */
  @Bean
  KeyPair keyPair() {
    try {
      var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048); // Generate a 2048-bit RSA key pair
      return keyPairGenerator.generateKeyPair();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Provides a JWKSource bean that supplies the RSA key as a JSON Web Key (JWK).
   */
  @Bean
  public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
    var jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, context) -> jwkSelector.select(jwkSet);
  }

  /**
   * Creates an RSAKey object using the generated RSA key pair.
   */
  @Bean
  public RSAKey rsaKey() {
    try {
      var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048); // Generate a 2048-bit RSA key pair
      var keyPair = keyPairGenerator.generateKeyPair();

      return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
          .privateKey(keyPair.getPrivate())
          .keyID(UUID.randomUUID().toString()) // Assign a unique ID to the key
          .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Configures a JwtDecoder bean for decoding JWT tokens using the public RSA key.
   */
  @Bean
  JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
    return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
  }

  /**
   * Configures a JwtEncoder bean for encoding JWT tokens using the private RSA key.
   */
  @Bean
  JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
    return new NimbusJwtEncoder(jwkSource);
  }

  /**
   * Provides an AuthenticationManager bean for authenticating users.
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}

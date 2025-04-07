package com.daily.my_dairy.security;

import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthSecurityController {

  private final JwtEncoder jwtEncoder; // Encoder for generating JWT tokens
  private final AuthenticationManager authenticationManager; // Manager for authenticating users

  public JwtAuthSecurityController(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager) {
    this.jwtEncoder = jwtEncoder;
    this.authenticationManager = authenticationManager;
  }

  /**
   * Authenticates the user and returns a JWT token if successful.
   *
   * @param authRequest The username and password provided by the user.
   * @return A response containing the generated JWT token.
   */
  @PostMapping("/authenticate")
  public JwtResponse authenticate(@RequestBody AuthRequest authRequest) {
    // Authenticate the user using the provided credentials
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
    );

    // Generate and return a JWT token
    return new JwtResponse(createToken(authentication));
  }

  /**
   * Creates a JWT token with claims including issuer, subject, expiration, and scope.
   *
   * @param authentication The authenticated user's details.
   * @return The generated JWT token as a string.
   */
  private String createToken(Authentication authentication) {
    var claims = JwtClaimsSet.builder()
        .issuer("self") // Set the token issuer as "self"
        .issuedAt(Instant.now()) // Token issued time
        .expiresAt(Instant.now().plusSeconds(60 * 30)) // Token expiration time (30 minutes)
        .subject(authentication.getName()) // Set the subject to the username
        .claim("scope", createScope(authentication)) // Add user roles/authorities as scope claim
        .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  /**
   * Extracts and concatenates the user's authorities/roles into a single space-separated string.
   *
   * @param authentication The authenticated user's details.
   * @return A space-separated string of authorities/roles.
   */
  private String createScope(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
  }
}

// Record for capturing username and password from the request body
record AuthRequest(String username, String password) {}

// Record for returning the JWT response containing the token string
record JwtResponse(String token) {}

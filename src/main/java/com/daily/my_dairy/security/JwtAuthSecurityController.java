package com.daily.my_dairy.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class JwtAuthSecurityController {

  private final JwtEncoder jwtEncoder;
  private final AuthenticationManager authenticationManager;

  public JwtAuthSecurityController(JwtEncoder jwtEncoder,
      AuthenticationManager authenticationManager) {
    this.jwtEncoder = jwtEncoder;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/authenticate")
  public JwtResponse authenticate(@RequestBody AuthRequest authRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
    );
    return new JwtResponse(createToken(authentication));
  }

  private String createToken(Authentication authentication) {
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(1800))
        .subject(authentication.getName())
        .claim("scope", createScope(authentication))
        .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  private String createScope(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
  }
}

// request/response records
record AuthRequest(String username, String password) {

}

record JwtResponse(String token) {

}
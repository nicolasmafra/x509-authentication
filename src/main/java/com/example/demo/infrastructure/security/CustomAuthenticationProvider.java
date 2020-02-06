package com.example.demo.infrastructure.security;

import com.example.demo.model.Usuario;
import com.example.demo.service.UserService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Optional;

@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (!(authentication.getCredentials() instanceof X509Certificate)) {
            return null;
        }
        X509Certificate cert = (X509Certificate) authentication.getCredentials();
        String chavePublica = Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded());
        log.debug("Chave pública: {}", chavePublica);
        Optional<Usuario> optionalUsuario = userService.findByChavePublica(chavePublica);
        if (optionalUsuario.isPresent()) {
            PreAuthenticatedAuthenticationToken preAuth = new PreAuthenticatedAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials());
            preAuth.setAuthenticated(true);
            preAuth.setDetails(optionalUsuario.get());
            return preAuth;
        } else {
            String message = "Chave pública não cadastrada.";
            log.info(message);
            throw new BadCredentialsException(message);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}

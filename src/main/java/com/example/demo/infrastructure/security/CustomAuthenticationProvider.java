package com.example.demo.infrastructure.security;

import com.example.demo.util.X509Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Function<String, Optional<?>> buscaPorChavePublica;

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (!(authentication.getCredentials() instanceof X509Certificate)) {
            return null;
        }
        X509Certificate cert = (X509Certificate) authentication.getCredentials();
        String chavePublica = X509Util.getBase64PublicKey(cert);
        Object details = buscaPorChavePublica.apply(chavePublica)
                .orElseThrow(() -> {
                    log.info("Recebendo chave pública não cadastrada: " + chavePublica);
                    return new BadCredentialsException("Chave pública não cadastrada.");
                });
        PreAuthenticatedAuthenticationToken preAuth = new PreAuthenticatedAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials());
        preAuth.setAuthenticated(true);
        preAuth.setDetails(details);
        return preAuth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}

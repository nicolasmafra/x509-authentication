package com.example.demo.service;

import com.example.demo.X509Utils;
import com.example.demo.model.Usuario;
import com.example.demo.model.Usuario_;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<Usuario> findByChavePublica(String chavePublica) {
        String chavePublicaNormal = chavePublica.replaceAll("[^A-Za-z0-9+/=]", "");
        Specification<Usuario> specification = (root, cq, cb) -> cb.equal(root.get(Usuario_.chavePublica), chavePublicaNormal);
        return userRepository.findOne(specification);
    }

    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        if (authentication.getDetails() instanceof Usuario) {
            return (Usuario) authentication.getDetails();
        } else if (authentication.getCredentials() instanceof X509Certificate) {
            String chavePublica = X509Utils.getBase64PublicKey((X509Certificate) authentication.getCredentials());
            return findByChavePublica(chavePublica).orElse(null);
        } else {
            return null;
        }
    }
}

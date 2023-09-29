package com.benzaied.gestiondestock.services.auth;

import com.benzaied.gestiondestock.Repository.ClientRepository;
import com.benzaied.gestiondestock.Repository.UtilisateurRepository;
import com.benzaied.gestiondestock.dto.ClientDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.model.Client;
import com.benzaied.gestiondestock.model.Utilisateur;
import com.benzaied.gestiondestock.model.auth.ExtendedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    /*@Autowired
    private UtilisateurService service;*/

    private final UtilisateurRepository utilisateurRepository;

    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findByEmail(email);

        if (utilisateurOptional.isPresent()) {
            UtilisateurDto utilisateur = UtilisateurDto.fromEntity(utilisateurOptional.get());

            if (utilisateur != null) {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                utilisateur.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));

                Integer entrepriseId = (utilisateur.getEntreprise() != null) ? utilisateur.getEntreprise().getId() : null;

                if (entrepriseId == null) {
                    return new ExtendedUser(utilisateur.getEmail(), utilisateur.getMotDePasse(), authorities);
                } else {
                    return new ExtendedUser(utilisateur.getEmail(), utilisateur.getMotDePasse(), entrepriseId, authorities);
                }
            }
        } else{

            Optional<Client> clientOptional = clientRepository.findByEmail(email);

            if (clientOptional.isPresent()) {
                ClientDto client = ClientDto.fromEntity(clientOptional.get());

                if (client != null) {
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(client.getRole().name()));

                    if (client.getIdEntreprise() == null) {
                        return new ExtendedUser(client.getEmail(), client.getMotDePasse(), authorities);
                    } else {
                        return new ExtendedUser(client.getEmail(), client.getMotDePasse(), client.getIdEntreprise(), authorities);
                    }
                }
            }

        }

        return null;

    }

}

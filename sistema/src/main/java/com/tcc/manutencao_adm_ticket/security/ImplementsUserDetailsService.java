package com.tcc.manutencao_adm_ticket.security;

import javax.transaction.Transactional;

import com.tcc.manutencao_adm_ticket.model.Usuario;
import com.tcc.manutencao_adm_ticket.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class ImplementsUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ImplementsUserDetailsService(UsuarioRepository usuarioRepository)
    {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if(usuario == null){
            throw new UsernameNotFoundException("Usuario não encontrado!");
        }
        return new User(usuario.getUsername(), new BCryptPasswordEncoder().encode(usuario.getPassword()), true, true, true, true, usuario.getAuthorities());
    }

//    private Set<GrantedAuthority> getAuthorities(Usuario usuario){
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        for (Role role: usuario.getRoles())
//        {
//            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
//            authorities.add(grantedAuthority);
//        }
//        return authorities;
//    }
}

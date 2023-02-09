package br.ufsm.poli.csi.tapw.pilacoin.security.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.Usuario;
import br.ufsm.poli.csi.tapw.pilacoin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Object[][] objeto = usuarioRepository.findUsuarioByEmail(email);

        if (objeto != null) {
            Usuario usuario = new Usuario(objeto);
            User.UserBuilder user = User.withUsername(usuario.getEmail())
                    .authorities(usuario.getAutoridade())
                    .password(usuario.getSenha());
            return user.build();
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
    }

}

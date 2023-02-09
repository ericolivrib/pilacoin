package br.ufsm.poli.csi.tapw.pilacoin.repository;

import br.ufsm.poli.csi.tapw.pilacoin.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT u.id, u.nome, u.email, u.senha, u.chave_publica, u.autoridade " +
            "FROM usuario u WHERE u.email = ?1", nativeQuery = true)
    Object[][] findUsuarioByEmail(String email);

    @Query(value = "SELECT u.chave_publica FROM usuario u WHERE u.nome = ?1", nativeQuery = true)
    byte[] findPublicKeyByNome(String nome);

}

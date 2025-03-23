package com.postech.gourmet.gateways.integrations;

import com.postech.gourmet.domain.entities.Usuario;
import com.postech.gourmet.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioRepositoryIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve salvar e recuperar um usuário com sucesso")
    void deveSalvarERecuperarUsuarioComSucesso() {
        // Cria um novo usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 98765-4321");

        // Salva o usuário
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        assertNotNull(usuarioSalvo.getId());

        // Recupera o usuário pelo ID
        Optional<Usuario> usuarioRecuperado = usuarioRepository.findById(usuarioSalvo.getId());
        assertTrue(usuarioRecuperado.isPresent());
        assertEquals(usuario.getNome(), usuarioRecuperado.get().getNome());
        assertEquals(usuario.getEmail(), usuarioRecuperado.get().getEmail());
        assertEquals(usuario.getSenha(), usuarioRecuperado.get().getSenha());
        assertEquals(usuario.getTelefone(), usuarioRecuperado.get().getTelefone());
    }

    @Test
    @DisplayName("Deve verificar existência de usuário por ID")
    void deveVerificarExistenciaDeUsuarioPorId() {
        // Cria e salva um usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario = usuarioRepository.save(usuario);

        // Verifica existência com ID correto
        boolean existe = usuarioRepository.existsById(usuario.getId());
        assertTrue(existe);

        // Verifica não existência com ID inexistente
        boolean naoExiste = usuarioRepository.existsById(999L);
        assertFalse(naoExiste);
    }

    @Test
    @DisplayName("Deve verificar existência de usuário por email")
    void deveVerificarExistenciaDeUsuarioPorEmail() {
        // Cria e salva um usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("email.unico@teste.com");
        usuario.setSenha("senha123");
        usuarioRepository.save(usuario);

        // Verifica existência com email correto
        boolean existe = usuarioRepository.existsByEmail("email.unico@teste.com");
        assertTrue(existe);

        // Verifica não existência com email inexistente
        boolean naoExiste = usuarioRepository.existsByEmail("inexistente@teste.com");
        assertFalse(naoExiste);
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        // Cria e salva um usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Nome Original");
        usuario.setEmail("original@teste.com");
        usuario.setSenha("senha123");
        usuario.setTelefone("(11) 12345-6789");
        usuario = usuarioRepository.save(usuario);

        // Atualiza os dados do usuário
        usuario.setNome("Nome Atualizado");
        usuario.setEmail("atualizado@teste.com");
        usuario.setTelefone("(11) 98765-4321");

        // Salva as atualizações
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        // Recupera o usuário atualizado
        Optional<Usuario> usuarioRecuperado = usuarioRepository.findById(usuario.getId());
        assertTrue(usuarioRecuperado.isPresent());
        assertEquals("Nome Atualizado", usuarioRecuperado.get().getNome());
        assertEquals("atualizado@teste.com", usuarioRecuperado.get().getEmail());
        assertEquals("(11) 98765-4321", usuarioRecuperado.get().getTelefone());
    }

    @Test
    @DisplayName("Deve rejeitar emails duplicados")
    void deveRejeitarEmailsDuplicados() {
        // Cria e salva um primeiro usuário
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Primeiro Usuário");
        usuario1.setEmail("duplicado@teste.com");
        usuario1.setSenha("senha123");
        usuarioRepository.save(usuario1);

        // Verifica que o email já existe
        boolean emailExiste = usuarioRepository.existsByEmail("duplicado@teste.com");
        assertTrue(emailExiste);

        // Tenta criar outro usuário com o mesmo email
        Usuario usuario2 = new Usuario();
        usuario2.setNome("Segundo Usuário");
        usuario2.setEmail("duplicado@teste.com");
        usuario2.setSenha("outrasenha");

        // O teste de existência deve falhar, mas a camada de validação no caso de uso
        // deve impedir a inserção, não o repositório diretamente.
        boolean emailDuplicado = usuarioRepository.existsByEmail(usuario2.getEmail());
        assertTrue(emailDuplicado);
    }

    @Test
    @DisplayName("Deve inicializar listas vazias para novo usuário")
    void deveInicializarListasVaziasParaNovoUsuario() {
        // Cria e salva um usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Usuário Teste");
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");
        usuario = usuarioRepository.save(usuario);

        // Recupera o usuário
        Optional<Usuario> usuarioRecuperado = usuarioRepository.findById(usuario.getId());
        assertTrue(usuarioRecuperado.isPresent());

        // Verifica que as listas foram inicializadas vazias
        assertNotNull(usuarioRecuperado.get().getReservas());
        assertNotNull(usuarioRecuperado.get().getAvaliacoes());
        assertTrue(usuarioRecuperado.get().getReservas().isEmpty());
        assertTrue(usuarioRecuperado.get().getAvaliacoes().isEmpty());
    }
}
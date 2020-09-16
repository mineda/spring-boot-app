package br.gov.sp.fatec.springbootapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import br.gov.sp.fatec.springbootapp.entity.Autorizacao;
import br.gov.sp.fatec.springbootapp.entity.Usuario;
import br.gov.sp.fatec.springbootapp.repository.AutorizacaoRepository;
import br.gov.sp.fatec.springbootapp.repository.UsuarioRepository;
import br.gov.sp.fatec.springbootapp.service.SegurancaService;

@SpringBootTest
@Transactional
class SpringBootAppApplicationTests {

  @Autowired
  private UsuarioRepository usuarioRepo;

  @Autowired
  private AutorizacaoRepository autRepo;

  @Autowired
  private SegurancaService segService;

  @BeforeAll
  static void init(@Autowired JdbcTemplate jdbcTemplate) {
    jdbcTemplate.update(
        "insert into usr_usuario (usr_nome, usr_senha) values(?,?)",
            "Mineda", "SenhaF0rte");
    jdbcTemplate.update(
        "insert into aut_autorizacao (aut_nome) values(?)",
            "ROLE_ADMIN");
    jdbcTemplate.update(
        "insert into uau_usuario_autorizacao (usr_id, aut_id) values(?,?)",
            1L, 1L);
  }

  @Test
  void contextLoads() {
  }

  @Test
  void testaInsercao() {
    Usuario usuario = new Usuario();
    usuario.setNome("Usuario");
    usuario.setSenha("SenhaF0rte");
    usuario.setAutorizacoes(new HashSet<Autorizacao>());
    Autorizacao aut = new Autorizacao();
    aut.setNome("ROLE_USUARIO");
    autRepo.save(aut);
    usuario.getAutorizacoes().add(aut);
    usuarioRepo.save(usuario);
    assertNotNull(usuario.getAutorizacoes().iterator().next().getId());
  }

  @Test
  void testaInsercaoAutorizacao() {
    Usuario usuario = new Usuario();
    usuario.setNome("Usuario2");
    usuario.setSenha("SenhaF0rte");
    usuarioRepo.save(usuario);
    Autorizacao aut = new Autorizacao();
    aut.setNome("ROLE_USUARIO2");
    aut.setUsuarios(new HashSet<Usuario>());
    aut.getUsuarios().add(usuario);
    autRepo.save(aut);
    assertNotNull(aut.getUsuarios().iterator().next().getId());
  }

  @Test
  void testaAutorizacao() {
    Usuario usuario = usuarioRepo.findById(1L).get();
    assertEquals("ROLE_ADMIN", usuario.getAutorizacoes().iterator().next().getNome());
  }

  @Test
  void testaUsuario() {
    Autorizacao aut = autRepo.findById(1L).get();
    assertEquals("Mineda", aut.getUsuarios().iterator().next().getNome());
  }

  @Test
  void testaBuscaUsuarioNomeContains() {
    List<Usuario> usuarios = usuarioRepo.findByNomeContainsIgnoreCase("E");
    assertFalse(usuarios.isEmpty());
  }

  @Test
  void testaBuscaUsuarioNome() {
    Usuario usuario = usuarioRepo.findByNome("Mineda");
    assertNotNull(usuario);
  }

  @Test
  void testaBuscaUsuarioNomeQuery() {
    Usuario usuario = usuarioRepo.buscaUsuarioPorNome("Mineda");
    assertNotNull(usuario);
  }

  @Test
  void testaBuscaUsuarioNomeSenha() {
    Usuario usuario = usuarioRepo.findByNomeAndSenha("Mineda", "SenhaF0rte");
    assertNotNull(usuario);
  }

  @Test
  void testaBuscaUsuarioNomeSenhaQuery() {
    Usuario usuario = usuarioRepo.buscaUsuarioPorNomeESenha("Mineda", "SenhaF0rte");
    assertNotNull(usuario);
  }

  @Test
  void testaBuscaUsuarioNomeAutorizacao() {
    List<Usuario> usuarios = usuarioRepo.findByAutorizacoesNome("ROLE_ADMIN");
    assertFalse(usuarios.isEmpty());
  }

  @Test
  void testaBuscaUsuarioNomeAutorizacaoQuery() {
    List<Usuario> usuarios = usuarioRepo.buscaPorNomeAutorizacao("ROLE_ADMIN");
    assertFalse(usuarios.isEmpty());
  }

  @Test
  void testaServicoCriaUsuario() {
    Usuario usuario = segService.criarUsuario("normal", "senha123", "ROLE_USUARIO");
    assertNotNull(usuario);
  }

}

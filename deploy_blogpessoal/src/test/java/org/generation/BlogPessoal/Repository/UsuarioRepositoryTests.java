package org.generation.BlogPessoal.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.BlogPessoal.Model.Usuario;
import org.generation.BlogPessoal.service.UsuarioService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTests {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	void start() {
		usuarioRepository.save(new Usuario(0L, "Ramon Daniel Santos", "ramonzito@clovis.com", "1223456789", "https://i.imgur.com/FETvs2O.jpg"));

		usuarioRepository.save(new Usuario(0L, "Robson Carmo Santos", "robsonbruxinho@carmo.com", "mago1234", "https://i.imgur.com/FETvs2O.jpg"));

		usuarioRepository.save(new Usuario(0L, "Paola Bracho Santos", "ataldausurpadora@yahoo.com", "carlosdaniel", "https://i.imgur.com/FETvs2O.jpg"));

	}

	@Test
	@DisplayName("Retorna apenenas um usuário")
	public void deveRetornarUmUsuario() {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("ramonzito@clovis.com");
		assertTrue(usuario.get().getUsuario().equals("ramonzito@clovis.com"));

	}

	@Test
	@DisplayName("Deve retorna 3 usuários")

	public void deveRetornarTresUsuarios() {
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Santos");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Ramon Daniel Santos"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Robson Carmo Santos"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Paola Bracho Santos"));

	}

	@AfterAll

	public void end() {
		usuarioRepository.deleteAll();
	}
} 

package org.generation.BlogPessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;


import org.generation.BlogPessoal.Model.Usuario;
import org.generation.BlogPessoal.Model.UsuarioLogin;
import org.generation.BlogPessoal.Repository.UsuarioRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.stereotype.Service; 
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Optional<Usuario> cadastrarUsuario(Usuario user){
		
		if (usuarioRepository.findByUsuario(user.getUsuario()).isPresent())
			return Optional.empty();
		
		user.setSenha(encryptPassword(user.getSenha()));
		
		return Optional.of(usuarioRepository.save(user));
		
	}
	
	public Optional<Usuario> atualizarUsuario(Usuario user) {

		
		if(usuarioRepository.findById(user.getId()).isPresent()) {
			
			Optional<Usuario> searchUser = usuarioRepository.findByUsuario(user.getUsuario());
			
			if ( (searchUser.isPresent()) && ( searchUser.get().getId() != user.getId()))
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			
			user.setSenha(encryptPassword(user.getSenha()));

			return Optional.ofNullable(usuarioRepository.save(user));
			
		}
		
			return Optional.empty();
	
	}
	
	public Optional<UsuarioLogin> autentificarUsuario(Optional<UsuarioLogin> usuarioLogin){
		
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent()) {
			
			if(comparePassword(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(generateBasicToken(usuario.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				
				return usuarioLogin;
			}
		}
		
		return Optional.empty();
	}
	
	private String encryptPassword(String senha) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
	}
	
	private boolean comparePassword(String senhaDigitada, String senhaBanco) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada, senhaDigitada);
		
	}
	
	private String generateBasicToken(String user, String password) {
		
		String token = user + ":" + password;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
		
	}
}


package com.bolsadeideas.springboot.jpa.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.jpa.app.entity.Usuario;

public interface UsuarioDao extends CrudRepository<Usuario, Long> {

	public Usuario findByUsername(String username);
	
}

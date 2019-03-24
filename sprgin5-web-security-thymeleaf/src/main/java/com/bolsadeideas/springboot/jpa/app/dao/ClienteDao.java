package com.bolsadeideas.springboot.jpa.app.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.jpa.app.entity.Cliente;

public interface ClienteDao extends PagingAndSortingRepository<Cliente, Long>{

	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWhithFacturas(Long id);
}

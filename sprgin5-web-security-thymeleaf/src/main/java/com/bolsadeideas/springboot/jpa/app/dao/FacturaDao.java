package com.bolsadeideas.springboot.jpa.app.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.jpa.app.entity.Factura;

public interface FacturaDao extends CrudRepository<Factura, Long> {

	@Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id=?1")
	public Factura fetchByIdWhihClienteWhithItemFacturaWhithProducto(Long id);
}

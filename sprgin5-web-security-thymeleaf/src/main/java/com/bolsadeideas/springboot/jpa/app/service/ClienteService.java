package com.bolsadeideas.springboot.jpa.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.jpa.app.entity.Cliente;
import com.bolsadeideas.springboot.jpa.app.entity.Factura;
import com.bolsadeideas.springboot.jpa.app.entity.Producto;

public interface ClienteService  {
	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);
	
	public void save(Cliente cliente);
	
	public void delete(Long id);
	
	public Cliente findOne(Long id);
	
	public Cliente fetchByIdWhithFacturas(Long id);
	
	public List<Producto> findByNombre(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);
	
	public void deleteFactura(Long id);
	
	public Factura fetchByIdWhihClienteWhithItemFacturaWhithProducto(Long id);
	
	
}

package com.bolsadeideas.springboot.jpa.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.jpa.app.dao.ClienteDao;
import com.bolsadeideas.springboot.jpa.app.dao.FacturaDao;
import com.bolsadeideas.springboot.jpa.app.dao.ProductoDao;
import com.bolsadeideas.springboot.jpa.app.entity.Cliente;
import com.bolsadeideas.springboot.jpa.app.entity.Factura;
import com.bolsadeideas.springboot.jpa.app.entity.Producto;

@Service("clienteServiceImpl")
public class ClienteServiceImpl implements ClienteService  {
	@Autowired
	private ProductoDao productoDao;
	
	@Autowired
	private FacturaDao facturaDao;
	
	@Autowired
	private ClienteDao clienteDao;
	@Override
	@Transactional(readOnly=true) //envuelte el contenido del metodo en una transaccion

	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional//para insertar un nuevo registro

	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		clienteDao.save(cliente);
		
	}

	@Override
	@Transactional(readOnly=true)//solo lectura

	public void delete(Long id) {
		// TODO Auto-generated method stub
		clienteDao.deleteById(id);
	}
	
	public void delete(Cliente cliente) {
		// TODO Auto-generated method stub
		clienteDao.delete(cliente);
	}
	

	
	@Override
	@Transactional

	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public Cliente fetchByIdWhithFacturas(Long id) {
		// TODO Auto-generated method stub
		return clienteDao.fetchByIdWhithFacturas(id);
	}
	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		
//		return productoDao.findByNombre(term);
		return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {

		facturaDao.save(factura);
	}

	@Override
	public Producto findProductoById(Long id) {
		
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional (readOnly=true)
	public Factura findFacturaById(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);//springboot 2 : dacturaDao.deleteById(id)
		
	}

	@Override
	@Transactional(readOnly=true)
	public Factura fetchByIdWhihClienteWhithItemFacturaWhithProducto(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.fetchByIdWhihClienteWhithItemFacturaWhithProducto(id);
	}

	

}

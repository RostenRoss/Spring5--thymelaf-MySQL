package com.bolsadeideas.springboot.jpa.app.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.jpa.app.entity.Cliente;
import com.bolsadeideas.springboot.jpa.app.entity.Factura;
import com.bolsadeideas.springboot.jpa.app.entity.ItemFactura;
import com.bolsadeideas.springboot.jpa.app.entity.Producto;
import com.bolsadeideas.springboot.jpa.app.service.ClienteServiceImpl;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private ClienteServiceImpl clienteServiceImpl;
	
	@GetMapping("/form/{clienteId}")//ruta para mostrar el formulario
	public String crear(@PathVariable(value="clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente=clienteServiceImpl.findOne(clienteId);
		if(cliente==null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		Factura factura=new Factura();
		factura.setCliente(cliente);//a la factura le asociamos el cliente
		model.put("factura",factura);//pasamos la factura al modelo
		model.put("titulo", "crear Factura");//pasamos un titulo al modelo
		return "factura/form";
	}
	
	@GetMapping(value="/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
		return clienteServiceImpl.findByNombre(term);	
	}
	
	@PostMapping("/form")
	public String guardar(@Valid Factura factura,
		BindingResult result,
		Model model,
		@RequestParam(name="item_id[]", required=false) Long[] itemId,
		@RequestParam(name="cantidad[]", required=false) Integer[] cantidad,
		RedirectAttributes flash, 
		SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear factura");
			return"factura/form";
		}
		if(itemId== null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear factura");
			model.addAttribute("error", "La factura NO puede no tener lineas!");
			return"factura/form";
		}
		for(int i=0;i<itemId.length;i++) {
			Producto producto=clienteServiceImpl.findProductoById(itemId[i]);
			ItemFactura linea =new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
		}
		clienteServiceImpl.saveFactura(factura);
		status.setComplete();
		flash.addFlashAttribute("success", "Factura creada con exito!");
		return "redirect:/ver/"+factura.getCliente().getId();
	}
	
	@GetMapping("ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Model model, RedirectAttributes flash) {
		Factura factura=clienteServiceImpl.fetchByIdWhihClienteWhithItemFacturaWhithProducto(id);//clienteServiceImpl.findFacturaById(id);
		if(factura==null) {
			flash.addFlashAttribute("error","");
			return "redirect:/listar";
		}
		model.addAttribute("factura", factura);
		model.addAttribute("titulo","Factura: ".concat(factura.getDescripcion()));
		return "factura/ver";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id")Long id, RedirectAttributes flash) {
		Factura factura=clienteServiceImpl.findFacturaById(id);
		if(factura!=null) {
			clienteServiceImpl.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con exito!");
			return "redirect:/ver/"+factura.getCliente().getId();
		}
		flash.addFlashAttribute("error","La factura no existe en la base de datos, no se puedo eliminar!");
		return "redirect:/listar";
	}
	
}

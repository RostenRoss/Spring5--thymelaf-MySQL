package com.bolsadeideas.springboot.jpa.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.jpa.app.entity.Cliente;
import com.bolsadeideas.springboot.jpa.app.paginator.PageRender;
import com.bolsadeideas.springboot.jpa.app.service.ClienteServiceImpl;
import com.bolsadeideas.springboot.jpa.app.service.IUploadFileServiceImpl;
import com.bolsadeideas.springboot.jpa.app.view.xml.ClienteList;

@Controller
public class ClienteController {
	
	@Autowired
	private MessageSource messageSource;
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IUploadFileServiceImpl iUploadFileServiceImpl;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static String UPLOADS_FOLDER = "uploads";
	@Autowired
	@Qualifier("clienteServiceImpl")
	private ClienteServiceImpl clienteServiceImpl;
	
	@Secured("ROLE_USER")
	@GetMapping(value = "/uploads/{filename:.+}") // el ".+" le indica a spring que tambien incluya la extencion
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = iUploadFileServiceImpl.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@RequestMapping(value = {"/listar", "/"} , method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Authentication authentication, HttpServletRequest request, Locale locale) {
		if(authentication != null) {
			logger.info("Utilizando la inyeccion de dependencia Authentication: Hola usuario autenticado, tu username es:"+authentication.getName());
		}
		Authentication auth =SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			logger.info("Utilizando forma estatica SecurityContextHolder.getContext().getAuthentication(): Hola usuario autenticado, tu username es:"+auth.getName());
		}
		if(hasRole("ROLE_ADMIN")) {//para este metodo se debe agregar 'ROLE_' antes del rol
			
			logger.info("Hola "+auth.getName()+" tienes acceso!");
		}else {
			logger.info("Hola "+auth.getName()+" NO tienes acceso!");

		}
		
		if(request.isUserInRole("ADMIN")) {//a diferencia de este caso donde no se necesita colocarl el 'ROLE_'
			
			logger.info("Forma usando HttpServletRequest: Hola "+auth.getName()+" tienes acceso!");
		}else {
			logger.info("Forma usando HttpServletRequestHola "+auth.getName()+" NO tienes acceso!");
			
		}
		
		SecurityContextHolderAwareRequestWrapper securirtyContext=new SecurityContextHolderAwareRequestWrapper(request, "");
		if(securirtyContext.isUserInRole("ROLE_ADMIN")) {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola "+auth.getName()+"  tienes acceso!");
		}else {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola "+auth.getName()+" NO tienes acceso!");

		}
		
		Pageable pageRequest = new PageRequest(page, 5);
		Page<Cliente> clientes = clienteServiceImpl.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo",null,locale));
		model.addAttribute("clientes", clientes/* clienteServiceImpl.findAll() */);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash/* , SessionStatus status */) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "formulario de cliente");
			return "/form";
		}
		if (!foto.isEmpty()) {
			Cliente clienteDb = clienteServiceImpl.findOne(cliente.getId());
			log.info("foto: " + clienteDb.getFoto());
			if (cliente.getId() != null && cliente.getId() > 0 && clienteDb.getFoto() != null
					&& clienteDb.getFoto().length() > 0) {
				iUploadFileServiceImpl.delete(clienteDb.getFoto());

			}
			String uniquefilename = null;
			try {
				uniquefilename = iUploadFileServiceImpl.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Se a subido correctamente la foto: '" + uniquefilename + "'");

			cliente.setFoto(uniquefilename);
		}
		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con exito!" : "Cliente creado con exito!";
		logger.info("Tu id es: "+cliente.getId());
		clienteServiceImpl.save(cliente);
		logger.info("Tu id es: "+cliente.getId());

		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id != null && id > 0) {
			cliente = clienteServiceImpl.findOne(id);
			if (cliente == null) {

				flash.addFlashAttribute("error", "El id del cliente no existe en base de dato!");
			}
		} else {
			flash.addFlashAttribute("error", "El id del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteServiceImpl.findOne(id);

			clienteServiceImpl.delete(cliente);

			flash.addFlashAttribute("success", "Cliente eliminado con exito!");

			if (iUploadFileServiceImpl.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito");
			}
		}

		return "redirect:/listar";
	}
	
	@Secured("ROLE_USER")
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteServiceImpl.fetchByIdWhithFacturas(id);//clienteServiceImpl.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());

		return "ver";
	}
	
	private boolean hasRole(String role) {
		SecurityContext context=SecurityContextHolder.getContext();
		
		if(context==null) {
			return false;
		}
		Authentication auth=context.getAuthentication();
		if(auth==null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities=auth.getAuthorities(); //? es un generico que extiende de grantedauthority
		
		return authorities.contains(new SimpleGrantedAuthority(role));
//		for(GrantedAuthority authority: authorities) {
//			if(role.equals(authority.getAuthority())) {
//				logger.info("Hola usuario "+auth.getName()+" tienes el role '"+authority.getAuthority()+"'");
//				return true;
//			}
//		}
//		return false;

	}
	
	@GetMapping("/listar-rest")
	public @ResponseBody ClienteList listarRest() {
		
		
		return new ClienteList(clienteServiceImpl.findAll());
	}
}

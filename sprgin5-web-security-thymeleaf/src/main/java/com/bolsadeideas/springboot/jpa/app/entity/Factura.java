package com.bolsadeideas.springboot.jpa.app.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="facturas")
public class Factura implements Serializable {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String descripcion;
	private String observacion;
	@Column(name="create_at")
	@JsonFormat(pattern="yyy-MM-dd HH:mm:ss")
	private Timestamp createAt;
	
	@ManyToOne(fetch=FetchType.LAZY)//lazy es carga perezosa, evita que se traiga toda la carga de una, solo carga cuando se le llama con el metodo getCliente
	@JsonBackReference//es la parte posterior de la relacion y se omitira de la serializacion
	private Cliente cliente;
	
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="factura_id")
	private List<ItemFactura>items;
	
	
	
	@PrePersist
	public void prePersist(){
		createAt=new Timestamp(new Date().getTime());
	}
	
	
	
	
	
	
	public Factura() {
		this.items=new ArrayList<ItemFactura>();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Timestamp getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}
	@XmlTransient

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	
	public List<ItemFactura> getItems() {
		return items;
	}
	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}

	public void addItemFactura(ItemFactura item){
		this.items.add(item);
	}
	
	public Double getTotal(){
		Double total=0.0;
		
		int size=items.size();
		for (int i = 0; i < size; i++) {
			total+=items.get(i).calcularImporte();
		}
		return total;
	}
	private static final long serialVersionUID=1L;

}



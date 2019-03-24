package com.bolsadeideas.springboot.jpa.app.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="productos")
public class Producto implements Serializable{

	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private Double precio;
	
	@Column(name="create_at")
	@JsonFormat(pattern="yyy-MM-dd HH:mm:ss")
	private Timestamp createAt;
		
	@PrePersist
	public void prePersist(){
		createAt=new Timestamp(new Date().getTime());
	}
	
	
	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public String getNombre() {
		return nombre;
	}





	public void setNombre(String nombre) {
		this.nombre = nombre;
	}





	public Double getPrecio() {
		return precio;
	}





	public void setPrecio(Double precio) {
		this.precio = precio;
	}





	public Timestamp getCreateAt() {
		return createAt;
	}





	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}





	private static final long serialVersionUID = 1L;

}

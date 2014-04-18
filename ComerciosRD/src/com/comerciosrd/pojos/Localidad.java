package com.comerciosrd.pojos;

import java.io.Serializable;

public class Localidad implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idLocalidadPk;

	private Cliente cliente;
	private Categoria categoria;
	private Long idEstadoFk;

	private Provincia provincia;

	private String direccion;

	private String descripcion;

	private String telefono;

	private String email;

	private Double latitud;

	private Double longitud;

	private Integer indCercano = 0;
	private Double distancia;

	public static double lat;
	public static double lng;

	public Long getIdLocalidadPk() {
		return idLocalidadPk;
	}

	public void setIdLocalidadPk(Long idLocalidadPk) {
		this.idLocalidadPk = idLocalidadPk;
	}

	public Long getIdEstadoFk() {
		return idEstadoFk;
	}

	public void setIdEstadoFk(Long idEstadoFk) {
		this.idEstadoFk = idEstadoFk;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getIndCercano() {
		return indCercano;
	}

	public void setIndCercano(Integer indCercano) {
		this.indCercano = indCercano;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Double getDistancia() {
		return distancia;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}
}

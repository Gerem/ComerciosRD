package com.comerciosrd.pojos;

import java.io.Serializable;

public class Categoria implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idCategoriaPk;
	private String categoria;
	
	public Long getIdCategoriaPk() {
		return idCategoriaPk;
	}
	public void setIdCategoriaPk(Long idCategoriaPk) {
		this.idCategoriaPk = idCategoriaPk;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}

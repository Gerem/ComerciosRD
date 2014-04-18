package com.comerciosrd.pojos;

public class Provincia {
	private Long idProvinciaPk;
	private Long idPaisFk;
	private String nombreProvincia;
	
	public Long getIdProvinciaPk() {
		return idProvinciaPk;
	}
	public void setIdProvinciaPk(Long idProvinciaPk) {
		this.idProvinciaPk = idProvinciaPk;
	}
	public Long getIdPaisFk() {
		return idPaisFk;
	}
	public void setIdPaisFk(Long idPaisFk) {
		this.idPaisFk = idPaisFk;
	}
	public String getNombreProvincia() {
		return nombreProvincia;
	}
	public void setNombreProvincia(String nombreProvincia) {
		this.nombreProvincia = nombreProvincia;
	}
	public Long getIdEstadoFk() {
		return idEstadoFk;
	}
	public void setIdEstadoFk(Long idEstadoFk) {
		this.idEstadoFk = idEstadoFk;
	}
	private Long idEstadoFk;
}

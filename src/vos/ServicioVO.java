package vos;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 
 * @author JOSE
 */
public class ServicioVO {

	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty ("id")
	private Long id;
	
	@JsonProperty (value = "costoExtra")
	private Double costoExtra;
	
	@JsonProperty (value = "nombreServicio")
	private String nombreServicio;
	
	@JsonProperty (value = "idAlojamiento")
	private Long idAlojamiento;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public ServicioVO (@JsonProperty (value = "id") Long pId,
					   @JsonProperty (value = "costoExtra") Double pCosto, 
					   @JsonProperty (value = "nombreServicio") String pNombre,
					   @JsonProperty (value = "idAlojamiento") Long pIdAlojamiento){
		
		this.id = pId;
		this.costoExtra = pCosto;
		this.nombreServicio = pNombre;	
		this.idAlojamiento = pIdAlojamiento;
	}

	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------
	
	public Double getCostoExtra() {
		return costoExtra;
	}

	public void setCostoExtra(Double costoExtra) {
		this.costoExtra = costoExtra;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public Long getIdAlojamiento() {
		return idAlojamiento;
	}

	public void setIdAlojamiento(Long idAlojamiento) {
		this.idAlojamiento = idAlojamiento;
	}
}
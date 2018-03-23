package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un objeto de tipo Propuesta. 
 * @author Grupo C-13 
 */
public class ReservaVO {

	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty (value = "id")
	private Long id;
	
	@JsonProperty (value = "nochesReserva")
	private Integer nochesReserva;
	
	@JsonProperty (value = "fechaInicioReserva")
	private Date fechaInicioReserva;
	
	@JsonProperty (value = "fechaFinReserva")
	private Date fechaFinReserva;
	
	@JsonProperty (value = "idClienteTitular")
	private Long idClienteTitular;
	
	@JsonProperty (value = "cedulaClienteTitular")
	private Long cedulaClienteTitular;
	
	@JsonProperty (value = "idAlojamientoReserva")
	private Long idAlojamientoReserva;
	
	@JsonProperty (value = "nombreAlojamiento")
	private String nombreAlojamiento;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public ReservaVO(@JsonProperty (value = "id") Long pID,
					 @JsonProperty (value = "nocheReserva") Integer pNoches, 
					 @JsonProperty (value = "fechaInicioReserva") Date pInicio,
					 @JsonProperty (value = "fechaFinReserva") Date pFin,
					 
					 @JsonProperty (value = "idClienteTitular") Long pIdCliente,
					 @JsonProperty (value = "cedulaClienteTitular") Long pCedulaCliente,
					 @JsonProperty (value = "idAlojamientoReserva") Long pIdAlojamiento,
					 @JsonProperty (value = "nombreAlojamiento") String pNombreAlojamiento) {
		
		this.id = pID;
		this.nochesReserva = pNoches;
		this.fechaInicioReserva = pInicio;
		this.fechaFinReserva = pFin;
		this.idClienteTitular = pIdCliente;
		this.cedulaClienteTitular = pCedulaCliente;
		this.idAlojamientoReserva = pIdAlojamiento;
		this.nombreAlojamiento = pNombreAlojamiento;
	}
	
	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNochesReserva() {
		return nochesReserva;
	}

	public void setNochesReserva(Integer nochesReserva) {
		this.nochesReserva = nochesReserva;
	}

	public Date getFechaInicioReserva() {
		return fechaInicioReserva;
	}

	public void setFechaInicioReserva(Date fechaInicioReserva) {
		this.fechaInicioReserva = fechaInicioReserva;
	}

	public Date getFechaFinReserva() {
		return fechaFinReserva;
	}

	public void setFechaFinReserva(Date fechaFinReserva) {
		this.fechaFinReserva = fechaFinReserva;
	}

	public Long getIdClienteTitular() {
		return idClienteTitular;
	}

	public void setIdClienteTitular(Long idClienteTitular) {
		this.idClienteTitular = idClienteTitular;
	}

	public Long getCedulaClienteTitular() {
		return cedulaClienteTitular;
	}

	public void setCedulaClienteTitular(Long cedulaClienteTitular) {
		this.cedulaClienteTitular = cedulaClienteTitular;
	}

	public Long getIdAlojamientoReserva() {
		return idAlojamientoReserva;
	}

	public void setIdAlojamientoReserva(Long idAlojamientoReserva) {
		this.idAlojamientoReserva = idAlojamientoReserva;
	}

	public String getNombreAlojamiento() {
		return nombreAlojamiento;
	}

	public void setNombreAlojamiento(String nombreAlojamiento) {
		this.nombreAlojamiento = nombreAlojamiento;
	}
}
package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class AlojamientoVO {

	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------	
	
	@JsonProperty (value = "id")
	protected Long id;
	
	@JsonProperty (value = "capacidad")
	protected Integer capacidad;
	
	@JsonProperty (value = "direccion")
	protected String  direccion;
	
	@JsonProperty (value = "nombre")
	protected String nombre;
	
	@JsonProperty (value = "costo")
	protected Double costo;
	
	@JsonProperty (value = "tipoAlojamiento")
	protected String tipoAlojamiento;
	
	@JsonProperty (value = "serviciosAsociados")
	protected ArrayList <ServicioVO> serviciosAsociados;
	
	@JsonProperty (value = "idReserva")
	protected Long idReservas;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------	
	
	public AlojamientoVO(@JsonProperty (value = "id") Long pID,
						 @JsonProperty (value = "capacidad") Integer pCapacidad,
						 @JsonProperty (value = "direccion") String pDireccion,
						 @JsonProperty (value = "nombre") String pNombre,
						 @JsonProperty (value = "costo") Double pCosto,
						 @JsonProperty (value = "tipoAlojamiento") String pTipoAlojamiento,
						 @JsonProperty (value = "serviciosAsociados") ArrayList<ServicioVO> pServicios,
						 @JsonProperty (value = "idReserva") Long pIdReserva){
		
		this.id = pID;
		this.capacidad = pCapacidad;
		this.direccion = pDireccion;
		this.nombre = pNombre;
		this.costo = pCosto;
		this.tipoAlojamiento = pTipoAlojamiento;
		this.serviciosAsociados = pServicios;
		this.idReservas = pIdReserva;
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

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoAlojamiento() {
		return tipoAlojamiento;
	}

	public void setTipoAlojamiento(String tipoAlojamiento) {
		this.tipoAlojamiento = tipoAlojamiento;
	}

	public ArrayList<ServicioVO> getServiciosAsociados() {
		return serviciosAsociados;
	}

	public void setServiciosAsociados(ArrayList<ServicioVO> serviciosAsociados) {
		this.serviciosAsociados = serviciosAsociados;
	}

	public Long getIdReservas() {
		return idReservas;
	}

	public void setIdReservas(Long idReservas) {
		this.idReservas = idReservas;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}
}
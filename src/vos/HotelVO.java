package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un objeto de tipo Propuesta. 
 * @author Grupo C-13 
 */
public class HotelVO {

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------
	
	public final static int CAPACIDAD_MAX_HOTEL = 100;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty ("id")
	private Long id;
	
	@JsonProperty (value = "cantidadEstrellas")
	private Integer cantidadEstrellas;
	
	@JsonProperty (value = "conPermisos")
	private Boolean conPermisos;
	
	@JsonProperty (value = "misHabitaciones")
	private ArrayList <HabitacionHoteleraVO> misHabitaciones;	
	
	@JsonProperty (value = "idOperador")
	private Long idOperador;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public HotelVO ( @JsonProperty (value = "id") Long pID,
					 @JsonProperty (value = "misHabitaciones") ArrayList <HabitacionHoteleraVO> pHabitaciones,
					 @JsonProperty (value = "cantidadEstrellas") Integer pCantidadEstrellas,
					 @JsonProperty (value = "conPermisos") Boolean pPermisos,
					 @JsonProperty (value = "idOperador") Long idOperador) {
		
		this.id = pID;
		this.cantidadEstrellas = pCantidadEstrellas;
		this.misHabitaciones = pHabitaciones;
		this.conPermisos = pPermisos;
		this.idOperador = idOperador;
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
	
	public Integer getCantidadEstrellas() {
		return cantidadEstrellas;
	}

	public void setCantidadEstrellas(Integer cantidadEstrellas) {
		this.cantidadEstrellas = cantidadEstrellas;
	}

	public Boolean getConPermisos() {
		return conPermisos;
	}

	public void setConPermisos(Boolean conPermisos) {
		this.conPermisos = conPermisos;
	}

	public ArrayList<HabitacionHoteleraVO> getMisHabitaciones() {
		return misHabitaciones;
	}

	public void setMisHabitaciones(ArrayList<HabitacionHoteleraVO> misHabitaciones) {
		this.misHabitaciones = misHabitaciones;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}
}
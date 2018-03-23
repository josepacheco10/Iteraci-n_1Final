package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class ViviendaUniversitariaVO {

	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	public final static int CAPACIDAD_MAX_VIV_UNIV = 100;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty (value = "id")
	private Long id;
	
	@JsonProperty (value = "capacidadVivienda")
	private Integer capacidadVivienda;
	
	@JsonProperty (value = "ubicacionVivUni")
	private String ubicacionVivUni;
	
	@JsonProperty (value = "habitacionesUniv")
	private ArrayList <HabitacionUniversitariaVO> habitacionesUniv;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public ViviendaUniversitariaVO (@JsonProperty (value = "id") Long pID,
									@JsonProperty (value = "capacidadVivienda") Integer pCapacidad,
									@JsonProperty (value = "ubicacionVivUni") String pUbicacion,
									@JsonProperty (value = "habitacionesUniv") ArrayList <HabitacionUniversitariaVO> pHabitaciones){
		
		this.id = pID;
		this.capacidadVivienda = pCapacidad;
		this.ubicacionVivUni = pUbicacion;
		this.habitacionesUniv = pHabitaciones;
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
	
	public Integer getCapacidadVivienda() {
		return capacidadVivienda;
	}

	public void setCapacidadVivienda(Integer capacidadVivienda) {
		this.capacidadVivienda = capacidadVivienda;
	}

	public String getUbicacionVivUni() {
		return ubicacionVivUni;
	}

	public void setUbicacionVivUni(String ubicacionVivUni) {
		this.ubicacionVivUni = ubicacionVivUni;
	}

	public ArrayList<HabitacionUniversitariaVO> getHabitacionesUniv() {
		return habitacionesUniv;
	}

	public void setHabitacionesUniv(ArrayList<HabitacionUniversitariaVO> habitacionesUniv) {
		this.habitacionesUniv = habitacionesUniv;
	}
}
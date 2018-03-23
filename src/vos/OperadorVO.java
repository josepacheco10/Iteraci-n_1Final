package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un objeto de tipo Operador 
 * @author Grupo C-13 
 */
public class OperadorVO {

	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty(value = "id")
	private Long id;
	
	@JsonProperty (value = "cedulaOperador")
	private Integer cedulaOperador;
	
	@JsonProperty (value = "NIT")
	private Long NIT;
	
	@JsonProperty (value = "nombreOperador")
	private String nombreOperador;
	
	@JsonProperty (value = "relacionUniandes")
	private String relacionUniandes;
	
	@JsonProperty (value = "tipoOperador")
	private String tipoOperador;
	
	//---------------------------
	// Listas de alojamientos:
	//---------------------------
	
	// Habitacion
	@JsonProperty (value = "misHabitaciones")
	private ArrayList <HabitacionVO> misHabitaciones;
	
	// Apartamentos
	@JsonProperty (value = "misApartamentos")
	private ArrayList <ApartamentoVO> misApartamentos;
		
	// Viviendas Esporadicas
	@JsonProperty (value = "misViviendasEsp")
	private ArrayList <ViviendaEsp> misViviendasEsp;
	
	// Hostal
	@JsonProperty (value = "misHostales")
	private ArrayList<HostalVO> misHostales;
	
	// Hotel
	@JsonProperty (value = "misHoteles")
	private ArrayList<HotelVO> misHoteles;
	
	// Viviendas Universitarias
	@JsonProperty (value = "misViviendasUniversitarias")
	private ArrayList<ViviendaUniversitariaVO> misViviendasUniversitarias;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------

	public OperadorVO ( @JsonProperty (value = "id") Long pId, 
						@JsonProperty (value = "cedulaOperador") Integer pCedulaOp,
						@JsonProperty (value = "NIT") Long pNIT,
						@JsonProperty (value = "nombreOperador") String pNombre,
						@JsonProperty (value = "relacionUniandes") String pRelacion,
						@JsonProperty (value = "tipoOperador") String pTipo,
						
						@JsonProperty (value = "misHabitaciones") ArrayList<HabitacionVO> pHabitaciones,
						@JsonProperty (value = "misApartamentos") ArrayList<ApartamentoVO> pApartamentos,
						@JsonProperty (value = "misViviendasEsp") ArrayList <ViviendaEsp> pViviendasEsp,
						@JsonProperty (value = "misHostales") ArrayList<HostalVO> pHostales,
						@JsonProperty (value = "misHoteles") ArrayList<HotelVO> pHoteles,
						@JsonProperty (value = "misViviendasUniversitarias")ArrayList<ViviendaUniversitariaVO> pViviendasUniv
						) {
		
		this.id = pId;
		this.cedulaOperador = pCedulaOp;
		this.NIT = pNIT;
		this.nombreOperador = pNombre;
		this.relacionUniandes = pRelacion;
		this.tipoOperador = pTipo;
		
		// Listas de Alojamiento:
		this.misHabitaciones = pHabitaciones;
		this.misApartamentos = pApartamentos;
		this.misViviendasEsp = pViviendasEsp;
		this.misHostales = pHostales;
		this.misHoteles = pHoteles;
		this.misViviendasUniversitarias = pViviendasUniv;
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

	public Integer getCedulaOperador() {
		return cedulaOperador;
	}

	public void setCedulaOperador(Integer cedulaOperador) {
		this.cedulaOperador = cedulaOperador;
	}

	public Long getNIT() {
		return NIT;
	}

	public void setNIT(Long nIT) {
		NIT = nIT;
	}

	public String getNombreOperador() {
		return nombreOperador;
	}

	public void setNombreOperador(String nombreOperador) {
		this.nombreOperador = nombreOperador;
	}

	public String getRelacionUniandes() {
		return relacionUniandes;
	}

	public void setRelacionUniandes(String relacionUniandes) {
		this.relacionUniandes = relacionUniandes;
	}

	public String getTipoOperador() {
		return tipoOperador;
	}

	public void setTipoOperador(String tipoOperador) {
		this.tipoOperador = tipoOperador;
	}
	
	//----------------------------------------------------
	// Listas de Alojamientos:
	//----------------------------------------------------

	public ArrayList<HabitacionVO> getMisHabitaciones() {
		return misHabitaciones;
	}

	public void setMisHabitaciones(ArrayList<HabitacionVO> misHabitaciones) {
		this.misHabitaciones = misHabitaciones;
	}

	public ArrayList<ApartamentoVO> getMisApartamentos() {
		return misApartamentos;
	}

	public void setMisApartamentos(ArrayList<ApartamentoVO> misApartamentos) {
		this.misApartamentos = misApartamentos;
	}

	public ArrayList<ViviendaEsp> getMisViviendasEsp() {
		return misViviendasEsp;
	}

	public void setMisViviendasEsp(ArrayList<ViviendaEsp> misViviendasEsp) {
		this.misViviendasEsp = misViviendasEsp;
	}

	public ArrayList<HostalVO> getMisHostales() {
		return misHostales;
	}

	public void setMisHostales(ArrayList<HostalVO> misHostales) {
		this.misHostales = misHostales;
	}

	public ArrayList<HotelVO> getMisHoteles() {
		return misHoteles;
	}

	public void setMisHoteles(ArrayList<HotelVO> misHoteles) {
		this.misHoteles = misHoteles;
	}

	public ArrayList<ViviendaUniversitariaVO> getMisViviendasUniversitarias() {
		return misViviendasUniversitarias;
	}

	public void setMisViviendasUniversitarias(ArrayList<ViviendaUniversitariaVO> misViviendasUniversitarias) {
		this.misViviendasUniversitarias = misViviendasUniversitarias;
	}
}
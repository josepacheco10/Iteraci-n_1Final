package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class HabitacionUniversitariaVO extends AlojamientoVO {

	
	public final static int CAPACIDAD_MAXIMA = 100;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty (value = "idMiVivienda")
	private Long idMiVivienda;
	
	@JsonProperty (value = "menaje")
	private String menaje;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public HabitacionUniversitariaVO (@JsonProperty (value = "id") Long pID,
									  @JsonProperty (value = "capacidad") Integer pCapacidad,
									  @JsonProperty (value = "direccion") String pDireccion,
									  @JsonProperty (value = "nombre") String pNombre,
									  @JsonProperty (value = "costo") Double pCosto,
									  @JsonProperty (value = "tipoAlojamiento") String pTipoAlojamiento,
									  @JsonProperty (value = "serviciosAsociados") ArrayList<ServicioVO> pServicios,
									  @JsonProperty (value = "idReserva") Long pIdReserva,
									  
									  @JsonProperty (value = "menaje") String pMenaje,
									  @JsonProperty (value = "idMiVivienda") Long pIdMiVivienda){
		
		super(pID, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, pServicios, pIdReserva);
		this.menaje = pMenaje;
		this.idMiVivienda = pIdMiVivienda;
	}

	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------
	
	public String getMenaje() {
		return menaje;
	}

	public Long getIdMiVivienda() {
		return idMiVivienda;
	}

	public void setIdMiVivienda(Long idMiVivienda) {
		this.idMiVivienda = idMiVivienda;
	}

	public void setMenaje(String menaje) {
		this.menaje = menaje;
	}
}
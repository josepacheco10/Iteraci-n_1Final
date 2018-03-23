package vos;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonProperty;

public class HabitacionHoteleraVO extends AlojamientoVO {
	
	
	
	public final static int CAPACIDAD_MAXIMA = 100;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty (value = "costoNocheHab")
	private Double costoNocheHab;
	
	@JsonProperty (value = "tipoHabitacion")
	private String tipoHabitacion;
	
	@JsonProperty (value = "idMiHotel")
	private Long idMiHotel;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public HabitacionHoteleraVO(@JsonProperty (value = "id") Long pID,
							 @JsonProperty (value = "capacidad") Integer pCapacidad,
							 @JsonProperty (value = "direccion") String pDireccion,
							 @JsonProperty (value = "nombre") String pNombre,
							 @JsonProperty (value = "costo") Double pCosto,
							 @JsonProperty (value = "tipoAlojamiento") String pTipoAlojamiento,
							 @JsonProperty (value = "serviciosAsociados") ArrayList<ServicioVO> pServicios,
							 @JsonProperty (value = "idReserva") Long pIdReserva, 
							 
							 @JsonProperty (value = "costoNocheHab") Double pCostoNoc,
							 @JsonProperty (value = "tipoHabitacion") String pTipoHab,
							 @JsonProperty (value = "idMiHotel") Long pIdMiHotel){
		
		super(pID, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, pServicios, pIdReserva);
		this.costoNocheHab = pCostoNoc;
		this.tipoHabitacion = pTipoHab;
		this.idMiHotel = pIdMiHotel;
	}
	
	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------

	public Double getCostoNocheHab() {
		return costoNocheHab;
	}

	public void setCostoNocheHab(Double costoNocheHab) {
		this.costoNocheHab = costoNocheHab;
	}

	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	public Long getIdMiHotel() {
		return idMiHotel;
	}

	public void setIdMiHotel(Long idMiHotel) {
		this.idMiHotel = idMiHotel;
	}
}
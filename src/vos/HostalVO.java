package vos;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonProperty;

public class HostalVO extends  AlojamientoVO{

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------

	public final static int CAPACIDAD_MAX_HOSTAL = 100;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty (value = "conPermisos")
	private Boolean conPermisos;
	
	@JsonProperty (value = "horarioApertura")
	private String horarioApertura;
	
	@JsonProperty (value = "horarioCierre")
	private String horarioCierre;
	
	@JsonProperty (value = "tipoHabitacion")
	private String tipoHabitacion;
	
	@JsonProperty (value = "idOperador")
	private Long idOperador;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	public HostalVO (@JsonProperty (value = "id") Long pID,
					 @JsonProperty (value = "capacidad") Integer pCapacidad,
					 @JsonProperty (value = "direccion") String pDireccion,
					 @JsonProperty (value = "nombre") String pNombre,
					 @JsonProperty (value = "costo") Double pCosto,
					 @JsonProperty (value = "tipoAlojamiento") String pTipoAlojamiento,
					 @JsonProperty (value = "serviciosAsociados") ArrayList<ServicioVO> pServicios,
					 @JsonProperty (value = "idReserva") Long pIdReserva,
					 
					 @JsonProperty (value = "conPermisos") Boolean pPermisos,
					 @JsonProperty (value = "horarioApertura") String pHorarioAp,
					 @JsonProperty (value = "horarioCierre") String pHorarioCierre,
					 @JsonProperty (value = "tipoHabitacion") String pTipoHab,
					 @JsonProperty (value = "idOperador") Long idOperador) {
		
		super(pID, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, pServicios, pIdReserva);
		this.conPermisos = pPermisos;
		this.horarioApertura = pHorarioAp;
		this.horarioCierre = pHorarioCierre;
		this.tipoHabitacion = pTipoHab;
		this.idOperador = idOperador;
	}

	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------
	
	public Boolean getConPermisos() {
		return conPermisos;
	}

	public void setConPermisos(Boolean conPermisos) {
		this.conPermisos = conPermisos;
	}

	public String getHorarioApertura() {
		return horarioApertura;
	}

	public void setHorarioApertura(String horarioApertura) {
		this.horarioApertura = horarioApertura;
	}

	public String getHorarioCierre() {
		return horarioCierre;
	}

	public void setHorarioCierre(String horarioCierre) {
		this.horarioCierre = horarioCierre;
	}

	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}
}
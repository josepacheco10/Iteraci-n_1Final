package vos;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un objeto de tipo Operador 
 * @author Grupo C-13 
 */
public class HabitacionVO extends AlojamientoVO{

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------
	
	public final static int CANTIDAD_MAX_HABITACION = 100;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty (value = "costoPromocional")
	private Double costoPromocional;
	
	@JsonProperty (value = "idOperador")
	private Long idOperador;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public HabitacionVO (@JsonProperty (value = "id") Long pID,
			 			 @JsonProperty (value = "capacidad") Integer pCapacidad,
			 			 @JsonProperty (value = "direccion") String pDireccion,
			 			 @JsonProperty (value = "nombre") String pNombre,
			 			 @JsonProperty (value = "costo") Double pCosto,
			 			 @JsonProperty (value = "tipoAlojamiento") String pTipoAlojamiento,
			 			 @JsonProperty (value = "serviciosAsociados") ArrayList<ServicioVO> pServicios,
			 			 @JsonProperty (value = "idReserva") Long pIdReserva,
			 			 
						 @JsonProperty (value = "costoPromocional") Double pCostoProm,
						 @JsonProperty (value = "idOperador") Long pIdOp){

		
		super(pID, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, pServicios, pIdReserva);
		this.costoPromocional = pCostoProm;
		this.idOperador = pIdOp;
	}
	
	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------

	public Double getCostoPromocional() {
		return costoPromocional;
	}

	public void setCostoPromocional(Double costoPromocional) {
		this.costoPromocional = costoPromocional;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}
}
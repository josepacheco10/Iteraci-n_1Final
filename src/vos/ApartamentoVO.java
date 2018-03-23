package vos;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonProperty;


public class ApartamentoVO extends AlojamientoVO{

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------	
	
	public static final int CAPACIDAD_MAX_APTO = 1000;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------	
	
	@JsonProperty (value = "costoAdmin")
	private Double costoAdmin;
	
	@JsonProperty (value = "isAmoblado")
	private Boolean isAmoblado;
	
	@JsonProperty (value = "idOperador")
	private Long idOperador;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public ApartamentoVO (@JsonProperty (value = "id") Long pID,
			 			  @JsonProperty (value = "capacidad") Integer pCapacidad,
			 			  @JsonProperty (value = "direccion") String pDireccion,
			 			  @JsonProperty (value = "nombre") String pNombre,
			 			  @JsonProperty (value = "costo") Double pCosto,
			 			  @JsonProperty (value = "tipoAlojamiento") String pTipoAlojamiento,
			 			  @JsonProperty (value = "serviciosAsociados") ArrayList<ServicioVO> pServicios,
			 			  @JsonProperty (value = "idReserva") Long pIdReserva,
			 			  
						  @JsonProperty (value = "costoAdmin") Double pCostoAdmin,
						  @JsonProperty (value = "isAmoblado") Boolean pIsAmoblado,
						  @JsonProperty (value = "idOperador") Long pIdOp){
		
		super(pID, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, pServicios, pIdReserva);
		this.costoAdmin = pCostoAdmin;
		this.isAmoblado = pIsAmoblado;
		this.idOperador = pIdOp;
	}
	
	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------
	
	public Double getCostoAdmin() {
		return costoAdmin;
	}

	public void setCostoAdmin(Double costoAdmin) {
		this.costoAdmin = costoAdmin;
	}

	public Boolean getIsAmoblado() {
		return isAmoblado;
	}

	public void setIsAmoblado(Boolean isAmoblado) {
		this.isAmoblado = isAmoblado;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}
}
package vos;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa un objeto de tipo Vivienda Esporadica (Reservas por Evento) 
 * @author Grupo C-13 
 */
public class ViviendaEsp extends AlojamientoVO {

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------
	
	public final static int CAPACIDAD_MAX_VIVIENDA_ESP = 100;
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	@JsonProperty (value = "caractBasicas")
	private String caractBasicas;
	
	@JsonProperty (value = "nochesAnio")
	private Integer nochesAnio;
	
	@JsonProperty (value = "idOperador")
	private Long idOperador;
	
	@JsonProperty (value = "idSeguro")
	private Long idSeguro;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	public ViviendaEsp(@JsonProperty (value = "id") Long pID,
					   @JsonProperty (value = "capacidad") Integer pCapacidad,
					   @JsonProperty (value = "direccion") String pDireccion,
					   @JsonProperty (value = "nombre") String pNombre,
					   @JsonProperty (value = "costo") Double pCosto,
					   @JsonProperty (value = "tipoAlojamiento") String pTipoAlojamiento,
					   @JsonProperty (value = "serviciosAsociados") ArrayList<ServicioVO> pServicios,
					   @JsonProperty (value = "idReserva") Long pIdReserva,
					   
					   @JsonProperty (value = "caractBasicas") String pCaractBasicas,
					   @JsonProperty (value = "nochesAnio") Integer pNochesAnio,
					   @JsonProperty (value = "idOperador") Long pIdOp,
					   @JsonProperty (value = "idSeguro") Long pIdSeguro) {
		
		super(pID, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, pServicios, pIdReserva);
		this.caractBasicas = pCaractBasicas;
		this.nochesAnio = pNochesAnio;
		this.idOperador = pIdOp;
		this.idSeguro = pIdSeguro;
	}
	
	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------

	public String getCaractBasicas() {
		return caractBasicas;
	}

	public void setCaractBasicas(String caractBasicas) {
		this.caractBasicas = caractBasicas;
	}

	public Integer getNochesAnio() {
		return nochesAnio;
	}

	public void setNochesAnio(Integer nochesAnio) {
		this.nochesAnio = nochesAnio;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}

	public Long getIdSeguro() {
		return idSeguro;
	}

	public void setIdSeguro(Long idSeguro) {
		this.idSeguro = idSeguro;
	}
}
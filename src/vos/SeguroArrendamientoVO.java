package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class SeguroArrendamientoVO{
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------	
		
	@JsonProperty (value = "id")
	private Long id;
	
	@JsonProperty (value = "costoSeguro")
	private Double costoSeguro;
	
	@JsonProperty (value = "caractSeguro")
	private String caractSeguro;
	
	@JsonProperty (value = "idViviendaEsp")
	private Long idViviendaEsp;
	
	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------	
		
	public SeguroArrendamientoVO (@JsonProperty (value = "id") Long pId,
								  @JsonProperty (value = "costoSeguro") Double pCosto,
								  @JsonProperty (value = "caractSeguro") String pCaract,
								  @JsonProperty (value = "idViviendaEsp") Long pIdViviendaEsp){
		
		this.id = pId;
		this.costoSeguro = pCosto;
		this.caractSeguro = pCaract;
		this.idViviendaEsp = pIdViviendaEsp;
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

	public Double getCostoSeguro() {
		return costoSeguro;
	}

	public void setCostoSeguro(Double costoSeguro) {
		this.costoSeguro = costoSeguro;
	}

	public String getCaractSeguro() {
		return caractSeguro;
	}

	public void setCaractSeguro(String caractSeguro) {
		this.caractSeguro = caractSeguro;
	}

	public Long getIdViviendaEsp() {
		return idViviendaEsp;
	}

	public void setIdViviendaEsp(Long idViviendaEsp) {
		this.idViviendaEsp = idViviendaEsp;
	}	
}
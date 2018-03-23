package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC2 {
	
	@JsonProperty ("CAPACIDAD")
	private int CAPACIDAD;
	
	@JsonProperty ("COSTO")
	private double COSTO;
	
	@JsonProperty ("DIRECCION")
	private String DIRECCION;
	
	@JsonProperty ("NOMBRE")
	private String NOMBRE;
	
	@JsonProperty ("TIPO_ALOJAMIENTO")
	private String TIPO_ALOJAMIENTO;
	
	@JsonProperty ("NUMAPARICIONESOFERTA")
	private int NUMAPARICIONESOFERTA;

	public RFC2 (
			 @JsonProperty (value = "CAPACIDAD") int pCapacidad,
			 @JsonProperty (value = "COSTO") double pCosto,
			 @JsonProperty (value = "DIRECCION") String pDireccion,
			 @JsonProperty (value = "NOMBRE") String pNOMBRE,
			 @JsonProperty (value = "TIPO_ALOJAMIENTO") String pTipo,
			 @JsonProperty (value = "NUMAPARICIONESOFERTA") int pNUMApariciones) {

this.CAPACIDAD = pCapacidad;
this.COSTO = pCosto;
this.DIRECCION = pDireccion;
this.NOMBRE = pNOMBRE;
this.TIPO_ALOJAMIENTO = pTipo;
this.NUMAPARICIONESOFERTA = pNUMApariciones;
}
	
	

	public double getCOSTO() {
		return COSTO;
	}

	public void setCOSTO(double cOSTO) {
		COSTO = cOSTO;
	}

	public String getDIRECCION() {
		return DIRECCION;
	}

	public void setDIRECCION(String dIRECCION) {
		DIRECCION = dIRECCION;
	}

	public String getNOMBRE() {
		return NOMBRE;
	}

	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}

	public String getTIPO_ALOJAMIENTO() {
		return TIPO_ALOJAMIENTO;
	}

	public void setTIPO_ALOJAMIENTO(String tIPO_ALOJAMIENTO) {
		TIPO_ALOJAMIENTO = tIPO_ALOJAMIENTO;
	}

	public long getNUMAPARICIONESOFERTA() {
		return NUMAPARICIONESOFERTA;
	}

	public void setNUMAPARICIONESOFERTA(int NUMAPARICIONESOFERTA) {
		NUMAPARICIONESOFERTA = NUMAPARICIONESOFERTA;
	}



	public int getCAPACIDAD() {
		return CAPACIDAD;
	}



	public void setCAPACIDAD(int cAPACIDAD) {
		CAPACIDAD = cAPACIDAD;
	}
	
}

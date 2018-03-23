package vos;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Clase que representa a un objeto de tipo Cliente dentro del modelo de AlohAndes. 
 * @author Grupo C-13
 */
public class ClienteVO {
		
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	/**
	 * Identificador del cliente. 
	 */
	@JsonProperty(value="id")
	private Long id;
	
	/**
	 * El identificador legal de un cliente (cedula). 
	 */
	@JsonProperty(value="cedulaCliente")
	private Long cedulaCliente;
	
	/**
	 * La edad del cliente. 
	 */
	@JsonProperty(value="edadCliente")
	private Integer edadCliente;
	
	/**
	 * El apellido del cliente. 
	 */
	@JsonProperty(value="apellidoCliente")
	private String apellidoCliente;
	
	/**
	 * El nombre del cliente. 
	 */
	@JsonProperty(value="nombreCliente")
	private String nombreCliente;
	
	/**
	 * Preferencia del cliente en materia de alojamiento.
	 */
	@JsonProperty (value = "preferenciasAlojamiento")
	private String preferenciasAlojamiento;
		
	/**
	 * Relacion del cliente con la Universidad de los Andes.
	 */
	@JsonProperty (value = "relacionUniandes")
	private String relacionUniandes;
	
	/**
	 * Reserva(s) asociadas al cliente.
	 */
	@JsonProperty (value = "misReservas")
	private ArrayList <ReservaVO> misReservas;

	//----------------------------------------------------
	// Constructor
	//----------------------------------------------------
	
	/**
	 * Metodo constructor de la clase ClienteVO
	 * <b> post: </b> Crea el objeto Cliente y le asocia los valores dados.
	 * @param id - Identificador del Cliente.
	 * @param pNombre - Nombre del cliente.
	 * @param pEdad - Edad del Cliente.
	 * @param pCedula - Cedula del Cliente.
	 * @param pApellido - Apellido del Cliente.
	 */
	public ClienteVO (@JsonProperty(value="id") Long id,
					  @JsonProperty(value = "cedulaCliente") Long pCedula,
					  @JsonProperty(value = "edadCliente") Integer pEdad, 
					  @JsonProperty(value = "apellidoCliente") String pApellido,
					  @JsonProperty(value = "nombreCliente") String pNombre,
					  @JsonProperty (value = "preferenciasAlojamiento") String pPreferencias,
					  @JsonProperty (value = "relacionUniandes") String pRelacionUniandes,
					  @JsonProperty (value = "misReservas") ArrayList <ReservaVO> pReservas) {

		this.id = id;
		this.cedulaCliente = pCedula;
		this.edadCliente = pEdad;
		this.apellidoCliente = pApellido;
		this.nombreCliente = pNombre;
		this.relacionUniandes = pRelacionUniandes;
		this.preferenciasAlojamiento = pPreferencias;
		this.misReservas = pReservas;
	}
	
	//----------------------------------------------------
	// Metodos
	//----------------------------------------------------

	/**
	 * @return Identificador del cliente.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Modificar el identificador del cliente. 
	 * @param id Identificador nuevo a asociar. 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Cedula del cliente.
	 */
	public Long getCedulaCliente() {
		return cedulaCliente;
	}
	
	/**
	 * Modificar la cedula del cliente. 
	 * @param cedulaCliente Cedula nueva a asociar. 
	 */
	public void setCedulaCliente(Long cedulaCliente) {
		this.cedulaCliente = cedulaCliente;
	}

	/**
	 * @return Edad del cliente.
	 */
	public Integer getEdadCliente() {
		return edadCliente;
	}

	/**
	 * Modificar la edad del cliente. 
	 * @param edadCliente Edad nueva a asociar. 
	 */
	public void setEdadCliente(Integer edadCliente) {
		this.edadCliente = edadCliente;
	}

	/**
	 * @return Apellido del cliente.
	 */
	public String getApellidoCliente() {
		return apellidoCliente;
	}

	/**
	 * Modificar el apellido del cliente. 
	 * @param apellidoCliente Apellido nuevo a asociar. 
	 */
	public void setApellidoCliente(String apellidoCliente) {
		this.apellidoCliente = apellidoCliente;
	}

	/**
	 * @return Nombre del cliente.
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * Modificar el nombre del cliente. 
	 * @param nombreCliente Nombre nuevo a asociar. 
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
	 * @return Preferencia en materia de alojamiento del cliente.
	 */
	public String getPreferenciasAlojamiento() {
		return preferenciasAlojamiento;
	}

	/**
	 * Modificar la preferencia del cliente. 
	 * @param preferenciasAlojamiento Preferencia nueva a asociar. 
	 */
	public void setPreferenciasAlojamiento(String preferenciasAlojamiento) {
		this.preferenciasAlojamiento = preferenciasAlojamiento;
	}

	/**
	 * @return Identificador de la o las reservas del cliente.
	 */
	public ArrayList <ReservaVO> getReservas() {
		return misReservas;
	}

	/**
	 * Modificar el identificador de reserva del cliente. 
	 * @param idReservas Nuevo identificador a asociar. 
	 */
	public void setIdReservas(ArrayList <ReservaVO> idReservas) {
		this.misReservas = idReservas;
	}

	/**
	 * @return Relacion del cliente con la Universidad de los Andes.
	 */
	public String getRelacionUniandes() {
		return relacionUniandes;
	}

	/**
	 * Modificar la relacion del cliente con Uniandes. 
	 * @param relacionUniandes Nueva relacion a asociar. 
	 */
	public void setRelacionUniandes(String relacionUniandes) {
		this.relacionUniandes = relacionUniandes;
	}
}
package tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dao.DAOApartamento;
import dao.DAOCliente;
import dao.DAOHabitacion;
import dao.DAOHostal;
import dao.DAOHotel;
import dao.DAOReserva;
import dao.DAOViviendaEsp;
import dao.DAOViviendaUniv;
import dao.DAOperador;
import vos.ApartamentoVO;
import vos.ClienteVO;
import vos.HabitacionVO;
import vos.HostalVO;
import vos.HotelVO;
import vos.OperadorVO;
import vos.RFC2;
import vos.ReservaVO;
import vos.SeguroArrendamientoVO;
import vos.ViviendaEsp;
import vos.ViviendaUniversitariaVO;

public class AlohAndesTransactionManager {

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------	
	
	/**
	 * Constante que contiene el path relativo del archivo que tiene los datos de la conexion (Credenciales).
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion.
	 */
	private static String CONNECTION_DATA_PATH;
	
	
	//-----------------------------------------------------
	// Atributos
	//-----------------------------------------------------

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	@SuppressWarnings("unused")
	private String driver;
	
	/**
	 * Atributo que representa la conexion a la base de datos
	 */
	private Connection conn;

	public static final String USUARIO = "ISIS2304A871810";

	private ArrayList<Object> resources;
	
	//----------------------------------------------------
	// Metodos para realizar la conexion e inicializacion
	//----------------------------------------------------

	/**
	 * Metodo constructor de la clase AlohAndesTransactionManager.
	 * @param contextPathP Path absoluto que se encuentra en el servidor del contexto del deploy actual.
	 * @throws IOException Se genera una excepcion al tener dificultades con la inicializacion de la conexion.
	 * @throws ClassNotFoundException 
	 */
	public AlohAndesTransactionManager (String contextPathP) {
		resources = new ArrayList();
		
		try {
			CONNECTION_DATA_PATH = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
			initializeConnectionData();
		} 
		catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo encargado de inicializar los atributos utilizados para la conexion con la Base de Datos.<br/>
	 * <b>post: </b> Se inicializan los atributos para la conexion<br/>
	 * @throws IOException Se genera una excepcion al no encontrar el archivo o al tener dificultades durante su lectura<br/>
	 * @throws ClassNotFoundException 
	 */
	private void initializeConnectionData() throws IOException, ClassNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(new File(AlohAndesTransactionManager.CONNECTION_DATA_PATH));
		Properties properties = new Properties();
		
		properties.load(fileInputStream);
		fileInputStream.close();
		
		this.url = properties.getProperty("url");
		this.user = properties.getProperty("usuario");
		this.password = properties.getProperty("clave");
		this.driver = properties.getProperty("driver");
		
		//Class.forName(driver);
	}

	/**
	 * Metodo encargado de generar una conexion con la Base de Datos.<br/>
	 * <b>Precondicion: </b>Los atributos para la conexion con la Base de Datos han sido inicializados<br/>
	 * @return Objeto Connection, el cual hace referencia a la conexion a la base de datos
	 * @throws SQLException Cualquier error que se pueda llegar a generar durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("[PARRANDEROS APP] Attempting Connection to: " + url + " - By User: " + user);
		return DriverManager.getConnection(url, user, password);
	}
	
	//----------------------------------------------------
	// Metodos de la aplicacion (Transacciones): Clientes
	//----------------------------------------------------	
	
	/**
	 * Metodo que modela la transaccion que retorna todos los clientes existentes en la base de datos. <br/>
	 * @return List<ClienteVO> - Lista de los clientes en la BD.
	 * @throws Exception - Cualquier error que se genere durante la transaccion
	 */
	public List<ClienteVO> getAllClientes () throws Exception {
		
		DAOCliente daoCliente = new DAOCliente();
		List<ClienteVO> clientes;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			
			// Se obtienen 100 clientes.
			clientes = daoCliente.getClientes();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			
			try {daoCliente.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes;
	}
	
	/**
	 * Metodo que modela la transaccion que busca la reserva solicitada (de acuerdo a
	 * su cliente) en la base de datos. <br/>
	 * @param idCliente ID del Cliente que posee la reserva buscada. id != null
	 * @param idReserva ID de la reserva del cliente buscada.
	 * @return ReservaVO Reserva que se obtiene como resultado de la consulta.
	 * @throws Exception Cualquier error que se genere durante la transaccion
	 */
	public ClienteVO getCliente (Long idCliente) throws Exception {
		
		DAOCliente daoCliente = new DAOCliente();
		ClienteVO clienteBuscado = null;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			clienteBuscado = daoCliente.getCliente(idCliente);
			
			if(clienteBuscado == null) {
				throw new Exception("El cliente con ID: " + idCliente + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoCliente.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return clienteBuscado;
	}
	
	//----------------------------------------------------
	// Metodos de la aplicacion (Transacciones): Reservas
	//----------------------------------------------------	
	
	/**
	 * Metodo que modela la transaccion que retorna todos las reservas de un cliente existente en la base de datos. <br/>
	 * @return List<ReservaVO> - Lista de las reservas asociadas al cliente dado.
	 * @throws Exception - Cualquier error que se genere durante la transaccion
	 */
	public List<ReservaVO> getAllReservas(Long pIDCliente) throws Exception {
		
		DAOReserva daoReserva = new DAOReserva();
		List<ReservaVO> reservas;
		try 
		{
			this.conn = darConexion();
			daoReserva.setConn(conn);
			// Se obtienen 20 reservas del cliente dado.
			reservas = daoReserva.getReservasCliente(pIDCliente);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			
			try {daoReserva.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reservas;
	}
	
	/**
	 * Metodo que modela la transaccion que busca la reserva solicitada (de acuerdo a
	 * su cliente) en la base de datos. <br/>
	 * @param idCliente ID del Cliente que posee la reserva buscada. id != null
	 * @param idReserva ID de la reserva del cliente buscada.
	 * @return ReservaVO Reserva que se obtiene como resultado de la consulta.
	 * @throws Exception Cualquier error que se genere durante la transaccion
	 */
	public ReservaVO getReserva (Long idCliente, Long idReserva) throws Exception {
		
		DAOReserva daoReserva = new DAOReserva();
		ReservaVO reservaBuscada = null;
		try 
		{
			this.conn = darConexion();
			daoReserva.setConn(conn);
			reservaBuscada = daoReserva.getReserva(idCliente, idReserva);
			if(reservaBuscada == null) {
				throw new Exception("La reserva asociada al cliente con ID: " + idCliente + "y cuyo ID es: " + idReserva + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoReserva.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reservaBuscada;
	}
	
	/**
	 * Metodo que modela la transaccion que agrega una reserva a la base de datos aplicando las reglas de negocio.
	 * @param pIdCliente Identificador del cliente que desea realizar la reserva.
	 * @param newReserva Reserva a incluir en la base de datos.
	 * @throws Exception - Cualquier error que se genere agregando el bebedor
	 */
	public ReservaVO createReservaBussinessRules (Long pIdCliente, ReservaVO newReserva) throws Exception {	
		DAOReserva daoReserva = new DAOReserva( );
		ReservaVO reservaBuscada = null;
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);
			reservaBuscada = daoReserva.createReserva(pIdCliente, newReserva);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		
		finally {try {daoReserva.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return reservaBuscada;
	}
	
	/**
	 * Metodo que modela la transaccion que agrega una reserva a la base de datos aplicando las reglas de negocio.
	 * @param pIdCliente Identificador del cliente que desea realizar la reserva.
	 * @param newReserva Reserva a incluir en la base de datos.
	 * @throws Exception - Cualquier error que se genere agregando el bebedor
	 */
	public void deleteReservaBussinessRules (Long idCliente, Long idReserva) throws Exception {	
		DAOReserva daoReserva = new DAOReserva( );
		
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoReserva.deleteReserva(idCliente, idReserva);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		
		finally {try {daoReserva.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	//-------------------------------------------------------
	// Metodos de la aplicacion (Transacciones): Alojamientos
	//-------------------------------------------------------
	
	//-------------------------------------
	// Apartamento
	//-------------------------------------
	
	public ApartamentoVO getApartamento (Long idCliente, Long idReserva) throws Exception {
		
		DAOApartamento daoApto = new DAOApartamento();
		ApartamentoVO buscada = null;
		try 
		{
			this.conn = darConexion();
			daoApto.setConn(conn);
			buscada = daoApto.getApartamento(idCliente, idReserva);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoApto.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public ArrayList<ApartamentoVO> getApartamentos (Long idOp) throws Exception {
		DAOApartamento daoApto = new DAOApartamento();
		ArrayList<ApartamentoVO> buscada = new ArrayList<>();
		try 
		{
			this.conn = darConexion();
			daoApto.setConn(conn);
			buscada = daoApto.getApartamentos(idOp);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoApto.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public void deleteApartamentos (Long idOp, Long idApartamento) throws Exception {
		DAOApartamento daoApto = new DAOApartamento();
		try 
		{
			this.conn = darConexion();
			daoApto.setConn(conn);
			daoApto.deleteAlojamiento(idOp, idApartamento);
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoApto.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	
	//-------------------------------------
	// Habitacion
	//-------------------------------------
	
	public HabitacionVO getHabitacion (Long idCliente, Long idReserva) throws Exception {
		
		DAOHabitacion daoAlojamiento = new DAOHabitacion();
		HabitacionVO buscada = null;
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getHabitacion(idCliente, idReserva);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public ArrayList <HabitacionVO> getHabitaciones (Long idCliente) throws Exception {
		
		DAOHabitacion daoAlojamiento = new DAOHabitacion();
		ArrayList <HabitacionVO> buscada = new ArrayList<>();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getHabitaciones(idCliente);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public void deleteHabitacion(Long idOp, Long idApartamento) throws Exception {
		
		DAOHabitacion daoAlojamiento = new DAOHabitacion();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			daoAlojamiento.deleteAlojamiento(idOp, idApartamento);
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	//-------------------------------------
	// Hostal
	//-------------------------------------
	
	public HostalVO getHostal (Long idCliente, Long idReserva) throws Exception {
		
		DAOHostal daoAlojamiento = new DAOHostal();
		HostalVO buscada = null;
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getHostal(idCliente, idReserva);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public ArrayList <HostalVO> getHostales (Long idCliente) throws Exception {
		
		DAOHostal daoAlojamiento = new DAOHostal();
		 ArrayList <HostalVO> buscada = new ArrayList<>();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getHostales(idCliente);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public void deleteHostal (Long idOp, Long idApartamento) throws Exception {
		
		DAOHostal daoAlojamiento = new DAOHostal();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			daoAlojamiento.deleteHostal(idOp, idApartamento);
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	//-------------------------------------
	// Hotel
	//-------------------------------------
	
	public HotelVO getHotel (Long idCliente, Long idReserva) throws Exception {
		
		DAOHotel daoAlojamiento = new DAOHotel();
		HotelVO buscada = null;
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getHotel(idCliente, idReserva);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public ArrayList <HotelVO> getHoteles (Long idCliente) throws Exception {
		
		DAOHotel daoAlojamiento = new DAOHotel();
		 ArrayList <HotelVO> buscada = new ArrayList<>();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getHoteles(idCliente);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public void deleteHotel (Long idOp, Long idApartamento) throws Exception {
		
		DAOHotel daoAlojamiento = new DAOHotel();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			daoAlojamiento.deleteHotel(idOp, idApartamento);
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	//-------------------------------------
	// Vivienda Esporadica
	//-------------------------------------
	
	public ViviendaEsp getViviendaEsp (Long idCliente, Long idReserva) throws Exception {
		
		DAOViviendaEsp daoAlojamiento = new DAOViviendaEsp();
		ViviendaEsp buscada = null;
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getViviendaEsp(idCliente, idReserva);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public ArrayList <ViviendaEsp> getViviendasEsp (Long idCliente) throws Exception {
		
		DAOViviendaEsp daoAlojamiento = new DAOViviendaEsp();
		 ArrayList <ViviendaEsp> buscada = new ArrayList<>();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getViviendasEsporadicas(idCliente);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public void deleteViviendaEsp (Long idOp, Long idApartamento) throws Exception {
		
		DAOViviendaEsp daoAlojamiento = new DAOViviendaEsp();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			daoAlojamiento.deleteAlojamiento(idOp, idApartamento);
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	//-------------------------------------
	// Vivienda Universitaria
	//-------------------------------------
	
	public ViviendaUniversitariaVO getViviendaUniv (Long idCliente, Long idReserva) throws Exception {
		
		DAOViviendaUniv daoAlojamiento = new DAOViviendaUniv();
		ViviendaUniversitariaVO buscada = null;
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getViviendaUniv(idCliente, idReserva);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public ArrayList <ViviendaUniversitariaVO> getViviendasUniversitarias (Long idCliente) throws Exception {
		
		DAOViviendaUniv daoAlojamiento = new DAOViviendaUniv();
		 ArrayList <ViviendaUniversitariaVO> buscada = new ArrayList<>();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			buscada = daoAlojamiento.getViviendasUniv(idCliente);
			if(buscada == null) {
				throw new Exception("El recurso no existe - no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return buscada;
	}
	
	public void deleteViviendaUniv (Long idOp, Long idApartamento) throws Exception {
		
		DAOViviendaUniv daoAlojamiento = new DAOViviendaUniv();
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			daoAlojamiento.deleteVivUniv(idOp, idApartamento);
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoAlojamiento.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	//-------------------------------------------------------
	// Metodos de la aplicacion (Transacciones): Operadores
	//-------------------------------------------------------
	
	public List<OperadorVO> getAllOperadores () throws Exception {
		
		DAOperador daoCliente = new DAOperador();
		List<OperadorVO> clientes;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			// Se obtienen 100 clientes.
			clientes = daoCliente.getOperadores();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			
			try {daoCliente.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes;
	}
	
	/**
	 * Metodo que modela la transaccion que busca la reserva solicitada (de acuerdo a
	 * su cliente) en la base de datos. <br/>
	 * @param idCliente ID del Cliente que posee la reserva buscada. id != null
	 * @param idReserva ID de la reserva del cliente buscada.
	 * @return ReservaVO Reserva que se obtiene como resultado de la consulta.
	 * @throws Exception Cualquier error que se genere durante la transaccion
	 */
	public OperadorVO getOperador (Long idCliente) throws Exception {
		
		DAOperador daoCliente = new DAOperador();
		OperadorVO clienteBuscado = null;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			clienteBuscado = daoCliente.getOperador(idCliente);
			
			if(clienteBuscado == null) {
				throw new Exception("El cliente con ID: " + idCliente + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoCliente.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clienteBuscado;
	}
	
	/**
	 * Metodo que modela la transaccion que busca la reserva solicitada (de acuerdo a
	 * su cliente) en la base de datos. <br/>
	 * @param idCliente ID del Cliente que posee la reserva buscada. id != null
	 * @param idReserva ID de la reserva del cliente buscada.
	 * @return ReservaVO Reserva que se obtiene como resultado de la consulta.
	 * @throws Exception Cualquier error que se genere durante la transaccion
	 */
	public Double getMymoney (Long idCliente) throws Exception {
		
		DAOperador daoCliente = new DAOperador();
		Double clienteBuscado = null;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			clienteBuscado = daoCliente.getMyMoney(idCliente);
			
			if(clienteBuscado == null) {
				throw new Exception("El cliente con ID: " + idCliente + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try { daoCliente.cerrarRecursos(); if(this.conn!=null){this.conn.close();}}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clienteBuscado;
	}
	
	public ArrayList get20Ofertas() throws Exception
	{
		this.conn = darConexion();
		System.out.println(this.conn);
		ArrayList <RFC2> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT CAPACIDAD, COSTO, DIRECCION,  NOMBRE, TIPO_ALOJAMIENTO, NUMAPARICIONESOFERTA FROM (SELECT* FROM %1$s.ALOJAMIENTOS)B RIGHT OUTER JOIN(SELECT ID_ALOJAMIENTO,COUNT(ID_ALOJAMIENTO) AS NUMAPARICIONESOFERTA FROM %1$s.RESERVAS GROUP BY ID_ALOJAMIENTO ORDER BY COUNT(ID_ALOJAMIENTO)DESC FETCH FIRST 20 ROWS ONLY)A ON B.ID_ALOJAMIENTO = A.ID_ALOJAMIENTO", USUARIO);
		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToRFC2(rs));
		}
				
		return listaRetorno;

		
	}
	
	
	public RFC2 convertResultSetToRFC2 (ResultSet resultSet) throws SQLException, Exception {
			
			int pCapacidad = resultSet.getInt("CAPACIDAD");
			Double pCosto = resultSet.getDouble("COSTO");
			String Direccion = resultSet.getString("DIRECCION");
			String Nombre = resultSet.getString("NOMBRE");
			String Tipo = resultSet.getString("TIPO_ALOJAMIENTO");
			int Numero = resultSet.getInt("NUMAPARICIONESOFERTA");
	
			RFC2 objRetorno = new RFC2( pCapacidad,pCosto, Direccion, Nombre,Tipo,Numero);
			return objRetorno;
		}
	
}
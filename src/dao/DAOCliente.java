package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.ApartamentoVO;
import vos.ClienteVO;
import vos.HabitacionHoteleraVO;
import vos.HabitacionUniversitariaVO;
import vos.HabitacionVO;
import vos.HostalVO;
import vos.ReservaVO;
import vos.ViviendaEsp;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOCliente {

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------	
	
	/**
	 * Constante que hace referencia al usuario Oracle usado
	 * para generar las tablas.
	 */
	public static final String USUARIO = "ISIS2304A871810";
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------	
	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> resources;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;
	
	
	// DAO externos a utilizar para metodos de transferencia:
	/**
	 * DAO de las entidades generadas por el cliente: Reservas
	 */
	private DAOReserva daoReserva;
	
	/**
	 * DAO de un alojamiento de tipo: Apartamento.
	 */
	private DAOApartamento daoApto;
	
	/**
	 * DAO de un alojamiento de tipo: Habitacion.
	 */
	private DAOHabitacion daoHab;
	
	/**
	 * DAO de un alojamiento de tipo: Vivienda esporadica.
	 */
	private DAOViviendaEsp vivEsp;
	
	/**
	 * DAO de un alojamiento de tipo: Hostal.
	 */
	private DAOHostal hostal;
	
	/**
	 * DAO de un alojamiento de tipo: Habitacion Hotelera.
	 */
	private DAOHabitacionHotelera habHotel;
	
	/**
	 * DAO de un alojamiento de tipo: Habitacion Universitaria.
	 */
	private DAOHabitacionUniv habUniv;
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOCliente(){
		resources = new ArrayList<>();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------

	// GET BY ID
	/**
	 * Metodo para obtener un cliente con un identificador especifico en la base de datos.
	 * @param id Identificador del Cliente que se desea buscar en la base de datos.
	 * @return Un objeto de tipo ClienteVO con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public ClienteVO getCliente (Long id) throws SQLException, Exception {
		
		ClienteVO clienteRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.CLIENTES WHERE ID_CLIENTE = %2$d", USUARIO, id); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			clienteRetorno = convertResultSetToClienteVO(rs);
		}

		return clienteRetorno;	
	}
	
	// GET ALL
	public ArrayList <ClienteVO> getClientes() throws SQLException, Exception {
		
		ArrayList <ClienteVO> listaRetorno = new ArrayList<>();
		
		// Se extraen los primeros 100 clientes en la base de datos, esto debido a que
		// en la creacion de tablas se incluyeron 1000 datos relacionados con esta entidad.
		String sql = String.format("SELECT * FROM %1$s.CLIENTES WHERE ROWNUM <= 100", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToClienteVO(rs));
		}
		return listaRetorno;
	}
	
	// POST
	/**
	 * Crea un cliente en la base de datos a través de un VO del mismo tipo.
	 * @param newCliente Cliente referencia para poder crear la informacion en la BD.
	 * @throws SQLException En caso de que exista un problema en la conexion o en la sentencia SQL.
	 * @throws Exception En caso de que exista un problema con el metodo en general.
	 */
	public void createCliente (ClienteVO newCliente) throws SQLException, Exception {
		
		String SQL = String.format("INSERT INTO %1$s.CLIENTES (ID, CEDULA, EDAD, APELLIDO, NOMBRE, PREFERENCIAALOJAMIENTO) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '&7$s')", 
				USUARIO, 
				newCliente.getId(), 
				newCliente.getCedulaCliente(),
				newCliente.getEdadCliente(), 
				newCliente.getApellidoCliente(),
				newCliente.getNombreCliente(),
				newCliente.getPreferenciasAlojamiento());
		
		System.out.println(SQL);

		PreparedStatement prepStmt = conn.prepareStatement(SQL);
		resources.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	//UPDATE
	/**
	 * Actualiza un Cliente existente en la base de datos con nueva informacion de una entidad generada.
	 * @param newUser Entidad VO para obtener los atributos nuevos a asociar (ID se mantiene).
	 * @throws SQLException En caso de que exista un problema en la conexion o en la sentencia SQL.
	 * @throws Exception En caso de que exista un problema en el metodo en general.
	 */
	public void updateCliente (ClienteVO newUser) throws SQLException, Exception{
		
		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.CLIENTES ", USUARIO));
		sql.append (String.format (
				"SET CEDULA = '%1$s', EDAD = '%2$s', APELLIDO = '%3$s', NOMBRE = '%4$s', PREFERENCIAALOJAMIENTO = '%5$S'",
				newUser.getCedulaCliente(), 
				newUser.getApellidoCliente(),
				newUser.getNombreCliente(),
				newUser.getPreferenciasAlojamiento()));
		
		sql.append ("WHERE ID = " + newUser.getId());
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		resources.add(prepStmt);
		prepStmt.executeQuery();
	}

	//DELETE
	/**
	 * Elimina el cliente asociado con el identificador dado de la Base de Datos.
	 * @param oldUser Cliente referencia, el cual se desea borrar (haciendo uso de su ID)
	 * @throws SQLException En caso de que exista un problema con la conexion o la sentencia SQL
	 * @throws Exception En caso de que existan un problema en el metodo en general.
	 */
	public void deleteCliente (ClienteVO oldUser) throws SQLException, Exception{
		
		String SQL = String.format("DELETE FROM %1$s.CLIENTES WHERE ID = %2$d", USUARIO, oldUser.getId());

		System.out.println(SQL);
		
		PreparedStatement prepStmt = conn.prepareStatement(SQL);
		resources.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	//----------------------------------------------------
	// Metodos consulta externos
	//----------------------------------------------------
	
	// GET Apartamentos libres
	public ArrayList <ApartamentoVO> getClientePreferenciasApto (Long pID) throws SQLException, Exception {
		
		daoApto = new DAOApartamento();
		
		
		ClienteVO clienteAnalizar = getCliente(pID);
		ArrayList <ApartamentoVO> listaRetorno = new ArrayList<>();
		
		if(clienteAnalizar.getPreferenciasAlojamiento() == "Apartamento"){
			listaRetorno = daoApto.getApartamentosUnique();
		}
		return listaRetorno;
	}
	
	// Viviendas Esp libres
	public ArrayList <ViviendaEsp> getClientePreferenciasViviendaEsp (Long pID) throws SQLException, Exception {
		
		vivEsp = new DAOViviendaEsp();
		ClienteVO clienteAnalizar = getCliente(pID);
		ArrayList <ViviendaEsp> listaRetorno = new ArrayList<>();
		
		if(clienteAnalizar.getPreferenciasAlojamiento() == "Vivienda_Esp"){
			listaRetorno = vivEsp.getViviendasEspUnique();
		}
		return listaRetorno;
	}
	
	public ArrayList <HostalVO> getClientePreferenciasHostal (Long pID) throws SQLException, Exception {
		
		hostal = new DAOHostal();
		ClienteVO clienteAnalizar = getCliente(pID);
		ArrayList <HostalVO> listaRetorno = new ArrayList<>();
		
		if(clienteAnalizar.getPreferenciasAlojamiento() == "Hostal"){
			listaRetorno = hostal.getHostalesUnique();
		}
		return listaRetorno;
	}
	
	
	public ArrayList <HabitacionHoteleraVO> getClientePreferenciasHotel (Long pID) throws SQLException, Exception {
		
		
		habHotel = new DAOHabitacionHotelera();
		ClienteVO clienteAnalizar = getCliente(pID);
		ArrayList <HabitacionHoteleraVO> listaRetorno = new ArrayList<>();
		
		if(clienteAnalizar.getPreferenciasAlojamiento() == "Hotel"){
			listaRetorno = habHotel.getHabitacionesHotelerasUnique();
		}
		return listaRetorno;
	}
	
	/**
	 * Retorna las habitaciones, cuya capacidad + 1 no exceda la capacidad maxima, si el cliente las prefiere.
	 * @param pID Identificador del cliente que se buscara y analizara. Este debe existir en la Base de datos.
	 * @return ArrayList <HabitacionVO> Habitaciones disponibles para generar una reserva.
	 * @throws SQLException En caso de error en la conexion a la BD.
	 * @throws Exception
	 */
	public ArrayList <HabitacionVO> getClientePreferenciasHabitacion (Long pID) throws SQLException, Exception {
		
		daoHab = new DAOHabitacion();
		ClienteVO clienteAnalizar = getCliente(pID);
		ArrayList <HabitacionVO> listaRetorno = new ArrayList<>();
		
		if(clienteAnalizar.getPreferenciasAlojamiento() == "Habitacion"){
			listaRetorno = daoHab.getHabitacionesUnique();
		}
		return listaRetorno;
	}
	
	/**
	 * Retorna las habitaciones, cuya capacidad + 1 no exceda la capacidad maxima, si el cliente las prefiere.
	 * @param pID Identificador del cliente que se buscara y analizara. Este debe existir en la Base de datos.
	 * @return ArrayList <HabitacionVO> Habitaciones disponibles para generar una reserva.
	 * @throws SQLException En caso de error en la conexion a la BD.
	 * @throws Exception
	 */
	public ArrayList <HabitacionUniversitariaVO> getClientePreferenciasViviendaUniv (Long pID) throws SQLException, Exception {
		
		habUniv = new DAOHabitacionUniv();
		ClienteVO clienteAnalizar = getCliente(pID);
		ArrayList <HabitacionUniversitariaVO> listaRetorno = new ArrayList<>();
		
		if(clienteAnalizar.getPreferenciasAlojamiento() == "Vivienda_Univ"){
			listaRetorno = habUniv.getHabitacionesUnivUnique();
		}
		
		return listaRetorno;
	}

	//----------------------------------------------------
	// Metodos conexion - Auxiliares
	//----------------------------------------------------
	
	/**
	 * Metodo encargado de inicializar la conexion del DAO a la Base de Datos a partir del parametro <br/>
	 * <b>Postcondicion: </b> el atributo conn es inicializado <br/>
	 * @param connection la conexion generada en el TransactionManager para la comunicacion con la Base de Datos
	 */
	public void setConn(Connection connection){
		this.conn = connection;
	}
	
	/**
	 * Metodo que cierra todos los recursos que se encuentran en el arreglo de recursos<br/>
	 * <b>Postcondicion: </b> Todos los recurso del arreglo de recursos han sido cerrados.
	 */
	public void cerrarRecursos() {
		for(Object ob : resources){
			if(ob instanceof PreparedStatement)
				try {((PreparedStatement) ob).close();} 
				catch (Exception ex) {ex.printStackTrace();}
		}
	}
	
	//----------------------------------------------------
	// Metodos transferencia RS -> VO
	//----------------------------------------------------
	
	/**
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla Clientes) en una instancia de la clase Cliente.
	 * @param resultSet ResultSet con la informacion de un cliente que se obtuvo de la base de datos.
	 * @return Objeto de tipo Cliente, cuyos atributos corresponden a los valores asociados a un registro particular de la tabla Cliente.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public ClienteVO convertResultSetToClienteVO (ResultSet resultSet) throws SQLException, Exception{
		
		daoReserva = new DAOReserva();
		
		Long id = resultSet.getLong("ID_CLIENTE");
		Long pCedula = resultSet.getLong("CEDULA");
		Integer pEdad = resultSet.getInt("EDAD");
		String pApellido = resultSet.getString("APELLIDO");
		String pNombre = resultSet.getString("NOMBRE");
		String pPreferencias = resultSet.getString("PREFERENCIA_ALOJAMIENTO");
		String pRelacionUniandes = resultSet.getString("RELACION_UNIANDES");
		ArrayList <ReservaVO> pReservas = new ArrayList<>() ;

		ClienteVO objRetorno = new ClienteVO(id, pCedula, pEdad, pApellido, pNombre, pPreferencias, pRelacionUniandes, pReservas);
		return objRetorno;
	}
}
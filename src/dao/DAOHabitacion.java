package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import vos.HabitacionVO;
import vos.ServicioVO;

public class DAOHabitacion {

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
	
	// DAO externos necesarios para realizar requerimientos:
	/**
	 * DAO de la reserva que se desea manipular dentro de los requerimientos.
	 */
	private DAOReserva daoReserva;
	
	/**
	 * DAO con los servicios de la habitacion
	 */
	private DAOServicio servicios;
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOHabitacion() {
		resources = new ArrayList<>();
		daoReserva = new DAOReserva();
		servicios = new DAOServicio();
	}
	
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	
	// GET BY ID
	/**
	 * Metodo para obtener una habitacion con un identificador especifico asociado a un operador dado en la base de datos.
	 * @param idOperador Identificador del Operador que se desea buscar en la base de datos.
	 * @param idAlojamiento Identificador del alojamiento que se desea buscar.
	 * @return Un objeto de tipo HabitacionVO con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public HabitacionVO getHabitacion (Long idOperador, Long idAlojamiento) throws SQLException, Exception {
		
		HabitacionVO objetoRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.HABITACIONES NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACION NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d AND ID_HABITACION = %3$d", USUARIO, idOperador, idAlojamiento); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToHabitacionVO(rs);
		}
		return objetoRetorno;	
	}
	
	// GET ALL
	/**
	 * Metodo para obtener todas las habitaciones de un operador con un identificador especifico en la base de datos.
	 * @param id Identificador del Operador que tiene las habitaciones y se desea buscar en la base de datos.
	 * @return Una lista de objetos de tipo ApartamentoVO con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public ArrayList <HabitacionVO> getHabitaciones (Long idOperador) throws SQLException, Exception {
		
		ArrayList <HabitacionVO> listaRetorno = new ArrayList<>();		
		String sql = String.format("SELECT * FROM %1$s.HABITACIONES NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACION NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d", USUARIO, idOperador); 
		
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToHabitacionVO(rs));
		}
		return listaRetorno;
	}
	
	
	/**
	 * Obtiene todos las habitaciones del sistema sin restricciones.
	 * @return ArrayList con las habitaciones existentes en la base de datos.
	 * @throws SQLException En caso de que exista un problema en la sentencia o la conexion.
	 * @throws Exception En caso de que exista un problema en general.
	 */
	public ArrayList <HabitacionVO> getHabitacionesUnique() throws SQLException, Exception {
		
		ArrayList <HabitacionVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.HABITACIONES NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACION NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE CAPACIDAD + 1 < %2$s", USUARIO, HabitacionVO.CANTIDAD_MAX_HABITACION);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToHabitacionVO(rs));
		}
		return listaRetorno;
	}
	
	//DELETE
	/**
	 * Elimina el cliente asociado con el identificador dado de la Base de Datos.
	 * @param oldUser Cliente referencia, el cual se desea borrar (haciendo uso de su ID)
	 * @throws SQLException En caso de que exista un problema con la conexion o la sentencia SQL
	 * @throws Exception En caso de que existan un problema en el metodo en general.
	 */
	public void deleteAlojamiento (Long idOperador, Long idAlojamiento) throws SQLException, Exception {
		
		HabitacionVO habitacion = getHabitacion(idOperador, idAlojamiento);
		
		if (habitacion != null) {
			
			if (habitacion.getIdReservas() == 0){
				String SQL = String.format("DELETE FROM %1$s.HABITACIONES WHERE ID_HABITACION = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
				System.out.println(SQL);
				
				PreparedStatement prepStmt = conn.prepareStatement(SQL);
				resources.add(prepStmt);
				prepStmt.executeQuery();
			}
			
			else if (habitacion.getIdReservas() != 0){
				
				Long identificadorReserva = habitacion.getIdReservas();
				Date fechaFin = getReservaUniqueFecha(identificadorReserva);
				Date hoy = new Date();
				
				if (hoy.after(fechaFin)){
					String SQL = String.format("DELETE FROM %1$s.HABITACIONES WHERE ID_HABITACION = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
					
					System.out.println(SQL);
					
					PreparedStatement prepStmt = conn.prepareStatement(SQL);
					resources.add(prepStmt);
					prepStmt.executeQuery();
				}
			}
			else {throw new Exception("No es posible retirar el alojamiento ya que este cuenta con reservas vigentes");}
		}
		else {throw new Exception("La habitacion que desea eliminar no existe en la base de datos");}
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
	public HabitacionVO convertResultSetToHabitacionVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HABITACION");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		ArrayList <ServicioVO> pServicios = getServiciosHabitacion(id);
		Long pIdReserva = resultSet.getLong("ID_RESERVA");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		Double pCostoProm = resultSet.getDouble("COSTO_PROMOCIONAL");
	
		HabitacionVO objRetorno = new HabitacionVO(id, pCapacidad, pDireccion, 
				pNombre, pCosto, pTipoAlojamiento, pServicios, pIdReserva, pCostoProm, pIdOp);
		
		return objRetorno;
	}
	
	public ArrayList <ServicioVO> getServiciosHabitacion (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACION WHERE ID_HABITACION = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;	
	}
	
	//-----------------------------------------------
	// Converts Servicio
	//-----------------------------------------------
	public ServicioVO convertResultSetToServicioVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_SERVICIO");
		Double pCosto = resultSet.getDouble("COSTO");
		String pNombre = resultSet.getString("NOMBRE_SERVICIO");
		Long pIdAlojamiento = resultSet.getLong("ID_ALOJAMIENTO");

		ServicioVO objRetorno = new ServicioVO(id, pCosto, pNombre, pIdAlojamiento);
		return objRetorno;
	}	
	
	
	
	// GET UNIQUE
	public Date getReservaUniqueFecha (Long idReserva) throws SQLException, Exception {
		
		Date objetoRetorno = null;

		String sql = String.format("SELECT FECHA_FIN_RESERVA FROM %1$s.RESERVAS WHERE ID_RESERVA = %2$d", USUARIO, idReserva); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToFechaFinal(rs);
		}
		return objetoRetorno;	
	}
	
	public Date convertResultSetToFechaFinal (ResultSet resultSet) throws SQLException {
		Date pFin = resultSet.getDate("FECHA_FIN_RESERVA");
		return pFin;
	}
}
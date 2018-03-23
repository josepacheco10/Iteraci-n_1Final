package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.HabitacionHoteleraVO;
import vos.HotelVO;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOHotel {

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
	private ArrayList <Object> resources;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;
	
	/**
	 * 
	 */
	private DAOHabitacionHotelera habitaciones;
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOHotel() {
		resources = new ArrayList<>();
		habitaciones = new DAOHabitacionHotelera();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	
	public HotelVO getHotel (Long idOperador, Long idApartamento) throws SQLException, Exception {
		
		HotelVO objetoRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.HOTELES WHERE ID_OPERADOR = %2$d AND ID_HOTEL = %3$d", USUARIO, idOperador, idApartamento); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToHotelVO(rs);
		}
		return objetoRetorno;	
	}
	
	public ArrayList <HotelVO> getHoteles (Long idOperador) throws SQLException, Exception {
		
		ArrayList <HotelVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.HOTELES WHERE ID_OPERADOR = %2$d", USUARIO, idOperador);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToHotelVO(rs));
		}
		return listaRetorno;
	}
	
	
	/**
	 * Elimina un hostal con un identificador dado y asociado a un operador especifico.
	 * @param idOperador Identificador del manejador del Hostal
	 * @param idAlojamiento Identificador del Hostal a borrar.
	 * @throws SQLException En caso de que exista un problema en la sentencia o la conexion.
	 * @throws Exception En caso de que exista un problema en general.
	 */
	public void deleteHotel (Long idOperador, Long idAlojamiento) throws SQLException, Exception {
		
		HotelVO hotel = getHotel(idOperador, idAlojamiento);
		
		if (hotel != null) {
			
			ArrayList <HabitacionHoteleraVO> habitaciones = hotel.getMisHabitaciones();
			
			if (habitaciones.isEmpty()){
				String SQL = String.format("DELETE FROM %1$s.HOTELES WHERE ID_HOTEL = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
				System.out.println(SQL);
				PreparedStatement prepStmt = conn.prepareStatement(SQL);
				resources.add(prepStmt);
				prepStmt.executeQuery();
			}
			
			else {
				
				boolean noHayReservas = true;
				for (HabitacionHoteleraVO hab : habitaciones){
					if (hab.getIdReservas() != 0){
						noHayReservas = true;
					}
				}
				
				if (noHayReservas == true){
					String SQL = String.format("DELETE FROM %1$s.HOSTALES WHERE ID_HOSTAL = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
					System.out.println(SQL);
					
					PreparedStatement prepStmt = conn.prepareStatement(SQL);
					resources.add(prepStmt);
					prepStmt.executeQuery();
				}
			}
		}
		else {throw new Exception("El hostal que desea eliminar no existe en la base de datos.");}
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
	
	public HotelVO convertResultSetToHotelVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HOTEL");
		Integer pCantidadEstrellas = resultSet.getInt("CANTIDAD_ESTRELLAS");
		Boolean conPermisos = resultSet.getBoolean("CON_PERMISOS");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		ArrayList<HabitacionHoteleraVO> pHabitaciones = habitaciones.getHabitacionesHotel(id);
		
		HotelVO objRetorno = new HotelVO(id, pHabitaciones, pCantidadEstrellas, conPermisos, pIdOp);
		return objRetorno;
	}
}
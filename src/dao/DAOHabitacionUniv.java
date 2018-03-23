package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.HabitacionUniversitariaVO;
import vos.ServicioVO;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOHabitacionUniv {

	//----------------------------------------------------
	// Constantes
	//----------------------------------------------------	
	
	public final static int CAPACIDAD_MAXIMA = 100;
	
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
	 * Dao de los servicios
	 */
	private DAOServicio servicios;
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOHabitacionUniv (){
		resources = new ArrayList<>();
		servicios = new DAOServicio();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	
	/**
	 * Metodo para obtener todas los apartamentos de un operador con un identificador especifico en la base de datos.
	 * @param id Identificador del Operador que tiene los apartamentos y se desea buscar en la base de datos.
	 * @return Una lista de objetos de tipo ApartamentoVO con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public ArrayList <HabitacionUniversitariaVO> getHabitacionesUniversitarias (Long idVivienda) throws SQLException, Exception {
		
		ArrayList <HabitacionUniversitariaVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.HABITACIONESUNIV NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACIONUNIV NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_VIVIENDAUNIV = %2$d", USUARIO, idVivienda);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToHabUniVO(rs));
		}
		return listaRetorno;
	}
	
	
	/**
	 * Obtiene todos los apartamentos del sistema sin restricciones.
	 * @return ArrayList con los apartamentos existentes en la base de datos.
	 * @throws SQLException En caso de que exista un problema en la sentencia o la conexion.
	 * @throws Exception En caso de que exista un problema en general.
	 */
	public ArrayList <HabitacionUniversitariaVO> getHabitacionesUnivUnique() throws SQLException, Exception {
		
		ArrayList <HabitacionUniversitariaVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.HABITACIONESUNIV NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACIONUNIV NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE CAPACIDAD + 1 < %2$s", USUARIO, HabitacionUniversitariaVO.CAPACIDAD_MAXIMA);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToHabUniVO(rs));
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
	
	public HabitacionUniversitariaVO convertResultSetToHabUniVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HABITACIONUNIV");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		ArrayList <ServicioVO> pServicios = servicios.getServiciosHabitacionUniversitaria(id);
		Long pIdReserva = resultSet.getLong("ID_RESERVA");
		String pMenaje = resultSet.getString("MENAJE");
		Long pIdMiVivienda = resultSet.getLong("ID_VIVIENDAUNIV");
		
		HabitacionUniversitariaVO objRetorno = new HabitacionUniversitariaVO(id, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, pServicios, 
				pIdReserva, pMenaje, pIdMiVivienda);
		
		return objRetorno;
	}
}
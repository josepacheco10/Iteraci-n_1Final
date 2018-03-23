package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.HabitacionUniversitariaVO;
import vos.ViviendaUniversitariaVO;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOViviendaUniv {

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
	 * DAO de las habitaciones de una Viv Universitaria
	 */
	private DAOHabitacionUniv habitaciones;
		
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOViviendaUniv (){
		resources = new ArrayList<>();
		habitaciones = new DAOHabitacionUniv();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	public ViviendaUniversitariaVO getViviendaUniv (Long idOperador, Long idApartamento) throws SQLException, Exception {
		
		ViviendaUniversitariaVO objetoRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.VIVIENDASUNIV WHERE ID_OPERADOR = %2$d AND ID_VIVIENDAUNIV = %3$d", USUARIO, idOperador, idApartamento); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToViviendaUniVO(rs);
		}
		return objetoRetorno;	
	}
	
	public ArrayList <ViviendaUniversitariaVO> getViviendasUniv (Long idOperador) throws SQLException, Exception {
		
		ArrayList <ViviendaUniversitariaVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.VIVIENDASUNIV WHERE ID_OPERADOR = %2$d", USUARIO, idOperador);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToViviendaUniVO(rs));
		}
		return listaRetorno;
	}
	
	/**
	 * Elimina una Vivienda Universitaria con un identificador dado y asociado a un operador especifico.
	 * @param idOperador Identificador del manejador de la Vivienda Univ
	 * @param idAlojamiento Identificador de la vivienda a borrar.
	 * @throws SQLException En caso de que exista un problema en la sentencia o la conexion.
	 * @throws Exception En caso de que exista un problema en general.
	 */
	public void deleteVivUniv (Long idOperador, Long idAlojamiento) throws SQLException, Exception {
		
		ViviendaUniversitariaVO vivUniv = getViviendaUniv(idOperador, idAlojamiento);
		
		if (vivUniv != null) {
			
			ArrayList <HabitacionUniversitariaVO> habitaciones = vivUniv.getHabitacionesUniv();
			
			if (habitaciones.isEmpty()){
				String SQL = String.format("DELETE FROM %1$s.VIVIENDASUNIV WHERE ID_VIVIENDAUNIV = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
				System.out.println(SQL);
				PreparedStatement prepStmt = conn.prepareStatement(SQL);
				resources.add(prepStmt);
				prepStmt.executeQuery();
			}
			
			else {
				
				boolean noHayReservas = true;
				for (HabitacionUniversitariaVO hab : habitaciones){
					if (hab.getIdReservas() != 0){
						noHayReservas = true;
					}
				}
				
				if (noHayReservas == true){
					String SQL = String.format("DELETE FROM %1$s.VIVIENDASUNIV WHERE ID_VIVIENDAUNIV = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
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
	public ViviendaUniversitariaVO convertResultSetToViviendaUniVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HOSTAL");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pUbicacion = resultSet.getString("UBICACION");
		ArrayList<HabitacionUniversitariaVO> pHabitaciones = habitaciones.getHabitacionesUniversitarias(id);
		ViviendaUniversitariaVO objRetorno = new ViviendaUniversitariaVO(id, pCapacidad, 
				pUbicacion, pHabitaciones);
		return objRetorno;
	}
}
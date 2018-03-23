package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.ServicioVO;

public class DAOServicio {

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
		
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOServicio(){
		resources = new ArrayList<>();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: (GET - ALOJAMIENTO)
	//----------------------------------------------------
	
	public ArrayList<ServicioVO> getServiciosApto(Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS INNER JOIN %1$s.ALOJAMIENTOAPARTAMENTO ON SERVICIOS.ID_ALOJAMIENTO = ALOJAMIENTOAPARTAMENTO.ID_ALOJAMIENTO WHERE ID_APARTAMENTO = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
	}
	
	
	public ArrayList<ServicioVO> getServiciosViviendaEsp (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS INNER JOIN %1$s.ALOJAMIENTOVIVIENDAESP ON SERVICIOS.ID_ALOJAMIENTO = ALOJAMIENTOVIVIENDAESP.ID_ALOJAMIENTO WHERE ID_VIVIENDAESP = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
	}
	
	public ArrayList <ServicioVO> getServiciosHostal (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS NATURAL INNER JOIN %1$s.ALOJAMIENTOHOSTAL WHERE ID_HOSTAL = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
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
	
	public ArrayList <ServicioVO> getServiciosHabitacionHotelera (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS INNER JOIN %1$s.ALOJAMIENTOHABITACIONHOTELERA ON SERVICIOS.ID_ALOJAMIENTO = ALOJAMIENTOHABITACIONHOTELERA.ID_ALOJAMIENTO WHERE ID_HABITACIONHOTELERA = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
	}
	
	public ArrayList <ServicioVO> getServiciosHabitacionUniversitaria (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACIONUNIV WHERE ID_HABITACIONUNIV = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
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
	
	public ServicioVO convertResultSetToServicioVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_SERVICIO");
		Double pCosto = resultSet.getDouble("COSTO");
		String pNombre = resultSet.getString("NOMBRE_SERVICIO");
		Long pIdAlojamiento = resultSet.getLong("ID_ALOJAMIENTO");

		ServicioVO objRetorno = new ServicioVO(id, pCosto, pNombre, pIdAlojamiento);
		return objRetorno;
	}
}
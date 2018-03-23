package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import vos.HostalVO;
import vos.ServicioVO;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOHostal {
	
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
	 * Dao de los servicios
	 */
	private DAOServicio servicios;
	
	/**
	 * DAO de una reserva.
	 */
	private DAOReserva daoReserva;
	
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOHostal(){
		resources = new ArrayList<>();
		servicios = new DAOServicio();
		daoReserva = new DAOReserva();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	
	public HostalVO getHostal (Long idOperador, Long idApartamento) throws SQLException, Exception {
		
		HostalVO objetoRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.HOSTALES NATURAL INNER JOIN %1$s.ALOJAMIENTOHOSTAL NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d AND ID_HOSTAL = %3$d", USUARIO, idOperador, idApartamento); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToHostalVO(rs);
		}
		return objetoRetorno;	
	}
	
	public ArrayList <HostalVO> getHostales (Long idOperador) throws SQLException, Exception {
		
		ArrayList <HostalVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.HOSTALES NATURAL INNER JOIN %1$s.ALOJAMIENTOHOSTAL NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d", USUARIO, idOperador);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToHostalVO(rs));
		}
		return listaRetorno;
	}
	
	/**
	 * Obtiene todos los hosteles del sistema sin restricciones.
	 * @return ArrayList con los hostales existentes en la base de datos.
	 * @throws SQLException En caso de que exista un problema en la sentencia o la conexion.
	 * @throws Exception En caso de que exista un problema en general.
	 */
	public ArrayList <HostalVO> getHostalesUnique() throws SQLException, Exception {
		
		ArrayList <HostalVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.HOSTALES NATURAL INNER JOIN %1$s.ALOJAMIENTOHOSTAL NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE CAPACIDAD + 1 < %2$s", USUARIO, HostalVO.CAPACIDAD_MAX_HOSTAL);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToHostalVO(rs));
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
	public void deleteHostal (Long idOperador, Long idAlojamiento) throws SQLException, Exception {
		
		HostalVO hostal = getHostal(idOperador, idAlojamiento);
		
		if (hostal != null) {
			
			if (hostal.getIdReservas() == 0){
				String SQL = String.format("DELETE FROM %1$s.HOSTALES WHERE ID_HOSTAL = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
				System.out.println(SQL);
				
				PreparedStatement prepStmt = conn.prepareStatement(SQL);
				resources.add(prepStmt);
				prepStmt.executeQuery();
			}
			
			else if (hostal.getIdReservas() != 0){
				
				Long identificadorReserva = hostal.getIdReservas();
				Date fechaFinReserva = daoReserva.getReservaUniqueFecha(identificadorReserva);
				Date hoy = new Date();
				
				if (hoy.after(fechaFinReserva)){
					String SQL = String.format("DELETE FROM %1$s.HOSTALES WHERE ID_HOSTAL = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
					
					System.out.println(SQL);
					PreparedStatement prepStmt = conn.prepareStatement(SQL);
					resources.add(prepStmt);
					prepStmt.executeQuery();
				}
			}
			else {throw new Exception("No es posible retirar el alojamiento ya que este cuenta con reservas vigentes");}
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
	
	public HostalVO convertResultSetToHostalVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HOSTAL");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		ArrayList <ServicioVO> pServicios = servicios.getServiciosHostal(id);
		Long pIdReserva = resultSet.getLong("ID_RESERVA");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		Boolean pPermisos = resultSet.getBoolean("CON_PERMISOS");
		String pHorarioAp = resultSet.getString("HORARIO_APERTURA");
		String pHorarioCierre = resultSet.getString("HORARIO_CIERRE");
		String pTipoHab = resultSet.getString("TIPO_HABITACION");
		
		HostalVO objRetorno = new HostalVO(id, pCapacidad, pDireccion, pNombre,
				pCosto, pTipoAlojamiento, pServicios, pIdReserva, 
				pPermisos, pHorarioAp, pHorarioCierre, pTipoHab, pIdOp);
		
		return objRetorno;
	}
}
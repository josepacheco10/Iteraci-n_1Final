package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import vos.ServicioVO;
import vos.ViviendaEsp;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOViviendaEsp {

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
	 * Servicios de la vivienda
	 */
	private DAOServicio daoServicio;
	
	/**
	 * Seguro Arrendatario de la vivienda.
	 */
	private DAOSeguro daoSeguro;
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOViviendaEsp (){
		resources = new ArrayList<>();
		daoReserva = new DAOReserva();
		daoServicio = new DAOServicio();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	
	// GET BY ID		
	/**
	 * Metodo para obtener una vivienda esporadica con un identificador especifico asociado a un operador dado en la base de datos.
	 * @param idOperador Identificador del Operador que se desea buscar en la base de datos.
	 * @param idAlojameitno Identificador de la vivienda que se desea buscar.
	 * @return Un objeto de tipo ViviendaEsp con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public ViviendaEsp getViviendaEsp (Long idOperador, Long idAlojamiento) throws SQLException, Exception {
		
		ViviendaEsp objetoRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.VIVIENDASESP NATURAL INNER JOIN %1$s.ALOJAMIENTOVIVIENDAESP NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d AND ID_VIVIENDAESP = %3$d", USUARIO, idOperador, idAlojamiento); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToViviendaEspVO(rs);
		}
		return objetoRetorno;	
	}
	
	// GET ALL
	/**
	 * Metodo para obtener todas las viviendas de un operador con un identificador especifico en la base de datos.
	 * @param id Identificador del Operador que tiene los apartamentos y se desea buscar en la base de datos.
	 * @return Una lista de objetos de tipo ViviendaEsp con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public ArrayList <ViviendaEsp> getViviendasEsporadicas (Long idOperador) throws SQLException, Exception {
		
		ArrayList <ViviendaEsp> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.VIVIENDASESP NATURAL INNER JOIN %1$s.ALOJAMIENTOVIVIENDAESP NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d", USUARIO, idOperador); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToViviendaEspVO(rs));
		}
		return listaRetorno;
	}
	
	/**
	 * Obtiene todos las viviendas esp del sistema sin restricciones.
	 * @return ArrayList con las viviendas esp existentes en la base de datos.
	 * @throws SQLException En caso de que exista un problema en la sentencia o la conexion.
	 * @throws Exception En caso de que exista un problema en general.
	 */
	public ArrayList <ViviendaEsp> getViviendasEspUnique() throws SQLException, Exception {
		
		ArrayList <ViviendaEsp> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.VIVIENDASESP NATURAL INNER JOIN %1$s.ALOJAMIENTOVIVIENDAESP NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE CAPACIDAD + 1 < %2$s", USUARIO, ViviendaEsp.CAPACIDAD_MAX_VIVIENDA_ESP);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToViviendaEspVO(rs));
		}
		return listaRetorno;
	}

	//DELETE
	/**
	 * Elimina el alojamiento con ID dado, el cual esta asociado con el operador del identificador dado en la Base de Datos.
	 * @param oldUser Cliente referencia, el cual se desea borrar (haciendo uso de su ID)
	 * @throws SQLException En caso de que exista un problema con la conexion o la sentencia SQL
	 * @throws Exception En caso de que existan un problema en el metodo en general.
	 */
	public void deleteAlojamiento (Long idOperador, Long idAlojamiento) throws SQLException, Exception {
		
		ViviendaEsp alojamiento = getViviendaEsp(idOperador, idAlojamiento);
		
		if (alojamiento != null) {
			if (alojamiento.getIdReservas() == 0){
				String SQL = String.format("DELETE FROM %1$s.VIVIENDASESP WHERE ID_VIVIENDAESP = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
				
				System.out.println(SQL);
				
				PreparedStatement prepStmt = conn.prepareStatement(SQL);
				resources.add(prepStmt);
				prepStmt.executeQuery();
			}
			
			else if (alojamiento.getIdReservas() != 0){
				
				Long identificadorReserva = alojamiento.getIdReservas();
				Date fechaFin = daoReserva.getReservaUniqueFecha(identificadorReserva);
				Date hoy = new Date();
				
				if (hoy.after(fechaFin)){
					String SQL = String.format("DELETE FROM %1$s.VIVIENDASESP WHERE ID_VIVIENDAESP = %2$d AND ID_OPERADOR = %3$d", USUARIO, idAlojamiento, idOperador);
					System.out.println(SQL);
					
					PreparedStatement prepStmt = conn.prepareStatement(SQL);
					resources.add(prepStmt);
					prepStmt.executeQuery();
				}
			}
			
			else {throw new Exception("No es posible retirar el alojamiento ya que este cuenta con reservas vigentes");}
		}
		
		else {}
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
	
	
	public ViviendaEsp convertResultSetToViviendaEspVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_VIVIENDAESP");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		Long pIdReserva = resultSet.getLong("ID_RESERVA");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		String pCaract = resultSet.getString("CARACT_BASICAS");
		Integer pNochesAnio = resultSet.getInt("NOCHES_ANIO");
		ArrayList<ServicioVO> pServicios = daoServicio.getServiciosViviendaEsp(id);
		Long pSeguro = daoSeguro.getSeguroVivienda(id).getId();

		ViviendaEsp objRetorno = new ViviendaEsp(id, pCapacidad, pDireccion, pNombre, pCosto, 
				pTipoAlojamiento, pServicios, pIdReserva, pCaract, pNochesAnio, pIdOp, pSeguro);
		return objRetorno;
	}	
}
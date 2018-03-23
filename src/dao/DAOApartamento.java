package dao;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.ApartamentoVO;
import vos.ServicioVO;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOApartamento {

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
	 * Dao de los servicios
	 */
	private DAOServicio servicios;
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOApartamento (){
		resources = new ArrayList<>();
		
		
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	
	// GET BY ID
	/**
	 * Metodo para obtener un apartamento con un identificador especifico asociado a un operador dado en la base de datos.
	 * @param idOperador Identificador del Operador que se desea buscar en la base de datos.
	 * @param idApartamento Identificador del apartamento que se desea buscar.
	 * @return Un objeto de tipo ClienteVO con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public ApartamentoVO getApartamento (Long idOperador, Long idApartamento) throws SQLException, Exception {
		
		ApartamentoVO objetoRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.APARTAMENTOS NATURAL INNER JOIN %1$s.ALOJAMIENTOAPARTAMENTO NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d AND ID_APARTAMENTO = %3$d", USUARIO, idOperador, idApartamento); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToApartamentoVO(rs);
		}
		return objetoRetorno;	
	}
	
	// GET ALL
	/**
	 * Metodo para obtener todas los apartamentos de un operador con un identificador especifico en la base de datos.
	 * @param id Identificador del Operador que tiene los apartamentos y se desea buscar en la base de datos.
	 * @return Una lista de objetos de tipo ApartamentoVO con sus atributos. 
	 * @throws SQLException En caso de que exista un error en la conexión o en la sentencia SQL.
	 * @throws Exception En caso de que exista un error en el metodo en general. 
	 */
	public ArrayList <ApartamentoVO> getApartamentos (Long idOperador) throws SQLException, Exception {
		
		ArrayList <ApartamentoVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.APARTAMENTOS NATURAL INNER JOIN %1$s.ALOJAMIENTOAPARTAMENTO NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE ID_OPERADOR = %2$d", USUARIO, idOperador);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToApartamentoVO(rs));
		}
		return listaRetorno;
	}
	
	
	/**
	 * Obtiene todos los apartamentos del sistema sin restricciones.
	 * @return ArrayList con los apartamentos existentes en la base de datos.
	 * @throws SQLException En caso de que exista un problema en la sentencia o la conexion.
	 * @throws Exception En caso de que exista un problema en general.
	 */
	public ArrayList <ApartamentoVO> getApartamentosUnique() throws SQLException, Exception {
		
		ArrayList <ApartamentoVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.APARTAMENTOS NATURAL INNER JOIN %1$s.ALOJAMIENTOAPARTAMENTO NATURAL INNER JOIN %1$s.ALOJAMIENTOS NATURAL INNER JOIN %1$s.RESERVAS WHERE CAPACIDAD + 1 < %2$s", USUARIO, ApartamentoVO.CAPACIDAD_MAX_APTO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToApartamentoVO(rs));
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
	public void deleteAlojamiento (Long idOperador, Long idApartamento) throws SQLException, Exception {
		daoReserva = new DAOReserva();
		ApartamentoVO apartamento = getApartamento(idOperador, idApartamento);
		if (apartamento != null) {
			
			if (apartamento.getIdReservas() == 0){
				String SQL = String.format("DELETE FROM %1$s.APARTAMENTOS WHERE ID_APARTAMENTO = %2$d AND ID_OPERADOR = %3$d", USUARIO, idApartamento, idOperador);
				System.out.println(SQL);
				
				PreparedStatement prepStmt = conn.prepareStatement(SQL);
				resources.add(prepStmt);
				prepStmt.executeQuery();
			}
			
			else if (apartamento.getIdReservas() != 0){
				
				Long identificadorReserva = apartamento.getIdReservas();
				Date fechaFinReserva = daoReserva.getReservaUniqueFecha(identificadorReserva);
				Date hoy = new Date();
				
				if (hoy.after(fechaFinReserva)){
					String SQL = String.format("DELETE FROM %1$s.APARTAMENTOS WHERE ID_APARTAMENTO = %2$d AND ID_OPERADOR = %3$d", USUARIO, idApartamento, idOperador);
					
					System.out.println(SQL);
					PreparedStatement prepStmt = conn.prepareStatement(SQL);
					resources.add(prepStmt);
					prepStmt.executeQuery();
				}
			}
			else {throw new Exception("No es posible retirar el alojamiento ya que este cuenta con reservas vigentes");}
		}
		else {throw new Exception("El apartamentos que desea eliminar no existe o no está asociado a usted.");}
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
	
	public ApartamentoVO convertResultSetToApartamentoVO (ResultSet resultSet) throws SQLException, Exception {
		
		servicios = new DAOServicio();
		Long id = resultSet.getLong("ID_APARTAMENTO");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		ArrayList <ServicioVO> pServicios = servicios.getServiciosApto(id);
		Long pIdReserva = resultSet.getLong("ID_RESERVA");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		Double pCostoAdmin = resultSet.getDouble("COSTO_ADMIN");
		Boolean pIsAmoblado = resultSet.getBoolean("IS_AMOBLADO");
		
		ApartamentoVO objRetorno = new ApartamentoVO(id, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, 
				pServicios, pIdReserva, pCostoAdmin, pIsAmoblado, pIdOp);
		
		return objRetorno;
	}
}
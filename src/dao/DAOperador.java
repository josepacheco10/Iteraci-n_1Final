package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.ApartamentoVO;
import vos.HabitacionHoteleraVO;
import vos.HabitacionUniversitariaVO;
import vos.HabitacionVO;
import vos.HostalVO;
import vos.HotelVO;
import vos.OperadorVO;
import vos.ServicioVO;
import vos.ViviendaEsp;
import vos.ViviendaUniversitariaVO;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOperador {

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
	private DAOHotel hotel;
	
	/**
	 * DAO de un alojamiento de tipo: Habitacion Universitaria.
	 */
	private DAOViviendaUniv vivUniv;
	
	//----------------------------------------------------
	// Constructor inicialización
	//----------------------------------------------------
	
	public DAOperador (){
		resources = new ArrayList<>();
		daoApto = new DAOApartamento();
		daoHab = new DAOHabitacion();
		vivEsp = new DAOViviendaEsp();
		hostal = new DAOHostal();
		hotel = new DAOHotel();
		vivUniv = new DAOViviendaUniv();
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
	public OperadorVO getOperador(Long id) throws SQLException, Exception {
		
		OperadorVO clienteRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.OPERADORES WHERE ID_OPERADOR = %2$d", USUARIO, id); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			clienteRetorno = convertResultSetToOperadorVO(rs);
		}

		return clienteRetorno;	
	}
	
	// GET ALL
	public ArrayList <OperadorVO> getOperadores () throws SQLException, Exception {
		
		ArrayList <OperadorVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.OPERADORES", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			listaRetorno.add(convertResultSetToOperadorVO(rs));
		}
		return listaRetorno;
	}
	
	// GET DINERO
	public Double getMyMoney (Long id) throws SQLException, Exception{
		
		double dineroGanado = 0;
		OperadorVO operador = getOperador(id);
		
		ArrayList<HabitacionVO> pHabitaciones = operador.getMisHabitaciones();
		ArrayList<ApartamentoVO> pApartamentos = operador.getMisApartamentos();
		ArrayList<ViviendaEsp> pViviendasEsp = operador.getMisViviendasEsp();
		ArrayList<HostalVO> pHostales = operador.getMisHostales();
		ArrayList<HotelVO> pHoteles = operador.getMisHoteles();
		ArrayList<ViviendaUniversitariaVO> pViviendasUniv = operador.getMisViviendasUniversitarias();
		
		for (HabitacionVO hab : pHabitaciones ){
			dineroGanado += hab.getCosto();
		}
		
		for (ApartamentoVO hab : pApartamentos ){
			dineroGanado += hab.getCosto() + hab.getCostoAdmin();
		}
		
		for (ViviendaEsp hab : pViviendasEsp ){
			dineroGanado += hab.getCosto();
		}
		
		for (HostalVO hab : pHostales){
			dineroGanado += hab.getCosto();
		}
		
		for (HotelVO hab : pHoteles){
			
			for (HabitacionHoteleraVO habHotel : hab.getMisHabitaciones()){
				dineroGanado += habHotel.getCosto();
			}
		}
		
		for (ViviendaUniversitariaVO hab : pViviendasUniv){
			
			for (HabitacionUniversitariaVO habHotel : hab.getHabitacionesUniv()){
				dineroGanado += habHotel.getCosto();
			}
		}
		
		return dineroGanado;
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
	public OperadorVO convertResultSetToOperadorVO (ResultSet resultSet) throws SQLException, Exception{
		
		Long id = resultSet.getLong("ID_OPERADOR");
		Integer pCedula = resultSet.getInt("CEDULA");
		Long pNIT = resultSet.getLong("NIT");
		String pNombre = resultSet.getString("NOMBRE");
		String pRelacionUniandes = resultSet.getString("RELACION_UNIANDES");
		String pTipo = resultSet.getString("TIPO_OPERADOR");
		
		ArrayList<HabitacionVO> pHabitaciones = getHabitaciones(id);
		ArrayList<ApartamentoVO> pApartamentos = getApartamentos(id);
		ArrayList<ViviendaEsp> pViviendasEsp = getViviendasEsporadicas(id);
		ArrayList<HostalVO> pHostales = getHostales(id);
		ArrayList<HotelVO> pHoteles = getHoteles(id);
		ArrayList<ViviendaUniversitariaVO> pViviendasUniv = getViviendasUniv(id);
		
		OperadorVO objRetorno = new OperadorVO(id, pCedula, pNIT, pNombre, pRelacionUniandes, pTipo, 
				pHabitaciones, pApartamentos, pViviendasEsp, pHostales, 
				pHoteles, pViviendasUniv);
		
		return objRetorno;
	}
	
	
	//-----------------------------------------------
	// Metodos para ser feliz a las 9
	//-----------------------------------------------
	
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

	//-----------------------------------------------
	// Converts
	//-----------------------------------------------
	
	public ArrayList <ServicioVO> getServiciosHabitacion (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS NATURAL INNER JOIN %1$s.ALOJAMIENTOHABITACION WHERE ID_HABITACION = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
	}
	
	
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
	
	public ArrayList<ServicioVO> getServiciosApto(Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS NATURAL INNER JOIN %1$s.ALOJAMIENTOAPARTAMENTO WHERE ID_APARTAMENTO = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
	}
	
	public ApartamentoVO convertResultSetToApartamentoVO (ResultSet resultSet) throws SQLException, Exception {
		Long id = resultSet.getLong("ID_APARTAMENTO");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		ArrayList <ServicioVO> pServicios = getServiciosApto(id);
		Long pIdReserva = resultSet.getLong("ID_RESERVA");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		Double pCostoAdmin = resultSet.getDouble("COSTO_ADMIN");
		Boolean pIsAmoblado = resultSet.getBoolean("IS_AMOBLADO");
		
		ApartamentoVO objRetorno = new ApartamentoVO(id, pCapacidad, pDireccion, pNombre, pCosto, pTipoAlojamiento, 
				pServicios, pIdReserva, pCostoAdmin, pIsAmoblado, pIdOp);
		
		return objRetorno;
	}
	
	public ViviendaUniversitariaVO convertResultSetToViviendaUniVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HOSTAL");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pUbicacion = resultSet.getString("UBICACION");
		ArrayList<HabitacionUniversitariaVO> pHabitaciones = new ArrayList<>();
		ViviendaUniversitariaVO objRetorno = new ViviendaUniversitariaVO(id, pCapacidad, 
				pUbicacion, pHabitaciones);
		return objRetorno;
	}
	
	public ViviendaEsp convertResultSetToViviendaEspVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_VIVIENDAESP");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		Long pIdReserva = resultSet.getLong("ID_RESERVA");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		Integer pNochesAnio = resultSet.getInt("NOCHES_ANIO");
		ArrayList<ServicioVO> pServicios = getServiciosViviendaEsp(id);
		Long pSeguro = (long) 1;

		ViviendaEsp objRetorno = new ViviendaEsp(id, pCapacidad, pDireccion, pNombre, pCosto, 
				pTipoAlojamiento, pServicios, pIdReserva, "Es amplia y comoda", pNochesAnio, pIdOp, pSeguro);
		return objRetorno;
	}	
	
	public ArrayList<ServicioVO> getServiciosViviendaEsp (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS NATURAL INNER JOIN %1$s.ALOJAMIENTOVIVIENDAESP WHERE ID_VIVIENDAESP = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
	}
	
	public HostalVO convertResultSetToHostalVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HOSTAL");
		Integer pCapacidad = resultSet.getInt("CAPACIDAD");
		String pDireccion = resultSet.getString("DIRECCION");
		String pNombre = resultSet.getString("NOMBRE");
		Double pCosto = resultSet.getDouble("COSTO");
		String pTipoAlojamiento = resultSet.getString("TIPO_ALOJAMIENTO");
		ArrayList <ServicioVO> pServicios = getServiciosHostal(id);
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
	
	public ArrayList <ServicioVO> getServiciosHostal (Long pAlojamiento) throws SQLException, Exception{
		
		ArrayList <ServicioVO> listaRetorno = new ArrayList<>();
		String sql = String.format("SELECT * FROM %1$s.SERVICIOS NATURAL INNER JOIN %1$s.ALOJAMIENTOHOSTAL WHERE ID_HOSTAL = %2$d", USUARIO, pAlojamiento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {listaRetorno.add(convertResultSetToServicioVO(rs));}
		return listaRetorno;
	}
	
	public HotelVO convertResultSetToHotelVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_HOTEL");
		Integer pCantidadEstrellas = resultSet.getInt("CANTIDAD_ESTRELLAS");
		Boolean conPermisos = resultSet.getBoolean("CON_PERMISOS");
		Long pIdOp = resultSet.getLong("ID_OPERADOR");
		ArrayList<HabitacionHoteleraVO> pHabitaciones = new ArrayList<>();
		
		HotelVO objRetorno = new HotelVO(id, pHabitaciones, pCantidadEstrellas, conPermisos, pIdOp);
		return objRetorno;
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
}
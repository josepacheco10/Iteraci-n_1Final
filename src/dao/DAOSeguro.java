package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.SeguroArrendamientoVO;

/**
 * Clase DAO que se conecta la base de datos usando JDBC para resolver los requerimientos de la aplicacion.
 * @author Grupo C-13
 */
public class DAOSeguro {

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
	
	public DAOSeguro(){
		resources = new ArrayList<>();
	}
	
	//----------------------------------------------------
	// Metodos Arquitectura REST: 
	//----------------------------------------------------
	
	public SeguroArrendamientoVO getSeguroVivienda (Long idVivienda) throws SQLException, Exception {
		
		SeguroArrendamientoVO objetoRetorno = null;

		String sql = String.format("SELECT * FROM %1$s.SEGUROSARRENDAMIENTO WHERE ID_VIVIENDAESP = %2$d", USUARIO, idVivienda); 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		
		resources.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			objetoRetorno = convertResultSetToSeguroVO(rs);
		}
		return objetoRetorno;	
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
	
	public SeguroArrendamientoVO convertResultSetToSeguroVO (ResultSet resultSet) throws SQLException, Exception {
		
		Long id = resultSet.getLong("ID_SEGUROARRENDAMIENTO");
		Double pCosto = resultSet.getDouble("COSTO_SEGURO");
		String pCaract = resultSet.getString("CARACT_SEGURO");
		Long pIdViviendaEsp = resultSet.getLong("ID_VIVIENDAESP");

		SeguroArrendamientoVO objRetorno = new SeguroArrendamientoVO(id, pCosto, pCaract, pIdViviendaEsp);
		return objRetorno;
	}
}
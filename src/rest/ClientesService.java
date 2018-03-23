package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import tm.AlohAndesTransactionManager;
import vos.ClienteVO;
import vos.RFC2;

/**
 * Clase que expone servicios REST con ruta base:
 * @author Grupo C-13
 */
@Path("clientes")
public class ClientesService {

	//-----------------------------------------------
	// Atributos
	//-----------------------------------------------
	
	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	//-----------------------------------------------
	// Inicializacion
	//-----------------------------------------------
	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}


	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}

	//-----------------------------------------------
	// Servicios REST de la entidad
	//-----------------------------------------------
	
	/**
	 * Metodo GET que trae a todos los clientes en la Base de datos. <br/>
	 * @return	Response Status 200 - JSON que contiene a todos los clientes que estan en la Base de Datos <br/>
	 * 			Response Status 500 - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClientes () {
		
		try {
			
			AlohAndesTransactionManager transactManager = new AlohAndesTransactionManager(getPath());
			
			List<ClienteVO> clientesBD;
			clientesBD = transactManager.getAllClientes();
			return Response.status(200).entity(clientesBD).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
}
	

	@GET
	@Path("RFC2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response RFC22()
	{
		try{
			AlohAndesTransactionManager transactManager= new AlohAndesTransactionManager( getPath( ) );
			List<RFC2> DB;
			DB = transactManager.get20Ofertas();
			return Response.status(200).entity(DB).build();
		}
	 
	catch (Exception e) {
		return Response.status(500).entity(doErrorMessage(e)).build();
						}
	}

	/**
	 * Metodo GET que trae al cliente en la Base de Datos con el ID dado por parametro <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene al cliente cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getClienteById( @PathParam( "id" ) Long id )
	{
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			
			ClienteVO clienteBuscado = tm.getCliente(id);
			return Response.status( 200 ).entity(clienteBuscado).build();			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}
package com.aap.rest.client;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.rest.exception.ErrorResponseConverter;
import com.aap.rest.exception.ErrorResponseConverter.ErrorCode;

/**
 * Clase que sirve como interfaz cliente para acceder a los web service para la gestion documentos CSB <br/>
 * <br/>
 * Esta clase requiere que exista un fichero "ConfiguracionRSConta.properties" en la aplicacion cliente en el que se
 * indique en el atributo "urlEntrada" la URL base para los web services. Por ejemplo podria ser algo asi: <br/>
 * <br/>
 * http://localhost:8080/Contabilidad/ws<br/>
 * <br/>
 * Tambien es necesario indicar en el fichero properties los atributos "username" y "password" con los que la aplicación
 * se va a autenticar en el web service.
 */
public class PersonaClientRS {
	
	private static final Logger log = LoggerFactory.getLogger(PersonaClientRS.class);

	private static final String ENCODING = "UTF-8";
	private static final int STATUS_NO_CONTENT = 204;
	private static final String AUTHORIZATION_PROPERTY = "Authorization";

	private ErrorResponseConverter error = new ErrorResponseConverter();
	
//	private final String url;
//	private final String usernameAndPassword;
	
	public PersonaClientRS() {
//		ResourceBundle bun = ResourceBundle.getBundle("ConfigPersonaRS");
//		url = bun.getString("urlEntrada");
//		usernameAndPassword = bun.getString("username") + ":" + bun.getString("password");
	}

	/**
	 * Metodo para registrar un documento CSB asociado a una empresa y un origen de datos.
	 * 
	 * @param nombreDocumento
	 *            Nombre del documento CSB a almacenar.
	 * @param fechaPago
	 *            Fecha que se utilizara como los campos "Fecha de envio" y "Fecha de emision" del CSB.
	 * @param ordenante
	 *            Datos del ordenante de la transferencia.
	 * @param beneficiarios
	 *            Lista de beneficiarios de la transferencia.
	 * @param idEmpresa
	 *            Identificador de la empresa en la que se esta registrando el documento CSB
	 * @param nombreOrigen
	 *            Cadena con el nombre del origen de datos al que se va a asociar el documento CSB. Ver tabla Origen en
	 *            CONTA.
	 * @return Identificador del documento CSB almacenado.
	 */
	public VerificationResult verificarCredenciales(String assertion) {
		try {
			String audience = "http://localhost:8080";

			Client client = ClientBuilder.newClient();
			String url = "https://verifier.login.persona.org/verify";
//			url = url + "?assertion=" + URLEncoder.encode(assertion, ENCODING) + "&audience=" + URLEncoder.encode(audience, ENCODING) ;
			WebTarget userTarget = client.target(url);
//			String encodeObject = Base64.encodeBytes(usernameAndPassword.getBytes());

			Assertion assertionObject = new Assertion();
			assertionObject.setAssertion(assertion);
			assertionObject.setAudience(audience);
			
			VerificationResult resultado = userTarget.request(MediaType.APPLICATION_JSON_TYPE)
//			        .header(AUTHORIZATION_PROPERTY, encodeObject)
			        .post(Entity.entity(assertionObject, MediaType.APPLICATION_JSON_TYPE), VerificationResult.class);
		
			return resultado;
		} catch(BadRequestException e) {
			System.out.println(e);
//			error = e.getResponse().readEntity(ErrorResponseConverter.class);
		} catch(Exception e) {
			error = new ErrorResponseConverter(e.getMessage(), "Error no esperado", ErrorCode.ERR0R);
			log.error("Error no esperado llamando al webservice", e);
		}
		return null;
	}
	
	/**
	 * Metodo que se puede llamar despues de cada petición a los web services para ver los mensajes que ha devuelto el
	 * servidor, por ejemplo para ver los posibles errores de validacion.
	 * 
	 * @return Objeto que encapsula los posibles errores producidos en el servidor.
	 */
	public ErrorResponseConverter getError() {
		return error;
	}

}

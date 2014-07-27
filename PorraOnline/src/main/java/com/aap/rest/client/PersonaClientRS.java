package com.aap.rest.client;

import java.util.ResourceBundle;

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
 * Clase para llamar al servicio de comprobación de credenciales de Mozilla Persona
 *
 */
public class PersonaClientRS {
	
	private static final Logger log = LoggerFactory.getLogger(PersonaClientRS.class);

	private ErrorResponseConverter error = new ErrorResponseConverter();
		
	public PersonaClientRS() {

	}

	/**
	 * Metodo para verificar las credenciales proporcionadas por el usuario en los servidores de mozilla.
	 * @param assertion Assertion con los datos de identificacion
	 * @return Devuelve un objeto VerificationResult con los datos de la verificación, 
	 * null si ocurrió un error durante la verificación. 
	 */
	public VerificationResult verificarCredenciales(String assertion) {
		try {
			ResourceBundle bun 	= ResourceBundle.getBundle("persona");
			String audience = bun.getString("persona.audience");
			String url = bun.getString("persona.verification_url");

			Client client = ClientBuilder.newClient();
			WebTarget userTarget = client.target(url);

			Assertion assertionObject = new Assertion();
			assertionObject.setAssertion(assertion);
			assertionObject.setAudience(audience);
			
			VerificationResult resultado = userTarget.request(MediaType.APPLICATION_JSON_TYPE)
			        .post(Entity.entity(assertionObject, MediaType.APPLICATION_JSON_TYPE), VerificationResult.class);
		
			return resultado;
		} catch(BadRequestException e) {
			error = new ErrorResponseConverter(e.getMessage(), "Error no esperado", ErrorCode.ERR0R);
			log.error("Error de peticion", e);
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

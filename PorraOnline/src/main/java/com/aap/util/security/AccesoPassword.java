package com.aap.util.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aap.dto.Usuarios;
import com.aap.util.jsf.Contexts;

public class AccesoPassword {
	private static final Logger logger = LoggerFactory.getLogger(AccesoPassword.class);

	public static boolean existe_usuario(String login, String password)
			throws Exception {

		boolean valido = false;

		Session session = Contexts.getHibernateSession();
		Usuarios usu;
		login = login.trim();
		try{
			usu = (Usuarios)session.createCriteria(Usuarios.class)
											.add(Restrictions.eq("usu_username", login))
											.uniqueResult();
		} catch (NonUniqueResultException e) {
			Contexts.addErrorMessage("Username duplicado");
			return false;
		}
		if(usu != null) {
			String md5 = usu.getUsu_password();
			if(md5 != null) {
				valido = iguales(password, md5);
			}
		}

		return valido;

	}

	/**
	 * Obtiene el valor SHA-256 de una cadena de texto
	 * @param texto Cadena de la que obtener el SHA-256
	 * @return SHA-256 de texto
	 * @throws NoSuchAlgorithmException
	 */
	public static String obtenerSHA256(String texto) throws NoSuchAlgorithmException {
        String output="";
        if(texto != null) {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] digest = md.digest(texto.getBytes());
	        BigInteger number = new BigInteger(1, digest);
	        output = number.toString(16);
        }
        return output;
	}
	
	/**
	 * Comprueba si el SHA-256 de texto es igual a sha256 
	 * @param texto Valor a comprobar
	 * @param sha256 Valor de comprobación
	 * @return True si son iguales
	 * @throws NoSuchAlgorithmException
	 */
    public static boolean iguales(String texto, String sha256) throws NoSuchAlgorithmException {
    	boolean iguales = false;
    	if(texto == null || sha256 == null) {
    		if(texto == null && sha256 == null) {
    			iguales = true;
    		}
    	} else {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] digest = md.digest(texto.getBytes());
	        BigInteger supplied = new BigInteger(1, digest);
	        BigInteger checkval = new BigInteger(sha256, 16);
	        
	        iguales = supplied.equals(checkval);
    	}
        
        return iguales;
    }
}

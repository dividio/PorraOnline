package com.aap.util.jsf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de utilidades varias, para utilizar principalmente desde 
 * expresiones EL dentro páginas XHTML de JSF.
 * @author cica2
 *
 */
public final class FuncionesJSF {
	
	private static final long serialVersionUID = 8557453938331430148L;
	private static final Logger log = LoggerFactory.getLogger(FuncionesJSF.class);

	private FuncionesJSF() {
	}

	/**
	 * Función que busca si en una cadena hay alguna aparición de una subcadena, ignorando mayusculas y acentos.
	 * @param input Cadena en la que se quiere buscar.
	 * @param substring Subcadena a buscar.
	 * @return True si se encontró alguna aparición de la subcadena.
	 */
	public static boolean containsIgnoreCase(String input, String substring) {
        if (input == null) input = "";
        if (substring == null) substring = "";        
        String inputUC = input.toUpperCase().replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U');
        String substringUC = substring.toUpperCase().replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U');
        return indexOf(inputUC, substringUC) != -1;
    }
	
	/**
	 * Función que busca si en una cadena hay alguna aparición de una subcadena.
	 * @param input Cadena en la que se quiere buscar.
	 * @param substring Subcadena a buscar.
	 * @return True si se encontró alguna aparición de la subcadena.
	 */
	public static boolean containsIgnoreBooleanCase(String input, String substring) {
        if (input == null) input = "";
        if (substring == null) substring = "";        
        return input.equals(substring);
    }
	
	/**
	 * Función que obtiene la posición de una cadena dada dentro de otra.
	 * @param input Cadena en la que se busca el indice.
	 * @param substring Cadena a buscar.
	 * @return Posición de la subcadena, -1 si no se encontró.
	 */
	public static int indexOf(String input, String substring) {
        if (input == null) input = "";
        if (substring == null) substring = "";
        return input.indexOf(substring);
    }
	
	/**
	 * Función que compara dos cadenas de tipo fecha para ver si una está incluida en la otra. Esta
	 * función es útil para los filtros en los dataTable de RichFaces.
	 * @param input Cadena con formato yyyy-MM-dd.
	 * @param substring Subcadena en formato dd/MM/yyyy.
	 * @return True si la cadena contiene a la subcadena.
	 */
	public static boolean containsDate(String input, String substring) {
		String salida = input;
		if(! "".equals(input)){
			try {
				SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
				Date fecha = formatoEntrada.parse(input);
				SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
				salida = formatoSalida.format(fecha);
			} catch (ParseException e) {
				log.error("Error parseando una fecha.", e);
			}
		}
		return containsIgnoreCase(salida, substring);
    }
	
	/**
	 * Función que comprueba si la primera fecha es anterior o igual a la segunda
	 * @param input Fecha a comprobar en forma de String
	 * @param hasta Fecha límite hasta en forma de Date
	 * @return True si la primera es anterior o igual a la segunda
	 */
	public static boolean untilDate(String input, Date hasta) {
		boolean result = false;
		if(hasta == null) 
			return true;
		if(! "".equals(input)){
			try {
				SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
				Date fecha = formatoEntrada.parse(input);
				if(fecha == null)
					return false;
				if(fecha.compareTo(hasta) <= 0)
					result = true;
			} catch (ParseException e) {
				log.error("Error parseando una fecha.", e);
			}
		}	
		return result;
	}
	
	/**
	 * Función que comprueba si dos cadenas son iguales ignorando mayúsculas y acentos
	 * @param input Cadena a comparar.
	 * @param substring Subcadena a comparar.
	 * @return True si las cadenas son iguales.
	 */
	public static boolean equalsIgnoreCase(String input, String substring) {
		if (input == null) input = "";
		if (substring == null) substring = ""; 
		if ("".equals(substring)) return true;
        String inputUC = input.toUpperCase().replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U');
        String substringUC = substring.toUpperCase().replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U');
        return inputUC.equals(substringUC);
	}
	
	/**
	 * Función que comprueba si una primera cantidad es inferior o igual a una segunda.
	 * El límite hasta que puede ser introducido por el usuario se recoge como String
	 * ya que si el input de entrada fuese Double, la ausencia de valor llegaría como
	 * el valor 0.0, lo que condicionaría el resultado de filtro. Como String se permite
	 * tanto el uso del 0 como la ausencia de valor.
	 * @param input Cantidad numérica en forma de String
	 * @param hasta Número límite en forma de String
	 * @return True si la primera cantidad es inferior o igual a la segunda.
	 */
	public static boolean untilNumber(String input, String hasta) {
		boolean result = false;
		if(hasta == null) {
			return true;
		}
		if("".equals(hasta)) {
			return true;
		}
		if(!"".equals(input)){
			Double numero = new Double(input);
			Double limite = new Double(hasta);
			if(numero <= limite) {
				result = true;
			}
		}	
		return result;
	}
	/**
	 * Función de utilidad que concatena dos cadenas.
	 * @param a Cadena que aparecerá al principio.
	 * @param b Cadena que aparecerá al final.
	 * @return Cadenas concatenadas (a + b).
	 */
	public static String concat(String a, String b) {
		return a + b;
	}
	
	/**
	 * Función que limita cadenas de texto demasiado largas.
	 * @param cadena Cadena que debe limitarse
	 * @param limite Limite de caracteres
	 * @return Cadena limitada
	 */
	public static String limitaCadena(String cadena, int limite) {
		if(cadena != null) {
			if(cadena.length() > limite) {
				return cadena.substring(0, limite - 4) + "...";
			}
		}
		return cadena;
	}
	
	/**
	 * Indica si hay almacenados mensajes de error en el contexto JSF.
	 * @return True si se encontraron errores, false en caso contrario.
	 */
	public static boolean hayErrores() {
		Severity severity = Contexts.getFacesContext().getMaximumSeverity();
		if(severity != null && severity.equals(FacesMessage.SEVERITY_ERROR)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Indica si hay almacenados mensajes de warning en el contexto JSF.
	 * @return True si se encontraron errores, false en caso contrario.
	 */
	public static boolean hayWarnings() {
		Severity severity = Contexts.getFacesContext().getMaximumSeverity();
		if(severity != null && severity.equals(FacesMessage.SEVERITY_WARN)) {
			return true;
		}
		return false;
	}

}

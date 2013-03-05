package com.aap.util.jsf;

import java.util.Iterator;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

/**
 * Convenience class for contexts manipulation.
 */
public class Contexts {

	// not to be instantiated
	private Contexts() {
	}

	/**
	 * Obtiene el session actual de Hibernate.
	 * 
	 * @return EntityManager
	 */
	public static Session getHibernateSession() {
		return (Session) ((HttpServletRequest) FacesContext
		        .getCurrentInstance().getExternalContext().getRequest())
		        .getAttribute("session");
	}

	/**
	 * Obtiene el session HTTP.
	 * 
	 * @param create
	 *            si es cierto, la sesion sera creada si esta no existia.
	 * @return HttpSession
	 */
	public static HttpSession getSession(boolean create) {
		return (HttpSession) FacesContext.getCurrentInstance()
		        .getExternalContext().getSession(create);

	}

	/**
	 * Obtiene o crea el session HTTP.
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
		        .getExternalContext().getSession(true);
	}

	/**
	 * Obtiene el FacesContext.
	 * 
	 * @return FacesContext
	 */
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * Get mapped bean determined by its name
	 * 
	 * @param name
	 *            the name of the mapped bean
	 * @return Object
	 */
	public static Object getBean(String name) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ELContext elContext = fc.getELContext();

		Object bean = elContext.getELResolver().getValue(elContext, null, name);

		return bean;
	}

	/**
	 * Get a parameter from the http request
	 * 
	 * @param name
	 *            the name of the parameter
	 * @return String parameter value
	 */
	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
		        .getRequestParameterMap().get(name);
	}

	/**
	 * Obtiene un atributo de la sesion HTTP.
	 * 
	 * @param name
	 *            El nombre del atributo.
	 * @return Object con el valor del atributo.
	 */
	public static Object getSessionAttribute(String name) {
		return getSession().getAttribute(name);
	}

	/**
	 * Añade un mensaje de informacion al contexto JSF para ser mostrado en los
	 * componentes messages
	 * 
	 * @param message
	 *            Mensaje a mostrar
	 */
	public static void addInfoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
		        new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}

	/**
	 * Añade un mensaje de error al contexto JSF para ser mostrado en los
	 * componentes messages
	 * 
	 * @param message
	 *            Mensaje a mostrar
	 */
	public static void addErrorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
		        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
	}

	/**
	 * Añade un mensaje de warning al contexto JSF para ser mostrado en los
	 * componentes messages
	 * 
	 * @param message
	 *            Mensaje a mostrar
	 */
	public static void addWarningMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
		        new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
	}

	/**
	 * Elimina del FacesContext todos los mensajes que hayan sido introducidos.
	 */
	public static void clearAllMessages() {
		Iterator<FacesMessage> it = Contexts.getFacesContext().getMessages();
		while (it.hasNext()) {
			it.remove();
		}
	}

	/**
	 * Elimina del FacesContext todos los mensajes de informacion que hayan sido
	 * introducidos.
	 */
	public static void clearInfoMessages() {
		Iterator<FacesMessage> it = Contexts.getFacesContext().getMessages();
		while (it.hasNext()) {
			FacesMessage msg = it.next();
			if (msg.getSeverity().equals(FacesMessage.SEVERITY_INFO)) {
				it.remove();
			}
		}
	}

	/**
	 * Elimina del FacesContext todos los mensajes de warning que hayan sido
	 * introducidos.
	 */
	public static void clearWarningMessages() {
		Iterator<FacesMessage> it = Contexts.getFacesContext().getMessages();
		while (it.hasNext()) {
			FacesMessage msg = it.next();
			if (msg.getSeverity().equals(FacesMessage.SEVERITY_WARN)) {
				it.remove();
			}
		}
	}

	/**
	 * Elimina del FacesContext todos los mensajes de error que hayan sido
	 * introducidos.
	 */
	public static void clearErrorMessages() {
		Iterator<FacesMessage> it = Contexts.getFacesContext().getMessages();
		while (it.hasNext()) {
			FacesMessage msg = it.next();
			if (msg.getSeverity().equals(FacesMessage.SEVERITY_ERROR)) {
				it.remove();
			}
		}
	}

	/**
	 * Activa un flag en el request para que cuando acabe el ciclo JSF se haga
	 * un rollback de la transaccion de Hibernate. Nada de lo que se haya
	 * modificado en la base de datos durante el request será guardado.
	 */
	public static void abortarTransaccion() {
		HttpServletRequest request = (HttpServletRequest) FacesContext
		        .getCurrentInstance().getExternalContext().getRequest();
		request.setAttribute("abortarTransaccion", Boolean.valueOf(true));
	}

	/**
	 * Indica si se ha activado el flag que aborta la transaccion de Hibernate
	 * cuando finalice el ciclo de JSF.
	 * 
	 * @return
	 */
	public static boolean transaccionAbortada() {
		boolean abortar = false;
		HttpServletRequest request = (HttpServletRequest) FacesContext
		        .getCurrentInstance().getExternalContext().getRequest();
		Boolean abortarTransaccion = (Boolean) request
		        .getAttribute("abortarTransaccion");
		if (abortarTransaccion != null && abortarTransaccion) {
			abortar = true;
		}
		return abortar;
	}
}

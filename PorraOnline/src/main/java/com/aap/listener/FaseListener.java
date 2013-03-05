package com.aap.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase sirve para almacenar en el historial de navegación la última pagina visitada.
 * 
 * Además imprime en el log información de la fase JSF que se está ejecutando en cada momento.
 * 
 * @author cica2
 *
 */
public class FaseListener implements PhaseListener {

	private static final long serialVersionUID = -627974292960524266L;
	private static final Logger log = LoggerFactory.getLogger(FaseListener.class);

	public void beforePhase(PhaseEvent pe) {
		log.info("before - {}", pe.getPhaseId().toString());
	}

	public void afterPhase(PhaseEvent pe) {
		log.info("after - {}", pe.getPhaseId().toString());
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}

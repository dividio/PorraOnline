package com.aap.util;

import com.aap.dto.Partidas;

public class ApplicationUtil {
	public static boolean cambioPartida(Partidas partida, Long idPartida) {
		return idPartida != null && 
				(partida == null || 
				partida.getPa_id() == null || 
				partida.getPa_id().compareTo(idPartida) != 0);
	}
}

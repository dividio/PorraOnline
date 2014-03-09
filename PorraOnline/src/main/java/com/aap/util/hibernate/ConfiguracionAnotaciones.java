package com.aap.util.hibernate;

import org.hibernate.cfg.Configuration;

import com.aap.dto.Bonificaciones;
import com.aap.dto.Competidores;
import com.aap.dto.Eventos;
import com.aap.dto.Mensajes;
import com.aap.dto.Noticias;
import com.aap.dto.Partidas;
import com.aap.dto.Penalizaciones;
import com.aap.dto.Pronosticos;
import com.aap.dto.PuntosPosicion;
import com.aap.dto.Resultados;
import com.aap.dto.Roles;
import com.aap.dto.Usuarios;

public class ConfiguracionAnotaciones {

	public static void cargaAnotaciones(Configuration configuration) {
		configuration.addAnnotatedClass(Roles.class);
		configuration.addAnnotatedClass(Usuarios.class);

		configuration.addAnnotatedClass(Competidores.class);
		configuration.addAnnotatedClass(Eventos.class);
		configuration.addAnnotatedClass(Mensajes.class);
		configuration.addAnnotatedClass(Noticias.class);
		configuration.addAnnotatedClass(Partidas.class);
		configuration.addAnnotatedClass(Pronosticos.class);
		configuration.addAnnotatedClass(PuntosPosicion.class);
		configuration.addAnnotatedClass(Resultados.class);
		configuration.addAnnotatedClass(Bonificaciones.class);
		configuration.addAnnotatedClass(Penalizaciones.class);
	}
}

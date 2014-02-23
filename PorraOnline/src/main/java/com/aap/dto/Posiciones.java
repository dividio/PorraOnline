package com.aap.dto;

import java.io.Serializable;

public class Posiciones implements Serializable {

    private static final long serialVersionUID = 5299002500465485724L;

	private Long idPartida;
	private Long idEvento;
	private Long usu_id;
	private String usu_username;
	private Long posicion;
	private Long puntos;
	
	public Long getIdPartida() {
		return idPartida;
	}
	public void setIdPartida(Long idPartida) {
		this.idPartida = idPartida;
	}
	public Long getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}
	public Long getUsu_id() {
		return usu_id;
	}
	public void setUsu_id(Long usu_id) {
		this.usu_id = usu_id;
	}
	public String getUsu_username() {
		return usu_username;
	}
	public void setUsu_username(String usu_username) {
		this.usu_username = usu_username;
	}
	public Long getPosicion() {
		return posicion;
	}
	public void setPosicion(Long posicion) {
		this.posicion = posicion;
	}
	public Long getPuntos() {
		return puntos;
	}
	public void setPuntos(Long puntos) {
		this.puntos = puntos;
	}
	

}
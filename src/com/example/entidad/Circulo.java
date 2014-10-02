package com.example.entidad;

public class Circulo {
	private String ciudad;
	private String encargado;
	private String direccion;
	private String telefono;

	public Circulo(String ciudad, String encargado, String direccion,
			String telefono) {
		super();
		this.ciudad = ciudad;
		this.encargado = encargado;
		this.direccion = direccion;
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getEncargado() {
		return encargado;
	}

	public void setEncargado(String encargado) {
		this.encargado = encargado;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}

package com.example.entidad;

public class Cualidades {

		private String id;
		private String cualidad;
		private String mes;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCualidad() {
			return cualidad;
		}
		public void setCualidad(String cualidad) {
			this.cualidad = cualidad;
		}
		public String getMes() {
			return mes;
		}
		public void setMes(String mes) {
			this.mes = mes;
		}
		
		public String toString(){
			return this.mes+" "+":"+" "+cualidad;
		}	

}

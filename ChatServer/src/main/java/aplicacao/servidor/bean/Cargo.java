package aplicacao.servidor.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cargo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "IdCargo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(columnDefinition = "VARCHAR(100)",name = "CARGO")
	private String cargo;
	
	public Cargo() {
		
	}
	
	
	public Cargo(Integer id, String cargo) {
		super();
		this.id = id;
		this.cargo = cargo;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	
	
	

}

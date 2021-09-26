package aplicacao.servidor.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UsuarioBD implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "IdUsuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(columnDefinition = "VARCHAR(80)",name = "USUARIO")
	private String usuario;
	@Column(columnDefinition = "VARCHAR(10)",name = "SENHA")
	private String senha;
	@Column(columnDefinition = "VARCHAR(100)",name = "NOME")
	private String nome;
	@Column(columnDefinition = "VARCHAR(15)",name = "CPF") 
	private String cpf;
	@Column(columnDefinition = "VARCHAR(20)",name = "TELEFONE")
	private String telefone;
	@Column(columnDefinition = "VARCHAR(100)",name = "EMAIL")
	private String email;
	@Column(columnDefinition = "VARCHAR(50)",name = "PAIS")
	private String pais;
	@Column(columnDefinition = "CHAR(2)",name = "ESTADO")
	private String estado;
	@Column(columnDefinition = "VARCHAR(50)",name = "CIDADE")
	private String cidade;
	@Column(columnDefinition = "VARCHAR(80)",name = "RUA")
	private String rua;
	@ManyToOne
	@JoinColumn(name="IdCargo")
	private Cargo cargo;
	
	public UsuarioBD() {
	}
	


	public UsuarioBD(Integer id, String usuario, String senha, String nome, String cpf, String telefone, String email,
			String pais, String estado, String cidade, String rua, Cargo cargo) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.senha = senha;
		this.nome = nome;
		this.cpf = cpf;
		this.telefone = telefone;
		this.email = email;
		this.pais = pais;
		this.estado = estado;
		this.cidade = cidade;
		this.rua = rua;
		this.cargo = cargo;
	}


	public UsuarioBD(Usuario usuario) {
		this.id = usuario.getId();
		this.usuario = usuario.getUsuario();
		this.senha = usuario.getSenha();
	}

	public UsuarioBD(Integer id, String usuario, String senha) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.senha = senha;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}


	public Usuario convertUsuarioBDtoUsuario() {
		return new Usuario(id, this.usuario, senha);
	}
	
	
	
	
	
}

package aplicacao.servidor.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MensagemBD implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "IdMensagem ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(columnDefinition = "VARCHAR(1000)", name = "MENSAGEM")
	private String mensagem;
	@Column(columnDefinition = "DATETIME",name = "DATAHORA")
	private LocalDateTime dataHora;
	@ManyToOne
	@JoinColumn(name="Usuario_IdRementente")
	private UsuarioBD usuario;
	@ManyToOne
	@JoinColumn(name="Usuario_IdDestinatario")
	private UsuarioBD destinatario;
	
	
	public MensagemBD(String mensagem, UsuarioBD nomeUsuario, UsuarioBD destinatario) {
		this.mensagem = mensagem;
		this.destinatario = destinatario;
		this.usuario = nomeUsuario;
		
	}
	public MensagemBD(Mensagem mensagem) {
		try {
			this.id = mensagem.getId();
			this.mensagem = mensagem.getMensagem();
			this.usuario = new UsuarioBD(mensagem.getUsuario());
			this.dataHora = mensagem.getDateTime();
			this.destinatario = new UsuarioBD(mensagem.getDestinatario());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MensagemBD() {
		
	}
	public String getMensagem() {
		return mensagem;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeUsuario() {
		return usuario.getUsuario();
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}


	public UsuarioBD getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioBD nomeUsuario) {
		this.usuario = nomeUsuario;
	}

	public UsuarioBD getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(UsuarioBD destinatario) {
		this.destinatario = destinatario;
	}



	public LocalDateTime getDateTime() {
		return dataHora;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dataHora = dateTime;
	}



}

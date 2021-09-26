package aplicacao.servidor.bean;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Mensagem implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String mensagem;
	private Status status;
	private Usuario mensagemUsuario;
	private LocalDateTime dateTime;
	private Usuario usuario;
	private Usuario destinatario;
	
	
	public Mensagem(String mensagem, Usuario nomeUsuario, Usuario destinatario, Status status) {
		this.mensagem = mensagem;
		this.destinatario = destinatario;
		this.usuario = nomeUsuario;
		this.status = status;
		
	}
	
	public Mensagem() {
		
	}
	public String getMensagem() {
		return mensagem;
	}

	
	public Usuario getMensagemUsuario() {
		return mensagemUsuario;
	}

	public void setMensagemUsuario(Usuario mensagemUsuario) {
		this.mensagemUsuario = mensagemUsuario;
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

	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario nomeUsuario) {
		this.usuario = nomeUsuario;
	}

	public Usuario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}



	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}



	public enum Status {
		CONECTAR, DESCONECTAR, ENVIAR_UM, ENVIAR_TODOS, GETONLINE,CONFIRMADO,NEGADO,HORA
	}
}

package aplicacao.servidor;

import aplicacao.servidor.bean.Usuario;

public class DadosConexão {

	
	private Usuario usuario;
	private String host;
	private Integer port;
	
	public DadosConexão() {
		
	}
	
	
	public DadosConexão(Usuario usuario, Usuario destinatario, String host, Integer port) {
		this.usuario = usuario;
		this.host = host;
		this.port = port;
	}


	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
	
}

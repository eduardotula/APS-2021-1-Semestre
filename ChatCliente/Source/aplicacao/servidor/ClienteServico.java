package aplicacao.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import aplicacao.ClienteFrame;
import aplicacao.servidor.bean.Mensagem;
import aplicacao.servidor.bean.Usuario;


public class ClienteServico {
	
	private Socket socket;
	private ObjectOutputStream out;
	
	
	public ClienteServico() {
		
	}
	
	
	public void enviar(Mensagem ms) throws IOException {
			out.writeObject(ms);
	}

	
	public void desconectar() {
		Mensagem ms = new Mensagem();
		ms.setUsuario(ClienteFrame.dadosSessao.getUsuario());
		System.out.println(ClienteFrame.dadosSessao.getUsuario());
		ms.setMensagemUsuario(ClienteFrame.dadosSessao.getUsuario());
		ms.setStatus(Mensagem.Status.DESCONECTAR);
		System.out.println("Desconectado");
		try {
			out.writeObject(ms);
			socket.close();
			out.close();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void loginValido(Usuario usuario) {
		ClienteFrame.dadosSessao.setUsuario(usuario);
		ClienteFrame.setStatus(true, ClienteFrame.dadosSessao.getUsuario().getUsuario());
		getTodosOnline();
	}
	public void loginInvalido() {
		JOptionPane.showMessageDialog(null, "Usuario ou Senha inválidos");
		ClienteFrame.frameConexao();
	}
	public void receber(Mensagem ms) {
		ms.setMensagem(String.format("%s", ms.getMensagem()));
		ClienteFrame.appendMsg(ms,ms.getUsuario());
		ms.setDestinatario(ms.getUsuario());
		ms.setDateTime(LocalDateTime.now());
		ms.setUsuario(ClienteFrame.dadosSessao.getUsuario());
		ms.setStatus(Mensagem.Status.HORA);
		try {
			enviar(ms);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void receberTempo(Mensagem ms) {
		ms.setMensagem("Mensagem recebida: "+ms.getDateTime().toLocalTime());
		ClienteFrame.appendTempo(ms);
	}
	public void addOnline(Mensagem ms) {
		ClienteFrame.listaModel.addElement(ms.getMensagemUsuario());
	}
	public void removeOnline(Mensagem ms) {
		ms.setMensagem(String.format("%s está Desconectado",ms.getNomeUsuario()));
		ClienteFrame.appendMsg(ms,ms.getUsuario());
		ClienteFrame.listaModel.removeElement(ms.getMensagemUsuario());
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public ObjectOutputStream getOut() {
		return out;
	}
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	/**Envia uma requição para o servidor para obter todos os Usuarios online*/
	public void getTodosOnline() {
		Mensagem ms = new Mensagem();
		ms.setStatus(Mensagem.Status.GETONLINE);
		ms.setUsuario(ClienteFrame.dadosSessao.getUsuario());
		ClienteFrame.setChatEnable(true);
		try {
			enviar(ms);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void login(DadosConexão dados) throws IOException {
		Mensagem ms = new Mensagem();
		ms.setStatus(Mensagem.Status.CONECTAR);
		ms.setUsuario(dados.getUsuario());
		ms.setMensagemUsuario(dados.getUsuario());
		enviar(ms);
	}
	/**No momento que a conexão for iniciada uma mensagem será iserida uma mensagem na aba do chat*/
	public void iniciarConexao(DadosConexão dados) throws Exception{
		socket = new Socket(dados.getHost(), dados.getPort());
		out = new ObjectOutputStream(socket.getOutputStream());
		new ListenerSocket(socket).start();
		System.out.println("Conexão Realizada com Sucesso");
	}


	public class ListenerSocket extends Thread implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ObjectInputStream input;
		private Socket socket;
		
		public ListenerSocket(Socket socket) {
			try {
				this.socket = socket;
				this.input = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			Mensagem mensagem = null;
			try {
				while(socket.isClosed() == false && (mensagem = (Mensagem) input.readObject()) != null) {
					Mensagem.Status status = mensagem.getStatus(); //Tipo de Mensagem Recebida
					System.out.println("Cliente recebe mensagem do tipo " + status);
					System.out.println("Cliente Recebe mensagem de " + mensagem.getDestinatario());
					System.out.println("Cliente REcebe mensagem nome " + mensagem.getMensagemUsuario());
					if(status.equals(Mensagem.Status.CONECTAR)) { //Checa se o tipo da Mensagem é uma nova Conexão
						addOnline(mensagem);
					}else if(status.equals(Mensagem.Status.ENVIAR_UM)) { //Checa se o tipo da Mensagem é uma Mensagem Recebida
						receber(mensagem);
					}else if(status.equals(Mensagem.Status.HORA)) {
						receberTempo(mensagem);
					}else if(status.equals(Mensagem.Status.CONFIRMADO)) {
						loginValido(mensagem.getMensagemUsuario());
					}else if(status.equals(Mensagem.Status.NEGADO)) {
						loginInvalido();
					}else if(status.equals(Mensagem.Status.ENVIAR_TODOS)) {
						receber(mensagem);
					}else if(status.equals(Mensagem.Status.DESCONECTAR)) {
						removeOnline(mensagem);
					}else if(status.equals(Mensagem.Status.GETONLINE)) {
						addOnline(mensagem);
					}
					
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

	}

}

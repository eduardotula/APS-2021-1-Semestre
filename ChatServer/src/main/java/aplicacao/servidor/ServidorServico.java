package aplicacao.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import aplicacao.dao.ServidorDAO;
import aplicacao.gui.ServidorFrame;
import aplicacao.servidor.bean.Mensagem;
import aplicacao.servidor.bean.MensagemBD;
import aplicacao.servidor.bean.Usuario;


public class ServidorServico extends Thread{
	
	private ServerSocket svSocket;
	private static HashMap<Usuario, ObjectOutputStream> mapOnline = new HashMap<Usuario, ObjectOutputStream>();
	
	//Inicia o Servidor
	public ServidorServico(Integer port) {
		
		try {
			svSocket = new ServerSocket(port);
			System.out.println("Servidor Iniciado");
			JOptionPane.showMessageDialog(null, "Servidor Iniciado","Sucesso",JOptionPane.PLAIN_MESSAGE,new ImageIcon(getClass().getResource("/sucesso.png")));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao iniciar Servidor","Erro",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao iniciar Servidor","Erro",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		while(true) {
			try {
				Socket socket = svSocket.accept();
				new ListenerSocket(socket).start();
				System.out.println("Cliente Conectado");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//Indica para o servidor que o um Usuario fez a Conexão
	public static void conectar(ObjectOutputStream output, Mensagem mensagem) {
		try {
			if(mapOnline.get(mensagem.getMensagemUsuario()) == null && mensagem.getMensagemUsuario() != null) {
				System.out.println(mensagem.getMensagemUsuario().getUsuario());
				System.out.println(mensagem.getNomeUsuario());
				System.out.println(mensagem.getDestinatario());
				Usuario usuarioBanco = ServidorDAO.findUsuarioByName(mensagem.getMensagemUsuario().getUsuario()).convertUsuarioBDtoUsuario();
				Usuario usuario = mensagem.getMensagemUsuario();
				if(usuarioBanco != null && !mapOnline.containsKey(usuarioBanco)) {
					if(usuarioBanco.getSenha().equals(usuario.getSenha())) {
						mapOnline.put(usuarioBanco, output);
						System.out.println("usuario banco " + usuarioBanco);
						ServidorFrame.listaOnlineModel.addElement(usuarioBanco);
						Mensagem ms = new Mensagem(null, null, usuarioBanco, Mensagem.Status.CONFIRMADO);
						ms.setMensagemUsuario(usuarioBanco);
						enviar(ms);
						ms = new Mensagem(null, usuarioBanco, usuarioBanco, null);
						ms.setMensagemUsuario(usuarioBanco);
						sendOnline(ms);
					}else {
						Mensagem ms = new Mensagem(null, null, mensagem.getUsuario(), Mensagem.Status.NEGADO);
						enviar(ms, output);
					}
				}else {
					Mensagem ms = new Mensagem(null, null, mensagem.getUsuario(), Mensagem.Status.NEGADO);
					enviar(ms, output);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem ms = new Mensagem(null, null, mensagem.getUsuario(), Mensagem.Status.NEGADO);
			enviar(ms, output);
		}
		
	}
	//Encaminha a Mensagem para o destinatario desejado
	public static void enviar(Mensagem mensagem) {
		try {
			ObjectOutputStream destinatario = mapOnline.get(mensagem.getDestinatario());
			System.out.println("Envia conteudo " + mensagem.getMensagem());
			System.out.println("Destinatario " + mensagem.getDestinatario());
			System.out.println("Status " + mensagem.getStatus());
			if(destinatario != null) {
				destinatario.writeObject(mensagem);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Encaminha a Mensagem para o destinatario desejado
	public static void enviar(Mensagem mensagem, ObjectOutputStream output) {
		try {
			System.out.println("Envia conteudo " + mensagem.getMensagem());
			System.out.println("Destinatario " + mensagem.getDestinatario().getUsuario());
			//System.out.println("Usuario " + mensagem.getNomeUsuario());
			System.out.println("Status " + mensagem.getStatus());
			if(output != null) {
				output.writeObject(mensagem);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void enviarTodos(Mensagem mensagem){
		for(Map.Entry<Usuario, ObjectOutputStream> kv : mapOnline.entrySet()) {
			if(!kv.getKey().equals(mensagem.getUsuario())){
				try {
					System.out.println("Enviar tods " + mensagem.getStatus());
					kv.getValue().writeObject(mensagem);
					ServidorDAO.criarMensagem(new MensagemBD(mensagem));

				} catch (IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	public static void enviarTodosStatusOnline(Mensagem mensagem) {
		for(Map.Entry<Usuario, ObjectOutputStream> kv : mapOnline.entrySet()) {
			if(!kv.getKey().equals(mensagem.getUsuario())) {
				try {
					System.out.println("Enviar tods " + mensagem.getStatus());
					kv.getValue().writeObject(mensagem);
				} catch (IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void desconectar(Mensagem mensagem, Socket socket) {
		Mensagem ms = new Mensagem(null,
				mensagem.getMensagemUsuario(),
				null, Mensagem.Status.DESCONECTAR);
		System.out.println(ms.getUsuario().getUsuario());
		ms.setMensagemUsuario(mensagem.getMensagemUsuario());
		enviarTodosStatusOnline(ms);
		try {
			mapOnline.get(mensagem.getMensagemUsuario());
			socket.close();
			mapOnline.remove(mensagem.getMensagemUsuario());
			ServidorFrame.listaOnlineModel.removeElement(mensagem.getMensagemUsuario());
			System.out.println("Usuario " + mensagem.getMensagemUsuario().getUsuario() + " Desconectado");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendAllOnline(Mensagem mensagem) {
		for(Map.Entry<Usuario, ObjectOutputStream> o : mapOnline.entrySet()) {
			if(!o.getKey().equals(mensagem.getUsuario())) {
				Mensagem ms = new Mensagem();
				ms.setStatus(Mensagem.Status.GETONLINE);
				ms.setMensagemUsuario(o.getKey());
				ms.setDestinatario(mensagem.getUsuario());
				System.out.println(o.getKey().getUsuario() + " online");
				enviar(ms);
			}
		}
	}
	public static void sendOnline(Mensagem mensagem) {
		mensagem.setStatus(Mensagem.Status.CONECTAR);
		enviarTodosStatusOnline(mensagem);
	}
	
	public class ListenerSocket extends Thread implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ObjectInputStream input;
		private ObjectOutputStream output;
		private Socket socket;
		
		public ListenerSocket(Socket socket) {
			try {
				this.socket = socket;
				this.input = new ObjectInputStream(socket.getInputStream());
				this.output = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			Mensagem mensagem = null;
			try {
				while(!socket.isClosed() && (mensagem = (Mensagem) input.readObject()) != null) {
					Mensagem.Status status = mensagem.getStatus(); //Tipo de Mensagem Recebida
					if(status.equals(Mensagem.Status.CONECTAR)) { //Checa se o tipo da Mensagem é uma nova Conexão
						ServidorServico.conectar(output, mensagem);
					}else if(status.equals(Mensagem.Status.ENVIAR_UM)) { //Checa se o tipo da Mensagem é uma Mensagem Recebida
						ServidorServico.enviar(mensagem);
						ServidorDAO.criarMensagem(new MensagemBD(mensagem));
					}else if(status.equals(Mensagem.Status.HORA)) {
						ServidorServico.enviar(mensagem);
					}else if(status.equals(Mensagem.Status.ENVIAR_TODOS)) {
						ServidorServico.enviarTodos(mensagem);
					}else if(status.equals(Mensagem.Status.DESCONECTAR)) {
						ServidorServico.desconectar(mensagem, socket);
						this.interrupt();
					}else if(status.equals(Mensagem.Status.GETONLINE)) {
						ServidorServico.sendAllOnline(mensagem);
					}
					
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		

	}

}

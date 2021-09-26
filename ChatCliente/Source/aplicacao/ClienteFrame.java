package aplicacao;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;

import aplicacao.model.UsuarioListModel;
import aplicacao.servidor.ClienteServico;
import aplicacao.servidor.DadosConexão;
import aplicacao.servidor.bean.Mensagem;
import aplicacao.servidor.bean.Usuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

public class ClienteFrame extends JFrame{
	/**
	 * 
	 */
	private static ClienteServico cs;
	public static DadosConexão dadosSessao;
	private final Color cortextField = new Color(64,68,75);
	private final Color corBackground = new Color(47,49,54);
	private final Color corTexto = new Color(189,190,193);
	public static UsuarioListModel listaModel = new UsuarioListModel();
		
	
	//Visuais
	private static final long serialVersionUID = 1L;
	private static JButton btnEnviar = new JButton("Enviar");
	private final static JTextField txtMensagem = new JTextField();
	private final JMenuBar menuBar = new JMenuBar();
	private final static JMenu mnConexao = new JMenu("Conex\u00E3o");
	private final static JMenuItem btnConectar = new JMenuItem("Conectar");
	private final static JMenuItem btnDesconectar = new JMenuItem("Desconectar");
	private final JScrollPane scrollPane = new JScrollPane();
	private final JLabel lblUsersOnline = new JLabel("Usuarios Online");
	private final JList<String> JlistOnline = new JList<String>();
	private final JPanel menuPanel = new JPanel();
	private static JTabbedChat abasChat;
	private final static JLabel lblStatus = new JLabel("Desconectado");
	private final JLabel lblUsuario = new JLabel("Usuario Atual");
	private final static JTextField txtUsuario = new JTextField();
	private final JButton btnAdicionar = new JButton();
	private final JButton btnFechar = new JButton();
	private final JButton btnEnviarTodos = new JButton("Enviar Todos");
	private final JButton btnFecharTodos = new JButton("Fechar Abas");
	private final JLabel content = new JLabel();

	public ClienteFrame() {
		super("Concord");
		createAndShowGUI();
		frameConexao();
		setListeners();
		setIcons();
	}
	
	public void createAndShowGUI() {
		setSize(670,700);
		abasChat = new JTabbedChat();
		setMinimumSize(new Dimension(670,700));
		abasChat.setAlpha(0.25f);
		setIcons();
		setContentPane(content);
		getContentPane().setLayout(new MigLayout("", "[79.00][][][259.00,grow][:151.00:200,grow]", "[][][][grow][][35][]"));
		getContentPane().setBackground(corBackground);
		setJMenuBar(menuBar);
		txtMensagem.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtMensagem.setColumns(10);
		txtUsuario.setColumns(10);
		menuBar.add(mnConexao);
		mnConexao.add(btnConectar);
		mnConexao.add(btnDesconectar);
		FlowLayout flowLayout = (FlowLayout) menuPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		JOptionPane.getRootFrame().setAlwaysOnTop(true);
		JlistOnline.setOpaque(false);
		getContentPane().add(btnAdicionar, "flowx,cell 4 1");
		getContentPane().add(abasChat, "cell 0 2 4 2,grow");
		getContentPane().add(scrollPane, "cell 4 3,grow");
		menuBar.add(menuPanel);
		txtMensagem.setBackground(cortextField);
		txtMensagem.setForeground(corTexto);
		txtMensagem.setBorder(null);
		lblUsersOnline.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblUsersOnline);
		JlistOnline.setModel(listaModel);
		scrollPane.setViewportView(JlistOnline);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		lblUsuario.setForeground(Color.WHITE);
		lblStatus.setForeground(Color.red);
		//menuPanel.setBackground(corBackground);
		menuPanel.add(lblUsuario);
		menuPanel.setOpaque(false);
		menuPanel.add(txtUsuario);
		menuBar.add(lblStatus);
		txtUsuario.setForeground(Color.white);
		txtUsuario.setBackground(cortextField);
		txtUsuario.setEditable(false);
		getContentPane().add(txtMensagem, "flowx,cell 0 5 4 1,grow");
		getContentPane().add(btnFechar, "cell 4 1");
		getContentPane().add(btnEnviar, "flowx,cell 4 5,alignx left,growy");
		getContentPane().add(btnEnviarTodos, "cell 4 5,growy");
		getContentPane().add(btnFecharTodos, "cell 4 1");
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setFocusable(false);
		setChatEnable(false);
	}
	
	public void setListeners() {
		
		// Esta Action detecta quando o tamanho da janela for alterado
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				abasChat.redrawRowsHight();
			}
		});
		// Esta Action fecha todas as abas abertas no chat
		btnFecharTodos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				abasChat.fecharAbas();
			}
		});
		// Esta Action envia uma mensagem para todos usuarios na lista de online
		btnEnviarTodos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!txtMensagem.getText().isEmpty()) {
					if(txtMensagem.getText().length() < 999){
					try {
						Mensagem ms = new Mensagem();
						ms.setUsuario(dadosSessao.getUsuario());
						ms.setMensagem(txtMensagem.getText());
						ms.setStatus(Mensagem.Status.ENVIAR_TODOS);
						ms.setDateTime(LocalDateTime.now());
						for(int i = 0;i<listaModel.getSize();i++) {
							appendMsg(ms,listaModel.getUsuarioAt(i));
						}

						cs.enviar(ms);
						txtMensagem.setText(null);
					} catch (IOException e2) {
						JOptionPane.showMessageDialog(null, "Falha ao Enviar mensagem");
						e2.printStackTrace();
					}
					}else {
						JOptionPane.showMessageDialog(null, "Sua mensagem é muito longa","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
					
		// Esta Action aguarda a tecla enter ser pressioado enquando o componente txtMensagem tem o foco
		// Quando enter for pressionado o componente btnEnviar será pressionado no mesmo momento
		txtMensagem.addKeyListener(new KeyListener() {
			

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {//key enter
					btnEnviar.doClick();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}});
		
		/** Esta Action fecha a do chat que esta aberta no momento*/
		btnFechar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(abasChat.getSelectedIndex() > -1) {
					abasChat.removeAbaChat(abasChat.getAbaSelecionada());
				}else {
					JOptionPane.showMessageDialog(null, "Nenhum Usuario Selecionado");
				}
			}
		});
		/** Esta Action abre uma nova aba de conversa quando um dos itens da lista 
		 * de usuarios online for selecionado */
		btnAdicionar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JlistOnline.getSelectedIndex() > -1) {
					 if(abasChat.getIndiceAba(JlistOnline.getSelectedValue()) == -1) {
						 addAbaConversa(listaModel.getUsuarioAt(JlistOnline.getSelectedIndex()));
					 }else {
						 abasChat.setSelectedIndex(abasChat.getIndiceAba(JlistOnline.getSelectedValue()));
					 }
				}else {
					JOptionPane.showMessageDialog(null, "Nenhum Usuario Selecionado");
				}
			}
		});
		/*Esta Action Encerra a conexão quando o janela for fechada
		 */
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				if(cs != null) {
					cs.desconectar();
					cs = null;
				}
			 }
		});
		/* Esta Action abre uma nova aba de conversa quando um dos itens da lista 
		 * de usuarios online for selecionado duas vezes
		 */
		JlistOnline.addMouseListener(new MouseAdapter() {
			
			 public void mouseClicked(MouseEvent e) {
				 if(e.getClickCount() == 2) {
					 if(abasChat.getIndiceAba(JlistOnline.getSelectedValue()) == -1) {
						 addAbaConversa(listaModel.getUsuarioAt(JlistOnline.getSelectedIndex()));
					 }else {
						 abasChat.setSelectedIndex(abasChat.getIndiceAba(JlistOnline.getSelectedValue()));
					 }
				 }
			 }
			 
		});
		//Esta Action envia uma mensagem para um destinatario de acordo
		//com qual aba do chat esta aberta
		btnEnviar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(abasChat.getSelectedIndex() > -1 && !txtMensagem.getText().isEmpty()) {
					if(txtMensagem.getText().length() < 999){
						try {
							Mensagem ms = new Mensagem();
							ms.setUsuario(dadosSessao.getUsuario());
							ms.setMensagem(txtMensagem.getText());
							ms.setStatus(Mensagem.Status.ENVIAR_UM);
							ms.setDestinatario(abasChat.getAbaSelecionada());
							ms.setDateTime(LocalDateTime.now());
							appendMsg(ms, ms.getDestinatario());
							cs.enviar(ms);
							//appendTempo(ms);
							txtMensagem.setText(null);
						} catch (IOException e2) {
							JOptionPane.showMessageDialog(null, "Falha ao Enviar mensagem");
							e2.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Sua mensagem é muito longa","Error",JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(null, "Favor abrir uma aba");
				}
			}
		});
		/*
		 * Esta Action fecha a conexão soquete
		 */
		btnDesconectar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cs.desconectar();
				setChatEnable(false);
				btnConectar.setEnabled(true);
				mnConexao.setEnabled(true);
				setStatus(false, null);
				cs = null;
			}
		});
		/*
		 * Este Action exibe uma nova tela de conexão
		 */
		btnConectar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frameConexao();
			}
		});
	}
	
	/**Append o conteudo da mensagem em uma aba de acordo com o nome do usuario que enviou a mensagem*/
	public static void appendMsg(Mensagem ms, Usuario aba) {
		if(abasChat.getIndiceAba(aba) == -1) {
			abasChat.addAbaChat(aba);
			abasChat.appendMensagem(ms,aba);
		}else {
			abasChat.appendMensagem(ms,aba);
		}
	}
	/**Append o conteudo da mensagem em uma aba de acordo com o nome do usuario que enviou a mensagem*/
	public static void appendTempo(Mensagem ms) {
		if(abasChat.getIndiceAba(ms.getUsuario()) != -1) {
			abasChat.appendTime(ms.getUsuario(), ms.getDateTime());
		}
	}
	//
	
	/**Exibe o frame que permite o Usuario inserir informações sobre a conexão com o host */
	public static void frameConexao() {

		FrameCadastroInput fr = new FrameCadastroInput("Conexão");
		fr.setCampolbl1("Endereço");
		fr.getTxt1().setText("localhost");
		fr.setCampolbl2("Porta");
		fr.getTxt2().setText("12345");
		if(fr.showGUI() == JOptionPane.OK_OPTION) {
			try {
				String endere = fr.getCampo1();
				Integer porta = Integer.parseInt(fr.getCampo2());
				dadosSessao = new DadosConexão(null, null, endere, porta);
				cs = new ClienteServico();
				cs.iniciarConexao(dadosSessao);
				frameLogin();
				cs.login(dadosSessao);
			}catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(null, "Porta Inválida");
				frameConexao();
				e2.printStackTrace();
			}catch (Exception e3) {
				e3.printStackTrace();
				JOptionPane.showMessageDialog(null, "Não foi possível conectar com host");
				frameConexao();
			}
		}else {
			System.exit(0);
		}

	}
	public static void frameLogin() {
		FrameCadastroInput fr = new FrameCadastroInput("Login");
		fr.setCampolbl1("Usuario");
		fr.setCampolbl2("Senha");
		if(fr.showGUI() == JOptionPane.OK_OPTION) {
			dadosSessao.setUsuario(new Usuario(null, fr.getCampo1(), fr.getCampo2()));
		}else {
			System.exit(0);
		}
	}
	/**Habilita ou Desabilita o frame de Chat*/
	public static void setChatEnable(boolean status) {
		mnConexao.setEnabled(status);
		btnEnviar.setEnabled(status);
		txtMensagem.setEditable(status);
	}
	/**Adciona uma nova aba de chat*/
	public static void addAbaConversa(Usuario abaNome) {
		abasChat.addAbaChat(abaNome);
	}
	
	/**Altera o Status da conexão */
	public static void setStatus(boolean status, String usuario) {
		if(status) { 
			lblStatus.setText("Conectado");
			lblStatus.setForeground(Color.GREEN);
			txtUsuario.setText(usuario);
			btnDesconectar.setEnabled(true);
			btnConectar.setEnabled(false);
		}
		else { 
			lblStatus.setText("Desconectado"); 
			lblStatus.setForeground(Color.RED);
			txtUsuario.setText(usuario);
			btnConectar.setEnabled(true);
			btnDesconectar.setEnabled(false);
			listaModel.clear();
			abasChat.fecharAbas();
		}
	}
	public void setIcons() {
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ConcordIcon.png")));
			content.setIcon(new ImageIcon(getClass().getResource("/img/marginal.png")));
			btnAdicionar.setIcon(new ImageIcon(getClass().getResource("/mais.png")));
			btnFechar.setIcon(new ImageIcon(getClass().getResource("/menos.png")));
			btnFecharTodos.setIcon(new ImageIcon(getClass().getResource("/fechar.png")));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Falha em carregar imagens");
		}
	}

}

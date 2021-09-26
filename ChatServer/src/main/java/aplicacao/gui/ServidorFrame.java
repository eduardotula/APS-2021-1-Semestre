package aplicacao.gui;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import aplicacao.dao.ServidorDAO;
import aplicacao.model.TableModelUsuarios;
import aplicacao.model.UsuarioListModel;
import aplicacao.servidor.ServidorServico;
import aplicacao.servidor.bean.UsuarioBD;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumnModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.border.BevelBorder;

public class ServidorFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static TableModelUsuarios modelUsuarios = new TableModelUsuarios();
	private Integer port = 12345;
	private String endereco = "Localhost";
	private ServidorServico serv;
	public static UsuarioListModel listaOnlineModel = new UsuarioListModel();
	//Visuais
	private JTable tabelaUsuarios = new JTable();
	private JMenuBar menuBar = new JMenuBar();
	private JButton btnNovoUsuari = new JButton("Novo Usuário");
	private JScrollPane scrollLogins = new JScrollPane();
	private JLabel lblNewLabel = new JLabel("New label");
	private JScrollPane scrollOnline = new JScrollPane();
	private JLabel lblUsuariosOnline = new JLabel("Usuários Online");
	private JList<String> listaOnline = new JList<String>();
	private JButton btnEditar = new JButton("Editar ");
	private JButton btnApagarUsu = new JButton("Apagar Usuario");
	private final JMenu mnConfigura = new JMenu("Configura\u00E7\u00F5es do Servidor");
	private final JMenuItem mntmConfiguracoesServ = new JMenuItem("Propriedades");
	private final JMenuItem mntmFinalizar = new JMenuItem("Finalizar Servi\u00E7o");
	private final JMenuItem mntmIniciar = new JMenuItem("Iniciar Servi\u00E7o");
	private final JButton btnExibir = new JButton("Exibir Mensagens");

	public ServidorFrame() {
		super("Servidor");
		createAndShowGUI();
		setListeners();
		setIcons();
	}
	/**Cria todos o componentes visuais no frame*/
	public void createAndShowGUI() {
		setSize(670,500);
		setVisible(true);
		setMinimumSize(new Dimension(670,500));
		setLocationRelativeTo(null);
		menuBar.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setJMenuBar(menuBar);
		mnConfigura.setBorderPainted(true);
		mnConfigura.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		menuBar.add(mnConfigura);
		mnConfigura.add(mntmIniciar);
		mnConfigura.add(mntmFinalizar);
		mnConfigura.add(mntmConfiguracoesServ);
		mntmFinalizar.setEnabled(false);
		listaOnline.setModel(listaOnlineModel);
		tabelaUsuarios.setModel(modelUsuarios);
		TableColumnModel m = tabelaUsuarios.getColumnModel();
		m.getColumn(0).setMinWidth(60);
		m.getColumn(1).setPreferredWidth(300);
		m.getColumn(2).setMinWidth(80);
		getContentPane().setLayout(new MigLayout("", "[380.00,grow][100px:100px]", "[][][grow]"));
		getContentPane().add(btnNovoUsuari, "flowx,cell 0 1,alignx left");
		getContentPane().add(scrollLogins, "cell 0 2,grow");
		scrollLogins.setColumnHeaderView(lblNewLabel);
		scrollLogins.setViewportView(tabelaUsuarios);
		getContentPane().add(scrollOnline, "cell 1 2,grow");
		scrollOnline.setColumnHeaderView(lblUsuariosOnline);
		scrollOnline.setViewportView(listaOnline);
		getContentPane().add(btnEditar, "cell 0 1");
		getContentPane().add(btnApagarUsu, "cell 0 1");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(btnExibir, "cell 0 1");
		
	}
	/**Configura todos listeners para os componentes visuais*/
	public void setListeners() {
		
		
		btnExibir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tabelaUsuarios.getSelectedRow() != -1) {
					FrameMensagens mensag = new FrameMensagens((Integer) tabelaUsuarios.getValueAt(tabelaUsuarios.getSelectedRow(), 0));
					mensag.setSize(600,456);
				}else {
					JOptionPane.showMessageDialog(null, "Nenhum usuario selecionado", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
			 public void windowClosing(WindowEvent e) {
				 if(serv != null) {
					 serv.interrupt();
				 }
			 }
		});
		mntmIniciar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(serv == null) {
					serv = new ServidorServico(port);
					serv.start();
					mntmIniciar.setEnabled(false);
					mntmFinalizar.setEnabled(true);
				}else {
					JOptionPane.showInternalMessageDialog(null, "Servidor já iniciado");
				}
			}
		});
		mntmFinalizar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(serv != null) {
					serv.interrupt();
					serv = null;
					mntmIniciar.setEnabled(true);
					mntmFinalizar.setEnabled(false);
					JOptionPane.showMessageDialog(null, "Servidor finalizado");
				}
			}
		});
		mntmConfiguracoesServ.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					FramePropriedades fc = new FramePropriedades();
					fc.getTxt1().setEditable(false);
					fc.setCampolbl1("Endereço");
					fc.setCampolbl2("Porta");
					fc.setCampotxt1(endereco);
					fc.setCampotxt2(Integer.toString(port));
					int res = fc.showGUI();
					if(res == JOptionPane.OK_OPTION) {
						if(fc.getCampo2().length() > 5||fc.getCampo2().length() < 3) {
							JOptionPane.showMessageDialog(null, "Tamanho de Porta Inválida","Erro",JOptionPane.ERROR_MESSAGE);
						}else {
							port = Integer.parseInt(fc.getCampo2());

						}
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Porta Inválida","Erro",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		btnEditar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int modelRow = tabelaUsuarios.convertRowIndexToModel(tabelaUsuarios.getSelectedRow());
				
				new FrameCadastroCliente(modelUsuarios.getId(modelRow));
			}
		});
		
		btnNovoUsuari.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				new FrameCadastroCliente(null);
			}
		});
		
		btnApagarUsu.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(tabelaUsuarios.getSelectedColumns().length > 0 && JOptionPane.showConfirmDialog(null, "Deseja Apagar o Usuário Selecionado") == 0) {
					int[] selectedRows = tabelaUsuarios.getSelectedRows();
					for(int row : selectedRows) {
						int modelRow = tabelaUsuarios.convertRowIndexToModel(row);
						UsuarioBD usuario = modelUsuarios.getUsuarioAt(modelRow);
						try {
							ServidorDAO.apagarUsuario(usuario);
							modelUsuarios.removeRow(modelRow);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "Falha ao Alterar");
							e1.printStackTrace();
						}
					}
				}else {
					JOptionPane.showMessageDialog(null, "Nenhum Usuário Selecionado");
				}
			}
		});
	}
	private void setIcons() {
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ConcordServerIcon.png")));
			btnNovoUsuari.setIcon(new ImageIcon(getClass().getResource("/adicionar-usuario.png")));
			btnApagarUsu.setIcon(new ImageIcon(getClass().getResource("/remover-usuario.png")));
			btnEditar.setIcon(new ImageIcon(getClass().getResource("/editar.png")));
			btnExibir.setIcon(new ImageIcon(getClass().getResource("/mensagem.png")));
			mnConfigura.setIcon(new ImageIcon(getClass().getResource("/config.png")));
			mntmConfiguracoesServ.setIcon(new ImageIcon(getClass().getResource("/config.png")));
			mntmIniciar.setIcon(new ImageIcon(getClass().getResource("/iniciar.png")));
			mntmFinalizar.setIcon(new ImageIcon(getClass().getResource("/parar.png")));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

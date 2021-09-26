package aplicacao.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import aplicacao.dao.ServidorDAO;
import aplicacao.model.TableModelMensagens;

import javax.swing.JOptionPane;

public class FrameMensagens extends JFrame{
	
	private static final long serialVersionUID = 1L;
	public static TableModelMensagens modelMensagem;
	private static JTable tabelaMensagem = new JTable();
	private	JButton btnApagar = new JButton("Apagar Selecionado");
	private JButton btnEditar = new JButton("Editar Selecionado");
	private final JButton btnAdicionar = new JButton("Adicionar");
	private static Integer id;
	
	public FrameMensagens(Integer idUsuario) {
		super("Mensagens");
		id = idUsuario;
		modelMensagem = new TableModelMensagens();
		createAndShowGUI();
		setListeners();
		setIcons();
	}
	
	
	public void createAndShowGUI() {
		JScrollPane scroll = new JScrollPane();
		getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow]"));
		getContentPane().add(scroll, "cell 0 1,grow");
		scroll.setViewportView(tabelaMensagem);
		refreshTable();
		setVisible(true);
		setSize(561,456);
		setLocationRelativeTo(null);
	}
	
	public void setListeners() {
		getContentPane().add(btnAdicionar, "flowx,cell 0 0");
		getContentPane().add(btnEditar, "cell 0 0");
		getContentPane().add(btnApagar, "cell 0 0");
		btnApagar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(null, "Deseja apagar as linhas selecionadas");
				if(res == JOptionPane.OK_OPTION) {
					if(tabelaMensagem.getSelectedRows().length != 0) {
						try {
							for(int row : tabelaMensagem.getSelectedRows()) {
								int modelRow = tabelaMensagem.convertRowIndexToModel(row);
								ServidorDAO.apagarMensagem(modelMensagem.getMensagem(modelRow));
								refreshTable();
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, "Falha ao apagar Mensagens","Erro",JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada","Erro",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnEditar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tabelaMensagem.getSelectedRow() != -1) {
					 int modelRow = tabelaMensagem.convertRowIndexToModel(tabelaMensagem.getSelectedRow());
					 new FrameCadastroMensagem(modelMensagem.getMensagem(modelRow));
				}else {
					JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada");
				}
			}
		});
		btnAdicionar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new FrameCadastroMensagem();
			}
		});
		tabelaMensagem.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() >= 2) {
					btnEditar.doClick();
				}
			}
			
		});
	}
	public static void refreshTable() {
		modelMensagem.refreshModel(id);
		tabelaMensagem.setModel(modelMensagem);
		TableColumnModel m = tabelaMensagem.getColumnModel();
		m.getColumn(0).setMinWidth(40);
		m.getColumn(1).setPreferredWidth(600);
		m.getColumn(2).setMinWidth(130);
		m.getColumn(3).setMinWidth(80);
		m.getColumn(4).setMinWidth(80);
	}
	
	private void setIcons() {
		try {
			btnEditar.setIcon(new ImageIcon(getClass().getResource("/editar.png")));
			btnAdicionar.setIcon(new ImageIcon(getClass().getResource("/adicionar-mensagem.png")));
			btnApagar.setIcon(new ImageIcon(getClass().getResource("/apagarMsg.png")));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

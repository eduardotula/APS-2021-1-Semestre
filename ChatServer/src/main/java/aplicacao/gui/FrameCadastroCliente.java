package aplicacao.gui;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;


import aplicacao.dao.ServidorDAO;
import aplicacao.servidor.bean.Cargo;
import aplicacao.servidor.bean.UsuarioBD;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JPasswordField;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;

public class FrameCadastroCliente extends JFrame{
	private static final long serialVersionUID = 1L;
	private UsuarioBD usu;

	//Visuais
	private JButton btnCancelar = new JButton("Cancelar");
	private JButton btnSalvar = new JButton("Salvar");
	private JButton btnNovoCargo = new JButton("Novo Cargo");
	private JTextField txtUsuario;
	private JPasswordField txtSenha;
	private JFormattedTextField txtCpf;
	private JTextField txtTelefone;
	private JTextField txtEmail;
	private JTextField txtNome;
	private JTextField txtEndereco;
	private JFormattedTextField txtEstado;
	private JTextField txtCidade;
	private JTextField txtPais;
	private JTextField txtId;
	private JComboBox<Cargo> comboCargo = new JComboBox<Cargo>();
	private DefaultComboBoxModel<Cargo> comboModel = new DefaultComboBoxModel<Cargo>(ServidorDAO.getAllCargos());
	
	//Visuais
	public FrameCadastroCliente(Integer Id) {
		super("Cliente");
		startAndShowGUI();
		setDados(Id);
		setListeners();
	}
	
	public void startAndShowGUI() {
		setSize(370,389);
		setVisible(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JLabel lblUsuario = new JLabel("Usuario");
		JPanel panelDados = new JPanel();
		txtUsuario = new JTextField();
		JLabel lblSenha = new JLabel("Senha");
		txtSenha = new JPasswordField();
		JLabel lblNome = new JLabel("Nome");
		JLabel lblCpf = new JLabel("CPF");
		txtNome = new JTextField();
		JLabel lblTelefone = new JLabel("Telefone");
		txtTelefone = new JTextField();
		JLabel lblEmail = new JLabel("Email");
		txtEmail = new JTextField();
		JPanel panelEndereco = new JPanel();
		JLabel lblEndereco = new JLabel("Endere\u00E7o");
		txtEndereco = new JTextField();
		JLabel lblMunicipio = new JLabel("Municipio");
		JLabel lblEstado = new JLabel("Estado");
		JLabel lblPais = new JLabel("Pais");
		txtCidade = new JTextField();
		txtPais = new JTextField();
		JPanel panel = new JPanel();
		JLabel lblId = new JLabel("ID:");
		txtId = new JTextField();
		comboModel.addElement(new Cargo(0,""));
		comboCargo.setModel(comboModel);
		setcomboRenderer();
		System.out.println(comboCargo.getSelectedItem().getClass());

		try {
			MaskFormatter mascaraEstado = new MaskFormatter("UU");
			MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
			mascaraEstado.setPlaceholderCharacter('_');
			mascaraCpf.setPlaceholderCharacter('_');
			txtCpf = new JFormattedTextField(mascaraCpf);
			txtEstado = new JFormattedTextField(mascaraEstado);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addTab("Dados", null, panelDados, null);
		panelDados.setLayout(new MigLayout("", "[172.00,grow][192.00,grow][][]", "[][][][][][][][][][][][][]"));
		panelDados.add(lblId, "flowx,cell 0 0,alignx left");
		panelDados.add(lblUsuario, "cell 0 1 2 1");
		panelDados.add(txtUsuario, "cell 0 2 2 1,growx");
		txtUsuario.setColumns(10);
		panelDados.add(lblSenha, "cell 0 3");
		panelDados.add(txtSenha, "cell 0 4,growx");
		txtSenha.setColumns(10);
		panelDados.add(lblNome, "cell 0 5");
		panelDados.add(lblCpf, "cell 1 5");
		panelDados.add(txtNome, "cell 0 6,growx");
		txtNome.setColumns(10);
		panelDados.add(txtCpf, "cell 1 6,alignx left");
		txtCpf.setColumns(17);
		panelDados.add(lblTelefone, "cell 0 7");
		panelDados.add(txtTelefone, "cell 0 8,growx");
		txtTelefone.setColumns(10);
		panelDados.add(lblEmail, "cell 0 9");
		panelDados.add(txtEmail, "cell 0 10 2 1,alignx left");
		txtEmail.setColumns(30);
		
		txtId.setEditable(false);
		panelDados.add(txtId, "cell 0 0,alignx left");
		txtId.setColumns(10);
		
		JLabel lblCargo = new JLabel("Cargo");
		panelDados.add(lblCargo, "cell 0 11");
		panelDados.add(comboCargo, "cell 0 12,growx");
		
		panelDados.add(btnNovoCargo, "cell 1 12");
		tabbedPane.addTab("Endereço", null, panelEndereco, null);
		panelEndereco.setLayout(new MigLayout("", "[119.00,grow][103.00,grow][100,grow]", "[][][][]"));
		panelEndereco.add(lblEndereco, "cell 0 0");
		panelEndereco.add(txtEndereco, "cell 0 1 2 1,alignx left");
		txtEndereco.setColumns(35);
		panelEndereco.add(lblMunicipio, "cell 0 2");
		panelEndereco.add(lblEstado, "cell 1 2");
		panelEndereco.add(lblPais, "cell 2 2");
		panelEndereco.add(txtCidade, "cell 0 3,growx");
		txtCidade.setColumns(10);
		panelEndereco.add(txtEstado, "cell 1 3,alignx left");
		txtEstado.setColumns(2);
		panelEndereco.add(txtPais, "cell 2 3,growx");
		txtPais.setColumns(5);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.add(btnCancelar);
		panel.add(btnSalvar);
		
	}
	public void setListeners() {
		btnNovoCargo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String resposta = JOptionPane.showInputDialog("Digite o nome do Cargo");
				if(!resposta.isEmpty()|| resposta.length()<100) {
					ServidorDAO.criarCargo(new Cargo(null, resposta));
					comboCargo.setModel(new DefaultComboBoxModel<Cargo>(ServidorDAO.getAllCargos()));
				}else {
					JOptionPane.showMessageDialog(null, "Cargo inválido","Erro",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					if((!txtUsuario.getText().isEmpty() && txtSenha.getPassword().length >0 )|| comboCargo.getModel().getSize() == 0) {
						if(txtId.getText().isEmpty()) {
							
							usu = new UsuarioBD(null, txtUsuario.getText(),
									String.valueOf(txtSenha.getPassword()),txtNome.getText(), txtCpf.getText(),
									txtTelefone.getText(), txtEmail.getText(), txtPais.getText(), txtEstado.getText(),
									txtCidade.getText(), txtEndereco.getText(),(Cargo)comboCargo.getSelectedItem());
							ServidorDAO.criarUsuario(usu);
							
						}else {
							salvarDados(Integer.parseInt(txtId.getText()));
						}
						ServidorFrame.modelUsuarios.refreshModel();
						dispose();
					}else {
						JOptionPane.showMessageDialog(null, "Campos Usuario, Senha ou cargo Inválido,","Erro",JOptionPane.ERROR_MESSAGE);
					}
				}catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro de dados","Erro",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnCancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	/**Insere os dados em campos no panel*/
	private void setDados(Integer id) {
		try {
			UsuarioBD usuario = ServidorDAO.getUsuarioById(id);
			txtUsuario.setText(usuario.getUsuario());
			txtSenha.setText(usuario.getSenha());
			txtCidade.setText(usuario.getCidade());
			txtCpf.setText(usuario.getCpf());
			txtEmail.setText(usuario.getEmail());
			txtEndereco.setText(usuario.getRua());
			txtEstado.setText(usuario.getEstado());
			txtId.setText(Integer.toString(id));
			txtNome.setText(usuario.getNome());
			txtPais.setText(usuario.getPais());
			txtTelefone.setText(usuario.getTelefone());
			System.out.println(usuario.getCargo().getCargo());
			comboCargo.setSelectedItem(usuario.getCargo());
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**Commit todos os dados contidos no frame*/
	private void salvarDados(Integer id) {
		try {
			ServidorDAO.begin();
			UsuarioBD usu = ServidorDAO.getEm().find(UsuarioBD.class, id);
			usu.setCidade(txtCidade.getText());
			usu.setCpf((String) txtCpf.getValue());
			usu.setEmail(txtEmail.getText());
			usu.setEstado((String) txtEstado.getValue());
			usu.setNome(txtNome.getText());
			usu.setPais(txtPais.getText());
			usu.setRua(txtEndereco.getText());
			usu.setSenha(String.valueOf(txtSenha.getPassword()));
			usu.setTelefone(txtTelefone.getText());
			usu.setUsuario(txtUsuario.getText());
			usu.setCargo((Cargo) comboCargo.getSelectedItem());
			ServidorDAO.commit();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao salvar Usuario", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void setcomboRenderer() {
		class CellRenderer extends DefaultListCellRenderer {

		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component getListCellRendererComponent(
		                                   @SuppressWarnings("rawtypes") JList list,
		                                   Object value,
		                                   int index,
		                                   boolean isSelected,
		                                   boolean cellHasFocus) {
		        if (value instanceof Cargo) {
		            value = ((Cargo)value).getCargo();
		        }
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        return this;
		    }
		}
		comboCargo.setRenderer(new CellRenderer());
	}
}

package aplicacao.gui;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import aplicacao.dao.ServidorDAO;
import aplicacao.servidor.bean.MensagemBD;
import aplicacao.servidor.bean.UsuarioBD;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import java.awt.Component;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;

public class FrameCadastroMensagem {
	private JComboBox<UsuarioBD> comboReme = new JComboBox<UsuarioBD>();
	private JComboBox<UsuarioBD> comboDestina = new JComboBox<UsuarioBD>();
	private JTextArea txtMensagem = new JTextArea();
	private DefaultComboBoxModel<UsuarioBD> comboModel = new DefaultComboBoxModel<UsuarioBD>(
			ServidorDAO.getAllUsuarios());
	private DefaultComboBoxModel<UsuarioBD> comboModel2 = new DefaultComboBoxModel<UsuarioBD>(
			ServidorDAO.getAllUsuarios());
	private JFormattedTextField txtData;
	private JFormattedTextField txtHora;
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	@SuppressWarnings("unused")
	private MensagemBD mensagem;

	public FrameCadastroMensagem() {
		setFormattedTxt();
		comboReme.setModel(comboModel);
		comboModel2.addElement(null);
		comboDestina.setModel(comboModel2);
		mensagem = new MensagemBD();
		comboDestina.setRenderer(new ComboRender());
		comboReme.setRenderer(new ComboRender());
		showGUI();
		
	}

	public FrameCadastroMensagem(MensagemBD mensagem) {
		this.mensagem = mensagem;
		setFormattedTxt();
		txtMensagem.setText(mensagem.getMensagem());
		try {
			comboDestina.setModel(comboModel2);
			comboModel2.addElement(null);
			comboReme.setModel(comboModel);
			comboReme.setSelectedItem(mensagem.getUsuario());
			comboDestina.setSelectedItem(mensagem.getDestinatario());
			txtData.setText(mensagem.getDateTime().toLocalDate().format(format));
			txtHora.setText(mensagem.getDateTime().toLocalTime().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		comboDestina.setRenderer(new ComboRender());
		comboReme.setRenderer(new ComboRender());
		showGUI();
	}

	private void showGUI() {

		try {
			int res = JOptionPane.showConfirmDialog(null, getPanel(), "Mensagem", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, new ImageIcon(getClass().getResource("/mensagem.png")));
			System.out.println(comboModel.getSize());
			if (res == JOptionPane.OK_OPTION) {
				UsuarioBD reme = comboModel.getElementAt(comboReme.getSelectedIndex());
				UsuarioBD dest = comboModel.getElementAt(comboDestina.getSelectedIndex());
				mensagem.setMensagem(txtMensagem.getText());
				mensagem.setDestinatario(dest);
				mensagem.setUsuario(reme);
				mensagem.setDateTime(convertDataHora((String) txtHora.getText(), (String) txtData.getText()));
				if (mensagem.getId() == null) {
					ServidorDAO.criarMensagem(mensagem);
				}
				FrameMensagens.refreshTable();
			}
		} catch (DateTimeParseException e1) {
			JOptionPane.showConfirmDialog(null, "Data ou Hora inválidos", "Error", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "Falha ao Criar Mensagem", "Error", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow]", "[][98.00][][][][][][][]"));
		JLabel lblMensagem = new JLabel("Mensagem");
		panel.add(lblMensagem, "cell 0 0");
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 1,grow");
		scrollPane.setViewportView(txtMensagem);

		JLabel lblData = new JLabel("Data:");
		panel.add(lblData, "flowx,cell 0 2");

		JLabel lblHora = new JLabel("Hora:");
		panel.add(lblHora, "flowx,cell 0 3");
		JLabel lblRemetente = new JLabel("Remetente");
		panel.add(lblRemetente, "cell 0 4");
		panel.add(comboReme, "cell 0 5,growx");
		JLabel lblDestina = new JLabel("Destinatario");
		panel.add(lblDestina, "cell 0 6");
		panel.add(comboDestina, "cell 0 7,growx");
		txtMensagem.setLineWrap(true);
		panel.add(txtData, "cell 0 2");
		txtData.setColumns(13);
		panel.add(txtHora, "cell 0 3");
		txtHora.setColumns(13);
		return panel;
	}

	private void setFormattedTxt() {
		try {
			MaskFormatter maskData = new MaskFormatter("##/##/####");
			MaskFormatter maskHora = new MaskFormatter("##:##:##");
			maskData.setPlaceholderCharacter('_');
			maskHora.setPlaceholderCharacter('_');
			txtData = new JFormattedTextField(maskData);
			txtHora = new JFormattedTextField(maskHora);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	private LocalDateTime convertDataHora(String hora, String data) throws DateTimeParseException {
		LocalTime horaT;
		LocalDate dataD;
		horaT = LocalTime.parse(hora);
		dataD = LocalDate.parse(data, format);
		return LocalDateTime.of(dataD, horaT);
	}

	class ComboRender extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if (value != null && value instanceof UsuarioBD) {
				value = ((UsuarioBD) value).getUsuario();
			} else {
				value = "Todos";
			}
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			return this;
		}
	}
}
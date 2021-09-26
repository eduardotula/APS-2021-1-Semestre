package aplicacao;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Font;

public class FrameCadastroInput {
	//Visuais
	private JFrame frame;
	private JPanel panel;
	private JTextField txt1 = new JTextField();
	private JTextField txt2 = new JTextField();;
	private JLabel lbl1 = new JLabel();
	private JLabel lbl2 = new JLabel();
	private String nomeFrame;
	
	public FrameCadastroInput(String nomeFrame) {
		frame = getFrame();
		panel = getPanel();
		this.nomeFrame = nomeFrame;
	}
	public int showGUI() {
		return JOptionPane.showConfirmDialog(frame, panel,nomeFrame, JOptionPane.OK_CANCEL_OPTION);
	}
	private JPanel getPanel() {
		panel = new JPanel();
		txt1 = new JTextField();
		txt2 = new JTextField();
		txt2.setColumns(10);
		txt1.setColumns(10);
		panel.setLayout(new MigLayout("", "[170.00,grow][grow]", "[][][][][][][grow]"));
		panel.add(lbl1, "cell 0 0 2 1");
		panel.add(txt1, "cell 0 1,growx");
		panel.add(lbl2, "cell 0 2 2 1");
		panel.add(txt2, "cell 0 3,growx");
		return panel;
	}
	private JFrame getFrame() {
		frame = new JFrame();
		frame.setVisible(false);
		frame.setSize(280,120);
		frame.pack();
		return frame;
	}
	public void setCampolbl1(String campo1) {
		lbl1.setText(campo1);
		lbl1.setFont(new Font("Tahoma", Font.PLAIN, 14));
	}
	public void setCampolbl2(String campo2) {
		lbl2.setText(campo2);
		lbl2.setFont(new Font("Tahoma", Font.PLAIN, 14));
	}
	public void setCampotxt1(String campo1) {
		txt1.setText(campo1);
	}
	public void setCampotxt2(String campo2) {
		txt2.setText(campo2);
	}
	
	public JTextField getTxt1() {
		return txt1;
	}
	public void setTxt1(JTextField txt1) {
		this.txt1 = txt1;
	}
	public JTextField getTxt2() {
		return txt2;
	}
	public void setTxt2(JTextField txt2) {
		this.txt2 = txt2;
	}
	public String getCampo1() {
		if(!txt1.getText().isEmpty()) {
			return txt1.getText();
		}else {
			return null;
		}
	}
	public String getCampo2() {
		if(!txt2.getText().isEmpty()) {
			return txt2.getText();
		}else {
			return null;
		}
	}
}

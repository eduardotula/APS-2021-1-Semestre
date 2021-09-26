package aplicacao;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import aplicacao.model.MensagemTableModel;
import aplicacao.servidor.bean.Mensagem;
import aplicacao.servidor.bean.Mensagem.Status;
import aplicacao.servidor.bean.Usuario;

public class JTabbedChat extends JTabbedPane {

	private List<Usuario> listaAbas = new ArrayList<Usuario>();
	private final Color corTexto = new Color(189, 190, 193);
	private Font usuarioFonte = new Font("Tahoma", Font.BOLD, 15);
	private Font textoFonte = new Font("Tahoma", Font.PLAIN, 11);
	private ImageIcon confirmado = new ImageIcon(getClass().getResource("/confirmado.png"));
	private ImageIcon naoRecebido = new ImageIcon(getClass().getResource("/botao-x.png"));
	private float alpha;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JTabbedChat() {
		super();
		this.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
			  protected void paintContentBorder(Graphics g,int tabPlacement,int selectedIndex){}
			});
	    setOpaque(false);
	}

	public JTabbedChat(int tabPlacement) {
		super(tabPlacement, WRAP_TAB_LAYOUT);
		this.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI(){
			  protected void paintContentBorder(Graphics g,int tabPlacement,int selectedIndex){}
			});
	    setOpaque(false);
	}

	/**
	 * Adiciona uma Mensagem ao componente JTextArea de acordo com o indice da aba
	 */
	public void appendMensagem(Mensagem mensagem, Usuario aba) {
		JTable chat = getChatTxtAt(getIndiceAba(aba));
		MensagemTableModel m = (MensagemTableModel) chat.getModel();

		if (m.getRowCount() > 0 && m.getUltiUsua().equals(mensagem.getUsuario())) {
			m.addRow(new Mensagem(mensagem.getMensagem(), mensagem.getUsuario(), mensagem.getDestinatario(), Status.NEGADO));
			chat.setRowHeight(m.getRowCount() - 1, getWrapLine(mensagem.getMensagem()) * 18);

		} else {
			if (m.getRowCount() != 0) {
				m.addRow(new Mensagem(null, mensagem.getUsuario(), null, null));
				chat.setRowHeight(m.getRowCount() - 1, 2);
			}
			m.addRow(new Mensagem(mensagem.getNomeUsuario(), mensagem.getUsuario(), mensagem.getDestinatario(), null));
			chat.setRowHeight(m.getRowCount() - 1, 23);
			m.addRow(new Mensagem(mensagem.getMensagem(), mensagem.getUsuario(), mensagem.getDestinatario(), Status.NEGADO));
			chat.setRowHeight(m.getRowCount() - 1, getWrapLine(mensagem.getMensagem()) * 18);
		}
	}

	/**
	 * Adiciona o tempo que a mensagem foi recebida ao componente JTextArea de
	 * acordo com o indice da aba
	 */
	public void appendTime(Usuario usuario, LocalDateTime tempo) {
		JTable chat = getChatTxtAt(getIndiceAba(usuario));
		MensagemTableModel m = (MensagemTableModel) chat.getModel();
		Mensagem men = m.getValueAt(m.getRowCount()-1);
		men.setDateTime(tempo);
		men.setStatus(Status.CONFIRMADO);
		chat.repaint();
	}

	/** Retorna o componente JTextArea de acordo com o indice */
	public JTable getChatTxtAt(int indiceAba) {
		JScrollPane scroll = (JScrollPane) getComponent(indiceAba);
		return (JTable) scroll.getViewport().getView();
	}

	/** Adiciona um nova aba de conversa */
	public void addAbaChat(Usuario aba) {
		
		JTable tableChat = new JTable();
		tableChat.setShowGrid(false);
		tableChat.setTableHeader(null);
		tableChat.setOpaque(false);
		tableChat.setShowHorizontalLines(false);
		tableChat.setModel(new MensagemTableModel());
		TableColumnModel m = tableChat.getColumnModel();
		m.getColumn(0).setCellRenderer(new JMensagemRenderer());
		m.getColumn(1).setCellRenderer(new JIconRender());
		m.getColumn(1).setMaxWidth(16);
		m.getColumn(1).setWidth(16);
		tableChat.setForeground(corTexto);
		
		JScrollPane scrollMensagens = new JScrollPane(tableChat);
		scrollMensagens.setOpaque(false);
		scrollMensagens.getViewport().setOpaque(false);
		scrollMensagens.setPreferredSize(new Dimension(50, 50));
		addTab(aba.getUsuario(), null, scrollMensagens, null);
		listaAbas.add(aba);
	}
	 public void setAlpha(float value) {
	        if (alpha != value) {
	            float old = alpha;
	            this.alpha = value;
	            firePropertyChange("alpha", old, alpha);
	            repaint();
	        }
	    }

	    public float getAlpha() {
	        return alpha;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g.create();
	        g2d.setColor(getBackground());
	        g2d.setComposite(AlphaComposite.SrcOver.derive(getAlpha()));
	        g2d.fillRect(0, 0, getWidth(), getHeight());
	        g2d.dispose();
	        super.paintComponent(g);
	    }

	/** Remove uma aba */
	public void removeAbaChat(Usuario abaNome) {
		removeAbaChat(getIndiceAba(abaNome));
	}

	/** Remove uma aba */
	public void removeAbaChat(int indice) {
		removeTabAt(indice);
		listaAbas.remove(indice);
	}

	/** Retorna o nome da aba Atualmente selecionada */
	public String getNomeAbaSelecionada() {
		return getTitleAt(getSelectedIndex());
	}

	/** Retorna a Aba atualmente selecionada */
	public Usuario getAbaSelecionada() {
		return listaAbas.get(getSelectedIndex());
	}

	/** Obtem o indice de uma aba de acordo com o seu nome */
	public int getIndiceAba(Usuario aba) {
		return listaAbas.indexOf(aba);
	}

	/** Obtem o indice de uma aba de acordo com o seu nome */
	public int getIndiceAba(String abaNome) {
		return indexOfTab(abaNome);
	}

	/** Fecha todas as abas que estão abertas */
	public void fecharAbas() {
		int abas = listaAbas.size();
		for (int i = 0; i < abas; i++) {
			removeAbaChat(0);
		}
	}

	/**Resdesenha o tamanho de todas as linhas de acordo com o tamanho da mensagem*/
	public void redrawRowsHight() {
		JTable chat;
		MensagemTableModel model;
		for(Usuario aba : listaAbas) {
			chat = getChatTxtAt(getIndiceAba(aba));
			model = (MensagemTableModel) chat.getModel();
			for(int row = 0;row<model.getRowCount();row++) {
				Mensagem me = model.getValueAt(row);
				if(me.getMensagem() != null && !me.getMensagem().contentEquals(me.getNomeUsuario())) {
					int rowHeight = getWrapLine(me.getMensagem());
					chat.setRowHeight(row, rowHeight * 18);
					chat.repaint();
				}
			}
		}
	}
	/** Obtem a quantidade de linhas necessarias para um JTextArea conter a mensagem*/
	private int getWrapLine(String s) {
		JTextArea textArea = new JTextArea(s);
		textArea.setFont(textoFonte);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setSize(this.getSize().width,this.getSize().height);
	    AttributedString text = new AttributedString(textArea.getText());
	    FontRenderContext frc = textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();
	    AttributedCharacterIterator charIt = text.getIterator();
	    LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
	    Insets textAreaInsets = textArea.getInsets();
	    float formatWidth = textArea.getWidth() - textAreaInsets.left - textAreaInsets.right;
	    lineMeasurer.setPosition(charIt.getBeginIndex());

	    int noLines = 0;
	    while (lineMeasurer.getPosition() < charIt.getEndIndex())
	    {
	        lineMeasurer.nextLayout(formatWidth);
	        noLines++;
	    }

	    return noLines;
	}

	private class JMensagemRenderer extends JTextArea implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		Mensagem mens;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			mens = (Mensagem) value;
			this.setFont(textoFonte);
			this.setBorder(null);
			this.setText(mens.getMensagem());
			this.setOpaque(false);
			setLineWrap(true);
			setWrapStyleWord(true);
			
			if (mens.getMensagem() == null) {
				this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			} else if (mens.getMensagem().equals(mens.getNomeUsuario())) {
				this.setFont(usuarioFonte);
			}
			return this;
		}
	}
	private class JIconRender extends JTextArea implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		Mensagem mens;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if(value != null) {
				mens = (Mensagem) value;
				this.setText(mens.getMensagem());
				System.out.println(mens.getStatus() + " " +mens.getUsuario().equals(ClienteFrame.dadosSessao.getUsuario()) );
				if(mens.getStatus().equals(Status.CONFIRMADO)) {
					JLabel label = new JLabel("");
					label.setIcon(confirmado);
					return label;
				}else if(mens.getStatus().equals(Status.NEGADO) && mens.getUsuario().equals(ClienteFrame.dadosSessao.getUsuario())){
					JLabel label = new JLabel("");
					label.setIcon(naoRecebido);
					return label;
				}
			}
			return new JLabel();
		}
	}

}

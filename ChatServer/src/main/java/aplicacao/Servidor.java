package aplicacao;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;

import aplicacao.dao.ServidorDAO;
import aplicacao.gui.ServidorFrame;




public class Servidor {

	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new AluminiumLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				try {
					EntityManagerFactory nf = Persistence.createEntityManagerFactory("ChatAPS",readPropriedades());
					EntityManager em = nf.createEntityManager();
					em.setFlushMode(FlushModeType.AUTO);
					new ServidorDAO().setEm(em);
					new ServidorFrame();
				}catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao estabelecer conexão com o banco","Erro",JOptionPane.ERROR_MESSAGE);
					JOptionPane.showMessageDialog(null, "Por favor cheque se o arquivo PropriedadesBanco.txt \n"
							+ "contêm os dados corretos do banco de dados, por padrão o usuario e senha estão respectivamente como root e root");
				}
			}
		});
	}
	
	private static Map<String, String> readPropriedades() {
	     try {
		    String file ="PropriedadesBanco.txt";
		    BufferedReader reader = new BufferedReader(new FileReader(file));
			String currentLine;
			Map<String, String> properties = new HashMap<String, String>();
			while((currentLine = reader.readLine()) != null) {
				String[] splitLine = currentLine.split(",");
				properties.put(splitLine[0], splitLine[1]);
			}
			reader.close();
			return properties;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao ler arquivo PropriedadesBanco.txt","Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
	}
}

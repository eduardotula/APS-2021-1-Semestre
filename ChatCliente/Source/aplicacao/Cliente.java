package aplicacao;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.jtattoo.plaf.noire.NoireLookAndFeel;

public class Cliente {
	
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(new NoireLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new ClienteFrame();				
			}
		});
	}

}

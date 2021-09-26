package aplicacao;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFrameBackground extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image bImage;

    public JFrameBackground(String path){
           this.bImage = this.createImage(path);
           this.initComponents();
    }

    public void initComponents(){
           super.setContentPane(new NewContentPane());
           super.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private Image createImage(String path){
           return Toolkit.getDefaultToolkit().createImage("/marginal.png");
    }

    private class NewContentPane extends JPanel{
           /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected void paintComponent(final Graphics g) {
                   super.paintComponent(g);
                   g.drawImage(bImage, 0, 0, this);
           }
    }
}

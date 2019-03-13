package frame.menuPanels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import core.ControlInput;
import core.Core;
import core.ResourceManager;
import core.XMLSaveAndLoad;
import frame.JButton_01;
import frame.JScrollPaneBg;
import net.miginfocom.swing.MigLayout;

public class SavesPanel extends JScrollPaneBg {
	private static final long serialVersionUID = 6973604110107187761L;
	private ArrayList<JPanel> saveList= new ArrayList<>();
	private String filterString = "";
	
	public SavesPanel(JPanel container, String filterString) {
		super(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), false);
		setBackground(new Color(0, 0, 0, 0));
		this.filterString = filterString;
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getVerticalScrollBar().setUnitIncrement(16);
		setDoubleBuffered(true);
		
		JPanel viewport = new JPanel();
		viewport.setBackground(new Color(0,0,0,0));
		viewport.setOpaque(false);
		viewport.setLayout(new MigLayout("", "[fill]", "[fill]"));
		setViewportView(viewport);

		File[] saves = saveGameSearcher(Core.GAME_PATH_SAVES);
		String columns = "";
		if(saves != null) {
			for(int i = 0; i < saves.length; i++) {
				saveList.add(new SavesPanelElement(saves[i], container));
				columns += "[fill]";
			}
			viewport.setLayout(new MigLayout("", "[fill]", columns));
			
			for (int i = 0; i < saveList.size(); i++) {
				viewport.add(saveList.get(i), ("cell 0 " + i + ", grow"));
			}
		}

		
	}
	
	private File[] saveGameSearcher(String savesFolderPath){
        File savesFolder = new File(savesFolderPath);

        return savesFolder.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                    { 
                	 boolean b = false;
            		 if( filename.endsWith(".xml") && filename.toLowerCase().contains(filterString.toLowerCase())) {
            			 b = true;
            		 }
                	 return b;
                	}
        } );
	}
	
	private class SavesPanelElement extends JPanel {
		private static final long serialVersionUID = -5017892450502876588L;
		JButton btnDelete;
		JButton btnLoad;

		public SavesPanelElement(File save, JPanel savesPanelContainer) {
			setOpaque(false);
			setBackground(new Color(0,0,0,0));
			setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
			setDoubleBuffered(true);
			MigLayout miglay = new MigLayout("insets 4 4 4 4, gap 4px 4px", "[135px]["+(savesPanelContainer.getWidth()-202)+",fill]", "[fill][fill][fill][fill]");
			
			this.setLayout(miglay);
			
			JLabel lblSave = new JLabel("Save: "+save.getName().substring(0, (save.getName().length()-4)) );
			add(lblSave, "cell 0 0 2 1");
			
			btnDelete = new JButton_01("Delete");
			btnDelete.setBackground(Color.RED);
			btnDelete.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(new Boolean(Core.getSetting(Core.SETTING_ASK_SAVE_DELETE))) {
						if(JOptionPane.showConfirmDialog(null, "Sure you want to delete?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)  {
							save.delete();
							btnDelete.setText("Deleted!");
							btnDelete.setEnabled(false);
							btnLoad.setText("Deleted!");
							btnLoad.setEnabled(false);
						}
					} else {
						save.delete();
						btnDelete.setText("Deleted!");
						btnDelete.setEnabled(false);
						btnLoad.setText("Deleted!");
						btnLoad.setEnabled(false);
					}
					
				}
			});
			add(btnDelete, "cell 0 1,alignx left,aligny top");
			
			btnLoad = new JButton_01("Load");
			btnLoad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new XMLSaveAndLoad(save.getName().substring(0, save.getName().length()-4));
					XMLSaveAndLoad.loadGame(save);
					ControlInput.menuChanger.actionPerformed(new ActionEvent(this, 0, "frame.gamePanels.MainGamePanel"));
				}
			});
			add(btnLoad, "cell 0 3,grow");
			
		}
		@Override
		public void paint(Graphics g) {
			g.drawImage(ResourceManager.getBackground_02(), 0, 0, getWidth(), getHeight(), null);
			super.paint(g);
		}
	}

}

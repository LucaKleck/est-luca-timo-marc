package frame.menuPanels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import core.Core;
import core.actions.ControlInput;
import frame.MainJFrame;
import frame.customPresets.JButton_01;
import frame.customPresets.JPanelCustomBg;
import frame.graphics.ResourceManager;
import net.miginfocom.swing.MigLayout;

/**
 * Here the user can load a save by typing the save-name and searching the save then/or clicking on a
 * saved game
 * 
 * @author Luca Kleck
 * @see frame.MainJFrame
 */
public class LoadMenuPanel extends JPanelCustomBg implements ComponentListener, DocumentListener {
	private static final long serialVersionUID = 114L;
	
	private JTextField filterTextField;
	private JPanel SavesPanelContainer;
	
	public LoadMenuPanel() {
		super(ResourceManager.getBackground_05());
		setLayout(new MigLayout("insets 0 0 0 0", "[33%][33%][33%]", "[100%,fill]"));
		
		JPanelCustomBg container = new JPanelCustomBg(ResourceManager.getBackground_03(), true);
		container.setLayout(new MigLayout("insets 30 30 30 30", "[100%]", "[100%,fill][fill][fill]"));
		add(container, "cell 1 0,grow");
		
		JButton btnBack = new JButton_01("Back");
		btnBack.setActionCommand("frame.menuPanels.MainMenuPanel");
		btnBack.addActionListener(ControlInput.menuChanger);
		
		SavesPanelContainer  = new JPanel();
		SavesPanelContainer.setBackground(new Color(0,0,0,0));
		SavesPanelContainer.setOpaque(false);
		container.add(SavesPanelContainer, "cell 0 0,grow");
		SavesPanelContainer.setLayout(new MigLayout("", "[100%,fill]", "[100%,fill]"));
		
		JLabel lblSearch = new JLabel(MainJFrame.htmlStyleDefault+"Search");
		container.add(lblSearch, "flowx,cell 0 1,alignx left,aligny center");
		
		filterTextField = new JTextField();
		filterTextField.setColumns(1);
		filterTextField.getDocument().addDocumentListener(this);
		filterTextField.setBackground(new Color(182, 137, 100));
		filterTextField.setForeground(Color.YELLOW);
		filterTextField.setBorder(BorderFactory.createLineBorder(getBackground().darker().darker(), 4, true));
		container.add(filterTextField, "cell 0 1,grow");
		
		SavesPanelContainer.add(new SavesPanel(SavesPanelContainer, filterTextField.getText()), "cell 0 0,grow");
		
		
		JButton btnRefresh = new JButton_01("Refresh");
		
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SavesPanelContainer.removeAll();
				SavesPanelContainer.add(new SavesPanel(SavesPanelContainer, filterTextField.getText()));
			}
		});
		this.addComponentListener(this);
		
		JCheckBox chckbxAskBeforeDelete = new JCheckBox(MainJFrame.htmlStyleDefault+"Ask before deleting?");
		chckbxAskBeforeDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean b = new Boolean(Core.loadSetting(Core.SETTING_ASK_SAVE_DELETE));
				b = !b;
				Core.saveSetting(Core.SETTING_ASK_SAVE_DELETE, b.toString());
			}
		});
		chckbxAskBeforeDelete.setOpaque(false);
		chckbxAskBeforeDelete.setSelected(new Boolean(Core.loadSetting(Core.SETTING_ASK_SAVE_DELETE)));
		container.add(chckbxAskBeforeDelete, "flowx,cell 0 2,alignx right,aligny center");
		container.add(btnRefresh, "cell 0 2");
		container.add(btnBack, "cell 0 2");
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		SavesPanelContainer.removeAll();
		SavesPanelContainer.add(new SavesPanel(SavesPanelContainer, filterTextField.getText()));
		Core.getMainJFrame().repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		SavesPanelContainer.removeAll();
		SavesPanelContainer.add(new SavesPanel(SavesPanelContainer, filterTextField.getText()));
		Core.getMainJFrame().repaint();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		SavesPanelContainer.removeAll();
		SavesPanelContainer.add(new SavesPanel(SavesPanelContainer, filterTextField.getText()));
		Core.getMainJFrame().repaint();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		SavesPanelContainer.removeAll();
		SavesPanelContainer.add(new SavesPanel(SavesPanelContainer, filterTextField.getText()));
		Core.getMainJFrame().repaint();
	}
	
	
}
package sampleui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class App implements SelectionListener{
	private JFrame frame = null;
	private JLabel status = null;
	private FileFilter filter;
	private Mesh mesh = null;
	
	private Point optPos = new Point(0, 0);
	
	private class MeshColor{
		MeshColor(String s, Color c){
			name = s;
			color = c;
		}
		public String name;
		public Color color;
	}
	
	private MeshColor[] mesh_colors;
	
	public App()
	{
		filter = new FileFilter(){
			public String getDescription() {
				return "Object files *.obj";
			}
			
			public boolean accept(File f) {
				String name = f.getName();
				
				if(f.isDirectory())return true;
				
				if(name.length() >= 4 &&
						name.substring(name.length() - 4, name.length()).equals(".obj"))
					return true;
				
				return false;
			}
		};
		
		mesh_colors = new MeshColor[4];
		
		mesh_colors[0] = new MeshColor("edge", Color.blue);
		mesh_colors[1] = new MeshColor("unconnected edge", Color.red);
		mesh_colors[2] = new MeshColor("face", Color.gray);
		mesh_colors[3] = new MeshColor("peat_face", Color.green);
	}
	
	private JToolBar createToolBar()
	{
		JButton buttonSave = new JButton("save");
		buttonSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				save(arg0);
			}
			
		});

		JButton buttonLoad = new JButton("load");
		buttonLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				load(arg0);
			}
		});
		
		JButton buttonOpt = new JButton("opt");
		buttonOpt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				showOptions(e);
			}
		});
		
		JToolBar toolbar = new JToolBar("toolbar");
		toolbar.add(buttonSave);
		toolbar.add(buttonLoad);
		toolbar.add(buttonOpt);
		
		return toolbar;
	}
	
	public void setupUI()
	{
		JToolBar toolbar = createToolBar();
		
		status = new JLabel("status");
		JToolBar statusbar = new JToolBar("statusbar");
		statusbar.add(status);
		
		JPanel infoPane = new JPanel();
		infoPane.setMinimumSize(new Dimension(100, 400));
		infoPane.add(new JButton("cut"));
		infoPane.add(new JLabel("view as"));
		JComboBox viewType = new JComboBox(new String[] {"textured", "wired"});
		infoPane.add(viewType);
		
		JPanel drawingPane = new DrawingPane(this);
		//drawingPane.setMinimumSize(new Dimension(400, 400));
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setDividerLocation(150);
		split.setDividerSize(10);
		split.setLeftComponent(infoPane);
		split.setRightComponent(drawingPane);
		
		frame = new JFrame("woa");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container pane = frame.getContentPane();
		
		pane.add(toolbar, BorderLayout.PAGE_START);
		pane.add(split, BorderLayout.CENTER);
		pane.add(statusbar, BorderLayout.PAGE_END);

		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		final App app = new App();
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				app.setupUI();
			}
		});
	}
	
	private void save(ActionEvent e)
	{
		JFileChooser f = new JFileChooser();
		f.setFileFilter(filter);
		if(f.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
			mesh.save(f.getSelectedFile().getName());
		}
	}
	
	private void load(ActionEvent e)
	{
		JFileChooser f = new JFileChooser();
		f.setFileFilter(filter);
		if(f.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			String filename = f.getSelectedFile().getName();
			try{
				mesh = Mesh.load(filename);
			}catch(Exception ex){
				System.out.println("failed to load Mesh from " + filename + " error " + ex);
			}
		}
	}
	
	private void showOptions(ActionEvent e)
	{
		final JDialog opt = new JDialog(frame, "woa options");
		final JPanel colors = new JPanel(new GridLayout(mesh_colors.length, 2));
		
		JButton buttonOk = new JButton("ok");
		buttonOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Component[] comps = colors.getComponents();
				for(Component comp : comps){
					if(comp instanceof JButton){
						Color color = comp.getForeground();
					}
				}
				optPos = opt.getLocation();
				opt.setVisible(false);
			}
		});
		JButton buttonCancel = new JButton("cancel");
		buttonCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				optPos = opt.getLocation();
				opt.setVisible(false);
			}
		});
				
		JPanel pane = new JPanel();
		pane.add(buttonOk);
		pane.add(buttonCancel);

		for(final MeshColor c : mesh_colors){
			final JButton button = new JButton("\u25A0 change");
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Color newcolor = JColorChooser.showDialog(opt, c.name, button.getForeground());
					button.setForeground(newcolor);
				}
			});
			button.setForeground(c.color);
			colors.add(new JLabel(c.name));
			colors.add(button);
		}
		
		opt.setLayout(new BorderLayout());
		opt.add(pane, BorderLayout.PAGE_END);
		opt.add(colors, BorderLayout.CENTER);
		opt.pack();
		opt.setLocation(optPos);
		opt.setVisible(true);
	}

	@Override
	public void actionSelect(int x0, int y0, int x1, int y1, int width, int height)
	{
		if(mesh != null){
			float a = 0, b = 0, c = 0, d = 0;
			mesh.cut(a, b, c, d);
		}
	}
}

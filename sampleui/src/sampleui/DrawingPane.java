package sampleui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class DrawingPane extends JPanel implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7393917522141198721L;
	private boolean isDragging = false;
	private boolean isSelecting = false;
	private int dragStartX;
	private int dragStartY;
	private int dragStopX;
	private int dragStopY;
	private SelectionListener selectionListener;

	public DrawingPane(SelectionListener obj)
	{
		selectionListener = obj;
		setPreferredSize(new Dimension(400, 400));
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

	public void setSelection(boolean mode)
	{
		isSelecting = mode;
	}
		
	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("mouse dragged " + arg0.getX() + " " + arg0.getY());
		if(!isDragging){
			dragStartX = e.getX();
			dragStartY = e.getY();
			isDragging = true;
		}
		else{
			dragStopX = e.getX();
			dragStopY = e.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("mouse clicked " + arg0.getX() + " " + arg0.getY());
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		//System.out.println("mouse released " + arg0.getX() + " " + arg0.getY());
		if(isDragging){
			dragStopX = arg0.getX();
			dragStopY = arg0.getY();
			isDragging = false;
			System.out.println("drag from " + dragStartX + "x" + dragStartY + " to " +
					dragStopX + "x" + dragStopY);
			if(isSelecting && selectionListener != null)
				selectionListener.actionSelect(dragStartX, dragStartY, dragStopX, dragStopY, getWidth(), getHeight());
		}
	}
}

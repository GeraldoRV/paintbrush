package gui.trail;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class MouseTrail {
    private Graphics trailing;
    private Color color;
    private javax.swing.JPanel panel;
    private String shape;
    private LinkedList<Integer[]> coord; 
    
    public MouseTrail(javax.swing.JPanel panel, String shape) {
        this.shape = shape;
        this.panel = panel;
        coord = new LinkedList<>();
    }
    
    public void execTrail() throws InterruptedException {
        trailing = panel.getGraphics();
        this.paint();
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setShape(String shape) {
        this.shape = shape;
    }
    
    public Graphics getTrailingObject() {
        return this.trailing;
    }
    
    public int getQueueSize() {
        return coord.size();
    }
    
    public void setPanel(javax.swing.JPanel panel) {
        this.panel = panel;
    }
    
    public void clearQueue() {
        coord.clear();
    }
    
    private void delay(int time) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(time);
    }
    
    private void queueManage() throws InterruptedException {
        switch(shape) {
            case "Triangle":
                this.queueManageTriangule();
                break;
            default:
                this.queueManageRectOval();
                break;
        }
        
        // TODO: Delete this testing code
        for (Integer[] vec : coord) {
            System.out.println("[" + vec[0] + ", " + vec[1] + "]");
        }
    }
    
    private void queueManageRectOval() throws InterruptedException {
        if(coord.size() <= 5)
            coord.add(new Integer[]{
                        new Integer((int)panel.getMousePosition().getX()),
                        new Integer((int)panel.getMousePosition().getY())
                    });
        else {
            coord.removeFirst();
            coord.add(new Integer[]{
                        new Integer((int)panel.getMousePosition().getX()),
                        new Integer((int)panel.getMousePosition().getY())
                    });
            this.delay(80);
            this.refresh();
        }
    }
    
    private void queueManageTriangule() throws InterruptedException {
        if(coord.size() <= 5)
            coord.add(new Integer[]{
                        new Integer((int)panel.getMousePosition().getX()),
                        new Integer((int)panel.getMousePosition().getX()-12),
                        new Integer((int)panel.getMousePosition().getX()+12),
                        new Integer((int)panel.getMousePosition().getY()),
                        new Integer((int)panel.getMousePosition().getY()+18),
                        new Integer((int)panel.getMousePosition().getY()+18)
                    });
        else {
            coord.removeFirst();
            coord.add(new Integer[]{
                        new Integer((int)panel.getMousePosition().getX()),
                        new Integer((int)panel.getMousePosition().getX()-12),
                        new Integer((int)panel.getMousePosition().getX()+12),
                        new Integer((int)panel.getMousePosition().getY()),
                        new Integer((int)panel.getMousePosition().getY()+18),
                        new Integer((int)panel.getMousePosition().getY()+18)
                    });
            this.delay(80);
            this.refresh();
        }
    }
    
    public void refresh() {
        panel.repaint();
        coord.removeFirst();
    }
    
    private void paint() throws InterruptedException {
        switch(shape) {
            case "Circle":
                this.paintOval();
                this.queueManage();
                break;
                
            case "Square":
                this.paintRect();
                this.queueManage();
                break;
                
            case "Triangle":
                this.paintTriangle();
                this.queueManage();
                break;
                
            default:
                break;
        }
    }
    
    private void paintOval() {
        coord.stream().map((pos) -> {
            trailing.setColor(color);
            return pos;
        }).forEachOrdered((pos) -> {
            trailing.fillOval(
                    pos[0],
                    pos[1],
                    12, 
                    12
            );
        });
    }
    
    private void paintRect() {
        coord.stream().map((pos) -> {
            trailing.setColor(color);
            return pos;
        }).forEachOrdered((pos) -> {
            trailing.fillRect(
                    pos[0],
                    pos[1],
                    12, 
                    12
            );
        });
    }
    
    private void paintTriangle() {
        trailing.setColor(color);
        
        int[] x = new int[3];
        int[] y = new int[3];
        
        for (Integer[] pos : coord) {
            System.out.println("Tengo tamaño : " + pos.length);
            for (int i = 0; i < pos.length; i++) {
                if(i < 3)
                    x[i%3] = pos[i];
                else
                    y[i%3] = pos[i];
            }
            
            trailing.fillPolygon(new Polygon(x, y, 3));
        }
                
    }
}

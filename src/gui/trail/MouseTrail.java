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
    private final LinkedList<Integer[]> coord;

    public MouseTrail(javax.swing.JPanel panel, String shape) {
        this.shape = shape;
        this.panel = panel;
        coord = new LinkedList<>();
    }

    public void execTrail() throws InterruptedException {
        trailing = panel.getGraphics();
        paint();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Graphics getTrailingObject() {
        return trailing;
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
        switch (shape) {
            case "Triangle":
                queueManageTriangule();
                break;
            default:
                queueManageRectOval();
                break;
        }
    }

    private void queueManageRectOval() throws InterruptedException {
        if (coord.size() <= 5) {
            coord.add(new Integer[]{
                (int) panel.getMousePosition().getX(),
                (int) panel.getMousePosition().getY()});
        } else {
            coord.removeFirst();
            coord.add(new Integer[]{
                (int) panel.getMousePosition().getX(),
                (int) panel.getMousePosition().getY()});
            delay(80);
            refresh();
        }
    }

    private void queueManageTriangule() throws InterruptedException {
        if (coord.size() <= 5) {
            coord.add(new Integer[]{
                (int) panel.getMousePosition().getX(),
                (int) panel.getMousePosition().getX() - 12,
                (int) panel.getMousePosition().getX() + 12,
                (int) panel.getMousePosition().getY(),
                (int) panel.getMousePosition().getY() + 18,
                (int) panel.getMousePosition().getY() + 18});
        } else {
            coord.removeFirst();
            coord.add(new Integer[]{
                (int) panel.getMousePosition().getX(),
                (int) panel.getMousePosition().getX() - 12,
                (int) panel.getMousePosition().getX() + 12,
                (int) panel.getMousePosition().getY(),
                (int) panel.getMousePosition().getY() + 18,
                (int) panel.getMousePosition().getY() + 18});
            this.delay(80);
            this.refresh();
        }
    }

    public void refresh() {
        panel.repaint();
        coord.removeFirst();
    }

    private void paint() throws InterruptedException {
        switch (shape) {
            case "Circle":
                paintOval();
                break;

            case "Square":
                paintRect();
                break;

            case "Triangle":
                paintTriangle();
                break;

            default:
                break;
        }
        queueManage();
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

        coord.stream().map((pos) -> {
            for (int i = 0; i < pos.length; i++) {
                if (i < 3) {
                    x[i % 3] = pos[i];
                } else {
                    y[i % 3] = pos[i];
                }
            }
            return pos;
        }).forEachOrdered((_item) -> {
            trailing.fillPolygon(new Polygon(x, y, 3));
        });
    }
}

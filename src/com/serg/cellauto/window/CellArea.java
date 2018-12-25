package com.serg.cellauto.window;

import javax.swing.*;
import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CellArea extends JPanel {
    private int size;
    private Map<Rectangle, Boolean> rectangles = new LinkedHashMap<>();

    public CellArea(int size) {
        super();
        this.size = size;
    }

    public void addRectangle(int x, int y, int width, int height, Boolean isFull) {
        rectangles.put(new Rectangle(x, y, width, height), isFull);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.size, this.size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
       /* for (Map.Entry<Rectangle, Boolean> rect : rectangles.entrySet()) {
            g2.setColor(rect.getValue() ? Color.GREEN : Color.WHITE);
            g2.fill(rect.getKey());
        }*/
        try {
            rectangles.forEach((key, value) -> {
                g2.setColor(value ? Color.MAGENTA : Color.WHITE);
                g2.fill(key);
            });
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException!");
        }
    }
}

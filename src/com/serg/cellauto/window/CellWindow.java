package com.serg.cellauto.window;

import com.serg.cellauto.CellAutoUtil;
import com.serg.cellauto.enumeration.State;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CellWindow extends JFrame {

    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_WIDTH = 700;
    private JPanel container;
    private CellArea cellArea; //JPanel
    private JPanel controlPanel;
    private JLabel jlabel;
    private int areaWidth;
    private int controlPanelWidth;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private int iterationRepaint = 1;
    private Map<Integer, Double> expDataField = new LinkedHashMap<>();
    private Integer time = 13;

    public CellWindow() {
        super("Cellular Automata");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        areaWidth = DEFAULT_HEIGHT;
        this.cellArea = new CellArea(areaWidth);
        container.add(cellArea);
        initControlPanel();
        getContentPane().add(container);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initControlPanel() {
        this.controlPanel = new JPanel();
        controlPanelWidth = width - areaWidth;
        controlPanel.setPreferredSize(new Dimension(controlPanelWidth, height));
        controlPanel.setBackground(Color.LIGHT_GRAY);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(150, 30));

        JButton button = new JButton();
        button.setPreferredSize(new Dimension(150, 40));
        button.setText("Установить time");
        button.addActionListener(listener -> {
            time = Integer.valueOf(textField.getText());

        });

        JButton graphicOpenBtn = new JButton();
        graphicOpenBtn.setPreferredSize(new Dimension(150, 40));
        graphicOpenBtn.setText("Открыть графики");
        //theory variable
        int deltaX = 25;
        graphicOpenBtn.addActionListener(listener -> paintGraphics(time, deltaX));
        jlabel = new JLabel();
        jlabel.setPreferredSize(new Dimension(140, 30));
        jlabel.setText("iteration - 0");
        controlPanel.add(jlabel);
        controlPanel.add(addVerticalIndent(controlPanelWidth, 80));
        controlPanel.add(textField);
        controlPanel.add(button);
        controlPanel.add(addVerticalIndent(controlPanelWidth, 30));
        controlPanel.add(graphicOpenBtn);
        this.container.add(controlPanel);
    }

    private Component addVerticalIndent(int width, int height) {
        return Box.createRigidArea(new Dimension(width, height));
    }

    public void drawCellAutoIteration(State[][] area, int drawSize, long iteration) {
        int i = 0;
        int j = 0;

        for (State[] states : area) {
            for (State state : states) {
                cellArea.addRectangle(i, j, drawSize, drawSize, state == State.FULL);
                j += drawSize;
            }
            j = 0;
            i += drawSize;
        }
        if (iteration % this.iterationRepaint == 0) {
            cellArea.repaint();
        }

        jlabel.setText("iteration - " + iteration);
        if (iteration % 20 == 0) {
            expDataField = calculateExperimentData(area, iteration);
        }
    }

    private Map<Integer, Double> calculateData(int time, int deltaX) {
        Double Q = 160.0;
        Double D = 1.5;
        Map<Integer, Double> data = new HashMap<>();
        for (int x = 0; x < deltaX; x++) {
            Double y = (Q / Math.sqrt(Math.PI * D * time)) * Math.exp(-x * x / (4 * D * time));
            data.put(x, y);
        }
        return data;
    }

    private Map<Integer, Double> calculateExperimentData(State[][] area, long iteration) {
        int delta = (CellAutoUtil.getEndPoint(area.length) - CellAutoUtil.getStartPoint(area.length)) / 2;//deltaY = 60
        int centerPoint = CellAutoUtil.getStartPoint(area.length) + delta;
        int endPoint = CellAutoUtil.getEndPoint(area.length) + delta; //с запасом чтобы захватить расширение!
        int cellNumber = 0;
        int deltaVertical = 4;
        int x = 0;
        Map<Integer, Double> data = new LinkedHashMap<>();
        for (int i = centerPoint - deltaVertical / 10; i < centerPoint + deltaVertical; i++) { //по вертикали 8 клеток
            for (int j = centerPoint; j < endPoint; j++) {         //по горизонтали
                if (area[i][j] == State.FULL) {
                    cellNumber++;
                }
                if (j % 5 == 0) {  //подсчитывыем на каждой пятой клетке( завршаем подсчет блока) (12 точек)
                    data.merge(x++, (double) cellNumber, (oldV, newV) -> oldV + newV);
                    cellNumber = 0;
                }
            }
            cellNumber = 0;
            x = 0;
        }
        return data;
    }

    private void paintGraphics(Integer time, Integer deltaX) {
        JFrame graphicsFrame = new JFrame("Concentration graphics");
        JFreeChart jFreeChart = ChartFactory.createXYLineChart(
                "Concentration",
                "delta x, mkm",
                "C(x)",
                createDataSet(
                        calculateData(time, deltaX),
                        expDataField,
                        new HashMap<>()//interpolateData(expDataField)
                ),
                PlotOrientation.VERTICAL,
                true, true, false
        );
        ChartPanel panel = new ChartPanel(jFreeChart);
        panel.setPreferredSize(new Dimension(400, 400));

        graphicsFrame.getContentPane().add(panel);
        graphicsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        graphicsFrame.setBounds(0, 0, 500, 500);
        graphicsFrame.setLocationRelativeTo(null);
        graphicsFrame.setVisible(true);
    }

    private XYDataset createDataSet(Map<Integer, Double> data,
                                    Map<Integer, Double> expDataField,
                                    Map<Double, Double> expApproxy) {
        XYSeriesCollection collection = new XYSeriesCollection();
        final XYSeries expConcentration = new XYSeries("experiment");
        expDataField.forEach(expConcentration::add);
        collection.addSeries(expConcentration);

        final XYSeries xyApproxy = new XYSeries("theory");
        data.forEach(xyApproxy::add);
        collection.addSeries(xyApproxy);
        /*
        final XYSeries concentration = new XYSeries("analytic");
        data.forEach(concentration::add);
        collection.addSeries(concentration);
        */
        return collection;
    }

    private Map<Double, Double> interpolateData(Map<Integer, Double> data) {
        Map<Double, Double> result = new LinkedHashMap<>();


        PolynomialFunctionLagrangeForm form = new PolynomialFunctionLagrangeForm(
                data.keySet().stream().mapToDouble(Number::doubleValue).toArray(),
                data.values().stream().mapToDouble(Number::doubleValue).toArray()
        );
        int d = form.degree();
        System.out.println(d);
        /*for (double i = 0; i < data.size(); i++) {
            result.put(i, );
        }*/
        return result;
    }

}

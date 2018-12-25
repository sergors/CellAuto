package com.serg;

import com.serg.cellauto.CellAutoBuilder;
import com.serg.cellauto.window.CellWindow;

public class Main {

    public static void main(String[] args) {
        CellAutoBuilder cellAutoBuilder = new CellAutoBuilder();
        CellWindow cellWindow = new CellWindow();
        cellAutoBuilder.buildCellularAutomata(400, 100000, 0.9, 1, cellWindow);
    }
}

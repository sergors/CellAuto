package com.serg.cellauto;

import com.serg.cellauto.window.CellWindow;

public class CellAutoBuilder {

    public void buildCellularAutomata(int size,
                                      long iteration,
                                      double randPointer,
                                      int drawSize,
                                      CellWindow cellWindow) {
        CellularAutomataImpl cellAuto = new CellularAutomataImpl(size);
        cellAuto.startRandomInitialization(randPointer);
        long iter = 0;
        while (iter < iteration) {
            iter = cellAuto.calculateNextIteration();
            cellWindow.drawCellAutoIteration(cellAuto.getArea(), drawSize, iter);
        }
    }
}

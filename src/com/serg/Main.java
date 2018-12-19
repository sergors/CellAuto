package com.serg;

import com.serg.cellauto.CellAutoService;
import com.serg.cellauto.CellularAutomataImpl;

public class Main {

    public static void main(String[] args) {
        CellAutoService cellAutoService = new CellAutoService();

        CellularAutomataImpl cellAuto = new CellularAutomataImpl(20);
        cellAutoService.buildCellularAutomat(cellAuto);

    }
}

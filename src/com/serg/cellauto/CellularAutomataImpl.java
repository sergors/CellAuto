package com.serg.cellauto;

import com.serg.cellauto.enumeration.State;
import com.serg.cellauto.template.CellularAutomat;

import java.util.Random;

public class CellularAutomataImpl implements CellularAutomat {

    private final Random random = new Random();
    private final int size;
    private long iteration;
    private State[][] area;

    public CellularAutomataImpl(int size) {
        this.size = size;
        this.iteration = 0;
        this.area = new State[size][];
        for (int i = 0; i < size; i++) {
            area[i] = new State[size];
        }
    }

    public State[][] getArea() {
        return area;
    }

    public void startRandomInitialization(double randPointer) {
        Random rnd = new Random();

        int startX = CellAutoUtil.getStartPoint(size);
        int startY = CellAutoUtil.getStartPoint(size);

        int endX = CellAutoUtil.getEndPoint(size);
        int endY = CellAutoUtil.getEndPoint(size);

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {

                if (rnd.nextDouble() > randPointer) {
                    area[i][j] = State.EMPTY;
                } else {
                    area[i][j] = State.FULL;
                }
            }
        }
    }

    public long calculateNextIteration() {
        calculateTact(0);
        calculateTact(1);
        return ++iteration;
    }

    private void calculateTact(int startIndex) {
        for (int i = startIndex; i < size - 2; i += 2) {
            for (int j = startIndex; j < size - 2; j += 2) {
                calculateBlock(i, j);
            }
        }
    }

    private void calculateBlock(int i, int j) {
        State v0 = area[i][j];
        State v1 = area[i + 1][j];
        State v2 = area[i + 1][j + 1];
        State v3 = area[i][j + 1];
        if (random.nextDouble() < 0.5) { //рандом на выбор подстановки(вероятностное условие)
            area[i][j] = v1;
            area[i + 1][j] = v2;
            area[i + 1][j + 1] = v3;
            area[i][j + 1] = v0;
        } else {
            area[i][j] = v3;
            area[i + 1][j] = v0;
            area[i + 1][j + 1] = v1;
            area[i][j + 1] = v2;
        }
    }

}

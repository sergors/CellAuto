package com.serg.cellauto;

import com.serg.cellauto.enumeration.State;
import com.serg.cellauto.template.CellularAutomat;

import java.util.Random;

public class CellularAutomataImpl implements CellularAutomat {

    private final Random random = new Random();
    private final int size;
    private State[][] area;

    public CellularAutomataImpl(int size) {
        this.size = size;
        this.area = new State[size][];
        for (State[] column : this.area) {
            column = new State[size];
        }
    }

    public void startRandomInitialization() {
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (rnd.nextDouble() > 0.8) {
                    area[i][j] = State.EMPTY;
                } else {
                    area[i][j] = State.FULL;
                }
            }
        }
    }

    @Override
    public void calculateBlock(int i, int j) {
        State v0 = area[i][j];
        State v1 = area[i + 1][j];
        State v2 = area[i + 1][j + 1];
        State v3 = area[i][j + 1];
        if (random.nextDouble() < 0.5) { //рандом на выбор подстановки(вероятностное условие)
            area[i][j] = v1;
            area[+1][j] = v2;
            area[i + 1][j + 1] = v3;
            area[i][j + 1] = v0;
        } else {
            area[i][j] = v3;
            area[+1][j] = v0;
            area[i + 1][j + 1] = v1;
            area[i][j + 1] = v2;
        }
    }

    @Override
    public void calculateNextIteration() {
        for (int i = 0; i < size; i += 2) {
            for (int j = 0; j < size; j += 2) {
                calculateBlock(i, j);
            }
        }
    }
}

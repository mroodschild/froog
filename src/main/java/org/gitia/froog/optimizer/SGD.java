/*
 * Copyright 2018 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitia.froog.optimizer;

import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.optimizer.gradient.Gradient;
import org.gitia.froog.optimizer.gradient.GradientFactory;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SGD extends Backpropagation {

    protected int cantidadBach;
    int batchSize = 0; //tamaño del batch declarado por el usuario
    List<SimpleMatrix> Drop;
    boolean isDropOut = false;

    public SGD() {
    }

    /**
     *
     * @param net neural network to train
     * @param input every row is a feature and every column is a register
     * @param output every row is a feature and every column is a register
     */
    @Override
    public void train(Feedforward net, SimpleMatrix input, SimpleMatrix output) {
        this.net = net;
        this.input = new SimpleMatrix(input);
        this.output = new SimpleMatrix(output);
        init();
        initBatch();
        Clock clock = new Clock();
        for (int i = 0; i < this.epoch; i++) {
            for (int j = 0; j < cantidadBach; j++) {
                clock.start();
                SimpleMatrix bach_in = bachData(j, input);
                SimpleMatrix bach_out = bachData(j, output);
                Activations = (isDropOut) ? net.activationsDropout(bach_in) : net.activations(bach_in);
                costOverall = loss(Activations.get(Activations.size() - 1), bach_out);
                this.cost.add(costOverall);
                gradient.compute(net, Activations, gradW, gradB, bach_in, bach_out);
                updateRule.updateParameters(net, (double) bach_in.numCols(), L2_Lambda, learningRate, gradW, gradB);
                if (iteracion % printFrecuency == 0) {
                    printScreen(clock);
                }
                iteracion++;
            }
        }
    }

    /**
     * by default dropout is false
     * @param drop 
     */
    public void setDropOut(boolean drop) {
        isDropOut = drop;
        if (drop) {
            gradient = GradientFactory.getGradient(Gradient.DROPOUT);
            System.out.println("Dropout:\t" + isDropOut);
        } else {
            gradient = GradientFactory.getGradient(Gradient.STANDARD);
            System.out.println("Dropout:\t" + isDropOut);
        }
    }

    /**
     * Aquí inicializamos el bach, indicamos cuantas partes serán formadas,
     * según el bachSize, esto será guardado en cantidadBach, que luego será
     * utilizado en la selección de los datos.<br>
     *
     * Si bachSize menor o igual a 0, se tomarán todos los datos como tamaño de
     * bachSize.
     *
     */
    protected void initBatch() {
        if (batchSize <= 0) {
            batchSize = this.input.numCols();
            cantidadBach = 1;
        } else {
            cantidadBach = this.input.numCols() / batchSize;
        }
    }

    protected SimpleMatrix bachData(int part, SimpleMatrix data) {
        SimpleMatrix bach;
        if (part < (cantidadBach - 1)) {
            bach = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, batchSize * part + batchSize);
        } else {//como es la última parte tomamos los datos restantes.
            bach = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, SimpleMatrix.END);
        }
        return bach;
    }

    /**
     *
     * @param bachSize por defecto = 0 (sin bach)
     */
    public void setBatchSize(int bachSize) {
        this.batchSize = bachSize;
        System.out.println("Batch Size:\t" + this.batchSize);
    }

    public int getBatchSize() {
        return batchSize;
    }

}

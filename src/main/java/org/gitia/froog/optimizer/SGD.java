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

    //private static final Logger log = LogManager.getLogger(SGD.class);

    protected int cantidadBatch;
    protected int batchSize = 0; //tamaño del batch declarado por el usuario
    protected List<SimpleMatrix> Drop;
    protected boolean isDropOut = false;

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
        int L = net.getLayers().size() - 1;
        for (int i = 0; i < this.epoch; i++) {
            for (int j = 0; j < cantidadBatch; j++) {
                clock.start();
                SimpleMatrix batch_in = batchData(j, input);
                SimpleMatrix batch_out = batchData(j, output);
                //dejar la mascara varias iteraciones
                Activations = (isDropOut) ? net.activationsDropout(batch_in) : net.activations(batch_in);
                //calculamos si necesitamos quitar neuronas o matar pesos.
                costOverall = loss(Activations.get(L), batch_out);
                this.cost.add(costOverall);
                gradient.compute(net, Activations, gradW, gradB, batch_in, batch_out);
                updateRule.updateParameters(net, (double) batch_in.numCols(), L2_Lambda, learningRate, gradW, gradB);
                if (iteracion % printFrecuency == 0) {
                    printScreen(net, Activations.get(L), batch_out, clock, inputTest, outputTest, iteracion, testFrecuency, classification);
                }
                iteracion++;
            }
        }
        System.out.println("Finish Training, Resume:");
        printScreen(net, Activations.get(L), batchData(cantidadBatch-1, output), clock, inputTest, outputTest, iteracion, testFrecuency, classification);
    }

    /**
     * by default dropout is false
     *
     * @param drop
     */
    public void setDropOut(boolean drop) {
        isDropOut = drop;
        gradient = (drop)? GradientFactory.getGradient(Gradient.DROPOUT) : GradientFactory.getGradient(Gradient.STANDARD);
    }

    /**
     * Aquí inicializamos el batch, indicamos cuantas partes serán formadas,
     * según el batchSize, esto será guardado en cantidadbatch, que luego será
     * utilizado en la selección de los datos.<br>
     *
     * Si batchSize menor o igual a 0, se tomarán todos los datos como tamaño de
     * batchSize.
     *
     */
    protected void initBatch() {
        if (batchSize <= 0) {
            batchSize = this.input.numCols();
            cantidadBatch = 1;
        } else {
            cantidadBatch = this.input.numCols() / batchSize;
        }
    }

    protected SimpleMatrix batchData(int part, SimpleMatrix data) {
        SimpleMatrix batch;
        if (part < (cantidadBatch - 1)) {
            batch = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, batchSize * part + batchSize);
        } else {//como es la última parte tomamos los datos restantes.
            batch = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, SimpleMatrix.END);
        }
        return batch;
    }

    /**
     *
     * @param batchSize por defecto = 0 (sin batch)
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
        System.out.println("Batch Size:\t" + this.batchSize);
    }

    public int getBatchSize() {
        return batchSize;
    }

}

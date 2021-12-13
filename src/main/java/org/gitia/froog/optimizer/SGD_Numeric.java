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

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.optimizer.gradient.NumericGradient;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SGD_Numeric extends SGD {

    public SGD_Numeric() {
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
            for (int j = 0; j < cantidadBatch; j++) {
                clock.start();
                SimpleMatrix batch_in = batchData(j, input);
                SimpleMatrix batch_out = batchData(j, output);
                Activations = net.activations(batch_in);
                costOverall = loss(Activations.get(Activations.size() - 1), batch_out);
                this.cost.add(costOverall);
                NumericGradient ng = new NumericGradient();
                ng.compute(net, gradW, gradB, lossFunction, batch_in, batch_out);
                gradient.compute(net, Activations, gradW, gradB, batch_in, batch_out);
                updateRule.updateParameters(net, (double) batch_in.numCols(), L2_Lambda, learningRate, gradW, gradB);
                if (iteracion % printFrecuency == 0) {
                    printScreen(net, batch_in, batch_out, clock, inputTest, outputTest, iteracion, testFrecuency, classification);
                }
                iteracion++;
            }
        }
    }
}

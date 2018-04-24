/*
 * The MIT License
 *
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gitia.froog.trainingalgorithm;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.trainingalgorithm.gradient.NumericGradient;

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
            for (int j = 0; j < cantidadBach; j++) {
                clock.start();
                SimpleMatrix bach_in = bachData(j, input);
                SimpleMatrix bach_out = bachData(j, output);
                Activations = net.activations(bach_in);
                costOverall = loss(Activations.get(Activations.size() - 1), bach_out);
                this.cost.add(costOverall);
                //computeGradient(bach_in, bach_out);
                NumericGradient ng = new NumericGradient();
                ng.compute(net, gradW, gradB, lossFunction, bach_in, bach_out);
                gradient.compute(net, Activations, gradW, gradB, bach_in, bach_out);
                updateRule.updateParameters(net, (double) bach_in.numCols(), L2_Lambda, learningRate, gradW, gradB);
                if (iteracion % printFrecuency == 0) {
                    printScreen(clock);
                }
                iteracion++;
            }
        }
    }
}

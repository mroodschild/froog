/*
 * Copyright 2019
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
package org.gitia.froog.layer;

import java.util.Random;
import java.util.stream.IntStream;
import org.ejml.data.MatrixType;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.initialization.WeightInit;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class BatchNormalization implements Layer {

    protected SimpleMatrix gamma;//W[neuronas x 1]
    protected SimpleMatrix Beta;//B[neuronas x 1]

    protected Random random = new Random();

    /**
     *
     */
    public BatchNormalization() {
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input number of inputs
     * @param output number of neurons
     * @param WeightInit
     * @param random
     */
    public BatchNormalization(int input, int output, String WeightInit, Random random) {
        this(input, output, WeightInit, 0, random);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input number of inputs
     * @param output number of neurons
     * @param WeightInit
     * @param keepProb for dropout
     * @param random
     */
    public BatchNormalization(int input, int output, String WeightInit, double keepProb, Random random) {
        this.random = random;
        this.gamma = new SimpleMatrix(output, input);
        this.Beta = new SimpleMatrix(output, 1);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input cantidad de entradas
     * @param output cantidad de neuronas
     * @param random heredado
     */
    public BatchNormalization(int input, int output, Random random) {
        this(input, output, WeightInit.DEFAULT, random);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input
     * @param output
     */
    public BatchNormalization(int input, int output) {
        this(input, output, new Random());
    }

    /**
     *
     * @param W
     * @param B
     */
    public BatchNormalization(SimpleMatrix W, SimpleMatrix B) {
        this.gamma = W;
        this.Beta = B;
    }

    /**
     *
     * Ingresamos la entrada en el formato horizontal
     *
     * @param input
     * @return
     */
    public SimpleMatrix output(double[] input) {
        SimpleMatrix x = new SimpleMatrix(input.length, 1, true, input);
        return output(x);
    }

    /**
     *
     * @param a [out prev layer - m] where m is the amount of data
     * @return a<sub>L(i)</sub> = f(a<sub>L(i)-1</sub>)
     */
    @Override
    public SimpleMatrix output(SimpleMatrix a) {
        
        if (gamma == null) {
            initGamma(a);
        }
        

        return null;
    }
    
    public void initGamma(SimpleMatrix a){
        int row = a.numRows();
        int cols = a.numCols();
        if (gamma == null) {
            gamma = new SimpleMatrix(row, 1, MatrixType.DDRM);
        }
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double sum = 0;
                    for (int j = 0; j < cols; j++) {
                        //aux.set(idx, aux.get(idx)+B.get(i));
                        sum += a.get(idx++);
                    }
                    gamma.set(i, sum / (double) cols);
                });
    }

    /**
     * Retorna el número de neuronas
     *
     * @return
     */
    public int numNeuron() {
        return gamma.numRows();
    }

    /**
     *
     * @return
     */
    public Random getRandom() {
        return random;
    }

    /**
     *
     * @param random
     */
    public void setRandom(Random random) {
        this.random = random;
    }
}

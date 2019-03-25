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
package org.gitia.froog.layer;

import org.gitia.froog.transferfunction.FunctionFactory;
import org.gitia.froog.transferfunction.TransferFunction;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.initialization.WeightInit;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class BatchNormalization implements Layer {

    protected SimpleMatrix Gamma;//W[neuronas x entrada]
    protected SimpleMatrix Beta;//B[neuronas x 1]
    protected TransferFunction function;

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
     * @param funcion transfer function
     * @param WeightInit
     * @param random
     */
    public BatchNormalization(int input, int output, String funcion, String WeightInit, Random random) {
        this(input, output, funcion, WeightInit, 0, random);
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
     * @param funcion transfer function
     * @param WeightInit
     * @param keepProb for dropout
     * @param random
     */
    public BatchNormalization(int input, int output, String funcion, String WeightInit, double keepProb, Random random) {
        this.random = random;
        this.Gamma = new SimpleMatrix(output, input);
        this.Beta = new SimpleMatrix(output, 1);
        this.function = FunctionFactory.getFunction(funcion);
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
     * @param funcion función usadas en las neuronas
     * @param random heredado
     */
    public BatchNormalization(int input, int output, String funcion, Random random) {
        this(input, output, funcion, WeightInit.DEFAULT, random);
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
     * @param funcion
     */
    public BatchNormalization(int input, int output, String funcion) {
        this(input, output, funcion, new Random());
    }

    /**
     *
     * @param W
     * @param B
     * @param funcion
     */
    public BatchNormalization(SimpleMatrix W, SimpleMatrix B, String funcion) {
        this.Gamma = W;
        this.Beta = B;
        this.function = FunctionFactory.getFunction(funcion);
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
        return null;
    }

    /**
     * This function implements the Droupout regularization
     *
     * @param a input to the layer
     * @return return outputs after doing dropuot
     */
    public SimpleMatrix outputDropout(SimpleMatrix a) {
        return null;
    }

    /**
     * Retorna el número de neuronas
     *
     * @return
     */
    public int numNeuron() {
        return Gamma.numRows();
    }

    /**
     * las filas son las neuronas y las columnas son los pesos de cada una.<br>
     * <br>
     * W[neuronas x entrada] = cantidad de pesos en la capa
     *
     * @return
     */
    public SimpleMatrix getW() {
        return Gamma;
    }

    /**
     *
     * @param W
     */
    public void setW(SimpleMatrix W) {
        this.Gamma = W;
    }

    /**
     *
     * @return
     */
    public SimpleMatrix getB() {
        return Beta;
    }

    /**
     *
     * @param B
     */
    public void setB(SimpleMatrix B) {
        this.Beta = B;
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

    /**
     *
     * @return
     */
    public TransferFunction getFunction() {
        return function;
    }

    /**
     *
     * @param function
     */
    public void setFunction(TransferFunction function) {
        this.function = function;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return function.toString();
    }

}

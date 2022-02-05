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
//import org.ejml.data.BMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
//import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.initialization.WeightFactory;
import org.gitia.froog.layer.initialization.WeightInit;
import org.gitia.froog.util.UtilSimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Dense implements Layer {

    protected SimpleMatrix W;//W[neuronas x entrada]
    protected SimpleMatrix B;//B[neuronas x 1]
    protected TransferFunction function;
    protected WeightInit initWeight = WeightFactory.getFunction(WeightInit.DEFAULT);
    protected double keepProb = 0;
    protected SimpleMatrix Drop;
    protected DMatrixSparseCSC DropSparse;
    

    protected Random random = new Random();

    /**
     *
     */
    public Dense() {
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize number of inputs
     * @param units Positive integer, number of neurons
     * @param funcion transfer function
     * @param WeightInit
     * @param random
     */
    public Dense(int inputSize, int units, String funcion, String WeightInit, Random random) {
        this(inputSize, units, funcion, WeightInit, 0, random);
    }
    
    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize number of inputs
     * @param units Positive integer, number of neurons
     * @param funcion transfer function
     * @param WeightInit
     * @param random
     */
    public Dense(int inputSize, int units, TransferFunction funcion, String WeightInit, Random random) {
        this(inputSize, units, funcion, WeightInit, 0, random);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize number of inputs
     * @param units Positive integer, number of neurons
     * @param funcion transfer function
     * @param WeightInit
     * @param keepProb for dropout
     * @param random
     */
    public Dense(int inputSize, int units, String funcion, String WeightInit, double keepProb, Random random) {
        this(inputSize, units, FunctionFactory.getFunction(funcion), WeightInit, keepProb, random);
    }
    
    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize number of inputs
     * @param units Positive integer, number of neurons
     * @param funcion transfer function
     * @param WeightInit
     * @param keepProb for dropout
     * @param random
     */
    public Dense(int inputSize, int units, TransferFunction funcion, String WeightInit, double keepProb, Random random) {
        this.random = random;
        initWeight = WeightFactory.getFunction(WeightInit);
        initWeight.setRandom(this.random);
        this.W = new SimpleMatrix(units, inputSize);
        this.B = new SimpleMatrix(units, 1);
        initWeight.init(W);
        this.keepProb = keepProb;
        this.function = funcion;
        System.out.println("Layer\tinput:\t" + inputSize + "\tneurons:\t" + units + "\tfunction:\t" + funcion.toString() + "\tinit:\t" + initWeight.toString() + "\tkeepProb:\t" + this.keepProb);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize cantidad de entradas
     * @param units cantidad de neuronas
     * @param funcion función usadas en las neuronas
     * @param random heredado
     */
    public Dense(int inputSize, int units, String funcion, Random random) {
        this(inputSize, units, funcion, WeightInit.DEFAULT, random);
    }
    
    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize cantidad de entradas
     * @param units cantidad de neuronas
     * @param funcion función usadas en las neuronas
     * @param random heredado
     */
    public Dense(int inputSize, int units, TransferFunction funcion, Random random) {
        this(inputSize, units, funcion, WeightInit.DEFAULT, 0, random);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize
     * @param units
     * @param funcion
     */
    public Dense(int inputSize, int units, String funcion) {
        this(inputSize, units, funcion, new Random());
    }
    
    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param inputSize
     * @param units
     * @param funcion
     */
    public Dense(int inputSize, int units, TransferFunction funcion) {
        this(inputSize, units, funcion, new Random());
    }

    /**
     *
     * @param W
     * @param B
     * @param funcion
     */
    public Dense(SimpleMatrix W, SimpleMatrix B, String funcion) {
        this.W = W;
        this.B = B;
        this.function = FunctionFactory.getFunction(funcion);
    }
    
    /**
     *
     * @param W
     * @param B
     * @param funcion
     */
    public Dense(SimpleMatrix W, SimpleMatrix B, TransferFunction funcion) {
        this.W = W;
        this.B = B;
        this.function = funcion;
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
        return function.output(outputZ(a));
    }

    /**
     * This function implements the Droupout regularization
     *
     * @param a input to the layer
     * @return return outputs after doing dropuot
     */
    public SimpleMatrix outputDropout(SimpleMatrix a) {//agregarle una variable que indique se calcula la saturacion para generar la mascara
        if (this.keepProb > 0 && this.keepProb < 1) {         
            //calcular la saturación para el dropout
//despues de 1000 iteraciones recalcular la saturacion debería ser calculada en el backpropagation
//calcular el keep_prob generado por ese descarte
//generar un drop flexible, para intercambiar con opciones que se nos ocurra

            Drop = UtilSimpleMatrix.randomOnesSM(W.numRows(), a.numCols(), keepProb, this.random);
            return output(a).elementMult(Drop).divide(keepProb);
        } else {
            return output(a);
        }
    }

    /**
     * Ingresamos la entrada en formato vertical<br>
     * n = f(z)
     *
     * @param z
     * @return
     */
    public SimpleMatrix outputN(SimpleMatrix z) {
        return function.output(z);
    }

    /**
     *
     * @param a entrada a la capa (Número de entradas x 1)
     * @return z = W * A + B (Neuronas de la capa x 1)
     */
    public SimpleMatrix outputZ(SimpleMatrix a) {
        /*
        debería ser function.outputZ(w, a, b), tiene que ser interno porque la
        función softmax, no utiliza el bías.
         */
        return function.outputZ(W, a, B);
    }

    /**
     * Retorna el número de neuronas
     *
     * @return
     */
    public int numNeuron() {
        return W.numRows();
    }

    /**
     * las filas son las neuronas y las columnas son los pesos de cada una.<br>
     * <br>
     * W[neuronas x entrada] = cantidad de pesos en la capa
     *
     * @return
     */
    public SimpleMatrix getW() {
        return W;
    }

    /**
     *
     * @param W
     */
    public void setW(SimpleMatrix W) {
        this.W = W;
    }

    /**
     *
     * @return
     */
    public SimpleMatrix getB() {
        return B;
    }

    /**
     *
     * @param B
     */
    public void setB(SimpleMatrix B) {
        this.B = B;
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
     * @param keepProb
     */
    public void setKeepProb(double keepProb) {
        this.keepProb = keepProb;
    }

    /**
     *
     * @return
     */
    public double getKeepProb() {
        return keepProb;
    }

    /**
     *
     * @param Drop
     */
    public void setDrop(SimpleMatrix Drop) {
        this.Drop = Drop;
    }

    /**
     *
     * @return
     */
    public SimpleMatrix getDrop() {
        return Drop;
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

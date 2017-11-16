/*
 * The MIT License
 *
 * Copyright 2017 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
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
package org.gitia.froog.layer;

import org.gitia.froog.transferfunction.FunctionFactory;
import org.gitia.froog.transferfunction.TransferFunction;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.initialization.WeightFactory;
import org.gitia.froog.layer.initialization.WeightInit;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Layer {

    SimpleMatrix W;//W[neuronas x entrada]
    SimpleMatrix B;//B[neuronas x 1]
    TransferFunction function;
    WeightInit initWeight = WeightFactory.getFunction(WeightInit.DEFAULT);

    protected Random random = new Random();

    public Layer() {
    }

    /**
     * Inicializamos la capa para recibir una entrada y obtener una salida. <br>
     *
     * Las funciones de transferencia son:<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input cantidad de entradas
     * @param output cantidad de neuronas
     * @param funcion función usadas en las neuronas
     * @param WeightInit
     * @param random heredado
     */
    public Layer(int input, int output, String funcion, String WeightInit, Random random) {
        initWeight = WeightFactory.getFunction(WeightInit);
        initWeight.setRandom(random);
        this.W = new SimpleMatrix(output, input);
        this.B = new SimpleMatrix(output, 1);
        initWeight.init(W);
        initWeight.init(B);
        this.function = FunctionFactory.getFunction(funcion);
        System.out.println("Layer\tinput:\t" + input + "\tneurons:\t" + output + "\tfunction:\t" + funcion + "\tinit:\t" + initWeight.toString());
    }

    /**
     * Inicializamos la capa para recibir una entrada y obtener una salida. <br>
     *
     * Las funciones de transferencia son:<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input cantidad de entradas
     * @param output cantidad de neuronas
     * @param funcion función usadas en las neuronas
     * @param random heredado
     */
    public Layer(int input, int output, String funcion, Random random) {
        this(input, output, funcion, WeightInit.DEFAULT, random);
    }

    /**
     * Inicializamos la capa para recibir una entrada y obtener una salida. <br>
     *
     * Las funciones de transferencia son:<br>
     * logsig, tansig, purelim, softplus
     *
     *
     *
     * @param input
     * @param output
     * @param funcion
     */
    public Layer(int input, int output, String funcion) {
        this(input, output, funcion, new Random());
    }

    public Layer(SimpleMatrix W, SimpleMatrix B, String funcion) {
        this.W = W;
        this.B = B;
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
     * Ingresamos la entrada en formato vertical <br>
     * <br>
     *
     * a<sub>L(i)</sub> = -f(W * a<sub>L(i-1)</sub> + B)<br><br>
     * where:<br><br>
     * z = W * a<sub>L(i-1)</sub> + B
     *
     * @param a
     * @return
     */
    public SimpleMatrix output(SimpleMatrix a) {
        return function.output(outputZ(a));
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
//        System.out.println("W");
//        W.printDimensions();
//        System.out.println("a");
//        a.printDimensions();
//        System.out.println("B");
//        B.printDimensions();
        return function.outputZ(W, a, B);
        //return W.mult(a).plus(B);
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

    public void setW(SimpleMatrix W) {
        this.W = W;
    }

    public SimpleMatrix getB() {
        return B;
    }

    public void setB(SimpleMatrix B) {
        this.B = B;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public TransferFunction getFunction() {
        return function;
    }

    public void setFunction(TransferFunction function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return function.toString();
    }

}

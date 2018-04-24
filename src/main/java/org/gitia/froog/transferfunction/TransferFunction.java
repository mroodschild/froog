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
package org.gitia.froog.transferfunction;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public interface TransferFunction {

    public static String PURELIM = "purelim";
    public static String SOFTMAX = "softmax";
    public static String SOFTPLUS = "softplus";
    public static String LOGSIG = "logsig";
    public static String TANSIG = "tansig";
    public static String RELU = "relu";
    public static String PRELU = "prelu";

    /**
     * z = W * X + B
     *
     * @param z
     * @return retorna el resultado de la función deseada
     */
    public SimpleMatrix output(SimpleMatrix z);

    /**
     * 
     * @param W [neurons, inputs]
     * @param a [inputs, m] where m is the amount of data
     * @param B [neurons, 1]
     * @return 
     */
    public SimpleMatrix outputZ(SimpleMatrix W, SimpleMatrix a, SimpleMatrix B);

    public SimpleMatrix derivative(SimpleMatrix a);

    public SimpleMatrix derivative(SimpleMatrix yCalc, SimpleMatrix yObs);

    public SimpleMatrix derivative(SimpleMatrix a, double b);

}

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

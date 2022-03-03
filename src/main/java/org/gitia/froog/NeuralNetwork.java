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
package org.gitia.froog;

import java.util.List;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.Dense;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public interface NeuralNetwork {

    /**
     * get all layers
     * @return
     */
    public List<Dense> layers();

    /**
     * retornamos todas las salidas de todas las capas, cada fila de la entrada
     * y la salida es una característica y cada columna es un dato
     *
     * @param input
     * @return
     */
    public List<SimpleMatrix> activations(SimpleMatrix input);

    /**
     *
     *
     * @param input inputs to evaluate with dropout
     * @return outputs with dropout
     */
    public List<SimpleMatrix> activationsDropout(SimpleMatrix input);

    /**
     * 
     * @param input
     * @return 
     */
    public SimpleMatrix output(double[] input);

    /**
     * 
     * @param input
    * @return 
     */
    public SimpleMatrix output(SimpleMatrix input);

    /**
     *
     * @return W1, W2, ..., Wn
     */
    public SimpleMatrix getParamsW();

    /**
     *
     * @return B1, B2, ...,Bm
     */
    public SimpleMatrix getParamsB();

    /**
     *
     * @return W1, W2, ..., Wn, B1, B2, ...,Bm [1 x n]
     */
    public SimpleMatrix getParameters();

    /**
     * Copiamos los pesos en W2 W3 B2 B3 manteniendo la estructura inicial de
     * esas matrices, la forma de copiado es: <br>
     * <br>
     * 1 2 3 <br>
     * 4 5 6 <br>
     * 7 8 9 <br>
     *
     * @param weights
     */
    public void setParameters(SimpleMatrix weights);

}

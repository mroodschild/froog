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
package org.gitia.froog.lossfunction;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class RMSE implements LossFunction {

    /**
     * Calculamos el costo
     *
     * La entrada y la salida deben estar en formato vertical<br>
     *
     * @param Ycalc
     * @param Yobs
     * @return RMSE = sqrt(||(Yobs - Ycalc)||^2)/2)
     */
    @Override
    public double cost(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        return Math.sqrt(Yobs.minus(Ycalc).normF() / 2);
    }

    /**
     * Ingresan matrices donde cada columna representa un registro y cada fila
     * representa una característica, luego se implementa el CrossEntropy entre
     * las "y<sub>observadas</sub>" y las "y<sub>calculadas</sub>".<br>
     *
     * <br>
     * [Y<sub>11</sub>, Y<sub>12</sub>, ..., Y<sub>1m</sub>]<br>
     * [Y<sub>21</sub>, Y<sub>22</sub>, ..., Y<sub>2m</sub>]<br>
     * .
     * ..<br>
     * [Y<sub>n1</sub>, Y<sub>n2</sub>, ..., Y<sub>nm</sub>]<br>
     *
     * @param Ycalc
     * @param Yobs
     * @return RMSE = sqrt(∑[(||(Yobs - Ycalc)||^2)/2]/m)
     */
    @Override
    public double costAll(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        double m = Ycalc.numCols();
        return Math.sqrt(Yobs.minus(Ycalc).normF()/ (2 * m));
    }

    @Override
    public String toString() {
        return "RMSE";//MSE
    }

}

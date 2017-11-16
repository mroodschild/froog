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
package org.gitia.froog.lossfunction;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class MSE implements LossFunction {

    /**
     * Calculamos el costo
     *
     * La entrada y la salida deben estar en formato vertical<br>
     *
     * @param Ycalc
     * @param Yobs
     * @return (||(Yobs - Ycalc)||^2)/2 MSE
     */
    @Override
    public double cost(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        SimpleMatrix dif = Yobs.minus(Ycalc);//resta elemento a elemento
        return dif.transpose().mult(dif).scale(0.5).get(0);
    }

    /**
     * Ingresan matrices donde cada columna representa una característica de salida
     * y cada fila representa un nuevo registro, luego se implementa el mse
     * entre las "y<sub>observadas</sub>"  y las "y<sub>calculadas</sub>" <br>
     * <br>
     * [Y<sub>11</sub>, Y<sub>12</sub>, Y<sub>13</sub>]<br>
     * [Y<sub>21</sub>, Y<sub>22</sub>, Y<sub>23</sub>]<br>
     * [Y<sub>31</sub>, Y<sub>32</sub>, Y<sub>33</sub>]<br>
     * [..., ..., ...]<br>
     * [Y<sub>n1</sub>, Y<sub>n2</sub>, Y<sub>n3</sub>]<br>
     *
     * @param Ycalc
     * @param Yobs
     * @return MSE = ∑[(||(Yobs - Ycalc)||^2)/2]/m 
     */
    @Override
    public double costAll(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        SimpleMatrix y = Yobs.minus(Ycalc);
        //elementos al cuadrado, sumamos y realizamos la media
        double sum2 = y.elementMult(y).elementSum();
        return sum2 / (2 * y.numRows());
    }

    @Override
    public String toString() {
        return "MSE";//MSE
    }

}

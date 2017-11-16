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
package org.gitia.froog.trainingalgorithm;

import java.util.List;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class SGD extends Backpropagation {

    public SGD() {
    }

    /**
     *
     * calculamos la derivadas calculamos los gradientes retornamos el costo
     *
     */
    @Override
    public void calcularGradientes() {
        deltasZero();
        ND = inputBach.numRows();
        SimpleMatrix in;
        SimpleMatrix yObs;
        for (int i = 0; i < ND; i++) {//Aquí debemos paralelizar
            //extraemos la fila y la ponemos vertical
            in = inputBach.extractVector(true, i).transpose();
            yObs = outputBach.extractVector(true, i).transpose();
            
            //obtenemos la salida de todas las capas para ganar tiempo
            List<SimpleMatrix> outputs = net.outputLayers(in);
            //calculamos los delta
            SimpleMatrix yCalc = outputs.get(outputs.size() - 1);
            derivadaOutputLayers(yCalc, yObs);
            derivadaHiddenLayers(outputs);
            

            //calculamos los gradientes
            SimpleMatrix a_t = in.transpose();
            for (int j = 0; j < gradW.size(); j++) {
                SimpleMatrix d = deriv.get(j);
                //calculamos el gradiente
                gradW.set(j, d.mult(a_t));
                gradB.set(j, d);
                //preparamos la entrada para la siguiente 
                a_t = outputs.get(j).transpose();
            }

            for (int j = 0; j < gradW.size(); j++) {
                //agregamos el delta
                deltasW.set(j, deltasW.get(j).plus(gradW.get(j)));
                deltasB.set(j, deltasB.get(j).plus(gradB.get(j)));
            }

        }
    }

}

/*
 * The MIT License
 *
 * Copyright 2018 Matías Rodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.r;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;
import org.gitia.froog.trainingalgorithm.Backpropagation;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class rFeedforward {

    Feedforward net;
    Backpropagation bp;

    public rFeedforward() {
        net = new Feedforward();
        bp = new Backpropagation();
    }

    public void addLayer(int input, int neuronas, String function) {
        net.addLayer(new Layer(input, neuronas, function));
    }

    public double[] out(double[] matrix, int row, int col) {
        SimpleMatrix m = new SimpleMatrix(row, col, true, matrix);
        return net.output(m).getDDRM().getData();
    }
    
    public void bp(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol){
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, true, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, true, y);
        bp.setEpoch(10);
        bp.train(net, input, output);
    }

    public String hola() {
        return "Hola";
    }

    public void summary() {
        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer l = net.getLayers().get(i);
            System.out.println("Neuronas: "+l.numNeuron()+" Activation: "+l.getFunction().toString());
        }
    }
}

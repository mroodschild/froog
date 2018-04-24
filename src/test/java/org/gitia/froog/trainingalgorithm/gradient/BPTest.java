/*
 * The MIT License
 *
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.trainingalgorithm.gradient;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;
import org.gitia.froog.lossfunction.LossFunction;
import org.gitia.froog.trainingalgorithm.Backpropagation;
import org.gitia.froog.transferfunction.TransferFunction;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class BPTest {

    public static void main(String[] args) {
        double[][] x = {
            {1.62434536, -0.61175641, -0.52817175},
            {-1.07296862, 0.86540763, -2.3015387}};
        double[][] y = {{1, 0, 1}};
        double[][] w1 = {
            {-0.00416758, -0.00056267},
            {-0.02136196, 0.01640271},
            {-0.01793436, -0.00841747},
            {0.00502881, -0.01245288}};
        double[][] b1 = {{0.}, {0.}, {0.}, {0.}};
        double[][] w2 = {{-0.01057952, -0.00909008, 0.00551454, 0.02292208}};
        double[][] b2 = {{0.}};
        SimpleMatrix X = new SimpleMatrix(x);
        SimpleMatrix Y = new SimpleMatrix(y);
        SimpleMatrix W1 = new SimpleMatrix(w1);
        SimpleMatrix B1 = new SimpleMatrix(b1);
        SimpleMatrix W2 = new SimpleMatrix(w2);
        SimpleMatrix B2 = new SimpleMatrix(b2);
        
        Feedforward net = new Feedforward();
        net.addLayer(new Layer(W1, B1, TransferFunction.TANSIG));
        net.addLayer(new Layer(W2, B2, TransferFunction.LOGSIG));
        
        Backpropagation bp = new Backpropagation();
        bp.setEpoch(10000);
        bp.setLearningRate(1.2);
        bp.setMomentum(0);
        bp.setRegularization(0);
        bp.setPrintFrecuency(1000);
        
        bp.setLossFunction(LossFunction.LOGISTIC);
        
        bp.train(net, X, Y);
        System.out.println("");
        
        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer l = net.getLayers().get(i);
            System.out.println("W"+(i+1));
            l.getW().print("%.8f");
            System.out.println("B"+(i+1));
            l.getB().print("%.8f");
        }
        
    }
}

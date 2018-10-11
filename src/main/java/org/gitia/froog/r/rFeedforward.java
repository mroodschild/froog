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
import org.gitia.froog.trainingalgorithm.CG;
import org.gitia.froog.trainingalgorithm.SCG;
import org.gitia.froog.trainingalgorithm.SGD;
import org.gitia.froog.trainingalgorithm.accelerate.AccelerateRule;
import org.gitia.froog.trainingalgorithm.conjugategradient.beta.BetaFactory;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class rFeedforward {

    Feedforward net;
    Backpropagation bp;
    SGD sgd;
    CG cg;
    SCG scg;

    SimpleMatrix inputTest;
    SimpleMatrix outputTest;

    public rFeedforward() {
        net = new Feedforward();
        bp = new Backpropagation();
        sgd = new SGD();
        cg = new CG();
        scg = new SCG();
    }

    public void addLayer(int input, int neuronas, String function) {
        net.addLayer(new Layer(input, neuronas, function));
    }

    public double[] out(double[] matrix, int row, int col) {
        SimpleMatrix m = new SimpleMatrix(row, col, false, matrix);
        return net.output(m).getDDRM().getData();
    }

    public void bp(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol, int epochs, String acc, double acc_parm) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        bp.setEpoch(epochs);
        if (acc != null) {
            switch (acc) {
                case "adam":
                    bp.setAcceleration(AccelerateRule.adam(acc_parm, 0.999, 1e-5, 2));
                    break;
                case "momentum":
                    bp.setAcceleration(AccelerateRule.momentum(acc_parm));
                    break;
                case "momentum_rumelhart":
                    bp.setAcceleration(AccelerateRule.momentumRumelhart(acc_parm));
                    break;
                default:
                    break;
            }
        }
        if (inputTest != null) {
            bp.setInputTest(inputTest);
            bp.setOutputTest(outputTest);
            bp.setTestFrecuency(1);
        }
        bp.train(net, input, output);
    }

    public void sgd(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol,
            int epochs, int batchsize, String acc, double acc_parm) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        sgd.setEpoch(epochs);
        sgd.setBatchSize(batchsize);
        if (acc != null) {
            switch (acc) {
                case "adam":
                    sgd.setAcceleration(AccelerateRule.adam(acc_parm, 0.999, 1e-5, 2));
                    break;
                case "momentum":
                    sgd.setAcceleration(AccelerateRule.momentum(acc_parm));
                    break;
                case "momentum_rumelhart":
                    sgd.setAcceleration(AccelerateRule.momentumRumelhart(acc_parm));
                    break;
                default:
                    break;
            }
        }
        if (inputTest != null) {
            sgd.setInputTest(inputTest);
            sgd.setOutputTest(outputTest);
            sgd.setTestFrecuency(1);
        }
        sgd.train(net, input, output);
    }
    
    public void cg(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol,
            int epochs, String beta_rule) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        cg.setEpoch(epochs);
        cg.setBetaRule(BetaFactory.getBeta(beta_rule));
        if (inputTest != null) {
            cg.setInputTest(inputTest);
            cg.setOutputTest(outputTest);
            cg.setTestFrecuency(1);
        }
        cg.train(net, input, output);
    }
    
    public void scg(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol,
            int epochs) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        scg.setEpoch(epochs);
        if (inputTest != null) {
            scg.setInputTest(inputTest);
            scg.setOutputTest(outputTest);
            scg.setTestFrecuency(1);
        }
        scg.train(net, input, output);
    }

    public int getOutCount() {
        int L = net.getLayers().size();
        return net.getLayers().get(L - 1).numNeuron();
    }

    public void summary() {
        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer l = net.getLayers().get(i);
            System.out.println("Neuronas: " + l.numNeuron() + " Activation: " + l.getFunction().toString());
        }
    }

    public void setTestData(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol) {
        inputTest = new SimpleMatrix(xrow, xcol, false, x);
        outputTest = new SimpleMatrix(yrow, ycol, false, y);
    }
    
}

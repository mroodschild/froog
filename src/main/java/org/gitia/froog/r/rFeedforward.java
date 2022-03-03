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
package org.gitia.froog.r;

import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Dense;
import org.gitia.froog.optimizer.Backpropagation;
import org.gitia.froog.optimizer.CG;
import org.gitia.froog.optimizer.SCG;
import org.gitia.froog.optimizer.SGD;
import org.gitia.froog.optimizer.accelerate.AccelerateRule;
import org.gitia.froog.optimizer.conjugategradient.beta.BetaFactory;
import org.gitia.froog.util.data.OneHot;

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
    OneHot oneHot;

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
        net.addLayer(new Dense(input, neuronas, function));
    }

    public double[] out(double[] matrix, int row, int col) {
        SimpleMatrix m = new SimpleMatrix(row, col, false, matrix);
        return net.output(m).getDDRM().getData();
    }

    public void bp(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol,
            int epochs, String lossFunction, String acc, double acc_parm, boolean accuracy) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        bp.setLossFunction(lossFunction);
        bp.setClassification(accuracy);
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
            int epochs, int batchsize, String lossFunction, String acc,
            double acc_parm, boolean accuracy) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        sgd.setLossFunction(lossFunction);
        sgd.setClassification(accuracy);
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
            int epochs, String beta_rule,
            String loss_function,
            boolean accuracy) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        cg.setEpoch(epochs);
        cg.setBetaRule(BetaFactory.getBeta(beta_rule));
        cg.setLossFunction(loss_function);
        cg.setClassification(accuracy);
        if (inputTest != null) {
            cg.setInputTest(inputTest);
            cg.setOutputTest(outputTest);
            cg.setTestFrecuency(1);
        }
        cg.train(net, input, output);
    }

    public void scg(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol,
            int epochs, String loss_function,
            boolean accuracy) {
        SimpleMatrix input = new SimpleMatrix(xrow, xcol, false, x);
        SimpleMatrix output = new SimpleMatrix(yrow, ycol, false, y);
        scg.setEpoch(epochs);
        scg.setLossFunction(loss_function);
        scg.setClassification(accuracy);
        if (inputTest != null) {
            scg.setInputTest(inputTest);
            scg.setOutputTest(outputTest);
            scg.setTestFrecuency(1);
        }
        scg.train(net, input, output);
    }

    public int getOutCount() {
        int L = net.layers().size();
        return net.layers().get(L - 1).numNeuron();
    }

    public void summary() {
        for (int i = 0; i < net.layers().size(); i++) {
            Dense l = net.layers().get(i);
            System.out.println("Neuronas: " + l.numNeuron() + " Activation: " + l.getFunction().toString());
        }
    }

    public void setTestData(double[] x, int xrow, int xcol, double[] y, int yrow, int ycol) {
        inputTest = new SimpleMatrix(xrow, xcol, false, x);
        outputTest = new SimpleMatrix(yrow, ycol, false, y);
    }

    public double[] oneHot(String[] labels) {
        oneHot = new OneHot(labels);
        return oneHot.encode(labels);
    }

    public int getNumClasses() {
        return oneHot.getNumberOfClasses();
    }

    public void decodeOneHot(double[] x, int xrow, int xcol) {
        SimpleMatrix predict = new SimpleMatrix(xrow, xcol, false, x);
        for (int i = 0; i < xrow; i++) {
            String label = oneHot.tag(predict.extractVector(true, i).getDDRM().getData());
            System.out.println("idx: " + i + ":\t" + label);
        }
    }

}

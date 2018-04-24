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

import org.gitia.froog.Feedforward;
import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.statistics.Compite;
import org.gitia.froog.statistics.ConfusionMatrix;
import org.gitia.froog.trainingalgorithm.gradient.Gradient;
import org.gitia.froog.trainingalgorithm.gradient.StandardGradient;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Backpropagation extends TrainingAlgorithm {

    double costOverall;//costo despues de pasar todos los datos hasta terminar el mini bach
    double costOverallTest = -1;
    double costPartial;//costo parcial dato por dato
    List<SimpleMatrix> deriv = new ArrayList<>();//derivadas
    List<SimpleMatrix> Activations = new ArrayList<>();//Activations in every layers with train data
    int iteracion = 0;
    int printFrecuency = 1;
    Gradient gradient = new StandardGradient();

    public Backpropagation() {
    }

    //Training Algorithm
    /**
     *
     * @param net neural network to train
     * @param input every row is a feature and every column is a register
     * @param output every row is a feature and every column is a register
     */
    public void train(Feedforward net, SimpleMatrix input, SimpleMatrix output) {
        this.net = net;
        this.input = new SimpleMatrix(input);
        this.output = new SimpleMatrix(output);
        init();
        Clock clock = new Clock();
        for (int i = 0; i < this.epoch; i++) {
            clock.start();
            Activations = net.activations(input);
            costOverall = loss(Activations.get(Activations.size() - 1), output);
            this.cost.add(costOverall);
            gradient.compute(net, Activations, gradW, gradB, input, output);
            updateRule.updateParameters(net,(double) input.numCols(), L2_Lambda, learningRate, gradW, gradB);
            if (iteracion % printFrecuency == 0) {
                printScreen(clock);
            }
            iteracion++;
        }
    }

    /**
     *
     * @param net
     * @param input
     * @param output
     */
    public void train(Feedforward net, double[][] input, double[][] output) {
        Backpropagation.this.train(net,
                (new SimpleMatrix(input)).transpose(),
                (new SimpleMatrix(output)).transpose());
    }

    /**
     * Calculamos el costo de la iteración
     *
     * @param yCalc
     * @param yObs
     * @return loss = lossfunction + regularization * ||W||²
     */
    protected double loss(SimpleMatrix yCalc, SimpleMatrix yObs) {
        double loss;
        double m = yObs.numCols();
        loss = lossFunction.costAll(yCalc, yObs);
        return loss + L2_reg(m);
    }

    protected double L2_reg(double m) {
        double loss = 0;
        if (L2_Lambda > 0) {
            loss = Math.pow(net.getParamsW().normF(), 2) * L2_Lambda / (2 * m);
//            for (Layer layer : net.getLayers()) {
//                loss += layer.getW().elementPower(2).elementSum();
//            }
//            SimpleMatrix weights = net.getParamsW();
//            loss += weights.mult(weights.transpose()).scale(regularization).get(0);
//            loss = loss * regularization / (2 * m);
        }
        return loss;
    }

    /**
     * iniciamos los deltas según la cantidad de pesos y capas que hay en la red
     * y colocamos todos los deltas en 0.
     */
    public void init() {
        iteracion = 0;
        deriv.clear();
        gradB.clear();
        gradW.clear();
        cost.clear();
        for (int i = 0; i < net.getLayers().size(); i++) {
            deriv.add(new SimpleMatrix(1,1));
            gradW.add(new SimpleMatrix(net.getLayers().get(i).getW()));
            gradB.add(new SimpleMatrix(net.getLayers().get(i).getB()));
            gradW.get(i).zero();
            gradB.get(i).zero();
        }
        updateRule.init(gradW, gradB);
    }

    protected void printScreen(Clock clock) {
        double aciertoTrain = 0;
        double aciertoTest = 0;
        double costAllTrain = -1;
        if ((iteracion % testFrecuency) == 0 && inputTest != null) {
            SimpleMatrix yCalcTrain = net.output(this.input);
            SimpleMatrix yCalcTest = net.output(this.inputTest);
            costAllTrain = loss(yCalcTrain, this.output);
            costOverallTest = loss(yCalcTest, outputTest);
            this.costTest.add(costOverallTest);
            if (classification) {
                ConfusionMatrix cMatrixTrain = new ConfusionMatrix();
                cMatrixTrain.eval(Compite.eval(yCalcTrain.transpose()), this.output.transpose());
                aciertoTrain = cMatrixTrain.getAciertosPorc();
                ConfusionMatrix cMatrixTest = new ConfusionMatrix();
                cMatrixTest.eval(Compite.eval(yCalcTest.transpose()), outputTest.transpose());
                aciertoTest = cMatrixTest.getAciertosPorc();
            }
        }
        clock.stop();
        if ((iteracion % testFrecuency) != 0 || inputTest == null) {
            System.out.println(
                    "It:\t" + iteracion
                    + "\tTrain:\t" + costOverall
                    + "\tTime:\t" + clock.timeSec() + " s.");
        } else {
            if (classification) {
                System.out.println(
                        "It:\t" + iteracion
                        + "\tTrain:\t" + costOverall
                        + "\tTrain Complete:\t" + costAllTrain
                        + "\tTest:\t" + costOverallTest
                        + "\tTrain Aciertos:\t" + aciertoTrain + "\t%."
                        + "\tTest Aciertos:\t" + aciertoTest + "\t%."
                        + "\tTime:\t" + clock.timeSec() + "\ts."
                );
            } else {
                System.out.println(
                        "It:\t" + iteracion
                        + "\tTrain:\t" + costOverall
                        + "\tTrain Complete:\t" + costAllTrain
                        + "\tTest:\t" + costOverallTest
                        + "\tTime:\t" + clock.timeSec() + "\ts."
                );
            }
        }
    }

    public void setPrintFrecuency(int printFrecuency) {
        this.printFrecuency = printFrecuency;
    }

}

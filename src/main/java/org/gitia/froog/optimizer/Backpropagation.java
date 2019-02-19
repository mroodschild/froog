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
package org.gitia.froog.optimizer;

import org.gitia.froog.Feedforward;
import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.statistics.Compite;
import org.gitia.froog.statistics.ConfusionMatrix;
import org.gitia.froog.optimizer.accelerate.Accelerate;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Backpropagation extends TrainingAlgorithm {

    protected double costOverall;//costo despues de pasar todos los datos hasta terminar el mini bach
    protected double costOverallTest = -1;
    protected double costPartial;//costo parcial dato por dato
    protected List<SimpleMatrix> deriv = new ArrayList<>();//derivadas
    protected List<SimpleMatrix> Activations = new ArrayList<>();//Activations in every layers with train data
    protected int iteracion = 0;
    protected int printFrecuency = 1;
    protected double gradientClipping = 0;

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
        int L = net.getLayers().size() - 1;
        for (int i = 0; i < this.epoch; i++) {
            clock.start();
            Activations = net.activations(input);
            costOverall = loss(Activations.get(L), output);
            //System.out.println("Cost:\t"+costOverall);
            this.cost.add(costOverall);
            gradient.compute(net, Activations, gradW, gradB, input, output);
            computeGradientClipping(gradW, gradB);
            updateRule.updateParameters(net, (double) input.numCols(), L2_Lambda, learningRate, gradW, gradB);
            if (iteracion % printFrecuency == 0) {
                printScreen(clock);
            }
            iteracion++;
        }
        //printScreen(clock);
    }

    /**
     *
     * @param gradW
     * @param gradB
     */
    public void computeGradientClipping(List<SimpleMatrix> gradW, List<SimpleMatrix> gradB) {
        if (gradientClipping > 0) {
            SimpleMatrix g = getGradients(gradW, gradB);
            double norm = g.normF();
            //System.out.println("norm:\t"+norm);
            if (norm >= gradientClipping) {
                g = g.scale(gradientClipping / norm);
                //System.out.println("Se hizo Gradient Clipping");
            }
            setGradientsToList(g, gradW, gradB);
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
//            for (Dense layer : net.getLayers()) {
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
    @Override
    public void init() {
        iteracion = 0;
        deriv.clear();
        gradB.clear();
        gradW.clear();
        cost.clear();
        for (int i = 0; i < net.getLayers().size(); i++) {
            deriv.add(new SimpleMatrix(1, 1));
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
        //double costAllTrain = -1;
        if ((iteracion % testFrecuency) == 0 && inputTest != null) {
            //SimpleMatrix yCalcTrain = net.output(this.input);
            //costAllTrain = loss(yCalcTrain, this.output);
            SimpleMatrix yCalcTest = net.output(this.inputTest);
            costOverallTest = loss(yCalcTest, outputTest);
            this.costTest.add(costOverallTest);
            if (classification) {
                SimpleMatrix yCalcTrain = net.output(this.input);
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
                    + "\tTime:\t" + clock.timeSec() + "\ts.");
        } else {
            if (classification) {
                System.out.println(
                        "It:\t" + iteracion
                        + "\tTrain:\t" + costOverall
                        //+ "\tTrain Complete:\t" + costAllTrain
                        + "\tTest:\t" + costOverallTest
                        + "\tTrain Aciertos:\t" + aciertoTrain + "\t%."
                        + "\tTest Aciertos:\t" + aciertoTest + "\t%."
                        + "\tTime:\t" + clock.timeSec() + "\ts."
                );
            } else {
                System.out.println(
                        "It:\t" + iteracion
                        + "\tTrain:\t" + costOverall
                        //+ "\tTrain Complete:\t" + costAllTrain
                        + "\tTest:\t" + costOverallTest
                        + "\tTime:\t" + clock.timeSec() + "\ts."
                );
            }
        }
    }

    public void setPrintFrecuency(int printFrecuency) {
        this.printFrecuency = printFrecuency;
    }

    public void setGradientClipping(double gradientClipping) {
        this.gradientClipping = gradientClipping;
        System.out.println("Gradient Clipping Threshold:\t" + this.gradientClipping);
    }

    public double getGradientClipping() {
        return gradientClipping;
    }

    public void setAcceleration(Accelerate accelerate) {
        updateRule.setAccelerate(accelerate);
    }

}

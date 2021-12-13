/*
 * Copyright 2019
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.statistics.Compite;
import org.gitia.froog.statistics.ConfusionMatrix;
import org.gitia.froog.optimizer.accelerate.Accelerate;
import org.gitia.froog.util.History;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Backpropagation extends TrainingAlgorithm {

    protected double costOverall;//costo despues de pasar todos los datos hasta terminar el mini batch
    protected double costOverallTest = -1;
    protected double costPartial;//costo parcial dato por dato
    protected List<SimpleMatrix> deriv = new ArrayList<>();//derivadas
    protected List<SimpleMatrix> Activations = new ArrayList<>();//Activations in every layers with train data
    protected int iteracion = 0;
    protected int printFrecuency = 1;
    protected double gradientClipping = 0;
    protected History history = new History();

    private static final Logger log = LogManager.getLogger(Backpropagation.class);

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
                printScreen(net, Activations.get(L), output, clock, inputTest, outputTest, iteracion, testFrecuency, classification);
            }
            iteracion++;
        }
        System.out.println("Finish Training, Resume:");
        printScreen(net, Activations.get(L), output, clock, inputTest, outputTest, iteracion, testFrecuency, classification);
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

    protected void printScreen(Feedforward net, SimpleMatrix yCal, SimpleMatrix yObs, Clock clock,
            SimpleMatrix inputTest, SimpleMatrix outputTest,
            int iteracion, int testFrecuency, boolean classification) {
        double accTrain = 0;
        double accTest = 0;
        ConfusionMatrix cMatrixTrain = new ConfusionMatrix();
        ConfusionMatrix cMatrixTest = new ConfusionMatrix();
        history.addTrainCost(iteracion, costOverall);
        if (classification) {
            cMatrixTrain.eval(Compite.eval(yCal.transpose()), yObs.transpose());
            accTrain = cMatrixTrain.getAciertosPorc();
            history.addTrainAcc(iteracion, accTrain);
        }
        if ((iteracion % testFrecuency) == 0 && inputTest != null) {
            SimpleMatrix yCalcTest = net.output(inputTest);
            costOverallTest = loss(yCalcTest, outputTest);
            this.costTest.add(costOverallTest);
            history.addTestCost(iteracion, costOverallTest);
            if (classification) {
                cMatrixTest.eval(Compite.eval(yCalcTest.transpose()), outputTest.transpose());
                accTest = cMatrixTest.getAciertosPorc();
                history.addTestAcc(iteracion, accTest);
            }
        }
        clock.stop();
        double time = clock.timeSec();
        //if ((iteracion % testFrecuency) != 0 || inputTest == null) {
        //  log.info("It:\t{}\tTrain:\t{}\tTime:\t{}\ts.", iteracion, costOverall, time);
        //} else {
        if (classification && (iteracion % testFrecuency) == 0 && inputTest != null) {
            log.info("It:\t{}\tTrain:\t{}\tTest:\t{}\tTrain %:\t{}\tTest %:\t{}\tTime:\t{}\ts.", iteracion, costOverall, costOverallTest, accTrain, accTest, time);
        } else if (classification) {
            log.info("It:\t{}\tTrain:\t{}\tTrain %:\t{}\tTime:\t{}\ts.", iteracion, costOverall, accTrain, time);
        } else if ((iteracion % testFrecuency) == 0 && inputTest != null) {
            log.info("It:\t{}\tTrain:\t{}\tTest:\t{}\tTime:\t{}\ts.", iteracion, costOverall, costOverallTest, time);
            //}else if((iteracion % testFrecuency) == 0){
        } else {
            log.info("It:\t{}\tTrain:\t{}\tTime:\t{}\ts.", iteracion, costOverall, time);
        }
        //}
    }

    public History getHistory() {
        return history;
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

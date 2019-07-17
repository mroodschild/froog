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

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.statistics.Compite;
import org.gitia.froog.statistics.ConfusionMatrix;

public class SCG extends TrainingAlgorithm {

    SimpleMatrix pk;// = new SimpleMatrix();
    SimpleMatrix rk;// -gradient
    SimpleMatrix r_new;
    SimpleMatrix p_new;
    SimpleMatrix Sk;
    double deltaK;
    double uk, alphak;
    double sigma = 1e-8;// 0 < sigma <= 1e-4
    double sigmaK = 0;
    double lambdaT = 0;
    double lambdaK;
    double lambdaK_init = 1e-7; // 0 < lambdaK <= 1e-6 
    double nablaK = 0;
    int k = 0;
    int N = 0;
    boolean success = true;
    double E;
    double E_conj;

    SimpleMatrix Wk;
    SimpleMatrix W_new;
    List<SimpleMatrix> Ak = new ArrayList<>();

    int L;//Number of Layers

    Clock clock = new Clock();
    private static final Logger log = LogManager.getLogger(SCG.class);

    public SCG() {
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
        N = net.getParameters().getNumElements();
        this.Wk = net.getParameters();
        W_new = Wk.copy();
        init();
        boolean restart = true;
        Ak = net.activations(input);
        L = Ak.size() - 1;
        SimpleMatrix g1 = computeGradient(net, Ak, input, output);
        E = lossFunction.costAll(Ak.get(L), output);//modificacion
        rk = g1.negative();
        pk = rk.copy();
        //primeraDireccion();//paso1

        for (k = 1; k <= epoch; k++) {
            clock.start();

            if (restart) {
                sigmaK = 0;
                lambdaT = 0;
                lambdaK = lambdaK_init;// 0 < lambdaK <= 1e-6
                nablaK = 0;
                success = true;
                restart = false;
            }

            //informacionSegOrden();//paso 2
            if (success == true) {
                sigmaK = sigma / NormOps_DDRM.normP2(pk.getDDRM());
                net.setParameters(Wk.plus(pk.transpose().scale(sigmaK)));
                Ak = net.activations(input);
                SimpleMatrix g2 = computeGradient(net, Ak, input, output);
                Sk = g2.minus(g1).divide(sigmaK);
                deltaK = pk.transpose().mult(Sk).get(0);
            }
            //escalado();//paso 3
            double pk_nomrP2pow2 = Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2);
            deltaK = deltaK + (lambdaK - lambdaT) * pk_nomrP2pow2;
            //hessianPositive();//paso 4
            if (deltaK <= 0) {
                lambdaT = 2 * (lambdaK - deltaK / pk_nomrP2pow2);
                deltaK = -deltaK + lambdaK * pk_nomrP2pow2;
                lambdaK = lambdaT;
            }
            //tamanoPaso();//paso 5
            uk = pk.transpose().mult(rk).get(0);
            alphak = uk / deltaK;
            //comparacionParametros();//paso 6
            net.setParameters(Wk.plus(pk.transpose().scale(alphak)));
            Ak = net.activations(input);
            E_conj = lossFunction.costAll(Ak.get(L), output);
            nablaK = 2 * deltaK * (E - E_conj) / Math.pow(uk, 2);
            //evalNabla();//paso 7
            if (nablaK >= 0) {
                //Collections.copy(Ak, Ak_new);//agregado
                Wk = Wk.plus(pk.transpose().scale(alphak));
                net.setParameters(Wk);
                g1 = computeGradient(net, Ak, input, output);
                SimpleMatrix r_old = rk.copy();
                rk = g1.negative();//r = g1.negative()
                lambdaT = 0;
                success = true;
                E = E_conj;
                if (k % N == 0) {
                    pk = rk;//pk = rk;
                    restart = true;
                } else {
                    double rk_norm2 = Math.pow(NormOps_DDRM.normP2(rk.getDDRM()), 2);
                    double beta = (rk_norm2 - rk.transpose().mult(r_old).get(0)) / uk;
                    pk = rk.plus(pk.scale(beta));//pk = rk.plus(pk.scale(beta));
                }
                if (nablaK >= 0.75) {
                    lambdaK = lambdaK * 0.25;
                }
            } else {
                lambdaT = lambdaK;
                success = false;
            }
            //evalSmallNabla();//paso 8
            if (nablaK < 0.25) {
                lambdaK = lambdaK + (deltaK * (1 - nablaK) / pk_nomrP2pow2);
            }
            //paso 9 //finalizar algoritmo
            if (rk.normF() == 0) {
                clock.stop();
                //System.out.println("It:\t" + k + "\ttrain:\t" + E + "\ttime:\t" + clock.timeSec() + "\ts.");
                printScreen(net, Ak.get(L), output, clock, inputTest, outputTest, k, testFrecuency, classification);
                break;
            }
            clock.stop();
            
            if (k % printFrecuency == 0) {
                printScreen(net, Ak.get(L), output, clock, inputTest, outputTest, k, testFrecuency, classification);
            }
            //iteracion++;
            //System.out.println("It:\t" + k + "\ttrain:\t" + E + "\ttime:\t" + clock.timeSec() + "\ts.");

        }
    }
    
    protected void printScreen(Feedforward net, SimpleMatrix yCal, SimpleMatrix yObs, Clock clock,
            SimpleMatrix inputTest, SimpleMatrix outputTest,
            int iteracion, int testFrecuency, boolean classification) {
        double aciertoTrain = 0;
        double aciertoTest = 0;
        ConfusionMatrix cMatrixTrain = new ConfusionMatrix();
        ConfusionMatrix cMatrixTest = new ConfusionMatrix();
        double costOverallTest=0;
        double costOverall = E;
        if (classification) {
            cMatrixTrain.eval(Compite.eval(yCal.transpose()), yObs.transpose());
            aciertoTrain = cMatrixTrain.getAciertosPorc();
        }
        if ((iteracion % testFrecuency) == 0 && inputTest != null) {
            SimpleMatrix yCalcTest = net.output(inputTest);
            costOverallTest = lossFunction.costAll(yCalcTest, outputTest);
            this.costTest.add(costOverallTest);
            if (classification) {
                cMatrixTest.eval(Compite.eval(yCalcTest.transpose()), outputTest.transpose());
                aciertoTest = cMatrixTest.getAciertosPorc();
            }
        }
        clock.stop();
        double time = clock.timeSec();
        //if ((iteracion % testFrecuency) != 0 || inputTest == null) {
        //  log.info("It:\t{}\tTrain:\t{}\tTime:\t{}\ts.", iteracion, costOverall, time);
        //} else {
        if (classification && (iteracion % testFrecuency) == 0 && inputTest != null) {
            log.info("It:\t{}\tTrain:\t{}\tTest:\t{}\tTrain %:\t{}\tTest %:\t{}\tTime:\t{}\ts.", iteracion, costOverall, costOverallTest, aciertoTrain, aciertoTest, time);
        } else if (classification) {
            log.info("It:\t{}\tTrain:\t{}\tTrain %:\t{}\tTime:\t{}\ts.", iteracion, costOverall, aciertoTrain, time);
        } else if ((iteracion % testFrecuency) == 0 && inputTest != null) {
            log.info("It:\t{}\tTrain:\t{}\tTest:\t{}\tTime:\t{}\ts.", iteracion, costOverall, costOverallTest, time);
            //}else if((iteracion % testFrecuency) == 0){
        } else {
            log.info("It:\t{}\tTrain:\t{}\tTime:\t{}\ts.", iteracion, costOverall, time);
        }
        //}
    }

    /**
     *
     * @param net
     * @param A
     * @param X
     * @param Y
     * @return
     */
    public SimpleMatrix computeGradient(Feedforward net, List<SimpleMatrix> A, SimpleMatrix X, SimpleMatrix Y) {
        gradient.compute(net, A, gradW, gradB, X, Y);
        return getGradients(gradW, gradB);
    }

}

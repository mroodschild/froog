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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;

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
    double lambdaK = 1e-7;// 0 < lambdaK <= 1e-6
    double nablaK = 0;
    int k = 0;
    int N = 0;
    boolean success = true;

    SimpleMatrix Wk;
    SimpleMatrix W_new;
    List<SimpleMatrix> Ak = new ArrayList<>();
    List<SimpleMatrix> Ak_new = new ArrayList<>();

    int L;//Number of Layers

    Clock clock = new Clock();
    Clock clockStep = new Clock();
    Clock clockMid = new Clock();

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
        //------------------------------------
        this.net = net;
        N = net.getParameters().getNumElements();
        this.Wk = net.getParameters();
        W_new = Wk.copy();
        init();
        clockStep.start();
        Ak = net.activations(input);
        L = Ak.size() - 1;
        //------------------------------------
        //primeraDireccion();//paso1
        clockMid.start();
        SimpleMatrix g1 = computeGradient(net, Ak, input, output);
        clockMid.stop();
        clockMid.printTime("g1");
        rk = g1.negative();
        pk = rk.copy();
        success = true;
        clockMid.start();
        double E = lossFunction.costAll(Ak.get(L), output);//modificacion
        clockMid.stop();
        clockMid.printTime("g1");
        clockStep.stop();
        clockStep.printTime("Step1");
        for (k = 1; k <= epoch; k++) {
            clock.start();
            clockStep.start();
            //informacionSegOrden();//paso 2
            if (success == true) {
                sigmaK = sigma / NormOps_DDRM.normP2(pk.getDDRM());
                net.setParameters(Wk.plus(pk.transpose().scale(sigmaK)));
                Ak = net.activations(input);
                SimpleMatrix g2 = computeGradient(net, Ak, input, output);
                Sk = g2.minus(g1).divide(sigmaK);
                deltaK = pk.transpose().mult(Sk).get(0);
            }
            clockStep.stop();
            clockStep.printTime("Step2");
            clockStep.start();
            //escalado();//paso 3
            double pk_nomrP2pow2 = Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2);
            deltaK = deltaK + (lambdaK - lambdaT) * pk_nomrP2pow2;
            clockStep.stop();
            clockStep.printTime("Step3");
            clockStep.start();
            //hessianPositive();//paso 4
            if (deltaK <= 0) {
                lambdaT = 2 * (lambdaK - deltaK / pk_nomrP2pow2);
                deltaK = -deltaK + lambdaK * pk_nomrP2pow2;
                lambdaK = lambdaT;
            }
            clockStep.stop();
            clockStep.printTime("Step4");
            clockStep.start();
            //tamanoPaso();//paso 5
            uk = pk.transpose().mult(rk).get(0);
            alphak = uk / deltaK;
            clockStep.stop();
            clockStep.printTime("Step5");
            clockStep.start();
            //comparacionParametros();//paso 6
            net.setParameters(Wk.plus(pk.transpose().scale(alphak)));
            Ak_new = net.activations(input);//este deberia ser Ak_new
            double E_conj = lossFunction.costAll(Ak_new.get(L), output);//Ak_new
            nablaK = 2 * deltaK * (E - E_conj) / Math.pow(uk, 2);
            clockStep.stop();
            clockStep.printTime("Step6");
            clockStep.start();
            //evalNabla();//paso 7
            if (nablaK >= 0) {
                Collections.copy(Ak, Ak_new);//agregado
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
            clockStep.stop();
            clockStep.printTime("Step7");
            clockStep.start();
            //evalSmallNabla();//paso 8
            if (nablaK < 0.25) {
                lambdaK = lambdaK + (deltaK * (1 - nablaK) / pk_nomrP2pow2);
            }
            //actualizarPesos();
            clockStep.stop();
            clockStep.printTime("Step8");
            clockStep.start();
            //paso 9 //finalizar algoritmo
            if (rk.normF() == 0) {
                clock.stop();
                System.out.println("It:\t" + k + "\ttrain:\t" + E + "\ttime:\t" + clock.timeSec() + "\ts.");
                break;
            }
            clockStep.stop();
            clockStep.printTime("Step9");
            clockStep.start();
            clock.stop();
            System.out.println("It:\t" + k + "\ttrain:\t" + E + "\ttime:\t" + clock.timeSec() + "\ts.");
        }
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

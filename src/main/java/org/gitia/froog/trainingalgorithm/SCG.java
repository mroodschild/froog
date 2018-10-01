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
package org.gitia.froog.trainingalgorithm;

import java.util.ArrayList;
import java.util.List;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;

public class SCG extends Backpropagation {
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
    int L;//ultima capa

    Clock c = new Clock();

    public SCG() {
    }

    //Training Algorithm
    /**
     *
     * @param net neural network to train
     * @param input every row is a feature and every column is a register
     * @param output every row is a feature and every column is a register
     */
    @Override
    public void train(Feedforward net, SimpleMatrix input, SimpleMatrix output) {
        this.net = net;
        N = net.getParameters().getNumElements();
        this.Wk = net.getParameters();
        W_new = Wk.copy();
        init();
        //primeraDireccion();//paso1
        Ak = net.activations(input);
        L = Ak.size() - 1;
        SimpleMatrix g1 = computeGradient(net, Ak, input, output);
        rk = g1.negative();
        pk = rk.copy();
        success = true;
        double E = lossFunction.costAll(Ak.get(L), output);//modificacion
        for (k = 0; k < epoch; k++) {
            c.start();
            //informacionSegOrden();//paso 2
            if (success == true) {
                sigmaK = sigma / NormOps_DDRM.normP2(pk.getDDRM());
                net.setParameters(Wk.plus(pk.transpose().scale(sigmaK)));
                Ak = net.activations(input);
                SimpleMatrix g2 = computeGradient(net,Ak, input, output);
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
            double E_conj = lossFunction.costAll(Ak.get(L), output);
            nablaK = 2 * deltaK * (E - E_conj) / Math.pow(uk, 2);
            //evalNabla();//paso 7
            if (nablaK >= 0) {
                W_new = Wk.plus(pk.transpose().scale(alphak));
                net.setParameters(W_new);
                g1 = computeGradient(net, Ak, input, output);
                r_new = g1.negative();
                lambdaT = 0;
                success = true;
                if (k % N == 0) {
                    p_new = r_new;
                } else {
                    double r_new_norm2 = Math.pow(NormOps_DDRM.normP2(r_new.getDDRM()), 2);
                    double beta = (r_new_norm2 - r_new.transpose().mult(rk).get(0)) / uk;
                    p_new = r_new.plus(pk.scale(beta));
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
            //actualizarPesos();
            //paso 9 
            if (rk.normF() != 0) {
                rk = r_new;
                pk = p_new;
                Wk = W_new.copy();
            } else {
                net.setParameters(W_new);
                c.stop();
                System.out.println("k: " + k + "\tE:\t" + E + "\tE_conj:\t" + E_conj + "\trk_new:\t" + rk.normF() + "\ttime:\t" + c.timeSec() + "\ts.");
                break;
            }
            c.stop();
            System.out.println("k: " + k + "\tE:\t" + E + "\tE_conj:\t" + E_conj + "\trk_new:\t" + rk.normF() + "\ttime:\t" + c.timeSec() + "\ts.");
            /**
             * * Mejora **
             */
            if (nablaK >= 0) {
                E = E_conj;
            }
            c.stop();
            System.out.println("k: " + k + "\tE:\t" + E + "\tE_conj:\t" + E_conj + "\trk_new:\t" + rk.normF() + "\ttime:\t" + c.timeSec() + " s.");
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

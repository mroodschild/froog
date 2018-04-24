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
package org.gitia.froog.trainingalgorithm.scg;

import java.util.Random;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.trainingalgorithm.testfunction.TestFunction;
import org.gitia.froog.trainingalgorithm.testfunction.TestRosenbrock;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class TestSCGFunction1 {

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

    TestFunction function = new TestRosenbrock();
    SimpleMatrix Wk;
    SimpleMatrix W_new;

    public static void main(String[] args) {
        TestSCGFunction1 test = new TestSCGFunction1();
        SimpleMatrix X = SimpleMatrix.random_DDRM(2, 1, -10, 10, new Random(1));
        X.transpose().print();
        TestRosenbrock function = new TestRosenbrock();
        System.out.println("error:\t" + function.compute(X));
        X = test.train(200, X);
        X.transpose().print();
        System.out.println("error:\t" + function.compute(X));
    }

    public SimpleMatrix train(int iteraciones, SimpleMatrix X) {
        this.N = X.getNumElements();//numero de parametros a ajustar
        this.Wk = X;
        W_new = Wk;
        lambdaK = 1e-7;
        lambdaT = 0;
        
        //primeraDireccion();//paso 1
        rk = function.gradient(Wk).negative();
        pk = rk.copy();
        success = true;
        for (k = 0; k < iteraciones; k++) {
            System.out.println("k: "+k);
            //informacionSegOrden();//paso 2
            if (success == true) {
                sigmaK = sigma / NormOps_DDRM.normP2(pk.getDDRM());
                Sk = function.gradient(Wk.plus(pk.scale(sigmaK))).minus(function.gradient(Wk)).divide(sigmaK);
                deltaK = pk.transpose().mult(Sk).get(0);
            }
            //escalado();//paso 3
            deltaK = deltaK + (lambdaK - lambdaT) * Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2);
            //hessianPositive();//paso 4
            if (deltaK <= 0) {
                lambdaT = 2 * (lambdaK - deltaK / Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2));
                deltaK = -deltaK + lambdaK * Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2);
                lambdaK = lambdaT;
            }
            //tamanoPaso();//paso 5
            uk = pk.transpose().mult(rk).get(0);
            alphak = uk / deltaK;
            //comparacionParametros();//paso 6
            double E = function.compute(Wk);
            W_new = Wk.plus(pk.scale(alphak));
            double E_conj = function.compute(W_new);
            nablaK = 2 * deltaK * (E - E_conj) / Math.pow(uk, 2);
            evalNabla();//paso 7
            //paso 8
            evalSmallNabla();
            //actualizarPesos();
            //paso 9 
            if (rk.elementSum() != 0) {
                rk = r_new;
                pk = p_new;
                Wk = W_new.copy();
            } else {
                break;
            }
        }
        return Wk;
    }


    public void evalNabla() {
        if (nablaK >= 0) {
            W_new = Wk.plus(pk.scale(alphak));
            r_new = function.gradient(W_new).negative();
            lambdaT = 0; success = true;
            if (k % N == 0) {
                p_new = r_new;
            } else {
                double norm2 = Math.pow(NormOps_DDRM.normP2(r_new.getDDRM()), 2);
                double beta = (norm2 - r_new.transpose().mult(rk).get(0)) / uk;
                p_new = r_new.plus(pk.scale(beta));
            }
            if (nablaK >= 0.75) {
                lambdaK = lambdaK * 0.25;
            }
        } else {
            lambdaT = lambdaK;
            success = false;
        }
    }

    public void evalSmallNabla() {
        if (nablaK < 0.25) {
            lambdaK = lambdaK + (deltaK * (1 - nablaK) / Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2));
        }
    }

}

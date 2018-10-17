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
package org.gitia.froog.optimizer.scg;

import java.util.Random;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.optimizer.testfunction.TestFunction;
import org.gitia.froog.optimizer.testfunction.TestRosenbrock;

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

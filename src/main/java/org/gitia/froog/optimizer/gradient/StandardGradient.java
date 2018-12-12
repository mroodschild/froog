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
package org.gitia.froog.optimizer.gradient;

import java.util.List;
import java.util.Random;
import org.ejml.data.MatrixType;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class StandardGradient implements Gradient{

    /**
     *
     * @param net
     * @param Activations
     * @param gradW
     * @param gradB
     * @param X
     * @param Y
     */
    @Override
    public void compute(Feedforward net, List<SimpleMatrix> Activations, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB, SimpleMatrix X, SimpleMatrix Y) {
        //System.out.println("org.gitia.froog.optimizer.gradient.StandardGradient.compute()");
        Clock c = new Clock();
        Clock c2 = new Clock();
        Clock c3 = new Clock();
        c.start();
        c2.start();
        int L = Activations.size() - 1;
        int m = X.numCols();
        SimpleMatrix A = Activations.get(L);//ouputs
        SimpleMatrix dZ = A.minus(Y);//
        A = Activations.get(L - 1);
        SimpleMatrix dW = dZ.mult(A.transpose()).divide(m);//dZ3*A2.T/m
        SimpleMatrix sum = new SimpleMatrix(dZ.numRows(), 1);
        CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
        SimpleMatrix dB = sum.divide(m);
        
        gradW.set(L, dW);
        gradB.set(L, dB);
        
        SimpleMatrix W = net.getLayers().get(L).getW();
        SimpleMatrix dA;
        c2.stop();
        //c2.printTime("1ra Parte");
        c2.start();
        for (int i = L - 1; i >= 0; i--) {
            c3.start();
            dA = net.getLayers().get(i).getFunction().derivative(A);//0.625 seg
            c3.stop();
            //c3.printTime("derivative");
            c3.start();
            //SimpleMatrix dZ1 = W.transpose().mult(dZ).elementMult(dA);
            dZ = W.transpose().mult(dZ).elementMult(dA);
            
            c3.stop();
            //c3.printTime("dZ");
            c3.start();
            A = (i > 0) ? Activations.get(i - 1) : X;
            c3.stop();
            //c3.printTime("A");
            c3.start();
            SimpleMatrix At = A.transpose();
            c3.stop();
            //c3.printTime("At");
            c3.start();
            //System.out.println("dZ dimensiones");
            //dZ.printDimensions();
            //System.out.println("At dimensiones");
            //At.printDimensions();
            //System.out.println("dZ: "+dZ.getType()+" At: "+At.getType());
            dZ.mult(At);// 300x50000 50000x784
            c3.stop();
            c3.printTime("dZ*At");
            c3.start();
            //dZ1.mult(At);
//            SimpleMatrix m_aux = new SimpleMatrix(dZ.numRows(), At.numCols(), MatrixType.DDRM);
//            CommonOps_DDRM.mult(dZ.getDDRM(), At.getDDRM(), m_aux.getDDRM());

            c3.stop();
            //c3.printTime("dZ1*At");
            c3.start();
            dW = dZ.mult(A.transpose()).divide(m);// -> 43.27 seg
            c3.stop();
            //c3.printTime("dW");
            c3.start();
            CommonOps_DDRM.sumRows(dZ.getDDRM(), sum.getDDRM());
            c3.stop();
            //c3.printTime("sumRows");
            c3.start();
            dB = sum.divide(m);
            c3.stop();
            //c3.printTime("divide m");
            
            ///// -> 0.0000 seg
            c3.start();
            gradW.set(i, dW);
            gradB.set(i, dB);
            W = net.getLayers().get(i).getW();
            c3.stop();
            //c3.printTime("guardar pesos");
        }
        c2.stop();
        //c2.printTime("2da parte");
        c.stop();
        c.printTime("Calculo del gradiente TOTAL");
    }
}

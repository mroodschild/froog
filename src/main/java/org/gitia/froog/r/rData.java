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
import org.gitia.froog.statistics.Compite;
import org.gitia.froog.statistics.ConfusionMatrix;
import org.gitia.froog.util.data.OneHot;

/**
 *
 * @author Matías Rodschild <mroodschild@gmail.com>
 */
public class rData {

    ConfusionMatrix confusionMatrix = new ConfusionMatrix();
    OneHot oneHot;
    

    public rData() {
    
    }

    public void confusionMatrix(double[] observed, int rowObs, int colObs, double[] computed, int rowCom, int colComp, boolean binarize) {
        SimpleMatrix yObs = new SimpleMatrix(rowObs, colObs, false, observed);
        SimpleMatrix yCom = new SimpleMatrix(rowCom, colComp, false, computed);
        if(binarize){
            yCom = Compite.eval(yCom);
        }
        confusionMatrix.eval(yCom, yObs);
        System.out.println(confusionMatrix);
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

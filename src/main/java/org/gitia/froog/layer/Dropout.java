/*
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.layer;

import org.ejml.data.BMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Dropout{// implements Layer{
    
    double keepProb;
    SimpleMatrix Drop;

    public Dropout(double keepProb) {
        this.keepProb = keepProb;
    }
    
    //@Override
    public SimpleMatrix output(SimpleMatrix a) {
//         if (this.keepProb > 0 && this.keepProb < 1) {
//            //A = neurons x m
//            Drop = SimpleMatrix.random_DDRM(W.numRows(), a.numCols(), 0, 1, this.random);
//            BMatrixRMaj keepMatrix = new BMatrixRMaj(Drop.numRows(), Drop.numCols());
//            CommonOps_DDRM.elementLessThan(Drop.getDDRM(), keepProb, keepMatrix);
//            for (int i = 0; i < Drop.getNumElements(); i++) {
//                Drop.set(i, (keepMatrix.get(i)) ? 1 : 0);
//            }
//            return output(a).elementMult(Drop).divide(keepProb);
//        } else {
//            return output(a);
//        }
    return null;
    }
    
}

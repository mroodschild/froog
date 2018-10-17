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
package org.gitia.froog.lossfunction;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class LossFunctionFactory {

    /**
     * Implemented cost functions:<br>
     * linearRegression<br>
     * crossEntropy<br>
     *
     * @param CostFunction
     * @return
     */
    public static LossFunction getLossFunction(String CostFunction) {
        switch (CostFunction) {
            case LossFunction.MSE:
                return new MSE();
            case LossFunction.RMSE:
                return new RMSE();
            case LossFunction.CROSSENTROPY:
                return new CrossEntropyLoss();
            case LossFunction.LOGISTIC:
                return new Logistic();
            default:
                System.err.println(CostFunction + " no es una función de costo válida.");
                System.exit(0);
        }
        return null;
    }

}

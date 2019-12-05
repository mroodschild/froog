/*
 * Copyright 2019 Matías Roodschild <mroodschild@gmail.com>.
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
package org.gitia.froog.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class History {

    List<Error> trainCost = new ArrayList();
    List<Error> testCost = new ArrayList();
    List<Error> trainAcc = new ArrayList();
    List<Error> testAcc = new ArrayList();

    public History() {
    }

    public void addTrainCost(int iteration, double cost) {
        trainCost.add(new Error(iteration, cost));
    }

    public void addTrainAcc(int iteration, double acc) {
        trainAcc.add(new Error(iteration, acc));
    }

    public void addTestCost(int iteration, double cost) {
        testCost.add(new Error(iteration, cost));
    }

    public void addTestAcc(int iteration, double acc) {
        testAcc.add(new Error(iteration, acc));
    }

    public double getTrainLastCost(){
        int L = trainCost.size() - 1;
        return trainCost.get(L).getError();
    }
    
    public double getTestLastCost(){
        int L = testCost.size() - 1;
        return testCost.get(L).getError();
    }
    
    public double getTrainLastAcc(){
        int L = trainAcc.size() - 1;
        return trainAcc.get(L).getError();
    }
    
    public double getTestLastAcc(){
        int L = testAcc.size() - 1;
        return testAcc.get(L).getError();
    }

    public List<Error> getTrainCost() {
        return trainCost;
    }

    public List<Error> getTestCost() {
        return testCost;
    }

    public List<Error> getTrainAcc() {
        return trainAcc;
    }

    public List<Error> getTestAcc() {
        return testAcc;
    }

}

package org.gitia.froog.util.data;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 * @param <T>
 */
public class LabelEncoder<T> {
    private final List<T> classes;
    
    public LabelEncoder(T[] labels){
        classes = Arrays.asList(labels);
    }
    
    public  List<T> getClasses(){
        return classes;
    }
    
    public int getNumClasses(){
        return this.classes.size();
    }
    
    public int encode(T label){
        return classes.indexOf(label);
    }
    
    public T decode(int index){
        return classes.get(index);
    }
}
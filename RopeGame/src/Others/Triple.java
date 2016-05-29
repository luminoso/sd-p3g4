/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import java.io.Serializable;

/**
 * Generic Triple implementation
 * 
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 * @param <X> 1st value
 * @param <Y> 2nd value
 * @param <Z> 3rd value
 */
public class Triple<X, Y, Z> implements Serializable {
    private static final long serialVersionUID = -2457813912764797410L;
    
    private final X first;
    private final Y second;
    private final Z third;

    public Triple(X first, Y second, Z third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public X getFirst() {
        return first;
    }

    public Y getSecond() {
        return second;
    }

    public Z getThird() {
        return third;
    }
}

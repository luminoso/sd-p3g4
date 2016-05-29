package Others;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of a Vectorial Clock
 * 
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class VectorTimestamp implements Cloneable, Serializable {
    private static final long serialVersionUID = -424825419083102993L;
    
    private int[] timestamps;
    private int index;

    public VectorTimestamp(int size, int index) {
        this.timestamps = new int[size];
        this.index = index;
    }
    
    public void increment() {
        timestamps[index]++;
    }
    
    public void update(VectorTimestamp vt) {
        for(int i = 0; i < timestamps.length; i++)
            timestamps[i] = Math.max(timestamps[i], vt.timestamps[i]);
    }
    
    public int[] toIntArray() {
        return timestamps;
    }
    
    @Override
    public VectorTimestamp clone() {
        VectorTimestamp copy = null;
        
        try {
            copy = (VectorTimestamp) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(VectorTimestamp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        copy.index = index;
        copy.timestamps = Arrays.copyOf(timestamps, timestamps.length);
        
        return copy;
    }
}

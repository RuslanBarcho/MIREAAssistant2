package radonsoft.mireaassistant.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Day {

    @SerializedName("odd")
    @Expose
    private Odd odd;
    @SerializedName("even")
    @Expose
    private Even even;

    public Odd getOdd() {
        return odd;
    }

    public void setOdd(Odd odd) {
        this.odd = odd;
    }

    public Even getEven() {
        return even;
    }

    public void setEven(Even even) {
        this.even = even;
    }

}

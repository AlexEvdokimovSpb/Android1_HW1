package gb.myhomework.android1.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//"rain":{"1h":0.65}
public class Rain {
    @SerializedName("1h")
    @Expose
    private float oneH;

    public float getOneH() {
        return oneH;
    }

    public void setOneH(float oneH) {
        this.oneH = oneH;
    }
}

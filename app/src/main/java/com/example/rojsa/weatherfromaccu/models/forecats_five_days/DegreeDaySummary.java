
package com.example.rojsa.weatherfromaccu.models.forecats_five_days;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DegreeDaySummary {

    @SerializedName("Heating")
    @Expose
    private Heating heating;
    @SerializedName("Cooling")
    @Expose
    private Cooling cooling;

    public Heating getHeating() {
        return heating;
    }

    public void setHeating(Heating heating) {
        this.heating = heating;
    }

    public Cooling getCooling() {
        return cooling;
    }

    public void setCooling(Cooling cooling) {
        this.cooling = cooling;
    }

}

/**
 * @author lorie
 */
package programmablethermostat;

import java.io.Serializable;

public class ScheduledTemp implements Serializable {
    private String dayOfWeek;
    private double timeOfDay;
    
    public ScheduledTemp(String dayOfWeek, double timeOfDay) {
        this.dayOfWeek = "";
        this.timeOfDay = 0;
    }

    ScheduledTemp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public String getdayOfWeek() {
        return this.dayOfWeek;
    }
    public double gettimeOfDay() {
        return this.timeOfDay;
    }
    public double settimeOfDay(double newTime) {
        return (this.timeOfDay = newTime);
    }
    
    @Override
    public String toString() {
        return String.format("%s\t%f", this.dayOfWeek, this.timeOfDay);
    }
}

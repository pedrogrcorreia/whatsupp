package pt.isec.pd.a21280305.pedrocorreia.whatsupp.servermanager.logic.data;

public class Time {
    private int hour;
    private int minute;
    private int second;

    public Time(int hour, int minute, int second){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour(){
        return hour;
    }

    public void setHour(int hour){
        this.hour = hour;
    }

    public int getMinute(){
        return minute;
    }

    public void setMinute(int minute){
        this.minute = minute;
    }

    public int getSecond(){
        return second;
    }

    public void setSecond(int second){
        this.second = second;
    }

    public int compareTimes(Time later){
        int seconds = 0;
        int minutes = 0;
        if(later.getMinute() > this.getMinute()){
            minutes = (later.getMinute() - this.getMinute()) * 60;
            System.out.println(minutes);
        }
        seconds = later.getSecond() - this.getSecond();
        if(seconds > 0){
            return minutes + (Math.abs(later.getSecond() - this.getSecond()));
        }
        return minutes - (Math.abs(later.getSecond() - this.getSecond()));
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}

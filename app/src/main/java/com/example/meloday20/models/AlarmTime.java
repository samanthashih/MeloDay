package com.example.meloday20.models;

public class AlarmTime {
    private int hour;
    private int minute;

    public AlarmTime() {}

    public AlarmTime(String inputTime) {
        AlarmTime alarmTime = new AlarmTime();

        String[] splitHour = inputTime.split(":");
        hour = Integer.parseInt(splitHour[0]);
        String[] splitMinute = splitHour[1].split(" ");
        if (splitMinute[1].equals("PM")) {
            hour+= 12;
        }
        minute = Integer.parseInt(splitMinute[0]);
        alarmTime.hour = hour;
        alarmTime.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}

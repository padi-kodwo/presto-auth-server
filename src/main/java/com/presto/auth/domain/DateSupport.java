package com.presto.auth.domain;

public class DateSupport {

    private String dateString = "";
    private String year = "";
    private String month = "";
    private String day = "";
    private String hour = "";
    private String minutes = "";
    private String seconds = "";

    public DateSupport(String dateString) {
        this.dateString = dateString;
        try {
            //2017-01-17 23:43:58 UTC
            //2016-08-17T17:32:18.424Z

            String[] parts;

            if (dateString.contains(" ")) {
                parts = dateString.split(" ");
                String datePart = parts[0];
                String timePart = parts[1];

                resolveDate(datePart);
                resolveTime(timePart);
            } else if (dateString.contains("T")) {
                parts = dateString.split("T");
                String datePart = parts[0];
                String timePart = parts[1];

                resolveDate(datePart);
                resolveTime(timePart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resolveDate(String date) {
        String[] parts = date.split("-");
        this.year = parts[0];
        this.month = parts[1];
        this.day = parts[2];
    }

    private void resolveTime(String time) {
        String[] parts = time.split(":");
        this.hour = parts[0];
        this.minutes = parts[1];

        String toSplit = parts[2];

        String[] secondsParts = toSplit.split("\\.");
        this.seconds = secondsParts[0];
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }
}

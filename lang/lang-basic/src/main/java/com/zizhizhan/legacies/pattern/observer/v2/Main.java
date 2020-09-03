package com.zizhizhan.legacies.pattern.observer.v2;

import java.util.Observer;

public class Main {

    public static void main(String[] args) {
        WeatherReporter reporter = new WeatherReporter();

        reporter.addObserver(new TVDisplay());
        reporter.addObserver(new RadioDisplay());

        Observer o = new Someone();
        reporter.addObserver(o);

        reporter.setTemperature(32);
        System.out.println();

        reporter.setTemperature(31);
        System.out.println();

        reporter.setTemperature(31);
        System.out.println();

        reporter.deleteObserver(o);
        reporter.setTemperature(30);
    }

}

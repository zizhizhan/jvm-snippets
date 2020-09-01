package com.zizhizhan.legacies.pattern.observer;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class Bank {

	public static void main(String[] args){
		Bank bank = new Bank();
		bank.addEventHandler((target, arguments) -> {
			log.info("{} with {}.", target, arguments);
		});
		bank.withdraw(100);
	}

    ArrayList<Observer<Bank, Object>> handlers = new ArrayList<>();

    public void addEventHandler(Observer<Bank, Object> o) {
        handlers.add(o);
    }

    public void removeEventHandler(Observer<Bank, Object> o) {
        handlers.remove(o);
    }

    public void withdraw(int money) {
        String msg = "You have get $" + money;
        for (Observer<Bank, Object> item : handlers) {
            item.update(this, msg);
        }
    }

}




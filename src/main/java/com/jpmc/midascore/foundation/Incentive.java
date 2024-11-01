package com.jpmc.midascore.foundation;

public class Incentive {
    private float ammount;

    public Incentive() {
    }

    public Incentive(float ammount) {
        this.ammount = ammount;
    }

    public void setAmount(float ammount) {
        this.ammount = ammount;
    }

    public float getAmount() {
        return ammount;
    }
}
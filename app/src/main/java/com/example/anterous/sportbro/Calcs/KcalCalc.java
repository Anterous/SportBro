package com.example.anterous.sportbro.Calcs;

public class KcalCalc {

    private int length = 0;
    private int type = 0;
    int weight = 82;
    int height = 181;
    int age = 22;
    int gender = 0; // 0 = M / 1 = F
    int burn = 0;

    //Calories Burned = [(Age x 0.2017) — (Weight x 0.09036) + (Heart Rate x 0.6309) — 55.0969] x Time / 4.184 MALE
    //Calories Burned = [(Age x 0.074) — (Weight x 0.05741) + (Heart Rate x 0.4472) — 20.4022] x Time / 4.184 FEMALE
    //Hearth rates are based on an average of the selected excercise


    public void params(int type, int length){
        int hb = 0;
        switch(type) {
            case 0 :
                // Jogging
                // hb avg 120
                hb = 120;
                calculate(hb, length);
                break; // optional

            case 1 :
                //Cardio
                //hb avg 140
                hb = 140;
                calculate(hb, length);
                break; // optional

            case 2 :
                //dancing
                //hb avg 100
                hb = 100;
                calculate(hb, length);
                break;

            case 3 :
                //team sports
                // hb avg 115
                hb = 115;
                calculate(hb, length);
                break;
            default :
                return;
               //mistake
        }

    }

    private void calculate(int hb_avg, int length){
        int kcal_burned = 0;
        if ( gender == 0){
            kcal_burned = (int) (((age * 0.2017) - (weight * 0.09036) + (hb_avg * 0.6309) - 55.0969) * length / 4.184);
            setBurn(kcal_burned);
        }

    }

    private void setBurn(int kcal_burned) {
        this.burn = kcal_burned;
    }

    public int getBurn() {
        return burn;
    }
}

package radonsoft.mireaassistant.utils;

import android.support.v4.util.Pair;

class LessonTimeGenerator {
    static Pair<String, String> getTiming(int number){
        switch (number){
            case 0: return new Pair<>("09:00", "10:30");
            case 1: return new Pair<>("10:40", "12:10");
            case 2: return new Pair<>("13:10", "14:40");
            case 3: return new Pair<>("14:50", "16:20");
            case 4: return new Pair<>("16:30", "18:00");
            case 5: return new Pair<>("18:10", "19:40");
        }
        return new Pair<>("","");
    }
}

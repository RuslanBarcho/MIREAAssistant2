package radonsoft.mireaassistant.utils;

import android.support.v4.util.Pair;

public class LessonTimeGenerator {
    public static Pair<String, String> getTiming(int number){
        switch (number){
            case 0: return new Pair<>("09:00", "10:30");
            case 1: return new Pair<>("10:40", "12:10");
            case 2: return new Pair<>("13:00", "14:30");
            case 3: return new Pair<>("14:40", "16:10");
            case 4: return new Pair<>("16:20", "17:50");
            case 5: return new Pair<>("18:00", "19:30");
        }
        return new Pair<>("","");
    }
}

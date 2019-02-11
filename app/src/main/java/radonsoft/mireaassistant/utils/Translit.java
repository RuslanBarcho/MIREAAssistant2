package radonsoft.mireaassistant.utils;

public class Translit {

    public Translit(){

    }

    private String cyr2lat(char ch){
        switch (ch){
            case 'А': return "A";
            case 'Б': return "B";
            case 'В': return "V";
            case 'Г': return "G";
            case 'Д': return "D";
            case 'Е': return "E";
            case 'Ё': return "YO";
            case 'Ж': return "ZH";
            case 'З': return "Z";
            case 'И': return "I";
            case 'Й': return "J";
            case 'К': return "K";
            case 'Л': return "L";
            case 'М': return "M";
            case 'Н': return "N";
            case 'О': return "O";
            case 'П': return "P";
            case 'Р': return "R";
            case 'С': return "S";
            case 'Т': return "T";
            case 'У': return "U";
            case 'Ф': return "F";
            case 'Х': return "X";
            case 'Ц': return "C";
            case 'Ч': return "CH";
            case 'Ш': return "SH";
            case 'Щ': return "STH";
            case 'Ъ': return "HH";
            case 'Ы': return "Y";
            case 'Ь': return "JH";
            case 'Э': return "E`";
            case 'Ю': return "JU";
            case 'Я': return "JA";
            default: return String.valueOf(ch);
        }
    }

    public String cyr2lat(String s){
        StringBuilder sb = new StringBuilder(s.length()*2);
        for(char ch: s.toCharArray()){
            sb.append(cyr2lat(ch));
        }
        return sb.toString();
    }
}
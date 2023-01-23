package com.notelysia.newsandroidservices;

public class RandomNumber {
    //generate random number with length = 6
    public String generateRandomNumber() {
        int length = 6;
        String numbers = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (numbers.length() * Math.random());
            sb.append(numbers.charAt(index));
        }
        return sb.toString();
    }
}

package com.glasiem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.util.regex.Pattern;

public class Main {
    private static HashSet<Integer> wordsSet = new HashSet<>();

    private static List<String> words = new ArrayList<>();

    private static List<HashSet<Integer>> sets = new ArrayList<>();

    private static int maxDistance = Integer.MIN_VALUE;

    private static int getDistance(String a, String b) {
        int distance = Math.abs(a.length()-b.length());
        for (int i = 0; i < Math.min(a.length(),b.length()); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    private static void checkNext(int i){
        HashSet<Integer> tempSet = new HashSet<>();
        for (int wordID: wordsSet) {
            if (getDistance(words.get(wordID),words.get(i)) < maxDistance){
                tempSet.add(wordID);
                wordsSet.remove(wordID);
            }
        }
        if (i+1 < words.size()) {
            wordsSet.add(i);
            if (!tempSet.isEmpty()){
                checkNext(i+1);
                wordsSet.remove(i);
                for (int wordID: tempSet) {
                    wordsSet.add(wordID);
                }
                tempSet.clear();
            }
            checkNext(i+1);
        }
        else {
            wordsSet.add(i);
            if (!tempSet.isEmpty()) {
                doAdd();
                for (int wordID : tempSet) {
                    wordsSet.add(wordID);
                }
                tempSet.clear();
                wordsSet.remove(i);
                doAdd();
            }
            else doAdd();
        }
        wordsSet.remove(i);
    }

    private static void doAdd() {
        boolean addCheck = true;
        for (HashSet<Integer> set: sets) {
            if (set.containsAll(wordsSet)){
                addCheck = false;
            }
        }
        if (addCheck == true) sets.add((HashSet<Integer>) wordsSet.clone());
    }

    public static void main(String[] args) throws IOException {
        File file = new File("F:\\in.txt");
        Pattern delimPattern = Pattern.compile("[\\s~!@#$%^&*()_+\\-=\\[\\]\\\\/\"№;:?|{}.,]+");
        Scanner input = new Scanner(file);
        input.useDelimiter(delimPattern);
        while (input.hasNext()) {
            String word = input.next();
            if (word.length() > 30)
                word = word.substring(0, 30);
            String finalWord = word;
            if (words.stream().filter(x -> x.equals(finalWord)).count() == 0)
                words.add(word);
        }

        for (int i = 0; i < words.size(); i++) {
            for (int j = i + 1; j < words.size(); j++) {
                String firstWord = words.get(i);
                String secondWord = words.get(j);
                int distance = getDistance(firstWord, secondWord);
                if (distance > maxDistance)
                    maxDistance = distance;
            }
        }
        System.out.println("Max distance: " + maxDistance);
        System.out.println("Sets: ");
        wordsSet.add(0);
        checkNext(1);
        for (HashSet<Integer> set: sets) {
            if (set.size()>1){
                for (int wordID: set) {
                    System.out.print(words.get(wordID) + " ");
                }
                System.out.print("\n");
            }
        }
    }
}
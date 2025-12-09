package com.mryoda.diagnostics.api.utils;

import com.github.javafaker.Faker;
import java.util.Random;
import java.util.UUID;

/**
 * Data Generator Utility for generating test data
 */
public class DataGenerator {
    
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    
    private DataGenerator() {
        // Private constructor
    }
    
    /**
     * Generate random name
     */
    public static String generateRandomName() {
        return faker.name().fullName();
    }
    
    /**
     * Generate random first name
     */
    public static String generateRandomFirstName() {
        return faker.name().firstName();
    }
    
    /**
     * Generate random last name
     */
    public static String generateRandomLastName() {
        return faker.name().lastName();
    }
    
    /**
     * Generate random email
     */
    public static String generateRandomEmail() {
        return faker.internet().emailAddress();
    }
    
    /**
     * Generate random email with domain
     */
    public static String generateRandomEmail(String domain) {
        String username = faker.name().username();
        return username + "@" + domain;
    }
    
    /**
     * Generate random phone number
     */
    public static String generateRandomPhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }
    
    /**
     * Generate random address
     */
    public static String generateRandomAddress() {
        return faker.address().fullAddress();
    }
    
    /**
     * Generate random city
     */
    public static String generateRandomCity() {
        return faker.address().city();
    }
    
    /**
     * Generate random country
     */
    public static String generateRandomCountry() {
        return faker.address().country();
    }
    
    /**
     * Generate random zip code
     */
    public static String generateRandomZipCode() {
        return faker.address().zipCode();
    }
    
    /**
     * Generate random gender
     */
    public static String generateRandomGender() {
        return random.nextBoolean() ? "male" : "female";
    }
    
    /**
     * Generate random status
     */
    public static String generateRandomStatus() {
        return random.nextBoolean() ? "active" : "inactive";
    }
    
    /**
     * Generate random number
     */
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * Generate random string with length
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
    
    /**
     * Generate UUID
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generate random boolean
     */
    public static boolean generateRandomBoolean() {
        return random.nextBoolean();
    }
    
    /**
     * Generate random text
     */
    public static String generateRandomText(int sentences) {
        return faker.lorem().sentences(sentences).toString();
    }
    
    /**
     * Generate random paragraph
     */
    public static String generateRandomParagraph() {
        return faker.lorem().paragraph();
    }
    
    /**
     * Generate random company name
     */
    public static String generateRandomCompanyName() {
        return faker.company().name();
    }
    
    /**
     * Generate random job title
     */
    public static String generateRandomJobTitle() {
        return faker.job().title();
    }
    
    /**
     * Generate current timestamp
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    
    /**
     * Generate random alphanumeric string
     */
    public static String generateAlphanumeric(int length) {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(alphanumeric.charAt(random.nextInt(alphanumeric.length())));
        }
        return result.toString();
    }
}

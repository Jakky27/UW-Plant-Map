package edu.uw.cs403.plantmap.backend;

public class Greeter {
    public String getGreeting() {
        return "Hello, world!";
    }

    public static void main(String[] args) {
        Greeter greeter = new Greeter();
        System.out.println(greeter.getGreeting());
    }
}

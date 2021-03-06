package tobyspring.springbook.learningtest.spring.ioc.bean;

public class Hello {
    String name;
    Printer printer;

    public Hello() {
    }

    public Hello(Printer printer) {
        this.printer = printer;
    }

    public Hello(String name, Printer printer) {
        this.name = name;
        this.printer = printer;
    }

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        this.printer.print(sayHello());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public Printer getPrinter() {
        return printer;
    }
}

package org.project;

import java.lang.reflect.Method;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws Exception {
    reflectionMethod();
    System.out.println("-----");
    handleCommand();
  }

  static void reflectionMethod() throws Exception {
    Class<?> reflectionClass = Class.forName("org.project.reflection.LoadClassAtRuntime");
    Object reflectionObject = reflectionClass.getDeclaredConstructor().newInstance();

    Method method = reflectionClass.getDeclaredMethod("execute");

    // not good practice but it's an exercise so idc
    method.setAccessible(true);
    method.invoke(reflectionObject);
  }

  static void handleCommand() throws Exception {
    System.out.print("Enter command:");
    try (Scanner scanner = new Scanner(System.in)) {
      String command = scanner.nextLine();
      String[] userArguments = command.split(" ");

      Class<?> reflectionClass = Class.forName("org.project.reflection.CommandHandler");
      Object reflectionObject = reflectionClass.getDeclaredConstructor().newInstance();

      // Extract method name and parameters
      String methodName = userArguments[0];
      Method[] methods = reflectionClass.getDeclaredMethods();

      for (Method method : methods) {
        if (method.getName().equals(methodName)) {
          method.setAccessible(true);

          // Convert input arguments dynamically
          Class<?>[] paramTypes = method.getParameterTypes();
          Object[] parsedArgs = new Object[paramTypes.length];

          for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == int.class) {
              parsedArgs[i] = Integer.parseInt(userArguments[i + 1]);
            } else if (paramTypes[i] == String.class) {
              parsedArgs[i] = userArguments[i + 1];
            }
          }

          // Invoke the method dynamically
          method.invoke(reflectionObject, parsedArgs);
          return;
        }
      }

      System.out.println("Invalid command");
    }
  }
}

package Package;

import java.sql.Date;
import java.util.Objects;
import java.util.Scanner;

public class TodoAppClass {

    public static void start() {

        DataBaseManagerClass.createNewTables();   // we start creating tables for users and tasks in case they do not exist, if they do, we open them
        DataBaseManagerClass.allUsersWithoutPassword();
        Scanner access = new Scanner(System.in);  // scanner object to input commands
        boolean appRunning = true;                // flag to stay inside the app as long as we do not enter the command exit

        while (appRunning) {                      // while statement
            boolean redflag = true;               // flag to keep asking to enter the right command to log in, register or exit the app

            while (redflag) {                     // while statement
                System.out.println("Enter 1 to login, 2 to register, or 0 to exit:");           // possible choices to start using the app
                String choice = access.nextLine();                                              // prompt the user for input each time in the loop

                if (Objects.equals(choice, "1")) {
                    redflag = false;
                    System.out.println("Please enter your username:");
                    String username = access.nextLine();
                    System.out.println("Please enter your password:");
                    String password = access.nextLine();
                    int olduser = UserClass.login(username, password);
                    int userId =  DataBaseManagerClass.getUserId(username);

                    if (olduser != -1) {
                        System.out.println("Time to organize your tasks!");
                        userMenu(access, userId); // Show user menu after successful login
                    } else {
                        System.out.println("Invalid login credentials. Try again.");
                        redflag = true; // Allow re-login attempt
                    }
                } else if (Objects.equals(choice, "2")) {
                    redflag = false;
                    System.out.println("Please create your username:");
                    String username = access.nextLine();
                    System.out.println("Please create a password:");
                    String password = access.nextLine();


                    UserClass newuser = new UserClass(username, password);
                    newuser.register();

                    int userId =  DataBaseManagerClass.getUserId(username);
                    userMenu(access, userId); // Show user menu after successful registration
                } else if (Objects.equals(choice, "0")) {
                    System.out.println("Exiting the application. Goodbye!");
                    appRunning = false;
                    redflag = false; // Exit the initial loop
                } else {
                    System.out.println("You must enter 1, 2, or 0.");
                }
            }
        }

        access.close();
    }

    public static void userMenu(Scanner access, int userId) {

        boolean userMenuRunning = true;

        while (userMenuRunning) {
            System.out.println("Enter a command:");
            System.out.println("1. view tasks");
            System.out.println("2. add task");
            System.out.println("3. delete task");
            System.out.println("4. update task");
            System.out.println("5. exit");
            String command = access.nextLine();

            switch (command.toLowerCase()) {
                case "1":
                    System.out.println("Displaying tasks...");
                    DataBaseManagerClass.selectAllTasksForUser(userId);
                    break;
                case "2":

                    System.out.println("Please enter task title:");
                    String title = access.nextLine();
                    System.out.println("Please enter task description:");
                    String description = access.nextLine();
                    System.out.println("Please enter task status:");
                    String status = access.nextLine();
                    System.out.println("Please enter task deadline:");

                    Date deadline = null;
                    while (deadline == null) {
                        System.out.println("Enter task deadline (yyyy-MM-dd):");
                        String deadlineStr = access.nextLine();
                        try {
                            deadline = Date.valueOf(deadlineStr); // Convert the string to java.sql.Date
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
                        }
                    }


                    TaskClass newtask = new TaskClass(userId, title, description, status, deadline);
                    newtask.insert();

                    System.out.println("Adding a new task...");
                    System.out.println("Task has been added.");

                    System.out.println("Check below all your tasks.");
                    DataBaseManagerClass.selectAllTasksForUser(userId);

                    break;

                case "3":

                    System.out.println("These are your tasks:");
                    DataBaseManagerClass.selectAllTasksForUser(userId);

                    boolean deleteflag = true;

                    while (deleteflag) {
                        System.out.println("Enter the ID number of the task you would like to delete");
                        String id2 = access.nextLine();

                        int taskId2;
                        try {
                            taskId2 = Integer.parseInt(id2);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a numeric task ID.");
                            continue; // Continue the loop to prompt again
                        }


                        if (DataBaseManagerClass.isTaskBelongToUser(userId, taskId2)) {
                            try {
                                TaskManagerClass.delete(taskId2);
                                deleteflag = false; // Exit the loop after successful update
                            } catch (IllegalArgumentException e) {
                                System.out.println("An error occurred during the update. Please try again.");
                            }
                        } else {
                            System.out.println("Task ID does not belong to this user, choose the right one.");
                        }
                    }

                    break;

                case "4":
                    System.out.println("These are your tasks:");
                    DataBaseManagerClass.selectAllTasksForUser(userId);

                    boolean updateflag = true;

                    while (updateflag) {
                        System.out.println("Enter the ID number of the task you would like to update");
                        String id1 = access.nextLine();

                        int taskId1;
                        try {
                            taskId1 = Integer.parseInt(id1);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a numeric task ID.");
                            continue; // Continue the loop to prompt again
                        }


                            if (DataBaseManagerClass.isTaskBelongToUser(userId, taskId1)) {
                            try {
                                UserClass.userUpdate(access, taskId1);
                                updateflag = false; // Exit the loop after successful update
                            } catch (IllegalArgumentException e) {
                                System.out.println("An error occurred during the update. Please try again.");
                            }
                        } else {
                            System.out.println("Task ID does not belong to this user, choose the right one.");
                        }
                    }

                    break;


                case "exit":
                    System.out.println("Logging out and returning to the main menu.");
                    userMenuRunning = false;
                    break;
                default:
                    System.out.println("Unknown command. Please enter '1' for view tasks, '2' for add task, '3' for delete task, '4' for update task or 'exit'.");
                    break;
            }
        }
    }
}

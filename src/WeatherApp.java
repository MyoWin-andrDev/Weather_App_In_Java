import java.util.Scanner;

public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String city;
        do{
            //Retrieve User Input
            System.out.println("*===============================*");
            System.out.println("Enter City ( Say 'No' to Exit)");
            city = scanner.nextLine();

            if(city.equalsIgnoreCase("No")) break;

            //Get Location Data

        }while (!city.equalsIgnoreCase("No"));
    }
}

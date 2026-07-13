package weatherApp3;
import javax.swing.*;
import java.awt.*; 
import java.awt.event.*; 
import java.net.HttpURLConnection; 
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject; 
import javax.imageio.ImageIO;
import java.awt.Image;

public class WeatherApp3 { 
  
    JFrame frame; 
    JLabel cityLabel;
    JTextField cityField; 
    JTextArea resultArea; 
    JButton fetchButton; 
    JLabel iconLabel; 
    JLabel heading;
    JLabel backgroundLabel;
    
    
WeatherApp3() {
    
    frame = new JFrame(" Weather Forecast App ");
    frame.setSize(600, 500);
    frame.setBackground(Color.BLUE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);

     
    ImageIcon bgImage = new ImageIcon("src/background.jpg"); 
    backgroundLabel = new JLabel(bgImage);
    backgroundLabel.setBounds(0, 0, 600, 500);
    frame.setContentPane(backgroundLabel);
    backgroundLabel.setLayout(null);
    
    
    heading = new JLabel("Weather Forecast", JLabel.CENTER);
    heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
    heading.setForeground(Color.BLUE);
    heading.setBounds(150, 20, 300, 40);
    
    cityLabel = new JLabel("City:");
    cityLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
    cityLabel.setForeground(Color.BLUE);
    cityLabel.setBounds(180,90,60,30);
    
    cityField = new JTextField("Enter city Name:");
    cityField.setFont(new Font("Segoe UI", Font.BOLD, 18));
    cityField.setBackground(new Color(0, 102, 204));
    cityField.setForeground(Color.WHITE);
    cityField.setBounds(220, 90,200, 40);
    
    fetchButton = new JButton("Get Weather");
    fetchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
    fetchButton.setBackground(new Color(0,102,204));
    fetchButton.setForeground(Color.WHITE);
    fetchButton.setBounds(230,150, 130, 40);
    
    resultArea = new JTextArea();
    resultArea.setFont(new Font("Consolas", Font.PLAIN, 16));
    resultArea.setEditable(false);
    resultArea.setOpaque(false);
    resultArea.setForeground(Color.BLUE);
    resultArea.setBounds(190, 220, 400, 150);
    
    iconLabel = new JLabel("", JLabel.CENTER);
    iconLabel.setBounds(220, 300, 150, 150);
    iconLabel.setOpaque(false);

    backgroundLabel.add(heading);
    backgroundLabel.add(cityLabel);
    backgroundLabel.add(cityField);
    backgroundLabel.add(fetchButton);
    backgroundLabel.add(resultArea);
    backgroundLabel.add(iconLabel);
    
    fetchButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String city = cityField.getText().trim();
            if (!city.isEmpty()) {
                getWeather(city);
            } else {
                resultArea.setText("Please enter a valid city name.");
                iconLabel.setIcon(null);
            }
        }
    });
  
    frame.setVisible(true);
}

void getWeather(String city) {
    String apiKey = "10a20adf5c0f525d934c452e538d4380"; // Replace with your OpenWeatherMap API key
    String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
            "&appid=" + apiKey + "&units=metric";

    try {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder json = new StringBuilder();
        while (scanner.hasNext()) {
            json.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject obj = new JSONObject(json.toString());
        String cityName = obj.getString("name");
        double temp = obj.getJSONObject("main").getDouble("temp");
        String description = obj.getJSONArray("weather").getJSONObject(0).getString("description");
        int humidity = obj.getJSONObject("main").getInt("humidity");
        double wind = obj.getJSONObject("wind").getDouble("speed");

        String iconCode = obj.getJSONArray("weather").getJSONObject(0).getString("icon");
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        Image image=ImageIO.read(new URL(iconUrl)).getScaledInstance(100,100,Image.SCALE_SMOOTH);
        iconLabel.setIcon(new ImageIcon(image));

        resultArea.setText("City: " + cityName +
                "\nTemperature: " + temp + " °C" +
                "\nCondition: " + description +
                "\nHumidity: " + humidity + "%" +
                "\nWind Speed: " + wind + " m/s");

    } catch (Exception e) {
        resultArea.setText("Error: Invalid city name or no internet connection.");
        iconLabel.setIcon(null);
    }
}

public static void main(String[] args){
   WeatherApp3 obj= new WeatherApp3();
}

}

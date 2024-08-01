import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;

    public WeatherAppGui() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(465, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(225, 235, 251));
        mainPanel.setLayout(null);
        add(mainPanel, BorderLayout.CENTER);
        addGuiComponents(mainPanel);
    }

    private void addGuiComponents(JPanel panel) {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 350, 40);
        searchTextField.setFont(new Font("SansSerif", Font.PLAIN, 24));
        panel.add(searchTextField);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 15, 45, 40);
        panel.add(searchButton);

        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/welcome.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        panel.add(weatherConditionImage);

        JLabel tempText = new JLabel("Welcome!");
        tempText.setBounds(0, 350, 450, 54);
        tempText.setFont(new Font("SansSerif", Font.BOLD, 48));
        tempText.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(tempText);

        JLabel weatherConditionDescription = new JLabel("Enter a location to get weather updates.");
        weatherConditionDescription.setBounds(0, 405, 450, 45);
        weatherConditionDescription.setFont(new Font("SansSerif", Font.PLAIN, 20));
        weatherConditionDescription.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(weatherConditionDescription);

        JLabel humidityImg = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImg.setBounds(15, 500, 74, 66);
        panel.add(humidityImg);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> </html>");
        humidityText.setBounds(90, 500, 100, 70);
        humidityText.setFont(new Font("SansSerif", Font.PLAIN, 17));
        panel.add(humidityText);

        JLabel windspeedImg = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImg.setBounds(250, 500, 74, 66);
        panel.add(windspeedImg);

        JLabel windspeedText = new JLabel("WindSpeed");
        windspeedText.setBounds(340, 500, 150, 66);
        windspeedText.setFont(new Font("SansSerif", Font.BOLD, 17));
        panel.add(windspeedText);

        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();

                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                    weatherData = WeatherApp.getWeatherData(userInput);

                    if (weatherData != null) {
                        String weatherCondition = (String) weatherData.get("weather_condition");

                        switch (weatherCondition) {
                            case "Clear":
                                weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                                break;
                            case "Cloudy":
                                weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                                break;
                            case "Rain":
                                weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                                break;
                            case "Snow":
                                weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                                break;
                        }

                        double temperature = (double) weatherData.get("temperature");
                        tempText.setText(temperature + " °C");

                        long humidity = (long) weatherData.get("humidity");
                        humidityText.setText("<html><b>Humidity\n</b>" + humidity + "%</html>");

                        double windspeed = (double) weatherData.get("windspeed");
                        windspeedText.setText("<html><b>Windspeed\n</b>" + windspeed + "Km/H</html>");

                        weatherConditionDescription.setText("Current weather in " + userInput + ":");
                    } else {
                        weatherConditionImage.setIcon(loadImage("src/assets/locationF.png"));
                        tempText.setText("");
                        weatherConditionDescription.setText("Weather data not available for this location.");
                        humidityText.setText("<html><b>Humidity</b></html>");
                        windspeedText.setText("<html><b>Windspeed</b> </html>");
                    }
                }
        };

        searchButton.addActionListener(searchAction);
        searchTextField.addActionListener(searchAction);//searches for location inputted when Enter key is entered
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Can't find resource path");
        return null;
    }
}

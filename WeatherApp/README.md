# Weather Application

---
**WeatherApp** is a Java-based weather application that provides current weather updates based on user input. It uses external APIs to fetch weather data and presents it through a graphical user interface (GUI).

## Features

- **Weather Data Retrieval**: Fetches current weather data including temperature, humidity, weather conditions, and wind speed.
- **Graphical User Interface**: Displays weather information with images representing different weather conditions.
- **Search Functionality**: Allows users to search for weather information by entering a location.

## Components

1. **WeatherApp**:
    - Contains methods for fetching weather data and location data from external APIs.
    - Parses the API responses and extracts relevant information such as temperature, humidity, weather conditions, and wind speed.

2. **WeatherAppGui**:
    - Provides the graphical user interface for the application.
    - Includes search functionality to get weather data based on user input.
    - Displays weather information and updates the GUI based on the fetched data.

## Dependencies

- **JSON.simple**: A library for parsing JSON data. Include the following dependency in your `pom.xml` if using Maven, or download the JAR file manually:
  ```xml
  <dependency>
      <groupId>com.googlecode.json-simple</groupId>
      <artifactId>json-simple</artifactId>
      <version>1.1.1</version>
  </dependency>
  ```
- **Java SE**: Ensure you have JDK 8 or higher installed.

## How to Run

1. **Compile and Run**:
    - Navigate to the project directory.
   
    - Run the application:
      ```bash
      RunApp.java
      ```

2. **Resources**:
    - Ensure that the image files (`search.png`, `welcome.png`, `humidity.png`, `windspeed.png`, `clear.png`, `cloudy.png`, `rain.png`, `snow.png`, `locationF.png`) are located in the `src/assets` directory.

## Usage

1. **Search for Weather**:
    - Enter a location name in the search field and click the search button or press Enter.
    - The application will display the current weather conditions, temperature, humidity, and wind speed for the specified location.

2. **Weather Updates**:
    - The GUI will update to show an image representing the current weather condition and other relevant information.

## Troubleshooting

- **Error: Couldn't fetch location data**: Ensure that the location name is correct and try again else if you have already checked, check if you have stable internet connection.
- **Error: No hourly data in API response**: Check the API response format and verify the API endpoint is correct.




---


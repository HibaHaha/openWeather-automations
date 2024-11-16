import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenWeatherApiTests {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";
    private static final String API_KEY = ""; // please add your API_KEY here
    private static final String CITY = "London";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * Test that the API returns a 200 status code for a valid request.
     */
    @Test
    public void testStatusCode200() {
        given()
            .queryParam("q", CITY)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .statusCode(200);
    }

    /**
     * Test that the API returns JSON content type.
     */
    @Test
    public void testContentTypeJSON() {
        given()
            .queryParam("q", CITY)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .contentType(ContentType.JSON);
    }

    /**
     * Test response time to ensure performance criteria are met.
     */
    @Test
    public void testResponseTime() {
        given()
            .queryParam("q", CITY)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .time(lessThan(2000L)); // Response time should be less than 2000 ms
    }

    /**
     * Test that the response contains the correct city name.
     */
    @Test
    public void testCityNameInResponse() {
        given()
            .queryParam("q", CITY)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .body("name", equalTo(CITY));
    }

    /**
     * Validate the response schema for mandatory fields and data types.
     */
    @Test
    public void validateResponseSchema() {
        given()
            .queryParam("q", CITY)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .body("coord.lon", instanceOf(Float.class))
            .body("coord.lat", instanceOf(Float.class))
            .body("weather[0].id", instanceOf(Integer.class))
            .body("weather[0].description", instanceOf(String.class))
            .body("main.temp", instanceOf(Float.class))
            .body("main.pressure", instanceOf(Integer.class))
            .body("main.humidity", instanceOf(Integer.class))
            .body("name", equalTo(CITY));
    }

    /**
     * Test error handling when the `q` parameter is missing.
     */
    @Test
    public void testMissingRequiredParameter() {
        given()
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .statusCode(400)
            .body("message", equalTo("Nothing to geocode"));
    }

    /**
     * Test error handling for invalid API keys.
     */
    @Test
    public void testInvalidApiKey() {
        given()
            .queryParam("q", CITY)
            .queryParam("appid", "INVALID_API_KEY")
        .when()
            .get("/weather")
        .then()
            .statusCode(401)
            .body("cod", equalTo(401))
            .body("message", containsString("Invalid API key"));
    }

    /**
     * Test that the temperature falls within a realistic range (in Kelvin).
     */
    @Test
    public void testTemperatureRange() {
        Response response = given()
            .queryParam("q", CITY)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather");

        float temperature = response.jsonPath().getFloat("main.temp");
        assertTrue(temperature > 200 && temperature < 330, "Temperature is outside expected Kelvin range.");
    }

    /**
     * Test optional `rain` field presence based on weather conditions.
     */
    @Test
    public void testOptionalRainField() {
        Response response = given()
            .queryParam("q", CITY)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather");

        boolean hasRainField = response.jsonPath().getMap("$").containsKey("rain");
        if (hasRainField) {
            assertTrue(response.jsonPath().getMap("rain").containsKey("1h"), "Rain data missing hourly information.");
        } else {
            System.out.println("Rain field is not present; no rain reported.");
        }
    }

    /**
     * Test handling of latitude and longitude parameters instead of city name.
     */
    @Test
    public void testLatLonQueryParameters() {
        given()
            .queryParam("lat", 51.5085)
            .queryParam("lon", -0.1257)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .statusCode(200)
            .body("name", equalTo(CITY));
    }

    /**
     * Test invalid latitude and longitude inputs.
     */
    @Test
    public void testInvalidLatLonInputs() {
        given()
            .queryParam("lat", 9999)
            .queryParam("lon", 9999)
            .queryParam("appid", API_KEY)
        .when()
            .get("/weather")
        .then()
            .statusCode(400)
            .body("message", notNullValue());
    }
}

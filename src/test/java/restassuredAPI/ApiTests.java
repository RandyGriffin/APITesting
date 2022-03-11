package restassuredAPI;

import models.Product;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {
//Making API Requests and Validation
    @Test
    public void getCategories()
    {
        String endpoint = "http://localhost:80/api_testing/product/read.php";
        var response=given().when().get(endpoint).then();
        response.log().body();
    }

    @Test
    public void getProduct(){
        String endpoint = "http://localhost:80/api_testing/product/read_one.php";
        var response=
                given().
                        queryParam("id",2)
                .when()
                        .get(endpoint)
                .then()
                .assertThat()
                        .statusCode(200)
                        .body("id",equalTo("2"))
                        .body("name",equalTo("Cross-Back Training Tank"))
                        .body("description", equalTo("The most awesome phone of 2013!"))
                        .body("price", equalTo("299.00"))
                        .body("category_id", equalTo("2"))
                        .body("category_name", equalTo("Active Wear - Women"));

        response.log().body();
    }

    @Test
    public void createProduct()
    {
        String endpoint = "http://localhost:80/api_testing/product/create.php";
        String body = """
                {
                "name": "Water Bottle",
                "description": "Blue water bottle. Holds 64 ounces",
                "price": 12,
                "category_id": 1
                }
                """;

        var response=given().body(body).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void updateProduct()
    {
        String endpoint = "http://localhost:80/api_testing/product/update.php";
        String body = """
                {
                "id": 1000,
                "name": "Water Bottle",
                "description": "Blue water bottle. Holds 64 ounces",
                "price": 15,
                "category_id": 1
                }
                """;
        var response = given().body(body).when().put(endpoint).then();
        response.log().body();
    }

    @Test
    public void deleteProduct()
    {
        String endpoint = "http://localhost:80/api_testing/product/delete.php";
        String body = """
                {
                "id": 1000
                }
                """;
        var response = given().body(body).when().delete(endpoint).then();
        response.log().body();
    }

    @Test
    public void createSerializedProduct(){ //uses serialized body instead of plain string/text block
        String endpoint = "http://localhost:80/api_testing/product/create.php";
        Product product = new Product(
                "Water Bottle",
                "Blue water bottle. Holds 64 ounces",
                12,
                1
        );
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void createSerializedSweatBand(){ //uses serialized body instead of plain string/text block
        String endpoint = "http://localhost:80/api_testing/product/create.php";
        Product product = new Product(
                1002,
                "Sweatband",
                "Sweatband is a material worn to absorb sweat.",
                9,
                3
        );
        var response = given().body(product).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void updateSerializedSweatBand(){
        String endpoint = "http://localhost:80/api_testing/product/update.php";
        Product product = new Product(
                1002,
                "Sweatband",
                "Sweatband is a material worn to absorb sweat.",
                12,
                3


        );
        var response = given().body(product).when().put(endpoint).then();
        response.log().body();
    }

    @Test
    public void getSerializedSweatBand(){
        String endpoint = "http://localhost:80/api_testing/product/read_one.php";
        var response = given().queryParam("id", 1002).get(endpoint).then();
        response.log().body();
    }

    @Test
    public void deleteSerializedSweatBand(){
        String endpoint = "http://localhost:80/api_testing/product/delete.php";
        Product product = new Product(
                1002,
                "Sweatband",
                "Sweatband is a material worn to absorb sweat.",
                12,
                3

        );
        var response = given().body(product).when().delete(endpoint).then();
    }

    @Test
    public void getProducts(){
        String endpoint = "http://localhost:80/api_testing/product/read.php";
        given().
        when().
                get(endpoint).
                then().
                    log().
                      body().
                        assertThat().
                            statusCode(200).
                            //verify response body
                            body("records.size()", greaterThan(0)) //accessed the name of the array
                            .body("records.id", everyItem(notNullValue()))   //notNullValue verifies that there is a value in the array
                            .body("records.name", everyItem(notNullValue()))
                            .body("records.description", everyItem(notNullValue()))
                            .body("records.price", everyItem(notNullValue()))
                            .body("records.category_id", everyItem(notNullValue()))
                            .body("records.category_name", everyItem(notNullValue()))
                            .body("records.id[0]", equalTo("1003")); //verifies a specific field
    }

    @Test
    public void getProducts2(){
        String endpoint = "http://localhost:80/api_testing/product/read.php";
        given().
                when().
                get(endpoint).
                then().
                log().
                headers().
                assertThat().
                statusCode(200).
                header("Content-Type", equalTo("application/json; charset=UTF-8")). //Verify Response Header
                body("records.size()", greaterThan(0)) //accessed the name of the array
                .body("records.id", everyItem(notNullValue()))   //notNullValue verifies that there is a value in the array
                .body("records.name", everyItem(notNullValue()))
                .body("records.description", everyItem(notNullValue()))
                .body("records.price", everyItem(notNullValue()))
                .body("records.category_id", everyItem(notNullValue()))
                .body("records.category_name", everyItem(notNullValue()))
                .body("records.id[0]", equalTo("1003")); //verifies a specific field
    }

    @Test
    public void getDeserializedProduct(){
        String endpoint = "http://localhost:80/api_testing/product/read_one.php";
        Product expectedProduct = new Product(
                2,
                "Cross-Back Training Tank",
                "The most awesome phone of 2013!",
                299.00,
                2,
                "Active Wear - Women"
        );
        Product actualProduct =
                given().
                queryParam("id", "2").
        when().
            get(endpoint).
                as(Product.class);

        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));
    }

    @Test
    public void getVitaminProduct(){
        String endpoint = "http://localhost:80/api_testing/product/read.php";
        given()
                .queryParam("id", 18)
       .when()
                .get(endpoint)
       .then()
                .assertThat()
                    .statusCode(200)
                    .header("Content-Type", equalTo("application/json; charset=UTF-8"))


                    .body("id", equalTo("18"))
                    .body("name", equalTo("Multi-Vitamin (90 capsules)"))
                    .body("description", equalTo("A daily dose of our Multi-Vitamins fulfills a day "))
                    .body("price", equalTo("10.00"))
                    .body("category_id", equalTo("4"))
                    .body("category_name", equalTo("Supplements"));
    }

}

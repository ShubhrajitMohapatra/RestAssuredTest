package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class TrelloApi {

    public static String baseURL = "https://api.trello.com";
    public static String apiKey = "efbdea129009a665c69ff1f70afc30cc";
    public static String apiToken = "ATTAfbc04e437e222c1644b4273b582ece1d821a5e1e2bc413a43adbe4ed85ca0245269C83CA";
    public static String boardId;
    public static int rowCount;

    //Test Test
    public static String boardName, color, description;





    @Test(priority = 0)
    public void dataDriven() throws IOException {
        //STEP - 1 = I've to access the Excel File and store it in a variable
        File excelFile = new File("/Users/shubhrajitmohapatra/Downloads/Project/RestAssuredTest/DataDriven.xlsx");

        //STEP - 2 = Read the File
        FileInputStream fis = new FileInputStream(excelFile);

        //STEP - 3 = There are two Methods for 2 different file format. These are Apache POI methods
        //1-  If the Excel Format is .xls then use the method HSSF
        //2- And If the Excel File format is .xlsx then use XSSF method
        XSSFWorkbook workBook = new XSSFWorkbook(fis);

        //Test

        //STEP - 4 = Go to the sheet and then read data
        XSSFSheet workSheet = workBook.getSheet("Sheet1");


        rowCount = workSheet.getLastRowNum();
        for (int i=1; i<=rowCount;i++){
            boardName = workSheet.getRow(i).getCell(0).getStringCellValue();
            description = workSheet.getRow(i).getCell(1).getStringCellValue();
            color = workSheet.getRow(i).getCell(2).getStringCellValue();

            System.out.println(boardName+" -> "+description+" -> "+color);

            createBoard();
            getBoard();
            updateBoard();
            deleteBoard();

            //Any update is fine

        }





    }



    //Rest Assured contains 3 Methods
    //1-> given() - It means pre condition
    //2-> when() - Action Method / It contain http methods (GET,PUT,POST,PATCH,DELETE)
    //3-> then() - Output which is nothing but my result.
    @Test(priority = 1,enabled = false)
    public void createBoard(){

        //Step -1 = I've to add the Base URL
        //There are predefined libraries in RestAssured
        //Response - It is an interface to capture the response.
        // asString - is to get the response in string
        // asPrettyString - is to show the json in pretty format


        RestAssured.baseURI = baseURL;

        Response resp = given()
                .queryParam("key",apiKey)
                .queryParam("token",apiToken)
                .queryParam("name","TrelloTest12")
                .queryParam("prefs_background","orange")
                .header("Content-Type","application/json")

                .when()
                .post("/1/boards/")

                .then()
                .assertThat().statusCode(200)
                .extract().response();

        String JsonResp = resp.asString();

        //Get Board Id from the response.

        JsonPath js=new JsonPath(JsonResp);
        boardId = js.getJsonObject("id");

        System.out.println(boardId);
    }


    @Test(priority = 2,enabled = false)
    public void getBoard(){
        RestAssured.baseURI = baseURL;

        Response respo = given()
                .queryParam("key",apiKey)
                .queryParam("token",apiToken)


                .when()
                .get("/1/boards/"+boardId)

                .then().assertThat().statusCode(200)
                .extract().response();

        System.out.println(respo.asPrettyString());
    }

    @Test(priority = 3,enabled = false)
    public void updateBoard(){
        RestAssured.baseURI = baseURL;

        Response resp = given()
                .queryParam("key",apiKey)
                .queryParam("token",apiToken)
                .queryParam("name",boardName)
                .queryParam("desc",description)
                .queryParam("prefs_background",color)
                .header("Content-Type","application/json")

                .when().put("/1/boards/"+boardId)

                .then().assertThat().statusCode(200)
                .extract().response();
    }


    @Test(priority = 4, enabled = false)
    public void deleteBoard(){
        RestAssured.baseURI = baseURL;

        given()
                .queryParam("key",apiKey)
                .queryParam("token",apiToken)
                .header("Content-Type","application/json")

                .when()
                .delete("/1/boards/"+boardId)
                .then().assertThat().statusCode(200);
    }



}

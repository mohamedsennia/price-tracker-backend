package com.example.Price.Tracker;



import com.example.Price.Tracker.Record.Website;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Scrapper extends Thread{
    private String productName;
 private    Website website;
 private  Map<String,String> translation;

    public Scrapper(String productName,Website website){
        this.website=website;
      this.productName=productName;//Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sept,Oct,Nov,Dec
        translation = new HashMap<String, String>();
        translation.put("Jan","janvier");
        translation.put("Feb","février");
        translation.put("Mar","mars");
        translation.put("Jan","janvier");
        translation.put("Apr","avril");
        translation.put("May","mai");
        translation.put("Jun","juin");
        translation.put("Jul","juillet");
        translation.put("Aug","aout");
        translation.put("Sept","septembre");
        translation.put( "Oct","octobre");
        translation.put("Nov","novembre");
        translation.put( "Dec","décembre");

    }
    public void run() {
        switch (website){
            case Ebay -> {scrapeEbay();break;}

            //case bestbuy -> {scrapeBestBuy();break;}
        }
        }

    private void scrapeBestBuy() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless=new");
        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://www."+this.website.name()+".com");
            Thread.sleep(1000);
            for(WebElement clickable:driver.findElements(By.className("us-link"))){
                try {
                    clickable.click();
                    break;
                }catch (ElementNotInteractableException exception /*WebDriverException*/){

                }
            }
            WebElement searchInput=driver.findElement(By.id("gh-search-input"));
            searchInput.sendKeys(productName);
            searchInput.sendKeys(Keys.ENTER);
            float intial_average_price=0;//
            List<WebElement> items= driver.findElements(By.className("list-item"));
            int counter=0;
            WebElement price;
            WebElement title;
            for(WebElement item:items){
                try {


                price=item.findElement(By.className("priceView-hero-price"));
                price=price.findElements(By.tagName("span")).get(0);
                title=item.findElement(By.className("sku-title"));

                if (price.getText().length()>0&&title.getText().toLowerCase().contains(productName.toLowerCase())){
                    intial_average_price +=Float.parseFloat(price.getText().substring(1));
                    counter++;
                }
                }catch (WebDriverException webDriverException){

                }
            }
            intial_average_price=intial_average_price/counter;
            System.out.println("intial_average_price: "+intial_average_price);
            float average_price=0;
            counter=0;
            float treshHold=(intial_average_price*80)/100;
            for(WebElement item:items){
                try {


                    price = item.findElement(By.className("priceView-hero-price"));
                    price = price.findElements(By.tagName("span")).get(0);
                    title = item.findElement(By.className("sku-title"));

                    if (price.getText().length() > 0 && title.getText().toLowerCase().contains(productName.toLowerCase())) {
                        float value = Float.parseFloat(price.getText().substring(1));
                        if ((value < intial_average_price + treshHold) && (value > intial_average_price - treshHold)) {
                            average_price += value;
                            counter++;
                        }

                    }
                }catch (WebDriverException webDriverException){

                }
            }
            average_price=average_price/counter;
            System.out.println("average_price: "+average_price);

        }
        catch (Exception e){
            System.out.println(e);
        }finally {
            driver.quit();
        }
    }


    public void scrapeEbay(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        try {

            driver.get("https://www.ebay.fr/");
            Thread.sleep(1000);
            WebElement searchInput=driver.findElement(By.id("gh-ac"));
            WebElement searchButton=driver.findElement(By.id("gh-btn"));
            searchInput.clear();

            searchInput.sendKeys(productName);


            searchButton.click();
            Thread.sleep(1000);
            WebElement numberButton=driver.findElement(By.id("srp-ipp-menu"));
            numberButton.click();
            numberButton.findElements(By.tagName("li")).get(numberButton.findElements(By.tagName("li")).size()-1).click();

          WebElement sortButton=  driver.findElement(By.className("srp-sort"));
          sortButton.click();
          List<WebElement> sortOptions=sortButton.findElements(By.tagName("li"));
          sortOptions.get(2).click();
          Date date=new Date();//s-item__listingDate

            Calendar cal = Calendar.getInstance();
            System.out.println();
            float intial_average_price=0;//
            List<WebElement> items= driver.findElements(By.className("s-item"));
            int counter=0;
            WebElement price;
            WebElement title;
            for(WebElement item:items){
                try {


                price=item.findElement(By.className("s-item__price"));
                title=item.findElement(By.className("s-item__title"));
                if(!item.findElement(By.className("s-item__listingDate")).getText().split(" ")[0].equals(translation.get(new SimpleDateFormat("MMM").format(cal.getTime()).toString())+"-"+date.getDate())){

                    break;
                }
                if (price.getText().length()>0&&title.getText().toLowerCase().contains(productName.toLowerCase())){
                    intial_average_price +=Float.parseFloat(price.getText().replaceAll("[^0-9.]", ""));
                    counter++;
                }}catch (WebDriverException webDriverException){

                }

            }
            intial_average_price=intial_average_price/counter;
            System.out.println("intial_average_price: "+intial_average_price);
            float average_price=0;
            counter=0;
            float treshHold=(intial_average_price*80)/100;
            for(WebElement item:items){
                try {


                price=item.findElement(By.className("s-item__price"));
                title=item.findElement(By.className("s-item__title"));
                    if(!item.findElement(By.className("s-item__listingDate")).getText().split(" ")[0].equals(translation.get(new SimpleDateFormat("MMM").format(cal.getTime()).toString())+"-"+date.getDate())){

                        break;
                    }
                if (price.getText().length()>0&&title.getText().toLowerCase().contains(productName.toLowerCase())){
                    float value=Float.parseFloat(price.getText().replaceAll("[^0-9.]", ""));
                    if((value<intial_average_price+treshHold)&&(value>intial_average_price-treshHold)){
                        average_price +=value;
                        counter++;
                    }

                }
                }catch (WebDriverException webDriverException){

                }
            }
            average_price=average_price/counter;
            System.out.println("average_price: "+average_price);


        } catch (Exception e) {
            System.out.println(e);
        }finally {
            driver.quit();
        }
    }


}

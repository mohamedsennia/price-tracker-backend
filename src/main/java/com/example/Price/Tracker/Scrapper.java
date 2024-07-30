package com.example.Price.Tracker;



import com.example.Price.Tracker.Record.Record;
import com.example.Price.Tracker.Record.RecordService;
import com.example.Price.Tracker.product.Product;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.SimpleDateFormat;
import java.util.*;

public class Scrapper extends Thread{
    private Product product;

 private  Map<String,String> translation;
    private int threshHold;
private RecordService recordService;

    public Scrapper(Product product, RecordService recordService){
        this.threshHold=80;

      this.product=product;//Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sept,Oct,Nov,Dec
        translation = new HashMap<String, String>();
        translation.put("Jan","janvier");
        translation.put("Feb","février");
        translation.put("Mar","mars");
        translation.put("Jan","janvier");
        translation.put("Apr","avril");
        translation.put("May","mai");
        translation.put("Jun","juin");
        translation.put("Jul","juil.");
        translation.put("Aug","aout");
        translation.put("Sept","septembre");
        translation.put( "Oct","octobre");
        translation.put("Nov","novembre");
        translation.put( "Dec","décembre");
    this.recordService =recordService;
    }
    public void run() {
        initialScrapeEbay();
        }
    public void initialScrapeEbay(){

            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");

            WebDriver driver = new ChromeDriver(options);
            try {
                driver.get("https://www.ebay.fr/");
                Thread.sleep(1000);
                WebElement searchInput=driver.findElement(By.id("gh-ac"));
                WebElement searchButton=driver.findElement(By.id("gh-btn"));
                searchInput.clear();
                searchInput.sendKeys(product.getName());
                searchButton.click();
                Thread.sleep(1000);
                WebElement numberButton=driver.findElement(By.id("srp-ipp-menu"));
                numberButton.click();
                numberButton.findElements(By.tagName("li")).get(numberButton.findElements(By.tagName("li")).size()-1).click();
                WebElement sortButton=  driver.findElement(By.className("srp-sort"));
                sortButton.click();
                List<WebElement> sortOptions=sortButton.findElements(By.tagName("li"));
                sortOptions.get(2).click();
                Date date=new Date();
                Calendar cal = Calendar.getInstance();

                float intial_average_price=0;//
                List<WebElement> items= driver.findElements(By.className("s-item"));
                int counter=0;
                WebElement price;
                WebElement title;
                int i=0;
                for(WebElement item:items){
                    try {
                        i=i+1;

                        price=item.findElement(By.className("s-item__price"));

                        title=item.findElement(By.className("s-item__title"));
                        if(!item.findElement(By.className("s-item__listingDate")).getText().split(" ")[0].equals(translation.get(new SimpleDateFormat("MMM").format(cal.getTime()).toString())+"-"+date.getDate())){

                            break;
                        }
                        if (price.getText().length()>0&&title.getText().toLowerCase().contains(product.getName().toLowerCase())){

                            String priceString=price.getText().replaceAll(",", ".");
                            priceString=priceString.replaceAll("[^0-9.]", "");

                            float value=Float.parseFloat(priceString);

                            intial_average_price+=value;
                            counter++;
                        }}catch (WebDriverException webDriverException){
                        System.out.println(webDriverException);
                    }

                }
                intial_average_price=intial_average_price/counter;

                float average_price=0;
                counter=0;
                float treshHold=(intial_average_price*threshHold)/100;
                for(WebElement item:items){
                    try {


                        price=item.findElement(By.className("s-item__price"));

                        title=item.findElement(By.className("s-item__title"));
                        System.out.println(title.getText());
                        if(!item.findElement(By.className("s-item__listingDate")).getText().split(" ")[0].equals(translation.get(new SimpleDateFormat("MMM").format(cal.getTime()).toString())+"-"+date.getDate())){

                            break;
                        }
                        if (price.getText().length()>0&&title.getText().toLowerCase().contains(product.getName().toLowerCase())){
                            String priceString=price.getText().replaceAll(",", ".");
                            priceString=priceString.replaceAll("[^0-9.]", "");
                            float value=Float.parseFloat(priceString);
                            if((value<intial_average_price+treshHold)&&(value>intial_average_price-treshHold)){
                                average_price +=value;
                                counter++;
                            }

                        }
                    }catch (WebDriverException webDriverException){

                    }
                }

                average_price =average_price/counter;

                System.out.println(average_price);


            } catch (Exception e) {
                System.out.println(e);
            }finally {
                driver.quit();
                //daileScrapeEbay();
            }
    }



    public void daileScrapeEbay(){
        while (true){
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            long timeToSleep = (c.getTimeInMillis()-System.currentTimeMillis());
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.get("https://www.ebay.fr/");
            Thread.sleep(1000);
            WebElement searchInput=driver.findElement(By.id("gh-ac"));
            WebElement searchButton=driver.findElement(By.id("gh-btn"));
            searchInput.clear();
            searchInput.sendKeys(product.getName());
            searchButton.click();
            Thread.sleep(1000);
            WebElement numberButton=driver.findElement(By.id("srp-ipp-menu"));
            numberButton.click();
            numberButton.findElements(By.tagName("li")).get(numberButton.findElements(By.tagName("li")).size()-1).click();
          WebElement sortButton=  driver.findElement(By.className("srp-sort"));
          sortButton.click();
          List<WebElement> sortOptions=sortButton.findElements(By.tagName("li"));
          sortOptions.get(2).click();
          Date date=new Date();
            Calendar cal = Calendar.getInstance();

            float intial_average_price=0;//
            List<WebElement> items= driver.findElements(By.className("s-item"));
            int counter=0;
            WebElement price;
            WebElement title;
            int i=0;
            for(WebElement item:items){
                try {
    i=i+1;

                price=item.findElement(By.className("s-item__price"));

                title=item.findElement(By.className("s-item__title"));
                if(!item.findElement(By.className("s-item__listingDate")).getText().split(" ")[0].equals(translation.get(new SimpleDateFormat("MMM").format(cal.getTime()).toString())+"-"+date.getDate())){

                    break;
                }
                if (price.getText().length()>0&&title.getText().toLowerCase().contains(product.getName().toLowerCase())){

                    String priceString=price.getText().replaceAll(",", ".");
                    priceString=priceString.replaceAll("[^0-9.]", "");

                    float value=Float.parseFloat(priceString);

                    intial_average_price+=value;
                    counter++;
                }}catch (WebDriverException webDriverException){
    System.out.println(webDriverException);
                }

            }
            intial_average_price=intial_average_price/counter;

            float average_price=0;
            counter=0;
            float treshHold=(intial_average_price*threshHold)/100;
            for(WebElement item:items){
                try {


                price=item.findElement(By.className("s-item__price"));

                title=item.findElement(By.className("s-item__title"));
                    if(!item.findElement(By.className("s-item__listingDate")).getText().split(" ")[0].equals(translation.get(new SimpleDateFormat("MMM").format(cal.getTime()).toString())+"-"+date.getDate())){

                        break;
                    }
                if (price.getText().length()>0&&title.getText().toLowerCase().contains(product.getName().toLowerCase())){
                    String priceString=price.getText().replaceAll(",", ".");
                    priceString=priceString.replaceAll("[^0-9.]", "");
                    float value=Float.parseFloat(priceString);
                    if((value<intial_average_price+treshHold)&&(value>intial_average_price-treshHold)){
                        average_price +=value;
                        counter++;
                    }

                }
                }catch (WebDriverException webDriverException){

                }
            }

            average_price =average_price/counter;

           this.recordService.addRecord(new Record(product,date,average_price));


        } catch (Exception e) {
            System.out.println(e);
        }finally {
            driver.quit();

        }}
    }


}

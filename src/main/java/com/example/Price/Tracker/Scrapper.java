package com.example.Price.Tracker;



import com.example.Price.Tracker.Record.Record;
import com.example.Price.Tracker.Record.RecordService;
import com.example.Price.Tracker.product.Product;
import com.example.Price.Tracker.product.ProductService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.SimpleDateFormat;
import java.util.*;

public class Scrapper extends Thread{
    private Product product;

 private  Map<String,String> translation;
    private int threshHold;
private RecordService recordService;
private ProductService productService;
private String[] monthsArray=new String[12];
private  boolean firstTimescrapping;
    public Scrapper(Product product, RecordService recordService, ProductService productService,boolean firstTimescrapping){
        this.threshHold=80;

        monthsArray[0]="janv.";
        monthsArray[1]="févr.";
        monthsArray[2]="mars";
        monthsArray[3]="avr.";
        monthsArray[4]="mai";
        monthsArray[5]="juin";
        monthsArray[6]="juil.";
        monthsArray[7]="août";
        monthsArray[8]="sept.";
        monthsArray[9]="oct.";
        monthsArray[10]="nov.";
        monthsArray[11]="déc.";

      this.product=product;//Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sept,Oct,Nov,Dec
        translation = new HashMap<String, String>();
        translation.put("Jan","janv.");
        translation.put("Feb","févr.");
        translation.put("Mar","mars");

        translation.put("Apr","avr.");
        translation.put("May","mai");
        translation.put("Jun","juin");
        translation.put("Jul","juil.");
        translation.put("Aug","août");
        translation.put("Sept","sept.");
        translation.put( "Oct","oct.");
        translation.put("Nov","nov.");
        translation.put( "Dec","déc.");
    this.recordService =recordService;
    this.productService=productService;
    this.firstTimescrapping=firstTimescrapping;
    }
    public void run() {
       if(firstTimescrapping){
           initialScrapeEbay();
       }else{
           dailyScrapeEbay();
       }
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

                Calendar cal = Calendar.getInstance();

                float intial_average_price=0.0F;

                int counter=0;
                WebElement price;
                WebElement title;
                int i=0;

                String itemMonth;
                String itemDate;
                String previousDate,previousMonth;
                List<String> months=new ArrayList<String>();
                boolean shouldStop=false;

                previousDate=previousMonth="";
                List<WebElement> pages=driver.findElements(By.className("pagination__item"));
                List<WebElement> items;
                int pageNumber=0;
                for (WebElement page:pages){
                    pages=driver.findElements(By.className("pagination__item"));
                    pages.get(pageNumber).click();
                    pageNumber+=1;
                items= driver.findElements(By.className("s-item"));
                for(WebElement item:items){

                    try {
                        i=i+1;

                        price=item.findElement(By.className("s-item__price"));

                        title=item.findElement(By.className("s-item__title"));
                        itemDate=item.findElement(By.className("s-item__listingDate")).getText().split(" ")[0];
                        itemMonth=itemDate.split("-")[0];

                        if(!previousMonth.equals(itemMonth)&&!previousMonth.equals("")){
                            if(months.stream().anyMatch(itemMonth::equals)){

                                shouldStop=true;
                                break;
                            }
                            while (!monthsArray[new Date(cal.getTimeInMillis()).getMonth()].equals(itemMonth)){

                                cal.add(Calendar.MONTH,-1);

                            }

                            months.add(previousMonth);
                        }
                        previousMonth=itemMonth;
                        if (price.getText().length()>0&&title.getText().toLowerCase().contains(product.getName().toLowerCase())){
                            String priceString=price.getText().replaceAll(",", ".");
                            if(priceString.contains("à")){
                                continue;
                            }
                            priceString=priceString.replaceAll("[^0-9.]", "");
                            float value=Float.parseFloat(priceString);
                            if(!itemDate.equals(previousDate)&&!previousDate.isEmpty()){

                                cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf(previousDate.split("-")[1]));

                                this.recordService.addRecord(new Record(product,new Date(cal.getTimeInMillis()),intial_average_price/counter));
                                counter=0;
                                intial_average_price=0.0F;
                            }
                            previousDate=itemDate;

                                intial_average_price+=value;

                                counter++;

                        }




                    }catch (WebDriverException webDriverException){
                           if(!( webDriverException instanceof NoSuchElementException)){
                               System.out.println(webDriverException);
                           }

                    }

                }
                if(shouldStop){
                    break;
                }
            }

            } catch (Exception e) {
                System.out.println(e);
            }finally {
                driver.quit();
                product.toggleFinishedIntialScrapping();

                this.productService.save(product);
                dailyScrapeEbay();
            }
    }



    public void dailyScrapeEbay(){

        while (true){
            if(!this.productService.isActivated(product.getId())){
                break;
            }
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 10);
            c.set(Calendar.MINUTE, 9);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            long timeToSleep = (c.getTimeInMillis()-System.currentTimeMillis());
            c.add(Calendar.DAY_OF_MONTH, -1);
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
                    if(priceString.contains("à")){
                        continue;
                    }
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

                    if(priceString.contains("à")){
                        continue;
                    }
                    priceString=priceString.replaceAll("[^0-9.]", "");

                    float value=Float.parseFloat(priceString);
                    if((value<intial_average_price+treshHold)&&(value>intial_average_price-treshHold)){
                        average_price +=value;
                        counter++;
                    }

                }
                }catch (WebDriverException webDriverException){
                System.out.println(webDriverException);
                }
            }

            average_price =average_price/counter;
            if(!Float.isNaN(average_price)){
                this.recordService.addRecord(new Record(product,date,average_price));
            }else{
                this.recordService.addRecord(new Record(product,date,(float)0));
            }



        } catch (Exception e) {
            System.out.println(e);
        }finally {
            driver.quit();

        }
        }
    }


}

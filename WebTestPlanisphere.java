package planisphere;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebTestPlanisphere {
    WebDriver webDriver;
    WebDriverWait wait;
	String testDate;
	Date dt;
	int weekEnd;
	int termValue;
	int termValueWeekEnd;
	String dateFrom;
	String dateTo;
	int headCountValue;
	String breakFastValue;
	String planAvalue;
	String planBvalue;
	String guestValue;
	int planisphereError;
	String email;
	String password;
	String password2;
	String username;
	String rank;
	String address;
	String tel;
	String gender;
	String[] birthday = {"//"};
	String notification;

	private boolean acceptNextAlert = true;

	public void open() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\WebDrivers\\chromedriver_win32\\chromedriver.exe");
		//Mac
//		System.setProperty("webDriver.chrome.driver", "/Volumes/data/WebDriver/Chrome/chromedriver");
		webDriver = new ChromeDriver();
		Thread.sleep(3000);
		webDriver.get("https://hotel.testplanisphere.dev/ja/index.html");
		Thread.sleep(5000);
		webDriver.manage().window().maximize();
		Thread.sleep(2000);
		webDriver.navigate().refresh();

		dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testDate = sdf.format(dt);

		Thread.sleep(5000);
        wait = new WebDriverWait(webDriver, 10);
	}

	public void chroniumEdgeOpen() throws InterruptedException {
		System.setProperty("webdriver.edge.driver", "C:\\WebDrivers\\edgedriver_win64\\msedgedriver.exe");

		webDriver = new EdgeDriver();
		Thread.sleep(3000);
		webDriver.get("https://hotel.testplanisphere.dev/ja/index.html");
		Thread.sleep(5000);
		webDriver.manage().window().maximize();
		Thread.sleep(2000);
		webDriver.navigate().refresh();
		Thread.sleep(5000);
        wait = new WebDriverWait(webDriver, 10);
	}

	public void fireFoxOpen() throws InterruptedException {
		System.setProperty("webdriver.gecko.driver", "C:\\WebDrivers\\geckodriver_win64\\geckodriver.exe");

		webDriver = new FirefoxDriver();
		Thread.sleep(3000);
		webDriver.get("https://hotel.testplanisphere.dev/ja/index.html");
		Thread.sleep(5000);
		webDriver.manage().window().maximize();
		Thread.sleep(2000);
		webDriver.navigate().refresh();
		Thread.sleep(5000);
        wait = new WebDriverWait(webDriver, 10);

	}

	public void close() {
		webDriver.quit();
	}

	public void initCheck(String testCaseFilename, String resultFilename) throws IOException, InterruptedException {
    	File file = new File(testCaseFilename);
    	if (!file.exists()) {
    		System.out.print("テストケースファイルが存在しません");
    		return;
    	}
//    	FileReader fileReader = new FileReader(file);
    	InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "SHIFT_JIS");
    	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//    	BufferedReader bufferedReader = new BufferedReader(fileReader);
    	FileWriter resultFile = new  FileWriter(resultFilename, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(resultFile));

        String testType;
		String testCase;
        String[] testConf = {","};
        String commandLocater1;
        String commandLocater2;
        String commandLocater3;
        String testTitle;
        int waitTime;
        String inputText;
        String testResult;

    	while ((testCase = bufferedReader.readLine()) != null) {
    		testConf = testCase.split(",", -1);
            testType = testConf[0];
            switch(testType) {
            case("TESTTODAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testToday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                System.out.println(testResult);
                pw.print(testResult);
                break;
            case("TESTTEXTBOX"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testTextBox(testTitle, commandLocater1, inputText, waitTime);
                System.out.println(testResult);
                pw.print(testResult);
                break;
            case("TESTATTRIBUTE"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                if(inputText.length() == 0) {
                    testResult = testAttributeNull(testTitle, commandLocater1, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);

                }else {
                    testResult = testAttribute(testTitle, commandLocater1, inputText, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }
                break;
            case("TESTCHECKBOX"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testCheckBox(testTitle, commandLocater1, inputText, waitTime);
                System.out.println(testResult);
                pw.print(testResult);
            	break;
            case("EVENTLINK"):
            	commandLocater1 = testConf[2];
                waitTime = Integer.valueOf(testConf[7]);
                eventlink(commandLocater1, waitTime);
                break;

            default:
            }
            if (planisphereError == 1) {
//  		    	break;
            }

    	}
    	pw.close();
    	resultFile.close();
    	inputStreamReader.close();
//    	fileReader.close();

	}

	public void reserveTest(String testCaseFilename, String resultFilename) throws NumberFormatException, IOException, InterruptedException {
    	File file = new File(testCaseFilename);
    	if (!file.exists()) {
    		System.out.print("テストケースファイルが存在しません");
    		return;
    	}
    	FileReader fileReader = new FileReader(file);
    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Shift-JIS"));
    	FileWriter resultFile = new  FileWriter(resultFilename, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(resultFile));

        String testType;
		String testCase;
        String[] testConf = {","};
        String commandLocater1;
        String commandLocater2;
        String commandLocater3;
        String testTitle;
        String setText;
        int waitTime;
        int term;
        String inputText;
        int price;
        String testResult;
        String indicaterValueSpec;

    	while ((testCase = bufferedReader.readLine()) != null) {
    		testConf = testCase.split(",", -1);
            testType = testConf[0];
			switch(testType) {
            case("EVENTWEEKDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                weekday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTHOLIDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                holiday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 1;
                dateFromSet();
                break;
            case("EVENTSUNDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                sunday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTMONDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                monday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTTUESDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                tuesday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTWEDNESDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                wednesday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTTHURSDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                thursday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTFRIDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                friday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTSATURDAY"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                saturday(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
                break;
            case("EVENTILLUGALINPUT"):
                commandLocater3 = testConf[4];
                setText = testConf[5];
                waitTime = Integer.valueOf(testConf[7]);
                illugalInput(commandLocater3, setText, waitTime);
            	break;
            case("EVENTBRANKINPUT"):
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                brankInput(commandLocater3, waitTime);
            	break;
            case("EVENTTERM"):
            	commandLocater1 = testConf[2];
        	    setText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                textSet(commandLocater1, setText, waitTime);
                termValue = Integer.valueOf(setText) - weekEnd;
                termValueWeekEnd = weekEnd;
                term = termValue + termValueWeekEnd;
                termSet(term);
            	break;
            case("EVENTHEADCOUNT"):
            	commandLocater1 = testConf[2];
        	    setText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                textSet(commandLocater1, setText, waitTime);
                headCountValue = Integer.valueOf(setText);
            	break;
            case("EVENTBREAKFAST"):
            	commandLocater1 = testConf[2];
                setText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                eventid(commandLocater1, waitTime);
                breakFastValue = setText;
            	break;
            case("EVENTPLANA"):
            	commandLocater1 = testConf[2];
                setText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                eventid(commandLocater1, waitTime);
                planAvalue = setText;
            	break;
            case("EVENTPLANB"):
            	commandLocater1 = testConf[2];
                setText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                eventid(commandLocater1, waitTime);
                planBvalue = setText;
            	break;
            case("EVENTGUESTNAME"):
            	commandLocater1 = testConf[2];
                setText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                textSet(commandLocater1, setText, waitTime);
                guestValue = setText;
            	break;
            case("TEXTSETID"):
            	commandLocater1 = testConf[2];
        	    setText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                textSet(commandLocater1, setText, waitTime);
            	break;
            case("EVENTID"):
            	commandLocater1 = testConf[2];
                waitTime = Integer.valueOf(testConf[7]);
                eventid(commandLocater1, waitTime);
            	break;
            case("EVENTLINK"):
            	commandLocater1 = testConf[2];
                waitTime = Integer.valueOf(testConf[7]);
                eventlink(commandLocater1, waitTime);
        	    break;
            case("TESTALART"):
            	testTitle = testConf[1];
            	inputText = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
            	testResult = alartTest(inputText, waitTime, testTitle);
                System.out.println(testResult);
                pw.println(testResult);
            	break;
            case("TESTPRICE"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                price = Integer.valueOf(testConf[3]);
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testPrice(testTitle, commandLocater1, price, waitTime);
                System.out.println(testResult);
                pw.println(testResult);
            	break;
            case("TESTTERM"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                term = termValue + termValueWeekEnd;
                testResult = testTerm(testTitle, commandLocater1, commandLocater2, commandLocater3, term, waitTime);
                System.out.println(testResult);
                pw.print(testResult);
                break;
            case("TESTHEADCOUNT"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                if((inputText.length() == 0)&&(headCountValue > 0)) {
                    testResult = testHeadCount(testTitle, commandLocater1, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }else {
                    testResult = testAttribute(testTitle, commandLocater1, inputText, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }
                break;
            case("TESTBREAKFAST"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                if((inputText.length() == 0)&&(breakFastValue.length() != 0)) {
                    testResult = testBreakFast(testTitle, commandLocater1, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }else {
                    testResult = testAttribute(testTitle, commandLocater1, inputText, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }
                break;
            case("TESTPLANA"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                if((inputText.length() == 0)&&(planAvalue.length() != 0)) {
                    testResult = testPlanA(testTitle, commandLocater1, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }else {
                    testResult = testAttribute(testTitle, commandLocater1, inputText, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }
                break;
            case("TESTPLANB"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                if((inputText.length() == 0)&&(planBvalue.length() != 0)) {
                    testResult = testPlanB(testTitle, commandLocater1, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }else {
                    testResult = testAttribute(testTitle, commandLocater1, inputText, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }
                break;
            case("TESTGUESTNAME"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                if((inputText.length() == 0)&&(guestValue.length() != 0)) {
                    testResult = testGuesName(testTitle, commandLocater1, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }else {
                    testResult = testAttribute(testTitle, commandLocater1, inputText, waitTime);
                    System.out.println(testResult);
                    pw.print(testResult);
                }
            	break;

            case("TESTTEXTBOX"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testTextBox(testTitle, commandLocater1, inputText, waitTime);
                System.out.println(testResult);
                pw.print(testResult);
                break;
            case("TESTCHECKBOX"):
            	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                inputText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testCheckBox(testTitle, commandLocater1, inputText, waitTime);
                System.out.println(testResult);
                pw.print(testResult);
            	break;
            case("TESTTEXTID"):
            	commandLocater1 = testConf[2];
                testTitle = testConf[1];
                indicaterValueSpec = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testIndicaterID(commandLocater1, indicaterValueSpec, waitTime, testTitle);
                System.out.println(testResult);
                pw.println(testResult);
                break;
            case("EVENT3MONTHAGO"):
               	testTitle = testConf[1];
                commandLocater1 = testConf[2];
                commandLocater2 = testConf[3];
                commandLocater3 = testConf[4];
                waitTime = Integer.valueOf(testConf[7]);
                month3(testTitle, commandLocater1, commandLocater2, commandLocater3, waitTime);
                weekEnd = 0;
                dateFromSet();
            	break;
            case("REFRESH"):
        		webDriver.navigate().refresh();
        		dt = new Date();
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	    	testDate = sdf.format(dt);
        		Thread.sleep(5000);
            	break;
            default:
            }
            if (planisphereError == 1) {
//  		    	break;
            }

    	}
    	pw.close();
    	resultFile.close();
    	bufferedReader.close();
    	fileReader.close();
	}

	public void registTest(String testCaseFilename, String registUserFilename, String resultFilename) throws IOException, FileNotFoundException, InterruptedException {
    	File file = new File(testCaseFilename);
    	if (!file.exists()) {
    		System.out.print("テストケースファイルが存在しません");
    		return;
    	}
    	File file2 = new File(registUserFilename);
    	if (!file2.exists()) {
    		System.out.print("ユーザー登録ファイルが存在しません");
    		return;
    	}

    	InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "SHIFT_JIS");
    	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    	FileWriter resultFile = new  FileWriter(resultFilename, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(resultFile));

        String testType;
		String testCase;
		String dataList;
        String[] testConf = {","};
        String[] entryListData = {","};
        String commandLocater1;
        String commandLocater2;
        String commandLocater3;
        String testTitle;
        int waitTime;
        int entryNumber;
        String inputText;
        String inputText2;
        String inputText3[] = new String[3];
        String specText;
        String specText2;
        String testResult;

    	while ((testCase = bufferedReader.readLine()) != null) {
    		testConf = testCase.split(",", -1);
            testType = testConf[0];
            switch(testType) {
            case("REGISTDATASET"):
            	InputStreamReader inputStreamReader2 = new InputStreamReader(new FileInputStream(file2), "SHIFT_JIS");
        		BufferedReader entryListBufferReader = new BufferedReader(inputStreamReader2);
            	int dataNumber = Integer.valueOf(testConf[2]);
        		int cnt = 1;
            	while ((dataList = entryListBufferReader.readLine()) != null) {
        			entryListData = dataList.split(",", -1);
                    if(cnt == 1) {
                    	cnt += 1;
                    }else {
            			entryNumber = Integer.valueOf(entryListData[0]);
            			if(entryNumber == dataNumber) {
            				email = entryListData[1];
            				password = entryListData[2];
            				username = entryListData[3];
            				rank = entryListData[4];
            				address = entryListData[5];
            				tel = entryListData[6];
            				gender = entryListData[7];
            				birthday = entryListData[8].split("/", -1);
            				notification = entryListData[9];
            				break;
            			}
            		}
            		cnt += 1;
            	}
            	inputStreamReader2.close();
            	break;
            case("EVENTTEXTINPUT"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(commandLocater1.equals("email"))) {
            		inputText = email;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("password"))) {
            		inputText = password;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("password-confirmation"))) {
            		inputText = password;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("username"))) {
            		inputText = username;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("address"))) {
            		inputText = address;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("tel"))) {
            		inputText = tel;
            	}
            	textSet(commandLocater1, inputText,waitTime);
            	break;
            case("EVENTBIRTHDAYINPUT"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() != 0)) {
            		if(birthday.length != 1) {
                		inputText3[0] = birthday[0];
                		inputText3[1] = birthday[1];
                		inputText3[2] = birthday[2];
                    	birthdaySet(commandLocater1, inputText3, waitTime);
            		}
            	}
            	break;
            case("EVENTCHECK"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(commandLocater1.equals("rank"))) {
            		commandLocater1 = commandLocater1 + "-" + rank;
            	}
            	eventid(commandLocater1, waitTime);

            	break;
            case("EVENTDROPDOWN"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if(inputText.length() == 0) {
            		inputText = gender;
            	}
            	dropdownSelectByText(commandLocater1, inputText, waitTime);

            	break;
            case("EVENTCHECKBOX"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(notification.equals("yes"))) {
            		eventid(commandLocater1, waitTime);
            	}

            	break;
            case("EVENTLINK"):
            	commandLocater1 = testConf[2];
                waitTime = Integer.valueOf(testConf[7]);
                eventlink(commandLocater1, waitTime);
                break;
            case("EVENTCSSSELECTOR"):
            	commandLocater1 = testConf[2];
            	waitTime = Integer.valueOf(testConf[7]);
            	eventCSS(commandLocater1, waitTime);

            	break;
            case("TESTLOGIN"):

            	commandLocater1 = testConf[1];
            	commandLocater2 = testConf[2];
            	commandLocater3 = testConf[3];
            	inputText = testConf[4];
            	inputText2 = testConf[5];
            	specText = testConf[6];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(inputText2.length() == 0)) {
            		inputText = email;
            		inputText2 = password;
            	}
            	testResult = testLogin(commandLocater1, commandLocater2, commandLocater3, inputText, inputText2, specText, waitTime);
            	System.out.println(testResult);
            	pw.println(testResult);
            	break;
            case("REGISTEXIT"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[3];
            	specText = testConf[4];
            	specText2 = testConf[5];
            	waitTime = Integer.valueOf(testConf[7]);
            	testResult = testRegistExit(testTitle, commandLocater1, specText, specText2, waitTime);


            	break;

            default:
            }
            if (planisphereError == 1) {
//  		    	break;
            }

    	}
    	pw.close();
    	resultFile.close();
    	inputStreamReader.close();

	}

	public void reserveErrorTest(String testCaseFilename, String registUserFilename, String resultFilename) throws IOException, FileNotFoundException, InterruptedException {

    	File file = new File(testCaseFilename);
    	if (!file.exists()) {
    		System.out.print("テストケースファイルが存在しません");
    		return;
    	}
    	File file2 = new File(registUserFilename);
    	if (!file2.exists()) {
    		System.out.print("ユーザー登録ファイルが存在しません");
    		return;
    	}

    	InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "SHIFT_JIS");
    	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    	FileWriter resultFile = new  FileWriter(resultFilename, true);
        PrintWriter pw = new PrintWriter(new BufferedWriter(resultFile));

        String testType;
		String testCase;
		String dataList;
        String[] testConf = {","};
        String[] entryListData = {","};
        String commandLocater1;
        String commandLocater2;
        String commandLocater3;
        String testTitle;
        int waitTime;
        int entryNumber;
        String inputText;
        String inputText2;
        String inputText3[] = new String[3];
        String specText;
        String specText2;
        String testResult;

    	while ((testCase = bufferedReader.readLine()) != null) {
    		testConf = testCase.split(",", -1);
            testType = testConf[0];
            switch(testType) {
            case("REGISTDATASET"):
            	InputStreamReader inputStreamReader2 = new InputStreamReader(new FileInputStream(file2), "SHIFT_JIS");
        		BufferedReader entryListBufferReader = new BufferedReader(inputStreamReader2);
            	int dataNumber = Integer.valueOf(testConf[2]);
        		int cnt = 1;
            	while ((dataList = entryListBufferReader.readLine()) != null) {
        			entryListData = dataList.split(",", -1);
                    if(cnt == 1) {
                    	cnt += 1;
                    }else {
            			entryNumber = Integer.valueOf(entryListData[0]);
            			if(entryNumber == dataNumber) {
            				email = entryListData[1];
            				password = entryListData[2];
            				password2 = entryListData[3];
            				username = entryListData[4];
            				rank = entryListData[5];
            				address = entryListData[6];
            				tel = entryListData[7];
            				gender = entryListData[8];
            				birthday = entryListData[9].split("/", -1);
            				notification = entryListData[10];
            				break;
            			}
            		}
            		cnt += 1;
            	}
            	inputStreamReader2.close();
            	break;
            case("EVENTTEXTINPUT"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(commandLocater1.equals("email"))) {
            		inputText = email;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("password"))) {
            		inputText = password;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("password-confirmation"))) {
            		inputText = password2;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("username"))) {
            		inputText = username;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("address"))) {
            		inputText = address;
            	}
            	if((inputText.length() == 0)&&(commandLocater1.equals("tel"))) {
            		inputText = tel;
            	}
            	textSet(commandLocater1, inputText,waitTime);
            	break;
            case("EVENTBIRTHDAYINPUT"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() != 0)) {
            		if(birthday.length != 1) {
                		inputText3[0] = birthday[0];
                		inputText3[1] = birthday[1];
                		inputText3[2] = birthday[2];
                    	birthdaySet(commandLocater1, inputText3, waitTime);
            		}
            	}
            	break;
            case("EVENTCHECK"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(commandLocater1.equals("rank"))) {
            		commandLocater1 = commandLocater1 + "-" + rank;
            	}
            	eventid(commandLocater1, waitTime);

            	break;
            case("EVENTDROPDOWN"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if(inputText.length() == 0) {
            		inputText = gender;
            	}
            	dropdownSelectByText(commandLocater1, inputText, waitTime);

            	break;
            case("EVENTCHECKBOX"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[2];
            	inputText = testConf[3];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(notification.equals("yes"))) {
            		eventid(commandLocater1, waitTime);
            	}

            	break;
            case("EVENTLINK"):
            	commandLocater1 = testConf[2];
                waitTime = Integer.valueOf(testConf[7]);
                eventlink(commandLocater1, waitTime);
                break;
            case("EVENTCSSSELECTOR"):
            	commandLocater1 = testConf[2];
            	waitTime = Integer.valueOf(testConf[7]);
            	eventCSS(commandLocater1, waitTime);

            	break;
            case("TESTLOGIN"):

            	commandLocater1 = testConf[1];
            	commandLocater2 = testConf[2];
            	commandLocater3 = testConf[3];
            	inputText = testConf[4];
            	inputText2 = testConf[5];
            	specText = testConf[6];
            	waitTime = Integer.valueOf(testConf[7]);
            	if((inputText.length() == 0)&&(inputText2.length() == 0)) {
            		inputText = email;
            		inputText2 = password;
            	}
            	testResult = testLogin(commandLocater1, commandLocater2, commandLocater3, inputText, inputText2, specText, waitTime);
            	System.out.println(testResult);
            	pw.println(testResult);
            	break;
            case("REGISTEXIT"):
            	testTitle = testConf[1];
            	commandLocater1 = testConf[3];
            	specText = testConf[4];
            	specText2 = testConf[5];
            	waitTime = Integer.valueOf(testConf[7]);
            	testResult = testRegistExit(testTitle, commandLocater1, specText, specText2, waitTime);
            case("TESTTEXTX"):
                testTitle = testConf[1];
            	commandLocater1 = testConf[2];
                specText = testConf[3];
                waitTime = Integer.valueOf(testConf[7]);
                testResult = testTextX(commandLocater1, specText, waitTime, testTitle);
                System.out.println(testResult);
                pw.println(testResult);
            	break;

            default:
            }
            if (planisphereError == 1) {
//  		    	break;
            }

    	}
    	pw.close();
    	resultFile.close();
    	inputStreamReader.close();
	}







	private void dateFromSet() {
		String reserveFrom;
		int reserveYear;
		int reserveMonth;
		int reserveDay;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		reserveFrom = sdf.format(dt);
		reserveYear = Integer.valueOf(reserveFrom.substring(0, 4));
		reserveMonth = Integer.valueOf(reserveFrom.substring(5, 7));
		reserveDay = Integer.valueOf(reserveFrom.substring(8, 10));
		dateFrom = String.valueOf(reserveYear) + "年" + String.valueOf(reserveMonth) + "月" + String.valueOf(reserveDay) + "日";

	}

	private void termSet(int term) {
		Date reserveEnd;
		String reserveTo;
		int reserveToYear;
		int reserveToMonth;
		int reserveToDay;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, term);
		reserveEnd = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		reserveTo = sdf.format(reserveEnd);
		reserveToYear = Integer.valueOf(reserveTo.substring(0, 4));
		reserveToMonth = Integer.valueOf(reserveTo.substring(5, 7));
		reserveToDay = Integer.valueOf(reserveTo.substring(8, 10));
		dateTo = String.valueOf(reserveToYear) + "年" + String.valueOf(reserveToMonth) + "月" + String.valueOf(reserveToDay) + "日";


	}

	private void weekday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.SATURDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);

	}

	private void holiday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
//			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.MONDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.TUESDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		case Calendar.WEDNESDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		case Calendar.THURSDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		case Calendar.FRIDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.SATURDAY:
//			calendar.add(Calendar.DATE, 2);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void sunday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
//			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.MONDAY:
			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.TUESDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.WEDNESDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		case Calendar.THURSDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		case Calendar.FRIDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		case Calendar.SATURDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void monday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.MONDAY:
//			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.TUESDAY:
			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.WEDNESDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.THURSDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		case Calendar.FRIDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		case Calendar.SATURDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void tuesday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		case Calendar.MONDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.TUESDAY:
//			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.WEDNESDAY:
			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.THURSDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.FRIDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		case Calendar.SATURDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void wednesday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		case Calendar.MONDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		case Calendar.TUESDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.WEDNESDAY:
//			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.THURSDAY:
			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.FRIDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.SATURDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void thursday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		case Calendar.MONDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		case Calendar.TUESDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		case Calendar.WEDNESDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.THURSDAY:
//			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.FRIDAY:
			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.SATURDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void friday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.MONDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		case Calendar.TUESDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		case Calendar.WEDNESDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		case Calendar.THURSDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.FRIDAY:
//			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.SATURDAY:
			calendar.add(Calendar.DATE, 6);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void saturday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.DATE, 1);
		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			calendar.add(Calendar.DATE, 6);
			break;
		case Calendar.MONDAY:
			calendar.add(Calendar.DATE, 5);
			break;
		case Calendar.TUESDAY:
			calendar.add(Calendar.DATE, 4);
			break;
		case Calendar.WEDNESDAY:
			calendar.add(Calendar.DATE, 3);
			break;
		case Calendar.THURSDAY:
			calendar.add(Calendar.DATE, 2);
			break;
		case Calendar.FRIDAY:
			calendar.add(Calendar.DATE, 1);
			break;
		case Calendar.SATURDAY:
//			calendar.add(Calendar.DATE, 1);
			break;
		default:
		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void illugalInput(String commandLocater3, String setText, int waitTime) throws InterruptedException {
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
        inputBoxDay.sendKeys(setText);
        Thread.sleep(waitTime);
	}

	private void brankInput(String commandLocater3, int waitTime) throws InterruptedException {
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
	}

	private void month3(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		Date reserveDate;
		String testReserveDate;
		String reserveYear;
		String reserveMonth;
		String reserveDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.add(Calendar.MONTH, 4);
//		switch(calendar.get(Calendar.DAY_OF_WEEK)) {
//		case Calendar.SUNDAY:
//			calendar.add(Calendar.DATE, 1);
//			break;
//		case Calendar.SATURDAY:
//			calendar.add(Calendar.DATE, 2);
//			break;
//		default:
//		}

		reserveDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		testReserveDate = sdf.format(reserveDate);
		dt = reserveDate;

		reserveYear = testReserveDate.substring(0, 4);
        WebElement inputBoxYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxYear));
        inputBoxYear.clear();
        Thread.sleep(waitTime);
//        reserveYear = reserveYear + "\n";
        inputBoxYear.sendKeys(reserveYear);
        Thread.sleep(waitTime);

		reserveMonth = testReserveDate.substring(5, 7);
        WebElement inputBoxMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxMonth));
        inputBoxMonth.clear();
        Thread.sleep(waitTime);
//        reserveMonth = reserveMonth + "\n";
        inputBoxMonth.sendKeys(reserveMonth);
        Thread.sleep(waitTime);

		reserveDay = testReserveDate.substring(8, 10);
        WebElement inputBoxDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.elementToBeClickable(inputBoxDay));
        inputBoxDay.clear();
        Thread.sleep(waitTime);
//        reserveDay = reserveDay + "\n";
        inputBoxDay.sendKeys(reserveDay);
        Thread.sleep(waitTime);
	}

	private void eventlink(String commandLocater1, int waitTime) throws InterruptedException {
		WebElement elementPos = webDriver.findElement(By.partialLinkText(commandLocater1));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(elementPos);
		actions.perform();
		Thread.sleep(waitTime);

		WebElement element = webDriver.findElement(By.partialLinkText(commandLocater1));
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
        Thread.sleep(waitTime);
	}

	private void eventid(String commandLocater1, int waitTime) throws InterruptedException {
		WebElement elementPos = webDriver.findElement(By.id(commandLocater1));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(elementPos);
		actions.perform();
		Thread.sleep(waitTime);

		WebElement element = webDriver.findElement(By.id(commandLocater1));
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
        Thread.sleep(waitTime);
	}

	private void eventCSS(String commandLocater1, int waitTime) throws InterruptedException {
		WebElement elementPos = webDriver.findElement(By.cssSelector(commandLocater1));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(elementPos);
		actions.perform();
		Thread.sleep(waitTime);

		WebElement element = webDriver.findElement(By.cssSelector(commandLocater1));
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
        Thread.sleep(waitTime);
	}

	private void textSet(String commandLocater1, String setText, int waitTime) throws InterruptedException {
        WebElement inputBox = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBox));
        inputBox.clear();
        Thread.sleep(waitTime);
        inputBox.sendKeys(setText);
        Thread.sleep(waitTime);
	}

	private void birthdaySet(String commandLocater1, String[] inputText3, int waitTime) throws InterruptedException {
		WebElement inputBox = webDriver.findElement(By.id(commandLocater1));
		wait.until(ExpectedConditions.elementToBeClickable(inputBox));
		inputBox.clear();
		Thread.sleep(waitTime);
		inputBox.sendKeys(inputText3[0]);
		Thread.sleep(waitTime);
		inputBox.sendKeys(Keys.RIGHT);
		Thread.sleep(waitTime);
		inputBox.sendKeys(inputText3[1]);
		Thread.sleep(waitTime);
		inputBox.sendKeys(Keys.RIGHT);
		Thread.sleep(waitTime);
		inputBox.sendKeys(inputText3[2]);
		Thread.sleep(waitTime);
	}

	private void dropdownSelectByText(String commandLocater1, String inputText, int waitTime) throws InterruptedException {
		WebElement element = webDriver.findElement(By.id(commandLocater1));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(element);
		actions.perform();
		Thread.sleep(1000);
        Select output_Select = new Select(webDriver.findElement(By.id(commandLocater1)));
        output_Select.selectByVisibleText(inputText);
        Thread.sleep(waitTime);
	}

	private String testToday(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3,
			int waitTime) throws InterruptedException {
		String resultText;
		int todayYear = Integer.valueOf(testDate.substring(0, 4));
		int todayMonth = Integer.valueOf(testDate.substring(5, 7));
		int todayDay = Integer.valueOf(testDate.substring(8, 10));

        WebElement elementYear = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(elementYear));
        int yearText = Integer.valueOf(elementYear.getAttribute("value"));
 		if (yearText == todayYear) {
 			resultText = testDate + ", " + testTitle + ", spec: " + todayYear + ", result: " + yearText + " :<TestYear success>" + "\n";
 		} else {
 			resultText = testDate + ", " + testTitle + ", spec: " + todayYear + ", result: " + yearText + " :<TestYear fail>" + "\n";
 			planisphereError = 1;
 		}

 		WebElement elementMonth = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.visibilityOf(elementMonth));
        int monthText = Integer.valueOf(elementMonth.getAttribute("value"));
 		if (monthText == todayMonth) {
 			resultText = resultText + testDate + ", " + testTitle + ", spec: " + todayMonth + ", result: " + monthText + " :<TestMonth success>" + "\n";
 		} else {
 			resultText = resultText + testDate + ", " + testTitle + ", spec: " + todayMonth + ", result: " + monthText + " :<TestMonth fail>" + "\n";
 			planisphereError = 1;
 		}

 		WebElement elementDay = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.visibilityOf(elementDay));
        int dayText = Integer.valueOf(elementDay.getAttribute("value"));
 		if (dayText == todayDay) {
 			resultText = resultText + testDate + ", " + testTitle + ", spec: " + todayDay + ", result: " + dayText + " :<TestDay success>" + "\n";
 		} else {
 			resultText = resultText + testDate + ", " + testTitle + ", spec: " + todayDay + ", result: " + dayText + " :<TestDay fail>" + "\n";
 			planisphereError = 1;
 		}

        Thread.sleep(waitTime);
		return resultText;
	}

	private String testAttribute(String testTitle, String commandLocater1, String inputText, int waitTime) throws InterruptedException {
		String resultText;
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        String valueText = element.getAttribute("value");
 		Pattern pa = Pattern.compile(inputText);
 		Matcher ma = pa.matcher(valueText);
 		if (ma.find()) {
 			resultText = testDate + ", " + testTitle + ", spec: " + inputText + ", result: " + valueText + " :<Test success>";
 		} else {
 			resultText = testDate + ", " + testTitle + ", spec: " + inputText + ", result: " + valueText + " :<Test fail>";
 			planisphereError = 1;
 		}
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testAttributeNull(String testTitle, String commandLocater1, int waitTime) throws InterruptedException {
		String resultText;
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        String valueText = element.getAttribute("value");
        if(valueText.length() == 0) {
        	resultText = testDate + ", " + testTitle + ", :<Test success>";
        }else {
        	resultText = testDate + ", " + testTitle + ", :<Test fail>";
 			planisphereError = 1;
        }
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testCheckBox(String testTitle, String commandLocater1, String inputText, int waitTime) {
		String resultText = "";
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        if(inputText.equals("true")){
        	if(element.isSelected()) {
        		resultText = testDate + ", " + testTitle + ", :<ChecBox is Selected. Test success>";
        	}else {
        		resultText = testDate + ", " + testTitle + ", :<ChecBox is not Selected. Test fail>";
     			planisphereError = 1;
        	}
        }else if(inputText.equals("false")){
        	if(element.isSelected()) {
        		resultText = testDate + ", " + testTitle + ", :<ChecBox is Selected. Test fail>";
     			planisphereError = 1;
        	}else {
        		resultText = testDate + ", " + testTitle + ", :<ChecBox is not Selected. Test success>";
        	}
        }
		return resultText;
	}

	private String testTextX(String commandLocater1, String specText, int waitTime, String testTitle) throws InterruptedException {
		String regex;
		String visibleText;
		String resultText;
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		WebElement elementPos = webDriver.findElement(By.xpath(commandLocater1));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(elementPos);
		actions.perform();
		Thread.sleep(waitTime);

        WebElement element = webDriver.findElement(By.xpath(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        visibleText = element.getText();
        resultText = testTitle + ", " + "TestResult: " + visibleText + ", Spec: " + specText;
        ;
 		Pattern p2 = Pattern.compile(visibleText);
 		Matcher m2 = p2.matcher(specText);
 		if (m2.find()) {
 			resultText = sdf.format(dt) + ", " + resultText + " :<Visible Text success>";
 		} else {
 			resultText = sdf.format(dt) + ", " + resultText + " :<Visible Text failed>";
 			planisphereError = 1;
 		}
        Thread.sleep(waitTime);
		return resultText;
	}

	private String alartTest(String inputText, int waitTime, String testTitle) throws InterruptedException {
		String testResult;
		String alertText;
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert = webDriver.switchTo().alert();
		alertText = alert.getText();

 		Pattern p = Pattern.compile(inputText);
 		Matcher m = p.matcher(alertText);
 		if (m.find()) {
 			testResult = testDate + ", " + testTitle + ", Spec: " + inputText + ", Result:" + alertText + " :<Alart Test success>";
 		} else {
 			testResult = testDate + ", " + testTitle + ", Spec: " + inputText + ", Result:" + alertText + " :<Alart Test fail>";
 			planisphereError = 1;
 		}
 		Thread.sleep(waitTime);
        alert.accept();
        Thread.sleep(waitTime);
		return testResult;
	}

	private String testPrice(String testTitle, String commandLocater1, int price, int waitTime) {
		int priceData;
//		int up25Price = 8750;
//		int normalPrice = 7000;
		int calcPrice;
		String testResult;

        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        priceData = Integer.valueOf(element.getText());

//		if(breakFastValue.equals("on")) {
//			up25Price = up25Price + 1000;
//			normalPrice = normalPrice + 1000;
//		}
//		if(planAvalue == null) {
//
//		}else {
//			if(planAvalue.equals("on")) {
//				up25Price = up25Price + 1000;
//				normalPrice = normalPrice + 1000;
//			}
//
//		}
//		if(planBvalue == null) {
//
//		}else {
//			if(planBvalue.equals("on")) {
//				up25Price = up25Price + 1000;
//				normalPrice = normalPrice + 1000;
//			}
//
//		}
//		calcPrice = (termValue * normalPrice * headCountValue) + (termValueWeekEnd * up25Price * headCountValue);
        calcPrice = price;
        if(priceData == calcPrice) {
			testResult = testDate + ", " + testTitle + ", Spec: " + calcPrice + ", Result: " + priceData + ", <PriceTest success>";
		}else {
			testResult = testDate + ", " + testTitle + ", Spec: " + calcPrice + ", Result: " + priceData + ", <PriceTest fail>";
			planisphereError = 1;
		}
		return testResult;
	}

	private String testTerm(String testTitle, String commandLocater1, String commandLocater2, String commandLocater3, int term,int waitTime) throws InterruptedException {
		String resultText;

        WebElement elementReserveFrom = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(elementReserveFrom));
        String reserveFrom = elementReserveFrom.getText();

        WebElement elementReserveTo = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.visibilityOf(elementReserveTo));
        String reserveTo = elementReserveTo.getText();

        WebElement elementTerm = webDriver.findElement(By.id(commandLocater3));
        wait.until(ExpectedConditions.visibilityOf(elementTerm));
        int termData = Integer.valueOf(elementTerm.getText());

        if(reserveFrom.equals(dateFrom)) {
        	resultText = testDate + ", " + testTitle + ", ReserveFrom Spec: " + dateFrom + ", ReserveFrom Result: "+ reserveFrom + ", :<ReserveFromTest success>" + "\n";
        }else {
        	resultText = testDate + ", " + testTitle + ", ReserveFrom Spec: " + dateFrom + ", ReserveFrom Result: "+ reserveFrom + ", :<ReserveFromTest fail>" + "\n";
 			planisphereError = 1;
        }

        if(reserveTo.equals(dateTo)) {
        	resultText = resultText + testDate + ", " + testTitle + ", ReserveTo Spec: " + dateTo + ", ReserveTo Result: "+ reserveTo + ", :<ReserveToTest success>" + "\n";
        }else {
        	resultText = resultText + testDate + ", " + testTitle + ", ReserveTo Spec: " + dateTo + ", ReserveTo Result: "+ reserveTo + ", :<ReserveToTest fail>" + "\n";
 			planisphereError = 1;
        }

        if(termData == term) {
        	resultText = resultText + testDate + ", " + testTitle + ", Spec: " + term + ", Result: "+ termData + ", :<ReserveTermTest success>" + "\n";
        }else {
        	resultText = resultText + testDate + ", " + testTitle + ", Spec: " + term + ", Result: "+ termData + ", :<ReserveTermTest fail>" + "\n";
 			planisphereError = 1;
        }
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testHeadCount(String testTitle, String commandLocater1, int waitTime) throws InterruptedException {
		String resultText;
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        int headCountData = Integer.valueOf(element.getText());
        if(headCountData == headCountValue) {
        	resultText = testDate + ", " + testTitle + ", Spec: " + headCountValue + ", Result: "+ headCountData + ", :<ReserveHeadCountTest success>" + "\n";
        }else {
        	resultText = testDate + ", " + testTitle + ", Spec: " + headCountValue + ", Result: "+ headCountData + ", :<ReserveHeadCountTest fail>" + "\n";
 			planisphereError = 1;
        }
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testBreakFast(String testTitle, String commandLocater1, int waitTime) throws InterruptedException {
		String resultText;
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        String breakFastData = element.getText();
        if(breakFastData.equals("あり")) {
        	breakFastData = "on";
        }else {
        	breakFastData = "off";
        }
        if(breakFastData.equals(breakFastValue)) {
        	resultText = testDate + ", " + testTitle + ", Spec: " + breakFastValue + ", Result: "+ breakFastData + ", :<ReserveBreakFastTest success>" + "\n";
        }else {
        	resultText = testDate + ", " + testTitle + ", Spec: " + breakFastValue + ", Result: "+ breakFastData + ", :<ReserveBreakFastTest fail>" + "\n";
 			planisphereError = 1;
        }
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testPlanA(String testTitle, String commandLocater1, int waitTime) throws InterruptedException {
		String resultText;
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        String planAdata = element.getText().trim();
        if(planAdata.equals("昼からチェックインプラン")) {
        	planAdata = "on";
        }else {
        	planAdata = "off";
        }
        if(planAdata.equals(planAvalue)) {
        	resultText = testDate + ", " + testTitle + ", Spec: " + planAvalue + ", Result: "+ planAdata + ", :<ReservePlanA_Test success>" + "\n";
        }else {
        	resultText = testDate + ", " + testTitle + ", Spec: " + planAvalue + ", Result: "+ planAdata + ", :<ReservePlanA_Test fail>" + "\n";
 			planisphereError = 1;
        }
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testPlanB(String testTitle, String commandLocater1, int waitTime) throws InterruptedException {
		String resultText;
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        String planBdata = element.getText().trim();
        if(planBdata.equals("お得な観光プラン")) {
        	planBdata = "on";
        }else {
        	planBdata = "off";
        }
        if(planBdata.equals(planBvalue)) {
        	resultText = testDate + ", " + testTitle + ", Spec: " + planBvalue + ", Result: "+ planBdata + ", :<ReservePlanB_Test success>" + "\n";
        }else {
        	resultText = testDate + ", " + testTitle + ", Spec: " + planBvalue + ", Result: "+ planBdata + ", :<ReservePlanB_Test fail>" + "\n";
 			planisphereError = 1;
        }
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testGuesName(String testTitle, String commandLocater1, int waitTime) throws InterruptedException {
		String resultText;
        WebElement element = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(element));
        String guestData = element.getText().trim();
        if(guestData.equals(guestValue)) {
        	resultText = testDate + ", " + testTitle + ", Spec: " + guestValue + ", Result: "+ guestData + ", :<ReserveGuestNameTest success>" + "\n";
        }else {
        	resultText = testDate + ", " + testTitle + ", Spec: " + guestValue + ", Result: "+ guestData + ", :<ReserveGuestNameTest fail>" + "\n";
 			planisphereError = 1;
        }
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testLogin(String commandLocater1, String commandLocater2, String commandLocater3, String loginId, String loginPass, String specText,
			int waitTime) throws InterruptedException {
//    	wait = new WebDriverWait(webDriver, 10);
		String currentURL;

		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String testDate = sdf.format(dt);

		String testResult;
	    String id_pass = loginId + " / " + loginPass;

 		WebElement elementPos = webDriver.findElement(By.id(commandLocater1));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(elementPos);
		actions.perform();
		Thread.sleep(500);
        WebElement inputBox = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.elementToBeClickable(inputBox));
        inputBox.clear();
        Thread.sleep(500);
//        loginId = loginId + "\n";
        inputBox.sendKeys(loginId);
        Thread.sleep(500);

		WebElement elementPos2 = webDriver.findElement(By.id(commandLocater2));
		Actions actions2 = new Actions(webDriver);
		actions2.moveToElement(elementPos2);
		actions2.perform();
		Thread.sleep(500);
        WebElement inputBox2 = webDriver.findElement(By.id(commandLocater2));
        wait.until(ExpectedConditions.elementToBeClickable(inputBox2));
        inputBox2.clear();
        Thread.sleep(500);
//        loginPass = loginPass + "\n";
        inputBox2.sendKeys(loginPass);
        Thread.sleep(500);

		WebElement elementPos3 = webDriver.findElement(By.id(commandLocater3));
		Actions actions3 = new Actions(webDriver);
		actions3.moveToElement(elementPos3);
		actions3.perform();
		Thread.sleep(500);
		WebElement submitButton = webDriver.findElement(By.id(commandLocater3));
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));
		submitButton.click();

		Thread.sleep(waitTime);

		currentURL = webDriver.getCurrentUrl();
		Pattern pURL = Pattern.compile(specText);
		Matcher mURL = pURL.matcher(currentURL);
		if(mURL.find()) {
			testResult =  testDate + " : " + id_pass + " : <Login success >";
		}else {
			testResult = testDate + " : " + id_pass + " : <Login failed >";
		}

        Thread.sleep(waitTime);
		return testResult;
	}

	private String testRegistExit(String testTitle, String commandLocater1, String specText, String specText2,
			int waitTime) throws InterruptedException {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String testDate = sdf.format(dt);

		String testResult;
		String alertText;
		String alertText2;

		acceptNextAlert = true;

		WebElement exitButton = webDriver.findElement(By.cssSelector(commandLocater1));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(exitButton);
		actions.perform();
		Thread.sleep(500);
		exitButton = webDriver.findElement(By.cssSelector(commandLocater1));
		wait.until(ExpectedConditions.elementToBeClickable(exitButton));
		exitButton.click();
		Thread.sleep(waitTime);
		alertText = closeAlertAndGetItsText();
		Thread.sleep(waitTime);
		alertText2 = closeAlertAndGetItsText();

		if((alertText.equals(specText) == true)&&(alertText2.equals(specText2) == true)) {
			testResult =  testDate + " : " + testTitle + " : <Regist Delete success >";
		}else {
			testResult =  testDate + " : " + testTitle + " : <Regist Delete failed >";
		}
		return testResult;
	}

	private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = webDriver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	}

	private String testTextBox(String testTitle, String commandLocater1, String inputText, int waitTime) throws InterruptedException {
		String resultText;
        WebElement elementBox = webDriver.findElement(By.id(commandLocater1));
        wait.until(ExpectedConditions.visibilityOf(elementBox));
        String boxText = elementBox.getAttribute("value");
 		Pattern pBox = Pattern.compile(inputText);
 		Matcher mBox = pBox.matcher(boxText);
 		if (mBox.find()) {
 			resultText = testTitle + ", spec: " + inputText + ", result: " + boxText + " :<TestTextBox success>" + "\n";
 		} else {
 			resultText = testTitle + ", spec: " + inputText + ", result: " + boxText + " :<TestTextBox fail>" + "\n";
 			planisphereError = 1;
 		}
        Thread.sleep(waitTime);
		return resultText;
	}

	private String testIndicaterID(String commandLocater, String indicaterValueSpec, int waitTime, String testTitle) {
		String regex;
		String indicaterText;
		String resultText;
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        WebElement element = webDriver.findElement(By.id(commandLocater));
        wait.until(ExpectedConditions.visibilityOf(element));
        indicaterText = element.getText();
        resultText = sdf.format(dt) + ", " + testTitle + ", " + "TestResult: " + indicaterText + ", Spec: " + indicaterValueSpec;
        regex = indicaterValueSpec;
 		Pattern p2 = Pattern.compile(regex);
 		Matcher m2 = p2.matcher(indicaterText);
 		if (m2.find()) {
 			resultText = resultText + " :<Error Message success>";
 		} else {
 			planisphereError = 1;
 			resultText = resultText + " :<Error Message failed>";
 		}
		return resultText;
	}




}

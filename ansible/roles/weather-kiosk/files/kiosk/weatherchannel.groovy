#!/usr/bin/env groovy

@Grab(group='org.seleniumhq.selenium', module='selenium-java', version='2.47.1')
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.chrome.*;

import groovy.ui.Console;

scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent

waitTime = 5
location = 'USNY0794:1:US'

devmode = args.size() > 0 && args[0] == "dev"

println "Starting browser session..."

System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
def home = System.getenv()['HOME']
def options = new ChromeOptions();
devmode || options.addArguments("--kiosk");
devmode && options.addArguments("--disable-gpu");
devmode || options.addArguments("--force-device-scale-factor=1.5");
options.addArguments("user-data-dir=${home}/.config/chromium/");
driver = new ChromeDriver(options);

//profile = new FirefoxProfile();
//profile.setPreference('layout.css.devPixelsPerPx', 2);
//driver = new FirefoxDriver(profile);
js = (JavascriptExecutor)driver;
actions = new Actions(driver);


def setWindowSize() {
	def window = driver.manage().window()
	window.setPosition(new Point(0, 0));
	window.setSize(new Dimension(1920, 1080));
}

def inject_jquery() {
	js.executeScript(new File(scriptDir, "jquery-2.1.4.min.js").getText('UTF-8'));
}

def hideSocialButtons() {
	js.executeScript('$(".social-wrapper").css("display", "none");');
}

def scrollTo(sel) {
	def e = driver.findElement(sel);
	def r = js.executeScript('$("html, body").animate({scrollTop: $(arguments[0]).offset().top-15}, "slow");', e);
	return e;
}

def click(container, sel) {
	def e = container.findElement(sel);
	js.executeScript('$(arguments[0]).click();', e);
	hideSocialButtons();
}


def farming_forecast() {
	println "opening farming forecast..."
	driver.get("http://www.weather.com/forecast/agriculture/l/${location}");
	inject_jquery();

	1.upto(5) {
		js.executeScript('$("html, body").prop("scrollTop", 99999);');
		sleep(200);
	}

	1.upto(1) {
		hideSocialButtons();
		def e
		e = scrollTo(By.cssSelector("div.pane-content h2[data-translate='forecast_graphs.TITLE']"));
		e = e.findElement(By.xpath("../../../.."));
		sleep(100);
		click(e, By.cssSelector('li span[data-ng-bind-template="Today"]'));
		sleep(waitTime * 2000);
		click(e, By.cssSelector('li span[data-ng-bind-template="Hourly"]'));
		sleep(waitTime * 2000);
		click(e, By.cssSelector('li span[data-ng-bind-template="10 Day"]'));
		sleep(waitTime * 2000);

		e = scrollTo(By.cssSelector("div.pane-content h2[data-translate='localMap.MAPS']"));
		e = e.findElement(By.xpath("../../../.."));
		sleep(100);
		//click(e, By.xpath('//li[text()="Radar"]'));
		//sleep(waitTime * 2000);
		click(e, By.xpath('//li[text()="Radar/Clouds"]'));
		sleep(waitTime * 2000);
		click(e, By.xpath('//li[text()="24-Hr Precip"]'));
		sleep(waitTime * 2000);
	}
}


def animated_radar() {
	println "opening radar..."
	driver.get("http://www.weather.com/weather/radar/interactive/l/${location}?animation=true&interactiveMapLayer=radar");
	inject_jquery();
	def e

	// select radar+rain	
	//e = driver.findElement(By.cssSelector('div[data-layer-id="0040,0039"]'));
	//e.click()
	//sleep(50);
	js.executeScript('''
		$("div.main-content.content-row-1").css({"padding-right": 0, "margin-right": 0});
		$("div.wx-imap.wx-imap-tools").remove();
		//$("div.right-rail").remove(); $(window).trigger("resize");
		$(".collapse-rail-trigger").hide();
		$(".right-rail").toggleClass("toggle"); window.dispatchEvent(new Event("resize"));
	''');

	println "Zooming in"
	e = driver.findElement(By.cssSelector('div.wx-zoom-control div.wx-zoom-plus'));
	1.upto(2) {
		e.click();
		sleep(200);
	}
	sleep(200);
	
	// start animation
	/* past starts playing by default
	println "playing past radar"
	e = driver.findElement(By.cssSelector('div.wx-past.wx-play'));
	e.click();
	*/
	sleep(waitTime * 5000)

	println "playing radar forecast"
	e = driver.findElement(By.cssSelector('div.wx-future.wx-play'));
	e.click();
	sleep(waitTime * 5000);
	
	//System.console().readLine "Press return to exit..."
}


try {
	devmode && setWindowSize();
	1.upto(10) {
		farming_forecast();
		animated_radar();
	}
} catch (Exception e) {
	println ""
	println "Exception " + e
	System.console().readLine "All done, press return to exit..."
} finally {
	driver.quit();
}

package planisphere;

import java.io.IOException;

public class MainRegistPage {
	public static void main(String[] args) throws InterruptedException, IOException {
		String testCaseFilename;
		String registUserFilename;
		String resultFilename;

		WebTestPlanisphere registPage = new WebTestPlanisphere();
		registPage.open();

//		testCaseFilename = "C:\\Planisphere\\TestConfig\\RegistPage\\initCheckRegistPage.csv";
//		resultFilename = "C:\\PlanisphereTestResults\\RegistPage\\initCheckRegistPageResult.csv";
//		registPage.initCheck(testCaseFilename, resultFilename);
//		if(registPage.planisphereError == 1) {
//			System.exit(1);
//		}

		testCaseFilename = "C:\\Planisphere\\TestConfig\\RegistPage\\registTest.csv";
		registUserFilename = "C:\\Planisphere\\TestConfig\\RegistPage\\registData.csv";
		resultFilename = "C:\\PlanisphereTestResults\\RegistPage\\registTestResult.csv";
		registPage.registTest(testCaseFilename, registUserFilename, resultFilename);
		if(registPage.planisphereError == 1) {
			System.exit(1);
		}

//		testCaseFilename = "C:\\Planisphere\\TestConfig\\RegistPage\\registErrorTest.csv";
//		resultFilename = "C:\\PlanisphereTestResults\\RegistPage\\registErrorResult.csv";
//		registPage.reserveTest(testCaseFilename, resultFilename);
//		if(registPage.planisphereError == 1) {
//			System.exit(1);
//		}

		registPage.close();
	}
}

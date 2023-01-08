/*package com.iexceed.uiframework.utilites;

import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;

public class EmailReporter {

    private String outputDirectory;
    private String jsonFile;


    public EmailReporter(String outputDirectory, String jsonFile) {
        super();
        this.outputDirectory = outputDirectory;
        this.jsonFile = jsonFile;
    }


    public  void generateEmailReporter() {
        CucumberDetailedResults results = new CucumberDetailedResults();
        results.setOutputDirectory(outputDirectory);
        results.setOutputName("cucumber-results");
        results.setSourceFile(jsonFile);
        try {
            results.execute(true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	//public static void main(String[] args)
	{
		EmailReporter email = new EmailReporter("target/","target/cucumber.json");
		email.generateEmailReporter();

	}//

}*/

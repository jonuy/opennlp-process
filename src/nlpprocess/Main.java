package nlpprocess;

import nlpwrapper.OpenNlpWrapper;

public class Main {

	public static void main(String[] args) {
		OpenNlpWrapper nlp = new OpenNlpWrapper();
		try {
			String paragraph = "My name is Bob! Hi, there. How are you?";
			String[] sentences = nlp.detectSentence(paragraph, "models/en-sent.bin");
			for (int i=0; i<sentences.length; i++) {
				System.out.println("Sentence["+i+"] = "+sentences[i]);
			}
			
			String[] tokens = nlp.tokenize(paragraph, "models/en-token.bin");
			for (int i=0; i<tokens.length; i++) {
				System.out.println("tokens["+i+"] = "+tokens[i]);
			}
			
			String[] names = nlp.findNames(paragraph, "models/en-ner-person.bin");
			for (int i=0; i<names.length; i++) {
				System.out.println("names["+i+"] = "+names[i]);
			}
			
			String[] samples = nlp.tagPOS(paragraph, "models/en-pos-maxent.bin");
			for (int i=0; i<samples.length; i++) {
				System.out.println("samples["+i+"] = "+samples[i]);
			}
		}
		catch (Exception e) {
			
		}
	}

}

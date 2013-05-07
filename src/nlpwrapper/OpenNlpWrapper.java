package nlpwrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

public class OpenNlpWrapper {

	public String[] detectSentence(String input, String modelFile) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(modelFile);
		SentenceModel model = new SentenceModel(is);
		is.close();
		
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		String sentences[] = sdetector.sentDetect(input);
		
		return sentences;
	}
	
	public String[] tokenize(String input, String modelFile) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(modelFile);
		TokenizerModel model = new TokenizerModel(is);
		is.close();
		
		Tokenizer tokenizer = new TokenizerME(model);
		String tokens[] = tokenizer.tokenize(input);
		
		return tokens;
		
	}
	
	public String[] findNames(String input, String modelFile) throws FileNotFoundException, IOException {
		String[] tokens = this.tokenize(input, "models/en-token.bin");
		if (tokens.length > 0) {
			InputStream is = new FileInputStream(modelFile);
			TokenNameFinderModel model = new TokenNameFinderModel(is);
			is.close();
			
			NameFinderME nameFinder = new NameFinderME(model);
			Span[] names = nameFinder.find(tokens);
			
			String[] strNames = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				String name = "";
				for (int j = names[i].getStart(); j < names[i].getEnd(); j++) {
					name += tokens[j];
					if (j < names[i].getEnd() - 1) {
						name += " ";
					}
				}
				strNames[i] = name;
			}
			
			return strNames;
		}
		else {
			return null;
		}
	}
	
	public String[] tagPOS(String input, String modelFile) throws FileNotFoundException, IOException {
		POSModel model = new POSModelLoader().load(new File(modelFile));
		PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
		POSTaggerME tagger = new POSTaggerME(model);
		
		ObjectStream<String> lineStream = new PlainTextByLineStream(
			new StringReader(input));
		
		perfMon.start();
		ArrayList<String> samples = new ArrayList<String>();
		String line;
		while ((line = lineStream.read()) != null) {
			String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(line);
			String[] tags = tagger.tag(whitespaceTokenizerLine);
			
			POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
			samples.add(sample.toString());
			System.out.println("sample: "+sample.toString());
			
			perfMon.incrementCounter();
		}
		perfMon.stopAndPrintFinalResult();
		
		String[] inputTagged = new String[samples.size()];
		return samples.toArray(inputTagged);
	}
}

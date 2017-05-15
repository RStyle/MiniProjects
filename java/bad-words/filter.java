package prog2p1;

public class filter {
	
	public String filter1(String input, String[] badWords) {
		
		for(String b: badWords) {
			// input = input.replace(b, "***");	// case-sensetive
			input = input.replaceAll("(?i)"+b, "***");	// (?i) regex => case-insensetive
		}
		
		return input;
	}
	
	public String filter2(String input, String[] badWords) {
		// with " " "," "." "!"
		// case-sensetive
		
		input = " " + input + " ";
		
		for(String b: badWords) {
			input = input.replace(" "+b+" ", " *** ");
			input = input.replace(" "+b+",", " ***,");
			input = input.replace(" "+b+".", " ***.");
			input = input.replace(" "+b+"!", " ***!");
			input = input.replace(","+b+" ", ",*** ");
			input = input.replace(","+b+",", ",***,");
			input = input.replace(","+b+".", ",***.");
			input = input.replace(","+b+"!", ",***!");
			input = input.replace("."+b+" ", ".*** ");
			input = input.replace("."+b+",", ".***,");
			input = input.replace("."+b+".", ".***.");
			input = input.replace("."+b+"!", ".***!");
			input = input.replace("!"+b+" ", "!*** ");
			input = input.replace("!"+b+",", "!***,");
			input = input.replace("!"+b+".", "!***.");
			input = input.replace("!"+b+"!", "!***!");
		}
		
		input = input.substring(1, input.length()-1);	//delete first and last character
		
		return input;
	}

	public static void main(String[] args) {
		filter test = new filter();
		System.out.println(test.filter1("hallo ratte ich denke ratten. ratte! Ratte", new String[] {"ratte", "Maus"}));
		//hallo *** ich denke ***n. ***! ***
		System.out.println(test.filter2("hallo ratte ich denke ratten. ratte! Ratte", new String[] {"ratte", "Maus"}));
		//hallo *** ich denke ratten. ***! Ratte
	}

}

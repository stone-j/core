package core.variable;

public class Alphabet {
	
	private static String[] capitalLetters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private static String[] lowercaseLetters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

	public static String getLowercaseLetter(int index) {
		return lowercaseLetters[index];
	}
	
	public static String getCapitalLetter(int index) {
		return capitalLetters[index];
	}
}

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;


public class Teste {
public static void main(String[] args) {
	
	NumberFormat num = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
	
	System.out.println(num.getCurrency());
	System.out.println(num.format(1000));
}
}

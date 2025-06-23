import java.util.ArrayList;
import java.util.List;

public class PrimeFactor {
    public List<Integer> of(int number) {
        List<Integer> factors = new ArrayList<>();
        for(int divisor = 2; number > 1; divisor++){
            while(number % divisor == 0){
                factors.add(divisor);
                number /= divisor;
            }
        }
        return factors;
    }
}

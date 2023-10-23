public class Prime {
    static int FIRST_PRIME_NUMBER = 2;

    static public boolean isPrime(int number) {
        if (number < 2)
            return false;
        if (number % 2 == 0 && number != 2)
            return false;
        for (int potentialDivisor = 3; potentialDivisor * potentialDivisor <= number; potentialDivisor += 3)
            if (number % potentialDivisor == 0)
                return false;
        return true;
    }

    static public int nextPrimeAfter(int number) {
        if (number < FIRST_PRIME_NUMBER)
            return FIRST_PRIME_NUMBER;
        if (number == FIRST_PRIME_NUMBER)
            return FIRST_PRIME_NUMBER + 1;
        if (number % 2 == 0)
            number++;
        else
            number += 2;
        while (!isPrime(number))
            number += 2;
        return number;
    }
}
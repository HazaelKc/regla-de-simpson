import java.util.Scanner;
import java.lang.Math;

public class SimpsonIntegration {

    // Método para evaluar una función f(x) proporcionada por el usuario
    public static double evaluateFunction(double x, String function) {
        // Parsear y evaluar la función en el punto x
        String expression = function.replace("x", Double.toString(x));
        return evaluateExpression(expression);
    }

    // Método para evaluar una expresión matemática dada como cadena
    public static double evaluateExpression(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Carácter inesperado: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // suma
                    else if (eat('-')) x -= parseTerm(); // resta
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplicación
                    else if (eat('/')) x /= parseFactor(); // división
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // operador unario
                if (eat('-')) return -parseFactor(); // operador unario

                double x;
                int startPos = this.pos;
                if (eat('(')) { // paréntesis
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // números
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // funciones
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expression.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else throw new RuntimeException("Función desconocida: " + func);
                } else {
                    throw new RuntimeException("Carácter inesperado: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponente

                return x;
            }
        }.parse();
    }

    // Método para aproximar la integral definida usando el método de Simpson
    public static double simpsonIntegration(double a, double b, int n, String function) {
        double h = (b - a) / n;
        double integralSum = evaluateFunction(a, function) + evaluateFunction(b, function);

        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            integralSum += (i % 2 == 0) ? 2 * evaluateFunction(x, function) : 4 * evaluateFunction(x, function);
        }

        return (h / 3) * integralSum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar al usuario que ingrese la función, el intervalo y el número de subintervalos
        System.out.print("Ingrese la función f(x) (ejemplo: x^3/(1+sqrt(x))): ");
        String function = scanner.nextLine();

        System.out.print("Ingrese el límite inferior de integración (a): ");
        double a = scanner.nextDouble();

        System.out.print("Ingrese el límite superior de integración (b): ");
        double b = scanner.nextDouble();

        System.out.print("Ingrese el número de subintervalos (n): ");
        int n = scanner.nextInt();

        scanner.close();

        // Calcular la aproximación de la integral usando el método de Simpson
        double approxIntegral = simpsonIntegration(a, b, n, function);

        // Mostrar el resultado de la aproximación de la integral
        System.out.println("---------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------");
        System.out.println("funcion de la integra "+function );
        System.out.println("---------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------");
        System.out.println("intervalo inferior " + a);
        System.out.println("intervalo superior " + b);
        System.out.println("---------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------");
        System.out.println("N = " + n);
        System.out.println("---------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------");
        System.out.println("Aproximación de la integral definida usando Simpson: " + approxIntegral);
        System.out.println("---------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------");
        
    }
}

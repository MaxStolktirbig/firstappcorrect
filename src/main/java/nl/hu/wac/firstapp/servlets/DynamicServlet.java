package nl.hu.wac.firstapp.servlets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DynamicServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String int1 = request.getParameter("firstvalue");
        String int2 = request.getParameter("secondvalue");
        System.out.println(int1 +"\n" +int2);
        double intOne = Double.parseDouble(int1);
        double intTwo = Double.parseDouble(int2);
        String action = request.getParameter("type");
        double calculatedval = 0;
        String stringval = "";
        try {
            switch (action) {
                case "add":
                    calculatedval = intOne + intTwo;
                    stringval = "+";
                    break;
                case "substract":
                    calculatedval = intOne - intTwo;
                    stringval = "-";
                    break;
                case "multiply":
                    calculatedval = intOne * intTwo;
                    stringval = "*";
                    break;
                case "divide":
                    calculatedval = intOne / intTwo;
                    stringval = "/";
                    break;
                }
        }catch (NullPointerException e){
            System.out.println(e);
        }

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println(" <title>Calculator</title>");
        out.println("</head>");
        out.println("<body>");
        out.println(String.format("%.2f %s %.2f = %.2f", intOne, stringval, intTwo,calculatedval));
        out.println("</body>");
        out.println("</html>");
    }
}
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
        String[] options = {"add", "substract", "multiply", "divide"};
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println(" <title>Calculator</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<form action=\"DynamicServlet.do\" method=\"get\">");
        out.println("<div>");
        out.println("<input type=\"number\" name=\"firstvalue\" value =\""+intOne+"\"/>");
        out.println("<input type=\"number\" name=\"secondvalue\" value =\""+intTwo+"\"/>");
        out.println("<select name=\"type\" id=\"typeselector\">");

        for(String option : options) {
            String selected = "";
            if(option.contains(action)){
                selected = "selected";
            }
            out.println("<option "+selected+" value=\""+option+"\">"+option+"</option>");
        }
//        out.println("<option value=\"substract\">substract</option>");
//        out.println("<option value=\"multiply\">multiply</option>");
//        out.println("<option value=\"divide\">divide</option>");
        out.println("</select>");
        out.println("<input type=\"submit\" value=\"calculate\"/>");
        out.println("</div>\n" +
                "    </form>");
        out.println(String.format("%.2f %s %.2f = %.2f", intOne, stringval, intTwo,calculatedval));
        out.println("</body>");
        out.println("</html>");
    }
}
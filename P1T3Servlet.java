package ds.project1task3;

/*
 * @author Gautam Naik (AndrewID: gnaik)
 *
 * The welcome-file in the deployment descriptor (web.xml) points
 * to this servlet.  So it is also the starting place for the web
 * application.
 *
 * The servlet is acting as the Controller.
 * There are two views - index.jsp and result.jsp.
 * It decides between the two by determining if the user has selected an option or not.
 * If not selected, then it uses the index.jsp view, as a
 * starting place. If there is a selected option, then it decides the nature of processing to
 * be done based on the url endpoint. If the endpoint is '/getP1T3Servlet', it again chooses
 * index.jsp and sends the selected option to it. For a different endpoint, it chooses result.jsp
 * and sends the survey results to it.
 * The model is provided by P1T3Model.
 *
 * This particular project is meant to accept the user input -> a user selected option as answer to a question.
 * Then the project aims to store the user selected option every time the user selects and submits a new answer.
 * Then the project wants to provide a second endpoint("/getResults") where the survey results will be displayed.
 * This means that each option that was selected would be displayed along with a count which would signify the
 * number of times that particular option was selected by the users. On displaying the results once, the results would be
 * cleared so that there is scope for a new question to be added.
 *
 * The servlet handles the communication between the Model and the View. It provides input from View to the Model and
 * provides computation results from the Model to the View to be displayed to the end user. In this case,
 * the servlet sets the doctype attribute based on the user platform(mobile or desktop) so that the doctype can be
 * passed on to the views to make them dynamic. Then the servlet accepts the user selected option from the view(index.jsp) as a parameter.
 * Then based on the request URL endpoint, the servlet decides which set of commands need to be executed.
 * The servlet either adds the user selected option to the responses storage by calling the Model method addResponse
 * and transfers control to the view "index.jsp" while passing the selected option as a request attribute to it
 * or it displays the responses(survey results) using another Model method getResults and transfers control to the
 * view "result.jsp" while passing the selected option counts as a request attribute to it.
 *
 */

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.*;

/*
 *
 * The following WebServlet annotation gives instructions to the web container.
 * It states that when the user browses to the URL paths /getP1T3Servlet or /getResults
 * then the servlet with the name P1T3Servlet should be used.
 *
 */

@WebServlet(name = "P1T3Servlet",
        urlPatterns = {"/getP1T3Servlet", "/getResults"}) //servlet handles two url endpoints
public class P1T3Servlet extends HttpServlet {
    //Code referred to from the Lab 2 InterestingPictureServlet.java file.
    P1T3Model p1t3m = null;  // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        p1t3m = new P1T3Model();
    }

    // This servlet will reply to HTTP GET requests via this doGet method
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {



        // determine what type of device our user is(mobile or desktop)
        String ua = request.getHeader("User-Agent");


        // prepare the appropriate DOCTYPE for the view pages
        if (ua != null && ((ua.contains("Android")) || (ua.contains("iPhone")))) {
            //set the doctype attribute with the doctype html tag for mobile screen
            //this will be passed to the views to make them responsive
            request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
        } else {
            //set the doctype attribute with the doctype html tag for desktop screen
            //this will be passed to the views to make them responsive
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        }

        //get the user selected option from the view
        String option = request.getParameter("option");

        //check the request URL endpoint and make the appropriate method calls
        if (request.getServletPath().equals("/getP1T3Servlet")) {
            //if user has not selected any option, transfer control to the view "index.jsp"
            if (option == null) {
                RequestDispatcher view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
            }
            //call the Model's addResponse method to add the user selected option to storage
            p1t3m.addResponse(option);
            //set the user selected option as a request attribute named "selected" to pass it to the view "index.jsp"
            request.setAttribute("selected", option);
            //transfer control to the view "index.jsp"
            RequestDispatcher view = request.getRequestDispatcher("index.jsp");
            view.forward(request, response);
        } else {
            //call the Model's getResults method to fetch the user response survey results
            request.setAttribute("results", p1t3m.getResults());
            //transfer control to the view "result.jsp"
            RequestDispatcher view = request.getRequestDispatcher("result.jsp");
            view.forward(request, response);
        }

    }
}

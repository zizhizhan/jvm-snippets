package com.zizhizhan.legacies.thirdparty.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CalcServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String operand1 = req.getParameter("operand1");
		String operand2 = req.getParameter("operand2");
		String operation = req.getParameter("op");
		
		PrintWriter out = resp.getWriter();
		if(operand1 == null || operand2 == null){
			showHelp(out);
		}		
		try{
			int op1 = Integer.parseInt(operand1);
			int op2 = Integer.parseInt(operand2);
			if(operation == null || operation == "" || operation == "+"){
				showResult(op1 + op2, out);
			}else if(operation == "*"){
				showResult(op1 * op2, out);
			}else if(operation == "*"){
				showResult(op1 * op2, out);
			} else if(operation == "-"){
				showResult(op1 - op2, out);
			} else if(operation == "/"){
				showResult(op1 / op2, out);
			}else{
				throw new IllegalArgumentException("Unsupported operation: " + operation);
			}				
		}catch(Exception e){
			out.println("Error: " + e.getMessage());
			showHelp(out);
		}
		
	}

	private void showResult(int i, PrintWriter out) throws IOException {
		out.println("The result is " + i);		
	}

	private void showHelp(PrintWriter out) throws IOException {
		out.println("Usage: http://{hostname}:{port}/{target}?operand1={val1}&operand2={val2}&op={operation}");
		out.println("Example: http://localhost/test?operand1=3&operand2=5&op=+");
	}
}

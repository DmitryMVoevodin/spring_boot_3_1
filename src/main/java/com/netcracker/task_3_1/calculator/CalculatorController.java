package com.netcracker.task_3_1.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@Controller
@RequestMapping("/rest/calc")
public class CalculatorController {

    private String[] parametersForHTML = {"operation", "operator", "a", "b", "result"};

    @GetMapping("/add/{a}/{b}")
    public String add(@PathVariable String a, @PathVariable String b, Map<String, Object> model) {
        double[] result;
        try{ result = calculate(a, b, TypeOfOperation.ADD); } catch (NotValidException eNV) { return "pagerror"; }
        putIntoModel(model, "Add", "+", result);
        return "result";
    }

    @GetMapping("/subtract/{a}/{b}")
    public String subtract(@PathVariable String a, @PathVariable String b, Map<String, Object> model) {
        double[] result;
        try{ result = calculate(a, b, TypeOfOperation.SUBTRACT); } catch (NotValidException eNV) { return "pagerror"; }
        putIntoModel(model, "Subtract", "-", result);
        return "result";
    }

    @GetMapping("/multiply/{a}/{b}")
    public String multiply(@PathVariable String a, @PathVariable String b, Map<String, Object> model) {
        double[] result;
        try{ result = calculate(a, b, TypeOfOperation.MULTIPLY); } catch (NotValidException eNV) { return "pagerror"; }
        putIntoModel(model, "Multiply", "*", result);
        return "result";
    }

    @GetMapping("/divide/{a}/{b}")
    public String divide(@PathVariable String a, @PathVariable String b, Map<String, Object> model) {
        double[] result;
        try{ result = calculate(a, b, TypeOfOperation.DIVIDE); } catch (NotValidException eNV) { return "pagerror"; }
        putIntoModel(model, "Divide", "/", result);
        return "result";
    }

    @GetMapping
    public String getMainPage() {
        return "main";
    }

    @PostMapping
    public RedirectView calculationRedirect(@RequestBody String requestBody) {
        String[] param = requestBody.split("&");
        return new RedirectView("/rest/calc/" + (param[2].split("="))[1].toLowerCase() + "/" +
                (param[0].split("="))[1] + "/" + (param[1].split("="))[1]);
    }

    private double[] calculate(String a, String b, TypeOfOperation typeOfOperation) throws NotValidException {
        double[] result = new double[3];
        try{
            result[0] = Double.parseDouble(a);
            result[1] = Double.parseDouble(b);
        } catch (NumberFormatException eNF) {
            throw new NotValidException();
        }
        if(typeOfOperation == TypeOfOperation.DIVIDE && Math.abs(result[1]) < 0.000000000000001) {
            throw new NotValidException();
        }
        double scale = Math.pow(10, 10);
        switch (typeOfOperation) {
            case ADD:
                result[2] =  Math.round((result[0] + result[1]) * scale) / scale;
                break;
            case SUBTRACT:
                result[2] = Math.round((result[0] - result[1]) * scale) / scale;
                break;
            case MULTIPLY:
                result[2] = Math.round((result[0] * result[1]) * scale) / scale;
                break;
            case DIVIDE:
                result[2] = Math.round((result[0] / result[1]) * scale) / scale;
                break;
        }
        return result;
    }

    private enum TypeOfOperation{
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    private void putIntoModel(Map<String, Object> model, String operation, String operator, double[] result) {
        model.put(parametersForHTML[0], operation);
        model.put(parametersForHTML[1], operator);
        model.put(parametersForHTML[2], result[0]);
        model.put(parametersForHTML[3], result[1]);
        model.put(parametersForHTML[4], result[2]);
    }

}
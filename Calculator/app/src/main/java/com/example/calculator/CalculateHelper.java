package com.example.calculator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CalculateHelper {
    public static double num1;
    public static double num2;
    public static double resultNumber;

    private ArrayList splitTokens(String equation){
        String[] constant = equation.split(" ");

        ArrayList constantList = new ArrayList();
        double number=0;

        boolean flag= false;
        for(String data:constant){
            if(data.equals(" ")){
                continue;
            }
            if(checkNumber(data)){
                number = number*10+Double.parseDouble(data);
                flag=true;
            }
            else{
                if(flag){
                    constantList.add(number);
                    number=0;
                }
                flag=false;
                constantList.add(data);
            }
        }

        if(flag){
            constantList.add(number);
        }

        return constantList;
    }

    private ArrayList infixToPostfix(ArrayList constant){
        ArrayList result= new ArrayList();
        HashMap level = new HashMap();
        Stack stack = new Stack();

        level.put("*",3);
        level.put("/",3);
        level.put("+",2);
        level.put("-",2);
        level.put("(",1);

        for(Object object : constant){
            if(object.equals("(")){
                // 여는 괄호 '('를 만났을 때 스택에 푸시
                stack.push(object);
            } else if(object.equals(")")){
                // 닫는 괄호 ')'를 만났을 때 '(' 괄호를 만날 때까지 스택에서 팝
                while(!stack.isEmpty() && !stack.peek().equals("(")){
                    Object val = stack.pop();
                    if(!val.equals("(")){ // '('를 제외한 모든 요소를 결과 리스트에 추가
                        result.add(val);
                    }
                }
                stack.pop(); // '(' 괄호를 스택에서 제거
            } else if(level.containsKey(object)){
                // 현재 요소가 연산자일 경우
                while(!stack.isEmpty() && level.containsKey(stack.peek()) &&
                        Double.parseDouble(level.get(stack.peek()).toString()) >= Double.parseDouble(level.get(object).toString())){
                    // 스택의 top에 있는 연산자의 우선순위가 현재 연산자의 우선순위보다 크거나 같다면 결과 리스트에 추가
                    result.add(stack.pop());
                }
                stack.push(object); // 현재 연산자를 스택에 푸시
            } else {
                // 숫자일 경우 결과 리스트에 바로 추가
                result.add(object);
            }
        }

        while(!stack.isEmpty()){
            result.add(stack.pop());
        }
        return result;
    }

    private Double postFixEval(ArrayList expr){
        Stack numberStack = new Stack();
        for(Object o : expr){
            if(o instanceof Double){
                numberStack.push(o);
            }else if(o.equals("+")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2+num1);
            }else if(o.equals("-")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2-num1);
            }else if(o.equals("*")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2*num1);
            }else if(o.equals("/")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2/num1);
            }
        }
        resultNumber = (Double)numberStack.pop();

        return resultNumber;
    }

    public Double process(String equation){
        ArrayList postfix = infixToPostfix(splitTokens(equation));
        Double result = postFixEval(postfix);
        return result;
    }

    public boolean checkNumber(String str){
        char check;

        if(str.equals("")){
            return false;
        }

        for(int i=0;i<str.length();i++){
            check=str.charAt(i);
            if(check<48||check>58){
                if(check!='.')
                    return false;
            }
        }

        return true;
    }

}
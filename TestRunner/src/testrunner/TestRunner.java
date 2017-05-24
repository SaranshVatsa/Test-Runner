import java.io.*;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

//Defines the annotation
@Retention(RetentionPolicy.RUNTIME)  //If it's CLASS or SOURCE, it will not be detected
@interface MyTest{
int value() default 0;  //Single member annotation
String str() default "ONE";   //Having or not having parameters doesn't matter for this program
}

@Retention(RetentionPolicy.RUNTIME)
@interface MyTest2{
int value() default 5;  //Single member annotation
String str() default "MY TEST 2";   //Having or not having parameters doesn't matter for this program
}

class Test{
    
    //First method is annotated. value gets value of 100. str remains default as "ONE".
    @MyTest(100)
    public void func1(){
        System.out.println("Func1");
    }
    
    //Second method is annotated. value gets value 200. str remains default as "ONE"
    @MyTest(value=200)
    @MyTest2()
    public void func2(){
        System.out.println("Func2");
    }
    
    //Third method is not annotated with MyTest. This will be only method not invoked by the runner.
    //It is annotated by MyTest2 instead.
    @MyTest2()
    public void func3(){
        System.out.println("Func3");
    }
    
    //Fourth method is annotated. str gets value "FOUR", value gets value 400.
    @MyTest(str="FOUR", value=400)
    public void func4(){
        System.out.println("Func4");
    }
}
        
class Runner{
    
    //RunTests() accepts an object from main function.
    public void RunTests(Object test)throws Exception{
        
        Class c=test.getClass(); //Retrieves the Class from the object passed to the function
        Method[] allMethods=c.getDeclaredMethods(); //Retrieves all methods declared in the class retrieved above
        
        //Method[] allMethods=test.getClass().getDeclaredMethods(); //This can be used instead.
        
        //Loop runs for all methods one by one in the for-each. executeMethod() is called each time.
        for(Method m: allMethods){
            executeMethods(m, test);
        }
    }
    
    
    //executeMethods() accepts a Method from RunTests()
    public void executeMethods(Method m, Object test)throws Exception{
        
        System.out.println();
        //Retrieves all annotations for the method and prints them one by one.
        for(Annotation x: m.getAnnotations()){
                System.out.println("Annotation is: "+x);
            }
        //Invokes function is the MyTest annotation is present
        if(m.isAnnotationPresent(MyTest.class)){
            m.invoke(test);
        }
    }
}
        
public class TestRunner {
    public static void main(String[] args)throws IOException{
        
        Test test=new Test();
        Runner runner=new Runner();
        
        try{            
        runner.RunTests(test);  //passed the object of Test class to RunTests function in Runner class.
        }catch(Exception e){
            System.out.println("Error");
        }
    }
}

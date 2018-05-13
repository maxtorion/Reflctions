package sample;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

//Class with diffrent static functions supporting controler
public class Misc {
    public static void failedCreationMessage(Exception e, Class reflectionClass)
    {
        System.out.println("Cannot create instance of the class: "+ reflectionClass.getName()+"\n"+e.toString());
        System.out.println("Class annotations: ");
        Annotation[] ann = reflectionClass.getAnnotations();
        for (Annotation a:ann
                ) {
            System.out.format(" %s%n ", a.toString());
        }
        System.out.println("Is interface?: "+reflectionClass.isInterface());
        System.out.println("Is abstract?: " + Modifier.isAbstract(reflectionClass.getModifiers()));
        System.out.println("Is static?: "+Modifier.isStatic(reflectionClass.getModifiers()));
        System.out.println("Is private?: "+Modifier.isPrivate(reflectionClass.getModifiers()));
        System.out.println("Is protected?: "+Modifier.isProtected(reflectionClass.getModifiers()));

    }

    public static boolean checkIfGetterExists(String fieldName, Class classReflection)
    {
        final String getterName = fieldName.replace(fieldName.charAt(0),Character.toUpperCase(fieldName.charAt(0)));
        long getters = Arrays.asList(classReflection.getMethods()).parallelStream().filter(method -> method.getName().equals("get"+getterName)).count();

        if (getters > 0)
            return true;
        else
            return false;
    }



    public static Method obtainSetter(String fieldName, Class classReflection)
    {
        final String setterName = "set"+fieldName.replace(fieldName.charAt(0),Character.toUpperCase(fieldName.charAt(0)));

        for (Method method:
             classReflection.getMethods()) {
            if(method.getName().equals(setterName))
            {
                return method;
            }
        }

        return null;
    }
}

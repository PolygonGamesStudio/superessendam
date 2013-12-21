package resource;

import java.lang.reflect.Field;

public  class ReflectionHelper {


    public static Object createInstance(String className){
        Object instance = null;
        try{
           instance =  Class.forName(className).newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return instance;
    }


    public static void setFieldValue(Object object,String fieldName, String value){
        try{
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);


            if(field.getType().equals(String.class)){
                field.set(object,value);

            }else if(field.getType().equals(int.class)){
                field.set(object,Integer.decode(value));
            }
            field.setAccessible(false);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}

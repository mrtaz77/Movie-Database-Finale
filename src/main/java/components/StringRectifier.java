package components;

public class StringRectifier {
    public static String rectify(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        for(int i = 0; i< stringBuilder.length(); i++) {
            if((stringBuilder.charAt(i)>='A'&&stringBuilder.charAt(i)<='Z')||(stringBuilder.charAt(i)>='a'&&stringBuilder.charAt(i)<='z')){
                if(i==0 || stringBuilder.charAt(i-1)==' ')stringBuilder.replace(i,i+1,stringBuilder.substring(i,i+1).toUpperCase());
                else stringBuilder.replace(i,i+1,stringBuilder.substring(i,i+1).toLowerCase());
            }
        }
        return String.valueOf(stringBuilder);
    }
}

package TSB;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println(  "Hello World!" );
        HashTable<Integer,String > t = new HashTable<>(120);

        for (int i=0; i<100;i++) {
            t.put((int) (Math.random()*100),"Roberto" + i);
        }

        System.out.println(t.toString());
    }
}

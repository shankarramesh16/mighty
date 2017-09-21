public class test {
public static void main(String args[]){
	test t=new test();
	String str = "vicky";
	int hashcode=t.hashCode();
	byte[] arr=str.getBytes();
	System.out.println("Hashcode"+((Object)arr[4]).hashCode());
	
}
}

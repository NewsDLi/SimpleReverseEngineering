package cn.utils;

public class ToUpper {

	public static void main(String[] args) {
		ToUpper toUpper = new ToUpper();
		String beanName = toUpper.getName("user");
		System.out.println(beanName);
	}
	
	//首字母转大写
	public String firstWordToUp(String string){
		String value = String.valueOf(string.charAt(0)).toUpperCase()+string.substring(1);
		return value;
	}
	//首字母转小写
	public String firstWordToLow(String string){
		String value = String.valueOf(string.charAt(0)).toLowerCase()+string.substring(1);
		return value;
	}
	
	//例如String str = "asd_asd";--->String str = "asdAsd";
	public String getName(String string){
		String beanName = "";
		if(string.contains("_")){
			String[] split = string.split("_");
			for(int i=0;i<split.length;i++){
				beanName+=firstWordToUp(split[i]);
			}
		}else{
			beanName = firstWordToUp(string);
		}
		return firstWordToLow(beanName);
	}
	
}

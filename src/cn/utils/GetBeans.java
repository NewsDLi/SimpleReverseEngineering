package cn.utils;

import java.sql.Types;

public class GetBeans {

	public String getModel(String name,String path){
		String realPath = path.replaceAll("/", ".");
		ToUpper toUpper = new ToUpper();
		String firstWordToUp = toUpper.firstWordToUp(toUpper.getName(name));
		String start = "package "+realPath+";" +"\r\n\r\n"+
		"public class "+firstWordToUp+" {"+"\r\n";
		return start;
	}
	
	//获取对应的列名，列的get方法，set方法
	public String getBeanAttr(String[] clom){
		ToUpper toUpper = new ToUpper();
		String clomName = toUpper.getName(clom[1]);//列名
		String clomAttr = clom[2];
		String type = getType(clomAttr);//列类型
		return formateAttr(clomName, type);
	}
	
	//获取对应的列名，列的get方法，set方法
	public String getBeanGetAndSet(String[] clom){
		ToUpper toUpper = new ToUpper();
		String clomName = toUpper.getName(clom[1]);//列名
		String clomAttr = clom[2];
		String type = getType(clomAttr);//列类型
		return formateAttrGetAndSet(clomName, type);
	}
	
	//生成列的属性
	public String formateAttr(String clomName, String type){
		StringBuffer stringBuffer = new StringBuffer();
		String clom = "private "+type+" "+clomName+";"+"\r\n";
		stringBuffer.append(clom);
		return stringBuffer.toString();
	}
	
	//返回字符串类型的的get、set方法
	public String formateAttrGetAndSet(String clomName, String type){
		ToUpper toUpper = new ToUpper();
		StringBuffer stringBuffer = new StringBuffer();
		String getName = "public "+type+" get"+toUpper.firstWordToUp(clomName)+"() {	" +"\r\n"+
				"	return "+clomName+";	"+"\r\n" +
				"}"+"\r\n\r\n";
		String setName = "public void set"+toUpper.firstWordToUp(clomName)+"("+type+" "+clomName+") {" +"\r\n"+
				"		this."+clomName+" = "+clomName+";	" +"\r\n"+
				"}"+"\r\n\r\n";
		stringBuffer.append(getName);
		stringBuffer.append(setName);
		return stringBuffer.toString();
	}
	
	//判断列类型
	public String getType(String type){
		//默认类型为空
		String typeName = "";
		if(Integer.parseInt(type)==Types.VARCHAR){
			typeName = "String";
		}
		if(Integer.parseInt(type)==Types.BIGINT){
			typeName = "Long";
		}
		if(Integer.parseInt(type)==Types.INTEGER){
			typeName = "Integer";
		}
		if(Integer.parseInt(type)==Types.CHAR){
			typeName = "String";
		}
		if(Integer.parseInt(type)==Types.DECIMAL){
			typeName = "BigDecimal";
		}
		if(Integer.parseInt(type)==Types.BOOLEAN){
			typeName = "boolean";
		}
		if(Integer.parseInt(type)==Types.DATE){
			typeName = "date";
		}
		if(Integer.parseInt(type)==Types.FLOAT){
			typeName = "float";
		}
		if(Integer.parseInt(type)==Types.LONGVARCHAR){
			typeName = "String";
		}
		if(Integer.parseInt(type)==Types.TINYINT){
			typeName = "Integer";
		}
		return typeName;
	}
	
}

package cn.reverse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.utils.GetBeans;
import cn.utils.JdbcUtils;
import cn.utils.ToUpper;

public class Reverse {
	public static final String PATH = "cn/beans";//存放的包路径

	//执行此函数，生成与数据库相连接的表的bean对象
	public static void main(String[] args) throws SQLException, IOException {
		Reverse reverse = new Reverse();
		reverse.getTableDetails();
	}

	//获取所有的表，并把表封装到集合中并返回
	public List<String> getTables(Connection con) throws SQLException, IOException {
		List<String> listTable = new ArrayList<String>();
		DatabaseMetaData metaData = con.getMetaData();
		ResultSet rs = metaData.getTables(null, null, null,	new String[] { "TABLE" });
		while (rs.next()) {
			String table = rs.getString(3);
			listTable.add(table);
		}
		return listTable;
	}

	//获取所有表的所有的字段名，字段属性
	public List<Object> getTableMessage(Connection con) throws SQLException, IOException {
		//对表的字段、字段属性进行封装
		List<Object> tableDetali = new ArrayList<Object>();
		//调用getTables(con)方法获取所有的表的集合
		List<String> tables = getTables(con);
		DatabaseMetaData metaData = con.getMetaData();
		
		//遍历所有的表的集合
		for(String table:tables){
			//存放所有的表的表名，列名，列类型
			List<String[]> tableMess = new ArrayList<String[]>();
			//用来存放一个表中的表名，列名，列类型，每个集合中第一个元素为表名。
			String[] tableDetail = null;
			ResultSet rs = metaData.getColumns(null, null, table, null);
			while (rs.next()) {
				//获取表中的列，依次循环取出
				String colName = rs.getString(4); // 列名称
				//获取表中的列的属性值，依次取出
				int dataType = rs.getInt("DATA_TYPE"); // 数据类型
				tableDetail =new String[]{table, colName, String.valueOf(dataType)};
				tableMess.add(tableDetail);
			}
			tableDetali.add(tableMess);
		}
		return tableDetali;
	}

	//获取所有表的信息（表名，列名，列类型）
	public void getTableDetails() throws SQLException, IOException{
		Connection con = JdbcUtils.getConnection();
		List<Object> tableMessage = getTableMessage(con);
		for(Object obj:tableMessage){
			//每个list集合里面都存放着一张表信息
			List<String[]> tableMess = (List<String[]>) obj;
			//调用此方法生成对应表的bean对象
			generateBeen(tableMess);
		}
		con.close();
	}
	
	public void generateBeen(List<String[]> strings) throws IOException{
		int size = strings.size();
		String beanName = strings.get(0)[0];//获取表的名字
		StringBuffer stringBuffer = new StringBuffer();
		ToUpper toUpper = new ToUpper();
		String className = toUpper.firstWordToUp(toUpper.getName(beanName));//转换为类的名字
		GetBeans getBeans = new GetBeans();
		
		stringBuffer.append(getBeans.getModel(beanName,PATH));//获取Java类型的固定样式
		
		for(int i=0; i<size; i++){
			String[] clom = strings.get(i);//获取列名，列类型
			String beanAttr = getBeans.getBeanAttr(clom);
			stringBuffer.append(beanAttr);
		}
		stringBuffer.append("\r\n");
		for(int i=0; i<size; i++){
			String[] clom = strings.get(i);//获取列名，列类型
			String beanAttrGetAndSet = getBeans.getBeanGetAndSet(clom);
			stringBuffer.append(beanAttrGetAndSet);
		}
		
		//最后给bean对象加上最外面的闭括号
		stringBuffer.append("}");
		//file对象指定bean对象文件生成的路径
		File file = new File("src/"+PATH+"/"+className+".java");
		if(file.exists()){
			file.delete();
		}
		//把存放在字符串中的bean，样式读取待数据流中
		InputStream inputStream = new ByteArrayInputStream(stringBuffer.toString().getBytes());
		//输出流，用来把读取到的数据流输出到指定文件
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		//byte缓冲区
		byte[] buff = new byte[1024];
		int len=0;
		while((len=inputStream.read(buff))!=-1){
			fileOutputStream.write(buff, 0, len);
		}
		fileOutputStream.close();
		inputStream.close();
	}
	
}

package cn.store.utils;

import java.util.UUID;
//上传工具
public class UploadUtils {

	public static String getUUIDName(String realName){
		//realname  可能是  1.jpg   也可能是  1
		//获取后缀名
		int index = realName.lastIndexOf(".");
		if(index==-1){
			return UUID.randomUUID().toString().replace("-", "").toUpperCase();
		}else{
			return UUIDUtils.getId()+realName.substring(index);
		}
	}
	
	public static String getRealName(String name){
		// c:/upload/1.jpg    1.jpg
		//获取最后一个"/"
		int index = name.lastIndexOf("\\");
		return name.substring(index+1);
	}
	
	//保证任意文件存储的位置不同
	public static String getDir(String name){
		//任意一个对象都有一个hash码
		int i = name.hashCode();
		//将hash码转成16进制的字符串
		String hex = Integer.toHexString(i);
		int j=hex.length();
		for(int k=0;k<8-j;k++){
			hex="0"+hex;
		}
		return "/"+hex.charAt(0)+"/"+hex.charAt(1)+"/"+hex.charAt(2)+"/"+hex.charAt(3)+"/"+hex.charAt(4)+"/"+hex.charAt(5)+"/"+hex.charAt(6)+"/"+hex.charAt(7);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//String s="G:\\day17-基础加强\\resource\\1.jpg";
		String s="1.jgp";
		String realName = getRealName(s);
		//System.out.println(realName);
		
		String uuidName = getUUIDName(realName);
		//System.out.println(uuidName);
		
		String dir = getDir(realName);
		System.out.println(dir);
		
		
	}
}

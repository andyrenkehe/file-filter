package filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.Scanner;

public class FileFilter {
	private static final String DEFAULT_DIRECTORY = "/Users/mr.he/Desktop";
	private static final String ADDRESS = "/Users/mr.he/eclipse-workspace/wangda/web";
	private static final String DATE_STRING = "2018-5-16 10:00:00";
	
	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("请输入查询路径");
//		String address = scanner.nextLine();
//		System.out.println("请输入目标路径");
//		String des = scanner.nextLine();
//		System.out.println("请输入时间");
//		String dateStr = scanner.nextLine();
		String address = ADDRESS;
		String dateStr = DATE_STRING;
//		scanner.close();
		SimpleDateFormat[] sdf = {new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd")};
		Date time = null;
		for (SimpleDateFormat s : sdf) {
			try {
				time = s.parse(dateStr);
				if (time != null) {
					break;
				}
			} catch (ParseException e) {
			}
		}
		if (time == null) {
			System.out.println("时间格式错误,请重新输入");
			return;
		}
		
		String des = DEFAULT_DIRECTORY;
		boolean result = copyFile(address, time, des);
		if (result == true) {
			System.out.println("操作成功");
		} else {
			System.out.println("操作失败");
		}
		
	}
	
	public static boolean copyFile(String fileAddress, Date time, String des) {
		File file = new File(fileAddress);
		
		if (!file.exists() || !file.isDirectory()) {
			System.out.println("查询路径对应目录不存在");
			return false;
		}
		
		File desFile = new File(des);
		if (!desFile.exists()) {
			desFile.mkdir();
		}
		desFile = new File(desFile, file.getName());
		
		filterFile(file, desFile, time.getTime());
		return true;
	}
	
	public static void filterFile(File file, File desFile, long timestamp) {
		long lastModifiedDate;
		File[] files = file.listFiles();
		
		if (files == null || files.length <= 0) {
			return;
		}
		
		InputStream is = null;
		OutputStream os = null;
		try {
			for (File oneFile : files) {
	//			System.out.println("文件名：" + oneFile.getName());
				lastModifiedDate = oneFile.lastModified();
	//			Date date = new Date(lastModifiedDate);
	//			System.out.println("最近修改时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
				
				if (oneFile.isFile()&&!oneFile.isHidden()) {
					if (lastModifiedDate > timestamp) {
						if (!desFile.exists()) {
							desFile.mkdirs();
	//						System.out.println("创建目录：" + desFile.getAbsolutePath());
						}
						File desF = new File(desFile, oneFile.getName());
						
						is = new FileInputStream(oneFile);
						os = new FileOutputStream(desF);
						byte[] bytes = new byte[1024];
						int length;
						while ((length = is.read(bytes)) > 0) {
							os.write(bytes, 0, length);
						}
						
						is.close();
						os.close();
					}
				} else if (oneFile.isDirectory()&&!oneFile.isHidden()) {
					File desF = new File(desFile, oneFile.getName());
					filterFile(oneFile, desF, timestamp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

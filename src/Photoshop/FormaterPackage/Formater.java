package Photoshop.FormaterPackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.PixelPackage.Pixel;

import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Formater {
	
	Vector<Pixel> pixels;

	int height, width;
	
	public abstract Vector<Pixel> open(String file) throws FileIOExceptions, FileTypeException;
	
	public abstract void save(String file, int bpp) throws FileIOExceptions;

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getPath(String fname) throws FileIOExceptions, FileTypeException {
		Pattern p = Pattern.compile("^[^\\.]*\\.(.{3})");
		Matcher m = p.matcher(fname);
		if(m.matches()) {
			String type = m.group(1);
			if(type.equals("bmp")) {
				return fname;
			}
			else {
				if(type.equals("pam")) {
					PAMtoBMP(fname);
					String newFname = fname;
					newFname = newFname.substring(0,newFname.length()-3)+"bmp";
					return newFname;
				}
				else throw new FileTypeException();
			}
		}
		else throw new FileIOExceptions();
	}

	void PAMtoBMP(String fname) throws FileIOExceptions {
		String file = "Project1\\Debug\\Project1.exe" +" "+ fname + " "+0;

		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(file);
			process.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new FileIOExceptions();
		}

	}


}

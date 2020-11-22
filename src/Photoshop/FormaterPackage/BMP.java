package Photoshop.FormaterPackage;

import java.io.*;
import java.util.Vector;

import Photoshop.ErrorPackage.*;
import Photoshop.PixelPackage.*;

public class BMP extends Formater {


	
	public BMP(int width, int height, Vector<Pixel> pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}

	public BMP() {

	}

	@Override
	public Vector<Pixel> open(String file) throws FileIOExceptions, FileTypeException
	{
		pixels = new Vector<>();
		InputStream img;

		try {
			img = new FileInputStream(file);

			byte[] allBytes;

			allBytes = img.readAllBytes();												//Reading all bytes at once

			img.close();																//We can now close file

			byte[] check = {0x42, 0x4D}, header = new byte[2];


			System.arraycopy(allBytes, 0, header, 0, 2);			//Reading important (header) bytes from buffer


			boolean flag = true;

			for(int i =0; i<2; i++) {
				flag &= (header[i]!=check[i]);
			}
			if(flag)  throw new FileTypeException();						//Check if the type really is BMP

			//Important bytes are width, height, offset position where pixel array starts = offs and number of bits per pixel = bpp


			byte[] byteOffs , byteWidth, byteHeight, byteBPP;
			byteBPP = new byte[2];
			byteWidth = byteHeight = byteOffs = new byte[4];

			System.arraycopy(allBytes, 0x0A, byteOffs, 0, 4);

			int offs = ((byteOffs[0] & 0xff) | (byteOffs[1] & 0xff)<<8 | (byteOffs[2] & 0xff) << 16 | (byteOffs[3] & 0xff) << 24);		//byte to Integer

			System.arraycopy(allBytes, 0x12 , byteWidth, 0, 4);

			width = ((byteWidth[0] & 0xff) | (byteWidth[1] & 0xff)<<8 | (byteWidth[2] & 0xff) << 16 | (byteWidth[3] & 0xff) << 24);

			System.arraycopy(allBytes, 0x16 , byteHeight, 0, 4);

			height = ((byteHeight[0] & 0xff) | (byteHeight[1] & 0xff)<<8 | (byteHeight[2] & 0xff) << 16 | (byteHeight[3] & 0xff) << 24);

			System.arraycopy(allBytes, 0x1C, byteBPP, 0, 2);

			int bpp = ((byteBPP[0] & 0xff) | (byteBPP[1] & 0xff)<<8);

			int numPix = width * height;

			int j = offs;

			if(bpp == 32 ) {

				for(int i = 0; i<numPix; i++) {
					int blue = Byte.toUnsignedInt(allBytes[j++]);
					int green = Byte.toUnsignedInt(allBytes[j++]);
					int red = Byte.toUnsignedInt(allBytes[j++]);
					int alpha = Byte.toUnsignedInt(allBytes[j++]);

					Pixel p = new Pixel(red,green, blue, alpha);

					pixels.add(p);

				}
			}

			if(bpp == 24) {
				int k = 0;
				for(int i = 0; i<numPix; i++) {
					if  ((++k % width != 1) || (k==1)){
						int blue = Byte.toUnsignedInt(allBytes[j++]);
						int green = Byte.toUnsignedInt(allBytes[j++]);
						int red = Byte.toUnsignedInt(allBytes[j++]);


						Pixel p = new Pixel(red,green, blue);
						pixels.add(p);

					}
					else {
						k=0;
						j+=(width%4);												//Skipping padding bytes
						i--;
					}
				}
			}

			return pixels;

		} catch (IOException e) {
			throw new FileIOExceptions();
		}
	}

	//	@Override
	public void save(String file, int bpp) throws FileIOExceptions {
		OutputStream img;
		try {

			img = new FileOutputStream(file);

			byte[] header = {0x42, 0x4D};

			int fileSize = 54 + width*height*4;																	//Standard size

			byte[] bytefileSize = new byte[] {(byte)fileSize , (byte)(fileSize >> 8), (byte)(fileSize >> 16), (byte)(fileSize >> 24)};

			byte[] res = {0,0,0,0}, res1 = {0x28, 0,0,0},  res2 = {0x01, 0} , res3 = {0x13, 0x0B, 0,0};			//Constant values for BMP files

			byte[] byteOffs = {0x36,0,0,0};																		//Standard offset position

			byte[] byteWidth = new byte[] {(byte)width , (byte)(width >> 8), (byte)(width >> 16), (byte)(width >> 24)};
			byte[] byteHeight = new byte[] {(byte)height , (byte)(height >> 8), (byte)(height >> 16), (byte)(height >> 24)};

			byte[] byteBPP = new byte[] {(byte)bpp , (byte)(bpp >> 8)}; 

			int mapSize = width*height*4;																	//This size is for both 24 and 32 bpp images, for 24 padding is added

			byte[] byteMapSize = new byte[] {(byte)mapSize , (byte)(mapSize >> 8), (byte)(mapSize >> 16), (byte)(mapSize >> 24)};

			img.write(header);
			img.write(bytefileSize);
			img.write(res);
			img.write(byteOffs);
			img.write(res1);
			img.write(byteWidth);
			img.write(byteHeight);
			img.write(res2);
			img.write(byteBPP);
			img.write(res);
			img.write(byteMapSize);
			img.write(res3);
			img.write(res3);
			img.write(res);
			img.write(res);


			if(bpp == 32) {
				for(Pixel i : pixels) {
					img.write(i.getBlue());
					img.write(i.getGreen());
					img.write(i.getRed());
					img.write((int) (i.getAlpha()*255));
				}
			}
			else
				if(bpp == 24) {
					int k = 0;
					for(int i = 0; i<width*height; i++) {
						if  ((++k % width != 1) || (k==1)){
							img.write(pixels.get(i).getBlue());
							img.write(pixels.get(i).getGreen());
							img.write(pixels.get(i).getRed());
						}
						else {
							k=0;
							for(int j = 0; j<(width%4); j++) 
								img.write(0);
							i--;
						}
					}
					for(int j = 0; j<(width%4); j++) {									//Last padding group which is not included in for-loop above
						img.write(0);
					}
				}

			img.close();


		} catch (IOException e) {
			throw new FileIOExceptions();
		}

	}

}

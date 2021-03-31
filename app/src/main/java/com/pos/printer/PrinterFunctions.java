package com.pos.printer;

import android.util.Log;

public class PrinterFunctions {
	static {
		try {
			Log.i("JNI", "Trying to load PosPPdrv.so");
			System.loadLibrary("PosPPdrv");
		}
		catch (UnsatisfiedLinkError ule) {
			Log.e("JNI", "WARNING: Could not load PosPPdrv.so");
		}
	}
	//--------------------------Function
	public static native int PortDiscovery(String portName,int portSettings);
	public static native int OpenPort(String portName,int portSettings);
	public static native int ClosePort(String portName);
	public static native int PrintPDF417Code(String portName,int portSettings,int Columns,int Rows,int Width,int Height,int CorrectionLevel,String dArray);
	public static native int PrintQrCode(String portName,int portSettings,int n,int mod,int size,int version,String dArray);
	public static native int OpenCashDrawer(String portName,int portSettings,int m,int t);
	public static native int CheckStatus(String portName,int portSettings,int n);
	public static native int PrintCode39(String portName,int portSettings,String barcodeData,int option,int height,int width);
	public static native int PrintCode93(String portName,int portSettings,String barcodeData,int option,int height,int width);
	public static native int PrintCodeITF(String portName,int portSettings,String barcodeData,int option,int height,int width);
	public static native int PrintCode128(String portName,int portSettings,String barcodeData,int option,int height,int width);
	public static native int PreformCut(String portName,int portSettings,int cuttype);
	public static native int PrintText(String portName,int portSettings,int underline,int invertColor,int emphasized,int upsideDown,int heightExpansion,int widthExpansion,int leftMargin,int alignment,String textData);
	public static native int PrintTextKanji(String portName,int portSettings,int underline,int invertColor,int emphasized,int upsideDown,int heightExpansion,int widthExpansion,int leftMargin,int alignment,String textData);
	public static native int PrintBitmapImage(String portName,int portSettings,int m,String ImgUrl);
	public static native int PrintSampleReceipt(String portName,int portSettings);
	public static native int PrintSampleReceiptCn(String portName,int portSettings);
	//--------------------------
	public static native int PrintBarCode(String portName,int portSettings,String barcodeData,int option,int height,int width,int alignment,int mod);
	public static native int DefineNVBitImage(String portName,int portSettings,String ImgUrl);
	public static native int DefineNVBitImageTwo(String portName,int portSettings,String ImgUrl1,String ImgUrl2);
	public static native int PrintNVBitImage(String portName,int portSettings,int n,int m);
	public static native int SelectPrintMode(String portName,int portSettings,int mod);
	public static native int CancelPrintDataInPageMode(String portName,int portSettings);
	public static native int SelectPrintDirectionInPageMode(String portName,int portSettings,int n);
	public static native int SetPrintingAreaInPageMode(String portName,int portSettings,int X,int Y,int Width,int Height);
	public static native int SetPrintPositionInPageMode(String portName,int portSettings,int p);
	public static native int PrintDataInPageMode(String portName,int portSettings);
	public static native int SetLineSpacing(String portName,int portSettings,int n);
	public static native int SelectCharacterFont(String portName,int portSettings,int n);
	public static native int SelectCodePage(String portName,int portSettings,String CodePagName);
	public static native int SelectInternationalCharacter(String portName,int portSettings,String CharName);
	//--------------------------
}

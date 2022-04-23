package com.pos.printer.usb;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;

import androidx.annotation.Nullable;

import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnections;
import com.dantsu.escposprinter.connection.usb.UsbDeviceHelper;

public class UsbPrintersConnectionsLocal extends UsbConnections {

    /**
     * Create a new instance of UsbPrintersConnectionsLocal
     *
     * @param context Application context
     */
    public UsbPrintersConnectionsLocal(Context context) {
        super(context);
    }

    /**
     * Easy way to get the first USB printer paired / connected.
     *
     * @return a UsbConnection instance
     */
    @Nullable
    public static UsbConnection selectFirstConnected(Context context) {
        UsbPrintersConnectionsLocal printers = new UsbPrintersConnectionsLocal(context);
        UsbConnection[] bluetoothPrinters = printers.getList();

        if (bluetoothPrinters == null || bluetoothPrinters.length == 0) {
            return null;
        }

        return bluetoothPrinters[0];
    }


    /**
     * Get a list of USB printers.
     *
     * @return an array of UsbConnection
     */
    @Nullable
    public UsbConnection[] getList() {
        UsbConnection[] usbConnections = super.getList();

        if(usbConnections == null) {
            return null;
        }

        int i = 0;
        UsbConnection[] printersTmp = new UsbConnection[usbConnections.length];
        for (UsbConnection usbConnection : usbConnections) {
            UsbDevice device = usbConnection.getDevice();
            if(device.getVendorId()==4070 && device.getProductId() == 33054) {
                int usbClass = device.getDeviceClass();
                if (usbClass == UsbConstants.USB_CLASS_PER_INTERFACE && UsbDeviceHelper.findPrinterInterface(device) != null) {
                    usbClass = UsbConstants.USB_CLASS_PRINTER;
                }
                if (usbClass == UsbConstants.USB_CLASS_PRINTER) {
                    printersTmp[i++] = new UsbConnection(this.usbManager, device);
                }
            }
        }

        UsbConnection[] usbPrinters = new UsbConnection[i];
        System.arraycopy(printersTmp, 0, usbPrinters, 0, i);
        return usbPrinters;
    }
    
}

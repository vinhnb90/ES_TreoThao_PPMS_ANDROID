package com.es.tungnv.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * Created by TUNGNV on 3/11/2016.
 */
public class InvoicePrinter {

    public static final int REQUEST_ENABLE_BT = 1;

    public static int SUCCESS = 0;
    public static int EXCEPTION = 1;
    public static int ERROR_CONNECTION = 2;
    public static int HAVE_NO_MAC = 3;

    public BluetoothAdapter mBTAdapter;
    private BluetoothSocket mBTSocket;
    public String MAC;

    public InvoicePrinter(String mac) {
        MAC = mac;
        ConnectPrinter();
    }

    private int ConnectPrinter() {
        if (MAC == null)
            return HAVE_NO_MAC;

        mBTAdapter = null;
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

        return SUCCESS;

    }


    public void Print(String content) {
        final String strPrint = content;

        new Thread(new Runnable() {

            public void run() {

                mBTAdapter.cancelDiscovery();

                try {

                    final BluetoothDevice mdevice = mBTAdapter.getRemoteDevice(MAC);

                    if (MAC == null) {
                        return;
                    }

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Method m = mdevice.getClass().getMethod(
                                        "createRfcommSocket",
                                        new Class[] { int.class });
                                mBTSocket = (BluetoothSocket) m.invoke(mdevice, 1);
                                mBTSocket.connect();

                                OutputStream os = mBTSocket.getOutputStream();
                                os.flush();

                                os.write((strPrint).getBytes());
                                os.write(new byte[] { ESC.ESC, (byte) ('R'), 94 });
                                os.write(new byte[] { ESC.ESC, (byte) ('K'), ESC.K2, ESC.K9, ESC.CR });

                                os.write(10);

                                Thread.sleep(500);

                            } catch (Exception e) {
                                ConnectPrinter();
                            } finally {
                                if (mBTSocket != null) {
                                    try {
                                        mBTSocket.close();
                                        mBTSocket = null;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }).start();

                } catch (Exception e) {
                    ConnectPrinter();

                } finally {
                }
            }

        }).start();

    }

    public void Print(final byte[] bImage) {

        new Thread(new Runnable() {

            public void run() {

                mBTAdapter.cancelDiscovery();

                try {

                    final BluetoothDevice mdevice = mBTAdapter.getRemoteDevice(MAC);

                    if (MAC == null) {
                        return;
                    }

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Method m = mdevice.getClass().getMethod(
                                        "createRfcommSocket",
                                        new Class[] { int.class });
                                mBTSocket = (BluetoothSocket) m.invoke(mdevice, 1);
                                mBTSocket.connect();

                                OutputStream os = mBTSocket.getOutputStream();
                                os.flush();

                                os.write(bImage);
                                os.write(new byte[] { ESC.ESC, (byte) ('R'), 94 });
                                os.write(new byte[] { ESC.ESC, (byte) ('K'), ESC.K2, ESC.K9, ESC.CR });

                                os.write(10);

                                Thread.sleep(500);

                            } catch (Exception e) {
                                ConnectPrinter();
                            } finally {
                                if (mBTSocket != null) {
                                    try {
                                        mBTSocket.close();
                                        mBTSocket = null;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }).start();

                } catch (Exception e) {
                    ConnectPrinter();

                } finally {
                }
            }

        }).start();

    }

}

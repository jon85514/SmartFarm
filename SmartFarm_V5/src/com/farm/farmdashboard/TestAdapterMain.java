package com.farm.farmdashboard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import main.java.surelyhuman.jdrone.control.physical.tello.*;
public class TestAdapterMain {
	public static void main(String args[]) throws SocketException, UnknownHostException, FileNotFoundException {
		TelloDrone telloDrone = new TelloDrone();
		try {
			telloDrone.getAccelerationX();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
